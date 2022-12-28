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
        defaultUsers.add(User.newUserWithDefaultSettings("yudin.david@gmail.com", "12345"));
        defaultUsers.add(User.newUserWithDefaultSettings("saf@gmail.com", "12345"));
        defaultUsers.add(User.newUserWithDefaultSettings("sharon@gmail.com", "12345"));
        userRepository.saveAll(defaultUsers);
    }
}