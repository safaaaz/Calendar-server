package calendar.controllers;

import calendar.entities.Event;
import calendar.entities.User;
import calendar.entities.UserRolePair;
import calendar.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class NotificationsController {
    @Autowired
    private EventService eventService;

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
        if(event.getOrganizer().getNotificationSettings().getEventDataChanged() && event.getOrganizer().getNotificationSettings().getByPopUp()){
            relevantUsers.add(event.getOrganizer().getEmail());
        }
        relevantUsers.remove(resEvent.getEditorEmail());
        if(relevantUsers.isEmpty()) relevantUsers=new ArrayList<>();
        String message ="user with email: "+resEvent.getEditorEmail()+" update event with title: "+resEvent.getEventTitle();
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
