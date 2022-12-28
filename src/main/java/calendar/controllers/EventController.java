package calendar.controllers;

import calendar.DTO.CreateEventDTO;
import calendar.DTO.EmailList;
import calendar.DTO.UpdateEventDTO;
import calendar.DTO.UserDTO;
import calendar.entities.Event;
import calendar.entities.User;
import calendar.exceptions.MissingEventFieldException;
import calendar.services.AuthService;
import calendar.services.EventService;
import calendar.services.NotificationService;
import calendar.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static org.hibernate.internal.util.StringHelper.isBlank;

@RestController
@CrossOrigin
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private NotificationService notificationService;

    public static final Logger logger = LogManager.getLogger(EventController.class);

    /**
     * Fetches an existing event by its ID.
     * @param eventId The ID of the event to fetch.
     * @return An object representing the fetched event.
     */
    @RequestMapping(value = "findOne/{eventId}", method = RequestMethod.GET)
    public ResponseEntity<Event> fetchEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.fetchEventById(eventId));
    }



    /**
     * This function adds a new event based on the CreateEventDTO object, and the user token provided in the request header.
     * It checks that the title and dateTime are provided and throws a MissingEventFieldException if either are missing.
     * The function then gets the user from the token and returns the response entity containing the added event.
     *
     * @param createEventDTO The CreateEventDTO object containing the details of the event to add
     * @param token The token of the user who is adding the event
     * @return A response entity containing the added event
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<Event> addEvent(@RequestBody CreateEventDTO createEventDTO, @RequestHeader("token") String token) {

        System.out.println(createEventDTO.toString());

        if (isBlank(createEventDTO.title)) {
            throw new MissingEventFieldException("title");
        }
        if (isNull(createEventDTO.dateTime)) {
            throw new MissingEventFieldException("date and time");
        }

        User organizer = authService.getCachedUser(token);

        return ResponseEntity.ok(eventService.add(createEventDTO, organizer));
    }

    /**
     * Retrieves a list of events belonging to the user provided in the token parameter for the month specified in the month parameter.
     *
     * @param token   the user's token
     * @param month   the month number to retrieve events for
     * @return        ResponseEntity<List<Event>> containing the list of events
     */
    @RequestMapping(value = "myEventsByMonth/{month}", method = RequestMethod.GET)
    public ResponseEntity<List<Event>> getMyEventsByMonth(@RequestHeader(value="token") String token, @PathVariable int month){
        logger.info("In get events by month function");
        logger.info("user with token: "+token+" want to get his events for month "+month);
        User user = authService.getCachedUser(token);
        user = userService.fetchUserById(user.getId());

        return ResponseEntity.ok(eventService.getEventsByMonth(user,month));
    }

    /**
     * This function retrieves events for the user with the given token for the given month.
     * @param token The token of the user who wants to retrieve his events
     * @param month The month for which the events should be retrieved
     * @return ResponseEntity<Map<String,List<Event>>> a response entity containing a list of events for the given month
     */
    @RequestMapping(value = "share/sharedCalendars/{month}", method = RequestMethod.POST)
    public ResponseEntity<Map<String,List<Event>>> sharedCalendarByMonth(@RequestHeader("token") String token, @PathVariable int month, @RequestBody EmailList emailList) {
        //TODO: check month number is legal(1-12)
        if (isNull(month)) {
            throw new MissingEventFieldException("month");
        }

        User user = authService.getCachedUser(token);
        user = userService.fetchUserById(user.getId());
        List<User> others = emailList.getEmails().stream().flatMap(email -> Stream.of(userService.fetchUserByEmail(email)))
                                             .collect(Collectors.toList());

        return ResponseEntity.ok(eventService.getSharedCalendarByMonth(user,others,month));
    }

    /**
     * This function returns a list of users associated with the event that the specified user has shared.
     *
     * @param token the authentication token of the user
     * @return a ResponseEntity containing a list of UserDTOs associated with the event that the specified user has shared
     */
    @RequestMapping(value = "share/shareList", method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> shareList(@RequestHeader("token") String token) {

        User user = authService.getCachedUser(token);
        user = userService.fetchUserById(user.getId());

        return ResponseEntity.ok(eventService.shareList(user));
    }

    /**
     * Deletes the event with the specified eventId
     *
     * @param eventId Long - ID of the event to delete
     *
     * @return ResponseEntity<String> - An HTTP response with the status of the operation
     */
    @RequestMapping(value = "deleteEventById/{eventId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> getEventsByMonth(@PathVariable Long eventId){
        logger.info("In delete events by id function");
        return ResponseEntity.ok(eventService.deleteEventById(eventId));
    }

    /**
     * This function is used to delete an event using its id.
     *
     * @param eventId The eventId of the event to be deleted
     * @return ResponseEntity<String> An HTTP response containing the result of the delete operation
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteEventById(@RequestHeader("eventId") Long eventId){
        logger.info("In delete event by its id");
        return ResponseEntity.ok(eventService.deleteEventById(eventId));
    }

    /**
     * Invite a guest to the event
     *
     * @param eventId the id of the event to invite the guest to
     * @param userDTO the userDTO with the guest's email
     * @return ResponseEntity<UserDTO> of the invited guest
     */
    @RequestMapping(value = "inviteGuest", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> inviteGuest(@RequestHeader Long eventId, @RequestBody UserDTO userDTO) {
        if (isBlank(userDTO.email)) {
            throw new IllegalArgumentException("missing guest email");
        }

        Event event = eventService.fetchEventById(eventId);
        User guest = userService.fetchUserByEmail(userDTO.email);

        return ResponseEntity.ok(eventService.inviteGuest(event, guest));
    }

    /**
     * This function removes a guest from an event based on the eventId and UserDTO.
     *
     * @param eventId the id of the event
     * @param userDTO the UserDTO containing the email of the guest
     * @return the User that was removed
     * @throws IllegalArgumentException if the guest email is blank
     */
    @RequestMapping(value = "removeGuest/{eventId}", method = RequestMethod.POST)
    public ResponseEntity<User> removeGuest(@PathVariable Long eventId, @RequestBody UserDTO userDTO) {
        if (isBlank(userDTO.email)) {
            throw new IllegalArgumentException("missing guest email");
        }

        Event event = eventService.fetchEventById(eventId);
        User guest = userService.fetchUserByEmail(userDTO.email);

        return ResponseEntity.ok(eventService.removeGuest(event, guest));
    }


    /**
     * This function handles the request to update an event.
     *
     * @param updateEventDTO The UpdateEventDTO object containing the information about the event to be updated.
     * @param token The token of the user associated with the event.
     *
     * @return A ResponseEntity containing a ResponseUpdatedEvent object.
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<ResponseUpdatedEvent> updateEvent(@RequestBody UpdateEventDTO updateEventDTO, @RequestHeader("token") String token) {

        System.out.println(updateEventDTO.toString());

        if (isBlank(updateEventDTO.title)) {
            throw new MissingEventFieldException("title");
        }
        if (isNull(updateEventDTO.dateTime)) {
            throw new MissingEventFieldException("date and time");
        }

        User user = authService.getCachedUser(token);

        notificationService.sendUpdateEventNotification(updateEventDTO);

        return ResponseEntity.ok(eventService.updateEvent(updateEventDTO, user));
    }


    public static class ResponseUpdatedEvent{
        private Long eventId;
        private String eventTitle;
        private String editorEmail;

        @Override
        public String toString() {
            return "ResponseUpdatedEvent{" +
                    "eventId=" + eventId +
                    ", eventTitle='" + eventTitle + '\'' +
                    ", editorEmail='" + editorEmail + '\'' +
                    '}';
        }

        public ResponseUpdatedEvent(Long eventId, String eventTitle, String editorEmail) {
            this.eventId = eventId;
            this.eventTitle = eventTitle;
            this.editorEmail = editorEmail;
        }
        public ResponseUpdatedEvent(){}
        public Long getEventId() {
            return eventId;
        }

        public String getEventTitle() {
            return eventTitle;
        }

        public String getEditorEmail() {
            return editorEmail;
        }
    }


    /**
     * Makes a User an Admin of an Event.
     *
     * @param userDTO A UserDTO object containing the email address of the User to be made an Admin.
     * @param eventId The unique identifier of the Event the User is to be made an Admin of.
     * @return A ResponseEntity object containing the UserDTO object of the User made an Admin.
     */
    @RequestMapping(value = "/makeAdmin", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> makeAdmin(@RequestBody UserDTO userDTO, @RequestHeader("eventId") Long eventId) {
        if (isBlank(userDTO.email)) {
            throw new IllegalArgumentException("missing guest email");
        }

        Event event = eventService.fetchEventById(eventId);
        User user = userService.fetchUserByEmail(userDTO.email);

        return ResponseEntity.ok(eventService.makeAdmin(event,user));
    }
}
