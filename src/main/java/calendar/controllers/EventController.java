package calendar.controllers;

import calendar.DTO.CreateEventDTO;
import calendar.entities.Event;
import calendar.entities.User;
import calendar.exceptions.MissingFieldException;
import calendar.services.EventService;
import calendar.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "findOne/{eventId}", method = RequestMethod.GET)
    public ResponseEntity<Event> fetchEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.fetchEventById(eventId));
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<Event> addEvent(@RequestBody CreateEventDTO createEventDTO) {
        if (isBlank(createEventDTO.title) || isNull(createEventDTO.organizerId) || isNull(createEventDTO.dateTime)) {
            throw new MissingFieldException("Can't create event. A required field is Missing");
        }

        //TODO: convert datetime from user's UTC time to default UTC with utility class
        User organizer = userService.fetchUserById(createEventDTO.organizerId);
        //List<Attachment> = readAttachments(createEventDto.attachments);

        return ResponseEntity.ok(eventService.add(createEventDTO, organizer));
    }
}
