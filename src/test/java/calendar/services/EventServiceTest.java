package calendar.services;


import calendar.DTO.CreateEventDTO;
import calendar.DTO.UserDTO;
import calendar.entities.Event;
import calendar.entities.User;
import calendar.entities.UserRolePair;
import calendar.enums.UserRole;
import calendar.exceptions.EventNotFoundException;
import calendar.exceptions.IllegalOperationException;
import calendar.exceptions.InvalidEventDurationException;
import calendar.exceptions.UserAlreadyHaveRoleException;
import calendar.repositories.EventRepository;
import calendar.repositories.UserRepository;
import calendar.utils.Validate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private User anotherUser;

    private User organizer;
    private Event event;

    private UserRolePair guestRolePair;
    private UserRolePair adminRolePair;

    @BeforeEach
    void setUp() {
        user = User.newUserWithDefaultSettings("test@gmail.com", "12345");
        user.setId(1L);
        anotherUser = User.newUserWithDefaultSettings("another@gmail.com", "12345");
        anotherUser.setId(2L);
        organizer = User.newUserWithDefaultSettings("another@gmail.com", "12345");
        organizer.setId(2L);
        event = Event.createNewSimpleEvent("Event1", organizer, LocalDateTime.now());
        //event.setUserRoles();
        event.setId(1L);

        guestRolePair = UserRolePair.newGuest(user);
        adminRolePair = UserRolePair.newAdmin(user);
    }

    @Test
    @DisplayName("Event is returned when calling fetchEventById with existing event id")
    void fetchEventById_givenId_getEvent() {
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
        assertEquals(event, eventService.fetchEventById(1L));
    }

    @Test
    @DisplayName("fetching a non-existing event id throws exception")
    void fetchEventById_givenNonExistingId_throwsEventNotFoundException() {
        given(eventRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> eventService.fetchEventById(1L));
    }

    @Test
    @DisplayName("delete an existing event")
    void delete_givenEventId_eventDeleted() {
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
        given(userRepository.findAll()).willReturn(new ArrayList<>());
        assertEquals("Event has been deleted", eventService.deleteEventById(1L));
    }

    @Test
    @DisplayName("delete a non existing event - FAIL")
    void delete_givenNonExistingEventId_fail() {
        given(eventRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> eventService.fetchEventById(1L));
    }

    @Test
    @DisplayName("organizer invites himself as a guest - FAIL")
    void inviteGuest_givenOrganizer_fail() {
        event.setOrganizer(user);
        assertThrows(IllegalOperationException.class, () -> eventService.inviteGuest(event, user));
    }

    @Test
    @DisplayName("invite guest that is already in the guest list - FAIL")
    void inviteGuest_givenUserAlreadyGuest_fail() {
        event.getUserRoles().add(guestRolePair);
        assertThrows(UserAlreadyHaveRoleException.class, () -> eventService.inviteGuest(event, user));
    }

    @Test
    @DisplayName("invite guest that is already is an admin - FAIL")
    void inviteGuest_givenUserAlreadyAdmin_fail() {
        event.getUserRoles().add(adminRolePair);
        assertThrows(UserAlreadyHaveRoleException.class, () -> eventService.inviteGuest(event, user));
    }

    @Test
    @DisplayName("user role in event - organizer")
    void getUserRole_givenOrganizer_organizer() {
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
        event.setOrganizer(user);
        assertEquals(UserRole.ORGANIZER, eventService.getUserRole(user,event.getId()));
    }

    @Test
    @DisplayName("user role in event - admin")
    void getUserRole_givenOrganizer_admin() {
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
        event.getUserRoles().add(adminRolePair);
        assertEquals(UserRole.ADMIN, eventService.getUserRole(user,event.getId()));
    }

    @Test
    @DisplayName("user role in event - guest")
    void getUserRole_givenOrganizer_guest() {
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
        event.getUserRoles().add(guestRolePair);
        assertEquals(UserRole.GUEST, eventService.getUserRole(user,event.getId()));
    }

    @Test
    @DisplayName("make admin when user already admin")
    void makeAdmin_givenAdmin_fail() {
        event.getUserRoles().add(adminRolePair);
        assertThrows(UserAlreadyHaveRoleException.class, () -> eventService.makeAdmin(event, user));
    }

    @Test
    @DisplayName("make admin when user is not guest")
    void makeAdmin_givenNotGuest_fail() {
        assertThrows(IllegalOperationException.class, () -> eventService.makeAdmin(event, user));
    }
}
