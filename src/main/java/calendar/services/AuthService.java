package calendar.services;

import calendar.entities.NotificationSettings;
import calendar.entities.PreConfirmed;
import calendar.entities.User;
import calendar.enums.Provider;
import calendar.exceptions.TokenNotFound;
import calendar.exceptions.UserAlreadyRegistered;
import calendar.repositories.PreConfirmedRepository;
import calendar.repositories.UserRepository;
import calendar.utils.Email;
import calendar.utils.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    // users that did a valid login
    public HashMap<String, User> cachedUsers = new HashMap<>();

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreConfirmedRepository preConfirmedRepository;

    private final Logger logger;

    public AuthService() {
        this.logger = LogManager.getLogger(AuthService.class.getName());
    }

    /**
     * authenticates if the given user doesn't exist in the database, if so sends email for verification
     *
     * @param user (email and password type UserBody)
     * @return user, if something went wrong it returns null
     */
    public User register(User user) {

        // email does exist
        if (isEmailInDatabase(user.getEmail())) {
            logger.warn(user.getEmail() + " is already registered, registration failed");
            throw new UserAlreadyRegistered(user.getEmail() + " is already registered, registration failed");
        }

        PreConfirmed preConfirmed = new PreConfirmed(user.getEmail(), user.getPassword());
        sendEmail(preConfirmed);
        preConfirmedRepository.save(preConfirmed);

        return user;
    }

    /**
     * sends email verification for optional user
     *
     * @param unconfirmed (email, password and unique token type Unconfirmed)
     */
    private void sendEmail(PreConfirmed unconfirmed) {

        String destination = unconfirmed.getEmail();
        String title = "Please verify your registration";
        String txt = "Please click the link below to verify your registration:\n"
                + "http://localhost:8080/auth/verify/" + unconfirmed.getToken()
                + "\nThank you.";

        Email email = new Email.Builder().to(destination).subject(title).content(txt).build();
        mailSender.send(email.convertIntoMessage());

        logger.info("email authentication has been sent");
    }

    /**
     * verify the given token if it is not forged
     *
     * @param token (Unique key for each logged user)
     * @return token, if something went wrong returns null
     */
    public String verifyToken(String token) {

        PreConfirmed preConfirmed = preConfirmedRepository.findByToken(token);

        if (preConfirmed == null) {
            throw new TokenNotFound("the given token: " + token + " is forged, verification failed");
        }

        preConfirmedRepository.delete(preConfirmed);

        User user = new User(preConfirmed.getEmail(), preConfirmed.getPassword());
        userRepository.save(user);

        return token;
    }

    /**
     * authenticates if the given user is in the database
     *
     * @param temp (email and password type UserBody)
     * @return token (unique String that allows further actions)
     */
    public String login(User temp) {

        User user = userRepository.findByEmail(temp.getEmail()).get();
        if(user!=null){
            if (user.getProvider()==Provider.GITHUB || user.getPassword().equals(temp.getPassword())) {
                String token = Token.generate();
                cachedUsers.put(token, user);
                return token;
            }
        }
        return null;
    }

    /**
     * checks if the given email is in the database
     *
     * @param email
     * @return boolean
     */
    boolean isEmailInDatabase(String email) {
        return (userRepository.findByEmail(email) != null);
    }

    /**
     * main function to check if the user is logged, and able to perform actions
     *
     * @param token (Unique key for each logged user)
     * @return User
     */
    public User getCachedUser(String token) {
        return (token == null) ? null : cachedUsers.get(token);
    }
    public String registerGithubUser(String userEmail){
        User user = userRepository.findByEmail(userEmail).get();
        if(user==null){
            user = new User(userEmail, Provider.GITHUB);
            userRepository.save(user);
        }
        return login(user);
    }
}