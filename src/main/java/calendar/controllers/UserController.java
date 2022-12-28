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

    /**
     * This function retrieves a user from the userService by its userId.
     *
     * @param userId A Long representing the userId of the user to fetch
     *
     * @return A ResponseEntity containing the requested user, or an error
     */
    @RequestMapping(value = "findOne/{userId}", method = RequestMethod.GET)
    public ResponseEntity<User> fetchUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.fetchUserById(userId));
    }

    /**
     * This function fetches a user by their email address using the userService.fetchUserByEmail method.
     *
     * @param email A string containing the user's email address
     * @return A ResponseEntity containing the user object associated with the email address
     */
    @RequestMapping(value = "findOne/{email}", method = RequestMethod.GET)
    public ResponseEntity<User> fetchUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.fetchUserByEmail(email));
    }

    /**
     * This method is used to update the notification settings of a user.
     *
     * @param token The authentication token of the user
     * @param notification The notification settings object
     * @return The updated NotificationSettings object
     */
    @RequestMapping(value = "/settings/notification/update", method = RequestMethod.POST)
    public ResponseEntity<NotificationSettings> updateNotificationSettings(@RequestHeader("token") String token, @RequestBody NotificationSettings notification) {

        User user = authService.getCachedUser(token);
        // notification.setUser(user);
        return ResponseEntity.ok(userService.updateNotificationSettings(user, notification));
    }

    /**
     * This method handles the logic for sharing a calendar with another user.
     *
     * @param token The token of the user who is doing the sharing
     * @param userDTO The DTO containing the email of the user who will receive the calendar
     * @return An object with information about the user who was added to the calendar
     * @throws MissingEventFieldException If the email of the user to be added is missing
     */
    @RequestMapping(value = "share", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> shareCalendar(@RequestHeader("token") String token, @RequestBody UserDTO userDTO) {
        if (isBlank(userDTO.email)) {
            throw new MissingEventFieldException("email");
        }

        User user = authService.getCachedUser(token);
        User sharedWithUser = userService.fetchUserByEmail(userDTO.email);

        return ResponseEntity.ok(userService.addUserToMyCalendars(user, sharedWithUser));
    }
}
