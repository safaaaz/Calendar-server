package calendar;

import calendar.entities.NotificationSettings;
import calendar.entities.User;
import calendar.enums.TimeZone;
import calendar.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringApp {

    @Autowired
    UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }
    @EventListener(ApplicationReadyEvent.class)
    public void registerDefaultUsers(){
        List<User> defaultUsers = new ArrayList<>();
        User david = new User("david", "yudin.david@gmail.com", "12345", TimeZone.UTC_2);
        User safa = new User("safaa", "saf@gmail.com", "12345", TimeZone.UTC_12);
        User sharon = new User("sharon", "sharon@gmail.com", "12345", TimeZone.UTC_11);
        NotificationSettings nsDavid = NotificationSettings.defaultSettings(david);
        NotificationSettings nsSharon = NotificationSettings.defaultSettings(sharon);
        NotificationSettings nsSafa = NotificationSettings.defaultSettings(safa);
        david.setNotificationSettings(nsDavid);
        safa.setNotificationSettings(nsSafa);
        sharon.setNotificationSettings(nsSharon);
        defaultUsers.add(david);
        defaultUsers.add(safa);
        defaultUsers.add(sharon);
        userRepository.saveAll(defaultUsers);
    }
}