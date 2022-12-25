package calendar.controllers;

import calendar.DTO.UserDTO;
import calendar.entities.NotificationSettings;
import calendar.entities.User;
import calendar.exceptions.MissingEventFieldException;
import calendar.services.AuthService;
import calendar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.hibernate.internal.util.StringHelper.isBlank;

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

    @RequestMapping(value = "findOne/{email}", method = RequestMethod.GET)
    public ResponseEntity<User> fetchUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.fetchUserByEmail(email));
    }

    @RequestMapping(value = "/settings/notification/update", method = RequestMethod.POST)
    public ResponseEntity<NotificationSettings> updateNotificationSettings(@RequestHeader("token") String token, @RequestBody NotificationSettings notification) {

        User user = authService.getCachedUser(token);
        // notification.setUser(user);
        return ResponseEntity.ok(userService.updateNotificationSettings(user, notification));
    }

    @RequestMapping(value = "share", method = RequestMethod.POST)
    public ResponseEntity<User> shareCalendar(@RequestHeader("token") String token, @RequestBody UserDTO userDTO) {

        if (isBlank(userDTO.email)) {
            throw new MissingEventFieldException("email");
        }

        User user = authService.getCachedUser(token);
        User sharedWithUser = userService.fetchUserByEmail(userDTO.email);

        return ResponseEntity.ok(userService.addUserToMyCalendars(user, sharedWithUser));
    }
}
