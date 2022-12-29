package calendar.controllers;

import calendar.DTO.CreateEventDTO;
import calendar.DTO.UpdateEventDTO;
import calendar.DTO.UserDTO;
import calendar.entities.Event;
import calendar.entities.User;
import calendar.exceptions.MissingEventFieldException;
import calendar.services.AuthService;
import calendar.services.EventService;
import calendar.services.NotificationService;
import calendar.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @Mock
    private EventService eventService;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private EventController eventController;

    User validUser;
    Event validEvent;

    @BeforeEach
    void beforeEach() {
        validUser = new User("test.123@gmail.com", "12345");
        validEvent = new Event.Builder("New year's eve", validUser, LocalDateTime.now()).build();
    }

    @Test
    public void fetchEventById_validId_equalsHttpStatusOK() {
        given(eventService.fetchEventById(1L)).willReturn(validEvent);
        assertEquals(HttpStatus.OK, eventController.fetchEventById(1L).getStatusCode());
    }

    @Test
    public void addEvent_createEventDTO_equalsHttpStatusOK() {
        CreateEventDTO validCreateEventDTO = new CreateEventDTO();
        validCreateEventDTO.title = "title";
        validCreateEventDTO.dateTime = LocalDateTime.now();

        given(authService.getCachedUser("token-token-token")).willReturn(validUser);
        given(eventService.add(validCreateEventDTO, validUser)).willReturn(validEvent);
        assertEquals(HttpStatus.OK, eventController.addEvent(validCreateEventDTO, "token-token-token").getStatusCode());
    }

    @Test
    public void addEvent_invalidCreateEventDTO_throwsMissingEventFieldException() {
        CreateEventDTO invalidCreateEventDTO = new CreateEventDTO();

        assertThrows(MissingEventFieldException.class, () -> {
            eventController.addEvent(invalidCreateEventDTO, "token-token-token").getStatusCode();
        });
    }

    @Test
    public void updateEvent_validUpdateEventDTO_equalsHttpStatusOK() {
        UpdateEventDTO validUpdateEventDTO = new UpdateEventDTO();
        validUpdateEventDTO.title = "title";
        validUpdateEventDTO.dateTime = LocalDateTime.now();

        given(authService.getCachedUser("token-token-token")).willReturn(validUser);

        EventController.ResponseUpdatedEvent responseUpdatedEvent = new EventController.ResponseUpdatedEvent(1L, "title", "test.123@gmail.com");

        given(eventService.updateEvent(validUpdateEventDTO, validUser)).willReturn(responseUpdatedEvent);

        assertEquals(HttpStatus.OK, eventController.updateEvent(validUpdateEventDTO, "token-token-token").getStatusCode());
    }

    @Test
    public void updateEvent_invalidUpdateEventDTO_throwsMissingEventFieldException() {
        UpdateEventDTO invalidUpdateEventDTO = new UpdateEventDTO();
        assertThrows(MissingEventFieldException.class, () -> {
           eventController.updateEvent(invalidUpdateEventDTO, "token-token-token");
        });
    }

    @Test
    public void deleteEventById_validId_equalsHttpStatusOK() {
        given(eventService.deleteEventById(1L)).willReturn("Event has been deleted");
        assertEquals(HttpStatus.OK, eventController.deleteEventById(1L).getStatusCode());
    }

    @Test
    public void makeAdmin_validUserDTO_equalsHttpStatusOk(){
        UserDTO validUserDTO = new UserDTO();
        validUserDTO.email = "test.123@gmail.com";

        given(eventService.fetchEventById(1L)).willReturn(validEvent);
        given(userService.fetchUserByEmail(validUserDTO.getEmail())).willReturn(validUser);

        assertEquals(HttpStatus.OK, eventController.makeAdmin(validUserDTO, 1L).getStatusCode());
    }

    @Test
    public void makeAdmin_invalidUserDTO_throwsIllegalArgumentException(){
        UserDTO invalidUserDTO = new UserDTO();

        assertThrows(IllegalArgumentException.class, () -> {
            eventController.makeAdmin(invalidUserDTO, 1L).getStatusCode();
        });
    }
}
