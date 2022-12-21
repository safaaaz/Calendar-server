package calendar;

import calendar.entities.User;
import calendar.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SpringApp {

    @Autowired
    UserRepository userRepository;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void registerUser(){
//        User user = new User("yudin.david@gmail.com", "12345");
//        userRepository.save(user);
//    }
}