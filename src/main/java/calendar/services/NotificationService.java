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

    private final Logger logger;

    public NotificationService() {
        this.logger = LogManager.getLogger(AuthService.class.getName());
    }

    /**
     * If the user update an event details, email message will send to all the invited users
     *
     * @param updateEventDTO - the updated event
     */
    public void sendUpdateEventNotification(UpdateEventDTO updateEventDTO) {
        Long eventId = updateEventDTO.id;
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new EventNotFoundException("Event was not found for id: " + eventId);
        });
        Set<UserRolePair> userRoles = event.getUserRoles();

        for (UserRolePair userRolePair : userRoles) {
            User user = userRepository.findById(userRolePair.getUser().getId()).get();
            boolean byEmail = user.getNotificationSettings().getByEmail();
            boolean eventDataChanged = user.getNotificationSettings().getEventDataChanged();

            if (byEmail && eventDataChanged) {
                String title = String.format("event %s details has been updated", event.getTitle());
                String content = String.format("event %s by organizer %s  on date %s was updated",event.getTitle(), event.getOrganizer().getEmail(), event.getDateTime());
                sendEmail(user.getEmail(), title , content);
            }
        }
    }

    /**
     * send email to user
     *
     * @param destination - user's email
     * @param title       - message title
     * @param txt
     */
    private void sendEmail(String destination, String title, String txt) {
        Email email = new Email.Builder().to(destination).subject(title).content(txt).build();
        mailSender.send(email.convertIntoMessage());
        logger.info("email update event has been sent to: " + destination);
    }
}
