package calendar.services;

import calendar.entities.NotificationSettings;
import calendar.repositories.NotificationSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

//    @Autowired
//    NotificationSettingsRepository notificationSettingsRepository;
//
//    public NotificationSettings updateNotificationSettings(NotificationSettings temp){
//        NotificationSettings notificationSettings = new NotificationSettings(temp.getUser(),temp.getByEmail(), temp.getByPopUp(), temp.getUserStatusChanged(), temp.getEventDataChanged(), temp.getEventCanceled(), temp.getUserWasUninvited(), temp.getRemind1MinBefore(), temp.getRemind5MinBefore(), temp.getRemind10MinBefore());
//
//        notificationSettingsRepository.findByUserId(temp.getUser().getId());
//
//        notificationSettingsRepository.save(notificationSettings);
//
//        return notificationSettings;
//    }
}
