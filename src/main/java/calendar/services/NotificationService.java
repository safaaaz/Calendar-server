package calendar.services;

import calendar.DTO.UpdateEventDTO;
import calendar.entities.Event;
import calendar.entities.NotificationSettings;
import calendar.entities.User;
import calendar.entities.UserRolePair;
import calendar.exceptions.EventNotFoundException;
import calendar.exceptions.NotificationSettingsNotFoundException;
import calendar.exceptions.UserNotFoundException;
import calendar.repositories.EventRepository;
import calendar.repositories.NotificationSettingsRepository;
import calendar.repositories.UserRepository;
import calendar.utils.Email;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NotificationSettingsRepository notificationSettingsRepository;

    private final Logger logger;

    public NotificationService() {
        this.logger = LogManager.getLogger(AuthService.class.getName());
    }

    public void sendUpdateEventNotification(UpdateEventDTO updateEventDTO) {

        Long eventId = updateEventDTO.id;
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new EventNotFoundException("Event was not found for id: " + eventId);
        });
        Set<UserRolePair> userRoles = event.getUserRoles();

        for (UserRolePair userRolePair : userRoles) {

            System.out.println((long) userRolePair.getId());

            User user = userRepository.findById(userRolePair.getUser().getId()).orElseThrow(() -> {
                throw new UserNotFoundException("User was not found for id: " + userRolePair.getId());
            });

            System.out.println("1-@@@@@@@@@@@@@@@@@@");
            System.out.println(user.toString());
            System.out.println("@@@@@@@@@@@@@@@@@@");

            NotificationSettings notificationSettings = notificationSettingsRepository.findByUserId(user.getId()).orElseThrow(() -> {
                throw new NotificationSettingsNotFoundException("Notification settings were not found for user id:" + user.getId());
            });
            System.out.println("2-@@@@@@@@@@@@@@@@@@");
            System.out.println(notificationSettings);
            System.out.println("@@@@@@@@@@@@@@@@@@");
            if (notificationSettings.getByEmail() && notificationSettings.getEventDataChanged()) {
                sendEmail(user.getEmail(), "event was updated", "event was updated");
            }
        }
    }

    private void sendEmail(String destination, String title, String txt) {

        Email email = new Email.Builder().to(destination).subject(title).content(txt).build();
        mailSender.send(email.convertIntoMessage());

        logger.info("email update event has been sent to: " + destination);
    }
}
