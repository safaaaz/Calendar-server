package calendar.controllers;

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
public class AuthControllerTest {

    @Mock
    private AuthService authService;
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private User validUser;
    private User invalidEmailParamUser;
    private User invalidPasswordParamUser;

    @BeforeEach
    void beforeEach() {
        validUser = new User("test.123@gmail.com", "12345");

        invalidEmailParamUser = new User("test.123gmail.com", "12345"); // @ is missing
        invalidPasswordParamUser = new User("test.123@gmail.com", "1234"); // password is less than 5 characters
    }

    @Test
    void register_validUser_equalsHttpStatusOK() {
        given(authService.register(validUser)).willReturn(validUser);
        assertEquals(HttpStatus.OK, authController.createUser(validUser).getStatusCode(), "register with valid user parameters had to return status 200");
    }

    @Test
    void register_invalidEmailParamUser_equalsHttpStatusBadRequest() {
        assertEquals(HttpStatus.BAD_REQUEST, authController.createUser(invalidEmailParamUser).getStatusCode(), "register with invalid email parameter had to return status 400");
    }

    @Test
    void register_invalidPasswordParamUser_equalsHttpStatusBadRequest() {
        assertEquals(HttpStatus.BAD_REQUEST, authController.createUser(invalidPasswordParamUser).getStatusCode(), "register with invalid password parameter had to return status 400");
    }

    @Test
    void tokenVerification_invalidToken_equalsHttpStatusBadRequest() {
        assertEquals(HttpStatus.BAD_REQUEST, authController.tokenVerification("forged-token-earth-is-flat").getStatusCode(), "verification with forged token did had to return status 400");
    }

    @Test
    void login_validUser_equalsHttpStatusOk() {
        given(authService.login(validUser)).willReturn("legit-token");
        assertEquals(HttpStatus.OK, authController.login(validUser).getStatusCode(), "login with valid user had to return status 200");
    }

    @Test
    void login_invalidEmailParamUser_equalsHttpStatusNotFound() {
        assertEquals(HttpStatus.NOT_FOUND, authController.login(invalidEmailParamUser).getStatusCode(), "login with invalid password parameter had to return status 400");
    }

    @Test
    void login_invalidPasswordParamUser_equalsHttpStatusNotFound() {
        assertEquals(HttpStatus.NOT_FOUND, authController.login(invalidPasswordParamUser).getStatusCode(), "login with invalid password parameter had to return status 400");
    }
}
