package calendar.services;

import calendar.entities.User;
import calendar.exceptions.IllegalOperationException;
import calendar.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    User validUser;

    @BeforeEach
    public void beforeEach() {

        validUser = new User("valid.user.123@gmail.com", "12345");
    }

    @Test
    public void createUser_validUser_equalsUser() {
        given(userRepository.save(validUser)).willReturn(validUser);
        assertEquals(validUser, userService.createUser(validUser));
    }

    @Test
    public void fetchUserById_validUserId_equalsUser() {
        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(validUser));
        assertEquals(validUser, userService.fetchUserById(1L));
    }

    @Test
    public void fetchUserByEmail_validUserEmail_equalsUser() {
        given(userRepository.findByEmail("valid.user.123@gmail.com")).willReturn(Optional.ofNullable(validUser));
        assertEquals(validUser, userService.fetchUserByEmail("valid.user.123@gmail.com"));
    }

    @Test
    public void addUserToMyCalendars_validUserAndValidUser_throwsIllegalOperationException(){
        assertThrows(IllegalOperationException.class, () -> {
            userService.addUserToMyCalendars(validUser, validUser);
        });
    }
}
