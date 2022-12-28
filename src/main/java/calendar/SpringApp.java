package calendar;

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
}