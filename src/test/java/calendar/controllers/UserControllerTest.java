package calendar.controllers;

import calendar.entities.NotificationSettings;
import calendar.entities.User;
import calendar.services.AuthService;
import calendar.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    AuthService authService;

    @Mock
    UserService userService;

    @InjectMocks
    private UserController userController;

    User validUser;

    @BeforeEach
    void beforeEach() {
        validUser = new User("test.123@gmail.com", "12345");
    }

    @Test
    public void fetchUserById_userId_equalsHttpStatusOK () {
        given(userService.fetchUserById(1L)).willReturn(validUser);
        assertEquals(HttpStatus.OK, userController.fetchUserById(1L).getStatusCode());
    }


//    @Test
//    public void fetchUserById_wrongUserId_equalsHttpStatusBadRequest () {
//        assertEquals(HttpStatus.BAD_REQUEST, userController.fetchUserById(1L).getStatusCode());
//    }

    @Test
    public void fetchUserByEmail_userEmail_equalsHttpStatusOK () {
        given(userService.fetchUserByEmail(validUser.getEmail())).willReturn(validUser);
        assertEquals(HttpStatus.OK, userController.fetchUserByEmail(validUser.getEmail()).getStatusCode());
    }

//    @Test void updateNotificationSettings_tokenAndNotificationSettings_equalsHttpStatusOK () {
//        given(authService.getCachedUser("start@google")).willReturn(validUser);
//        NotificationSettings notificationSettings = new NotificationSettings(validUser, true, true, true, true, true, true, true, true, true);
//        assertEquals(HttpStatus.OK, userController.updateNotificationSettings("start@google", notificationSettings));
//    }
}
