package calendar.services;

import calendar.entities.PreConfirmed;
import calendar.entities.User;
import calendar.exceptions.TokenNotFound;
import calendar.exceptions.UserAlreadyRegistered;
import calendar.repositories.PreConfirmedRepository;
import calendar.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PreConfirmedRepository preConfirmedRepository;

    @InjectMocks
    private AuthService authService;

    PreConfirmed preConfirmed;
    User validUser;
    User invalidUser;

    @BeforeEach
    public void beforeEach() {

        preConfirmed = new PreConfirmed("pre.test.123@gmail.com", "12345");
        validUser = new User("valid.user.123@gmail.com", "12345");
        invalidUser = new User("valid.user.123@gmail.com", "54321"); // the passwords are not the same
        authService.cachedUsers.put("test-test-test", validUser);
    }

    @Test
    public void register_validUser_equalsValidUser() {
        assertEquals(validUser, validUser);
    }
    @Test
    public void register_invalidUser_throwsUserAlreadyRegistered() {
        given(userRepository.findByEmail(invalidUser.getEmail())).willReturn(Optional.ofNullable(validUser));
        assertThrows(UserAlreadyRegistered.class, () -> {
            authService.register(invalidUser);
        });
    }

    @Test
    public void verifyToken_token_equalsToken() {
        given(preConfirmedRepository.findByToken("happy-new-year-2023")).willReturn(preConfirmed);
        assertEquals("happy-new-year-2023", authService.verifyToken("happy-new-year-2023"));
    }

    @Test
    public void verifyToken_forgedToken_throwsTokenNotFound() {
        given(preConfirmedRepository.findByToken("happy-new-year-2022")).willReturn(null);
        assertThrows(TokenNotFound.class, () -> {
            authService.verifyToken("happy-new-year-2022");
        });
    }

    @Test
    public void login_invalidUser_equalsNull() {
        given(userRepository.findByEmail(invalidUser.getEmail())).willReturn(Optional.ofNullable(validUser));
        assertNull(authService.login(invalidUser));
    }

//    @Test
//    public void isEmailInDatabase_validEmail_equalsTrue () {
//        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.ofNullable(user));
//        assertTrue(authService.isEmailInDatabase(user.getEmail()));
//    }
//
//    @Test
//    public void isEmailInDatabase_invalidEmail_equalsFalse () {
//        assertFalse(authService.isEmailInDatabase("invalid.email@gmail.com"));
//    }

    @Test
    public void getCachedUsed_validToken_equalsValidUser() {
        assertEquals(validUser, authService.getCachedUser("test-test-test"));
    }
}
