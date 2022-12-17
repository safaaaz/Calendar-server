package calendar.services;

import calendar.entities.User;
import calendar.exceptions.UserNotFound;
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

    public User fetchUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFound("user not found with id " + id));
    }
}
