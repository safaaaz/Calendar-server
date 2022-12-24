package calendar.services;

import calendar.entities.NotificationSettings;
import calendar.entities.User;
import calendar.exceptions.UserNotFoundException;
import calendar.repositories.NotificationSettingsRepository;
import calendar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationSettingsRepository notificationSettingsRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User fetchUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user not found with id " + id));
    }

    public User fetchUserByEmail(String email) {
       return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("user not found with email " + email));
    }

    public NotificationSettings updateNotificationSettings(User user, NotificationSettings tempNotificationSettings){

        //NotificationSettings currentSettings = notificationSettingsRepository.findByUserId(user.getId()).orElseThrow(() -> {throw new UserNotFoundException("user was not found, notifications settings update failed");});
        //notificationSettingsRepository.delete(currentSettings);
        //userRepository.delete(user);

        //NotificationSettings newSettings = new NotificationSettings(user,tempNotificationSettings.getByEmail(), tempNotificationSettings.getByPopUp(), tempNotificationSettings.getUserStatusChanged(), tempNotificationSettings.getEventDataChanged(), tempNotificationSettings.getEventCanceled(), tempNotificationSettings.getUserWasUninvited(), tempNotificationSettings.getRemind1MinBefore(), tempNotificationSettings.getRemind5MinBefore(), tempNotificationSettings.getRemind10MinBefore());
        NotificationSettings currentSettings = user.getNotificationSettings();
        currentSettings.updateNotificationSettings(tempNotificationSettings);

        userRepository.save(user);

        //notificationSettingsRepository.save(newSettings);

        return currentSettings;
    }

    //This method shares one user calendar with another user.
    //user - the newly added user.
    public User addUserToMyCalendars(User user, User sharedWithUser) {
        //todo: can't add myself.

        if (sharedWithUser.addUserToMyCalendars(user) != null) {
            userRepository.save(sharedWithUser);
        } else {
            //throw user already in shared set.
        }

        return user;
    }
}
