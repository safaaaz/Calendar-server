package calendar.controllers;

import calendar.DTO.CreateEventDTO;
import calendar.entities.Event;
import calendar.entities.User;
import calendar.exceptions.MissingEventFieldException;
import calendar.filter.TokenFilter;
import calendar.services.AuthService;
import calendar.services.EventService;
import calendar.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public ResponseEntity<Event> addEvent(@RequestBody CreateEventDTO createEventDTO) {
        logger.info("In add event function");
        if (isBlank(createEventDTO.title)) {
            throw new MissingEventFieldException("title");
        }
        if (isNull(createEventDTO.organizerId)) {
            throw new MissingEventFieldException("organizer id");
        }
        if (isNull(createEventDTO.dateTime)) {
            throw new MissingEventFieldException("date and time");
        }

        User organizer = userService.fetchUserById(createEventDTO.organizerId);
        //List<Attachment> = readAttachments(createEventDto.attachments);

        return ResponseEntity.ok(eventService.add(createEventDTO, organizer));
    }
    @RequestMapping(value = "getEventsByMonth/{month}", method = RequestMethod.GET)
    public ResponseEntity<List<Event>> getEventsByMonth(@RequestHeader(value="token") String token, @PathVariable int month){
        logger.info("In get events by month function");
        logger.info("user with token: "+token+" want to get his events for month "+month);
        User user = authService.getCachedUser(token);
        user=userService.fetchUserById(user.getId());
        return ResponseEntity.ok(eventService.getEventsByMonth(user,month));
    }
    @RequestMapping(value = "deleteEventById/{eventId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> getEventsByMonth(@PathVariable Long eventId){
        logger.info("In delete events by id function");
        return ResponseEntity.ok(eventService.deleteEventById(eventId));
    }
}
