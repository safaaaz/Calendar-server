package calendar.controllers;

import calendar.entities.Response;
import calendar.entities.User;
import calendar.services.AuthService;
import calendar.utils.GitRoot;
import calendar.utils.Validate;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    private static final Logger logger = LogManager.getLogger(AuthController.class.getName());

    private static final Gson gson = new Gson();

    @Autowired
    private Environment env;
    public AuthController() {
    }

    /**
     * register endpoint, which at the end sends email verification using generated unique token
     *
     * @param user (email and password type UserBody)
     * @return email verification
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("register attempt with email: " + user.getEmail() + ", and password: " + user.getPassword());

        // validate input before, we proceed to service
        if (Validate.email(user.getEmail()) && Validate.password(user.getPassword())) {
            User verifiedUser = authService.register(user);
            return ResponseEntity.ok(verifiedUser);
        }
        logger.warn("email or password syntax validation did not pass, register failed");
        return ResponseEntity.badRequest().build(); // 400
    }

    /**
     * Authentication Using GitHub Account
     * @param code - the code that the client get from GitHub
     * @return a new token for the user that logged in
     */
    @RequestMapping(value = "registerUsingGitHub", method = RequestMethod.GET)
    public ResponseEntity<String> registerUsingGitHub(@RequestParam String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept","application/json");
        HttpEntity<String> entity = new HttpEntity<>(null,headers);
        String url="https://github.com/login/oauth/access_token?client_id="+env.getProperty("client_id")+"&client_secret="+env.getProperty("client_secret")+"&code="+code;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GitRoot> response = null;
        String userEmail;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET,entity, GitRoot.class);
            GitRoot gitResponse = response.getBody();
            userEmail = gitResponse.getEmailFromToken();
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(new Gson().toJson(authService.registerGithubUser(userEmail)));
    }
    /**
     * email verification, at the end the guest becomes a user in database
     *
     * @param token (taken from route)
     * @return response status - 200 or 404
     */
    @RequestMapping(value = "/verify/{token}")
    public ResponseEntity<String> tokenVerification(@PathVariable("token") String token) {

        token = authService.verifyToken(token);
        logger.info(token + " is legit");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("token", token);

        return ResponseEntity.ok().headers(responseHeaders).body("<h1>Email verification was done successfully</h1>");  // 200
    }

    /**
     * login endpoint, at the end receives unique token and stored at cache
     *
     * @param user (email and password)
     * @return response status - 200 or 404
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody User user) {
        logger.info("login attempt with email: " + user.getEmail() + ", and password: " + user.getPassword());

        // validate input before, we proceed to service
        if (Validate.email(user.getEmail()) /*&& Validate.password(user.getPassword())*/) {

            String token = authService.login(user);

            if (token != null) {
                logger.info("successful login, user's token:  " + token);

//                HttpHeaders responseHeaders = new HttpHeaders();
//                responseHeaders.add("token", token);

                return ResponseEntity.ok().body(new Gson().toJson(token)); // 200
            }
        }
        logger.warn("email or password syntax validation did not pass, login failed");
        return ResponseEntity.notFound().build(); // 404
    }
}