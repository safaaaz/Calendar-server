package calendar.controllers;

import calendar.entities.NotificationSettings;
import calendar.entities.User;
import calendar.services.AuthService;
import calendar.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/settings")
public class SettingsController {

//    @Autowired
//    NotificationService notificationService;
//
//    @Autowired
//    AuthService authService;
//
//    @RequestMapping(value = "/notification/update", method = RequestMethod.POST)
//    public ResponseEntity<NotificationSettings> updateNotification(@RequestHeader("token") String token, @RequestBody NotificationSettings notification) {
//
//        User user = authService.getCachedUser(token);
//        notification.setUser(user);
//        return ResponseEntity.ok(notificationService.updateNotification(notification));
//    }
}