package calendar.services;

import calendar.DTO.UserDTO;
import calendar.entities.NotificationSettings;
import calendar.entities.User;
import calendar.exceptions.IllegalOperationException;
import calendar.exceptions.UserAlreadyExistsException;
import calendar.exceptions.UserNotFoundException;
import calendar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }
    /**
     * get the user from the database by his id
     * @param id
     * @return user
     * @throws UserNotFoundException - if user not found
     */
    public User fetchUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user not found with id " + id));
    }

    /**
     * get the user from the database by his email
     * @param email
     * @return user
     * @throws UserNotFoundException - if user not found
     */
    public User fetchUserByEmail(String email) {
       return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("user not found with email " + email));
    }

    /**
     * Update notification settings for the user
     * @param user
     * @param settings
     * @return current user's settings
     */
    public NotificationSettings updateNotificationSettings(User user, NotificationSettings newSettings){
       NotificationSettings currentSettings = user.getNotificationSettings();
        currentSettings.setNotificationSettings(newSettings);
        userRepository.save(user);
        return currentSettings;
    }

    /**
     * This method shares one user calendar with another user
     * @param user - the newly added user
     * @param sharedWithUser
     * @return UserDTO - added user
     */
    public UserDTO addUserToMyCalendars(User user, User sharedWithUser) {
        if (user.equals(sharedWithUser)) {
            throw new IllegalOperationException("can't share calendar with myself");
        }

        if (sharedWithUser.addUserToMyCalendars(user) != null) {
            userRepository.save(sharedWithUser);
        } else {
            throw new UserAlreadyExistsException(sharedWithUser.getEmail());
        }

        return UserDTO.convertFromUser(sharedWithUser);
    }
}
