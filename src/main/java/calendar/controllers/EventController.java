package calendar.controllers;

import calendar.DTO.CreateEventDTO;
import calendar.DTO.UserDTO;
import calendar.entities.Event;
import calendar.entities.User;
import calendar.exceptions.MissingEventFieldException;

import calendar.services.AuthService;
import calendar.services.EventService;
import calendar.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static final Logger logger = LogManager.getLogger(EventController.class);

    @RequestMapping(value = "findOne/{eventId}", method = RequestMethod.GET)
    public ResponseEntity<Event> fetchEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.fetchEventById(eventId));
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<Event> addEvent(@RequestBody CreateEventDTO createEventDTO, @RequestHeader("token") String token) {

        if (isBlank(createEventDTO.title)) {
            throw new MissingEventFieldException("title");
        }
        if (isNull(createEventDTO.dateTime)) {
            throw new MissingEventFieldException("date and time");
        }

        User organizer = authService.getCachedUser(token);
        //User organizer = userService.fetchUserById(createEventDTO.organizerId);
        //List<Attachment> = readAttachments(createEventDto.attachments);

        return ResponseEntity.ok(eventService.add(createEventDTO, organizer));
    }

    @RequestMapping(value = "myEventsByMonth/{month}", method = RequestMethod.GET)
    public ResponseEntity<List<Event>> getMyEventsByMonth(@RequestHeader(value="token") String token, @PathVariable int month){
        logger.info("In get events by month function");
        logger.info("user with token: "+token+" want to get his events for month "+month);
        User user = authService.getCachedUser(token);
        user = userService.fetchUserById(user.getId());

        return ResponseEntity.ok(eventService.getEventsByMonth(user,month));
    }

    //This method gets all user events (private events excluded), if this
    //user has shared his calendar with me.
    @RequestMapping(value = "getSharedCalendarByMonth/{month}", method = RequestMethod.POST)
    public ResponseEntity<List<Event>>getSharedCalendarByMonth(@RequestHeader("token") String token, @PathVariable int month, @RequestBody UserDTO userDTO) {
        if (isNull(month)) {
            throw new MissingEventFieldException("month");
        }
        //TODO: check month number is legal(1-12)
        if (isBlank(userDTO.email)){
            throw new MissingEventFieldException("email");
        }

        User user = authService.getCachedUser(token);
        user = userService.fetchUserById(user.getId());
        User other = userService.fetchUserByEmail(userDTO.email);

        return ResponseEntity.ok(eventService.getSharedCalendarByMonth(user,other,month));
    }

//    @RequestMapping(value = "myEventsByMonth/{month}", method = RequestMethod.GET)
//    public ResponseEntity<Map<String,List<Event>>> getEventsByMonth(@RequestHeader(value="token") String token, @PathVariable int month){
//        logger.info("In get events by month function");
//        logger.info("user with token: " + token + " want to get his events for month " + month);
//        User user = authService.getCachedUser(token);
//        user = userService.fetchUserById(user.getId());             //Why getting the user twice?
//
//        Map<String,List<Event>> calendarsByEmail = new HashMap<>();
//        List<Event> eventsAsOrganizer = eventService.getEventsByMonth(user, month);
//        calendarsByEmail.put(user.getEmail(), eventsAsOrganizer);
//
//        //Collect all shared calendars data into a map (exclude all private events)
//        for (User u: user.getMySharedWithCalendars()) {
//            List<Event> publicEvents = eventService.getEventsByMonth(u, month).stream().filter(event -> event.isPrivate() == false).collect(Collectors.toList());
//            calendarsByEmail.put(u.getEmail(), publicEvents);
//        }
//
//        return ResponseEntity.ok(calendarsByEmail);
//    }
    @RequestMapping(value = "deleteEventById/{eventId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> getEventsByMonth(@PathVariable Long eventId){
        logger.info("In delete events by id function");
        return ResponseEntity.ok(eventService.deleteEventById(eventId));
    }

    @RequestMapping(value = "inviteGuest/{eventId}", method = RequestMethod.POST)
    public ResponseEntity<Event> inviteGuest(@PathVariable Long eventId, @RequestBody UserDTO userDTO) {
        if (isBlank(userDTO.email)) {
            throw new IllegalArgumentException("missing guest email");
        }

        Event event = eventService.fetchEventById(eventId);
        User guest = userService.fetchUserByEmail(userDTO.email);

        return ResponseEntity.ok(eventService.inviteGuest(event, guest));
    }
    @RequestMapping(value = "removeGuest/{eventId}", method = RequestMethod.POST)
    public ResponseEntity<User> removeGuest(@PathVariable Long eventId, @RequestBody UserDTO userDTO) {
        if (isBlank(userDTO.email)) {
            throw new IllegalArgumentException("missing guest email");
        }

        Event event = eventService.fetchEventById(eventId);
        User guest = userService.fetchUserByEmail(userDTO.email);

        return ResponseEntity.ok(eventService.removeGuest(event, guest));
    }
}
