package calendar.controllers;

import calendar.entities.Event;
import calendar.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @RequestMapping(value = "findOne/{eventId}", method = RequestMethod.GET)
    public ResponseEntity<Event> fetchEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.fetchEventById(eventId));
    }

    //TODO: check how to send event data to controller
//    @RequestMapping(value = "add", method = RequestMethod.GET)
//    public ResponseEntity<Event> addEvent() {
//        return ResponseEntity.ok(eventService.fetchEventById(eventId));
//    }
}
