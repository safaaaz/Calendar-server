package calendar.controllers;

import calendar.entities.NotificationSettings;
import calendar.entities.User;
import calendar.services.AuthService;
import calendar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "findOne/{userId}", method = RequestMethod.GET)
    public ResponseEntity<User> fetchUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.fetchUserById(userId));
    }

    @RequestMapping(value = "/settings/notification/update", method = RequestMethod.POST)
    public ResponseEntity<NotificationSettings> updateNotificationSettings(@RequestHeader("token") String token, @RequestBody NotificationSettings notification) {

        User user = authService.getCachedUser(token);
        // notification.setUser(user);
        return ResponseEntity.ok(userService.updateNotificationSettings(user, notification));
    }
}
