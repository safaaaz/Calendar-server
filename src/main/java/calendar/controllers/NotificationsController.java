package calendar.controllers;

import calendar.DTO.UserDTO;
import calendar.entities.Event;
import calendar.entities.User;
import calendar.entities.UserRolePair;
import calendar.repositories.EventRepository;
import calendar.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class NotificationsController {
    @Autowired
    private EventService eventService;
    @Autowired
    private EventRepository eventRepository;

    /**
     * This function is used to update events and create notifications for relevant users.
     * @param resEvent an instance of EventController.ResponseUpdatedEvent
     * @return an instance of NotificationDetails containing the message and the list of relevant users.
     */
    @MessageExceptionHandler()
    @MessageMapping("/update/")
    @SendTo("/topic/updates/")
    public NotificationDetails updateEventNotification(EventController.ResponseUpdatedEvent resEvent) {
        System.out.println(resEvent);
        Event event = eventService.fetchEventById(resEvent.getEventId());
        if(event==null){return null;}
        List<String> relevantUsers = event.getUserRoles().stream()
                .map(UserRolePair::getUser)
                .filter(user -> user.getNotificationSettings().getEventDataChanged() && user.getNotificationSettings().getByPopUp())
                .map(User::getEmail)
                .collect(Collectors.toList());
        System.out.println(event.getOrganizer().getNotificationSettings());

        if(event.getOrganizer().getNotificationSettings().getEventDataChanged() && event.getOrganizer().getNotificationSettings().getByPopUp()){
            relevantUsers.add(event.getOrganizer().getEmail());
        }
        relevantUsers.remove(resEvent.getEditorEmail());
        if(relevantUsers.isEmpty()) relevantUsers=new ArrayList<>();
        String message ="user with email: "+resEvent.getEditorEmail()+" update event with title: "+resEvent.getEventTitle();
        return new NotificationDetails(message,relevantUsers);
    }


    /**
     * This function sends a notification to the relevant users when a user is invited to an event.
     *
     * @param invitedUser a UserDTO object containing the eventId and email of the invited user.
     * @return a NotificationDetails object containing a message and a list of relevant users.
     */
    @MessageExceptionHandler()
    @MessageMapping("/inviteGuest/")
    @SendTo("/topic/updates/")
    public NotificationDetails inviteGuestNotification(UserDTO invitedUser) {
        System.out.println(invitedUser);
        Event event = eventService.fetchEventById(invitedUser.getEventId());
        if(event==null){return null;}
        List<String> relevantUsers = event.getUserRoles().stream()
                .map(UserRolePair::getUser)
                .map(User::getEmail)
                .collect(Collectors.toList());
        System.out.println(event.getOrganizer().getNotificationSettings());
        if(event.getOrganizer().getNotificationSettings().getByPopUp()){
            relevantUsers.add(event.getOrganizer().getEmail());
        }
        String message ="user with email: "+invitedUser.getEmail()+" has been invited to event : "+event.getTitle();
        return new NotificationDetails(message,relevantUsers);
    }
    public static class NotificationDetails{
        String message;
        List<String> relevantUsers;

        public NotificationDetails(String message, List<String> relevantUsers) {
            this.message = message;
            this.relevantUsers = relevantUsers;
        }
        public NotificationDetails(){}

        @Override
        public String toString() {
            return "NotificationDetails{" +
                    "message='" + message + '\'' +
                    ", relevantUsers=" + relevantUsers +
                    '}';
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setRelevantUsers(List<String> relevantUsers) {
            this.relevantUsers = relevantUsers;
        }

        public String getMessage() {
            return message;
        }

        public List<String> getRelevantUsers() {
            return relevantUsers;
        }
    }
}
