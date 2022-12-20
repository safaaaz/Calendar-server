package calendar.services;

import calendar.entities.Notification;
import calendar.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public Notification updateNotification(Notification temp){
        Notification notification = new Notification(temp.getUser(),temp.getByEmail(), temp.getByPopUp(), temp.getUserStatusChanged(), temp.getEventDataChanged(), temp.getEventCanceled(), temp.getUserWasUninvited(), temp.getRemind1MinBefore(), temp.getRemind5MinBefore(), temp.getRemind10MinBefore());
        notificationRepository.save(notification);

        return notification;
    }
}
