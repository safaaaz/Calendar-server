package calendar.services;

import calendar.DTO.CreateEventDTO;
import calendar.DTO.UserDTO;
import calendar.DTO.UpdateEventDTO;
import calendar.controllers.EventController;
import calendar.entities.Event;
import calendar.entities.User;
import calendar.enums.UserRole;
import calendar.exceptions.*;
import calendar.repositories.EventRepository;
import calendar.utils.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        //TODO: convert datetime from user's UTC(+/-) time to default UTC with utility class
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
        builder.isPrivate(eventDTO.isPrivate);


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

    public Event inviteGuest(Event event, User user) {
        if (event.getOrganizer() == user) {
            throw new IllegalOperationException("organizer can't be a guest at his own event");
        }

        if (event.inviteGuest(user) != null) {
            eventRepository.save(event);
        } else {
            throw new UserAlreadyHaveRoleException(UserRole.GUEST);
        }

        return event;
    }

    public User removeGuest(Event event, User user) {
        if (event.removeGuest(user) != null) {
            eventRepository.save(event);
            return user;
        } else {
            throw new IllegalOperationException("user is not a guest in this event");
        }
    }

    public List<Event> getSharedCalendarByMonth(User user, User other, int month) {
        if (!user.getMySharedWithCalendars().contains(other)) {
            throw new IllegalOperationException(String.format("You have to access to %s calendar", other.getName()));
        }

        List<Event> sharedEvents = this.getEventsByMonth(other, month).stream().filter(event -> event.isPrivate() == false).collect(Collectors.toList());
        //TODO: change filter above^ to include private events that user is invited to.
        return sharedEvents;
    }

    public List<UserDTO> shareList(User user) {
      return user.getMySharedWithCalendars().stream().flatMap(u -> Stream.of(UserDTO.convertFromUser(u))).collect(Collectors.toList());
    }
}
