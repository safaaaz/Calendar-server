package calendar.controllers;

import calendar.entities.User;
import calendar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "findOne/{userId}", method = RequestMethod.GET)
    public ResponseEntity<User> fetchUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.fetchUserById(userId));
    }

    @RequestMapping(value = "findOne/{email}", method = RequestMethod.GET)
    public ResponseEntity<User> fetchUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.fetchUserByEmail(email));
    }
}
