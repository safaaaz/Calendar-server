package calendar.services;

import calendar.DTO.CreateEventDTO;
import calendar.DTO.UpdateEventDTO;
import calendar.controllers.EventController;
import calendar.entities.Event;
import calendar.entities.User;
import calendar.enums.UserRole;
import calendar.exceptions.InvalidEventDurationException;
import calendar.exceptions.EventNotFoundException;
import calendar.exceptions.PastDateException;
import calendar.repositories.EventRepository;
import calendar.utils.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    public static final Logger logger = LogManager.getLogger(EventService.class);

    public Event fetchEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("event not found with id " + id));
    }

    public Event add(CreateEventDTO eventDTO, User organizer) {
        if (Validate.isInPast(eventDTO.dateTime)) {
            throw new PastDateException(eventDTO.dateTime);
        }
        if (!Validate.isValidDuration(eventDTO.duration)) {
            throw new InvalidEventDurationException(eventDTO.duration);
        }

        //TODO: convert datetime from user's UTC time to default UTC with utility class
        //LocalDateTime defaultUtc = Converter.convertToDefaultUtc(eventDTO.dateTime);


        Event.Builder builder = new Event.Builder(eventDTO.title, organizer, eventDTO.dateTime);
        if (!eventDTO.attachments.isEmpty()) {
            builder.attachments(eventDTO.attachments);
        }
        if (!eventDTO.description.isEmpty()) {
            builder.description(eventDTO.description);
        }
        if (!eventDTO.location.isEmpty()) {
            builder.location(eventDTO.location);
        }

        Event event = builder.build();
        eventRepository.save(event);

        return event;
    }

    public Event updateEvent(UpdateEventDTO updateEventDTO, User organizer) {

        // @@@ i think it should be on controller, we should discuss it @@@
        if (Validate.isInPast(updateEventDTO.dateTime)) {
            throw new PastDateException(updateEventDTO.dateTime);
        }
        if (!Validate.isValidDuration(updateEventDTO.duration)) {
            throw new InvalidEventDurationException(updateEventDTO.duration);
        }

        Event.Builder builder = new Event.Builder(updateEventDTO.title, organizer, updateEventDTO.dateTime);

        if (!updateEventDTO.attachments.isEmpty()) {
            builder.attachments(updateEventDTO.attachments);
        }
        if (!updateEventDTO.description.isEmpty()) {
            builder.description(updateEventDTO.description);
        }
        if (!updateEventDTO.location.isEmpty()) {
            builder.location(updateEventDTO.location);
        }

        Event updatedEvent = builder.build();

        Event currentEvent = eventRepository.findById(updateEventDTO.id).get();
        currentEvent.setEvent(updateEventDTO);

        eventRepository.save(currentEvent);

        return currentEvent;
    }

    public List<Event> getEventsByMonth(User user, int month) {
        logger.info("EventService: get event by month " + month);
        List<Event> events = user.getMyOwnedEvents()
                .stream()
                .filter(event -> event.getDateTime().getMonth().getValue() == month)
                .collect(Collectors.toList());
        return events;
    }

    public String deleteEventById(Long id) {
        eventRepository.deleteById(id);
        return "Event has been deleted";
    }

    public Event addGuest(Event event, User user) {

        event.addGuest(user);
        eventRepository.save(event);

        return event;
    }

//    public Event addRole(Event event, User user, UserRole role) {
//        switch (role) {
//            case GUEST:
//                event.addGuest(event, user);
//                break;
//            case ADMIN:
//                break;
//            default:
//        }
//
//        return event;
//    }
}
