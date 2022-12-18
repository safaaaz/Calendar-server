package calendar.controllers;

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
}
