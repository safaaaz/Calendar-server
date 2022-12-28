package calendar.services;

import calendar.DTO.CreateEventDTO;
import calendar.DTO.UserDTO;
import calendar.DTO.UpdateEventDTO;
import calendar.controllers.EventController;
import calendar.entities.Event;
import calendar.entities.User;
import calendar.entities.UserRolePair;
import calendar.enums.UserRole;
import calendar.exceptions.*;
import calendar.repositories.EventRepository;
import calendar.repositories.UserRepository;
import calendar.utils.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    public static final Logger logger = LogManager.getLogger(EventService.class);
    @Autowired
    private UserRepository userRepository;

    public Event fetchEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("event not found with id " + id));
    }

    public Event add(CreateEventDTO eventDTO, User organizer) {
//        if (Validate.isInPast(eventDTO.dateTime)) {
//            throw new PastDateException(eventDTO.dateTime);
//        }
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

    public EventController.ResponseUpdatedEvent updateEvent(UpdateEventDTO updateEventDTO, User user) {

        // @@@ i think it should be on controller, we should discuss it @@@
        if (Validate.isInPast(updateEventDTO.dateTime)) {
            throw new PastDateException(updateEventDTO.dateTime);
        }
        if (!Validate.isValidDuration(updateEventDTO.duration)) {
            throw new InvalidEventDurationException(updateEventDTO.duration);
        }

        Event.Builder builder = new Event.Builder(updateEventDTO.title, user, updateEventDTO.dateTime);

//        if (!updateEventDTO.attachments.isEmpty()) {
//            builder.attachments(updateEventDTO.attachments);
//        }
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
        EventController.ResponseUpdatedEvent resEvent = new EventController.ResponseUpdatedEvent(currentEvent.getId(),currentEvent.getTitle(),user.getEmail());
        return resEvent;
    }

    public List<Event> getEventsByMonth(User user, int month) {
        logger.info("EventService: get event by month " + month);
        List<Event> events = Stream.concat(user.getMyOwnedEvents().stream(), user.getSharedEvents().stream())
                .filter(event -> event.getDateTime().getMonth().getValue() == month)
                .collect(Collectors.toList());
        return events;
    }

    public String deleteEventById(Long id) {
        eventRepository.deleteById(id);
        return "Event has been deleted";
    }

    public UserDTO inviteGuest(Event event, User user) {
        if (event.getOrganizer() == user) {
            throw new IllegalOperationException("organizer can't be a guest at his own event");
        }

        if (event.inviteGuest(user) != null) {
            eventRepository.save(event);
            user.addSharedEvent(event);
            userRepository.save(user);
        } else {
            throw new UserAlreadyHaveRoleException("User already has a role in this event");
        }

        return UserDTO.convertFromUser(user);
    }

    public User removeGuest(Event event, User user) {
        if (event.removeGuest(user) != null) {
            eventRepository.save(event);
            //user.removeSharedEvent(event);
            return user;
        } else {
            throw new IllegalOperationException("user is not a guest in this event");
        }
    }

    //This function traverses a list of users and return all of their public events.
    public Map<String,List<Event>> getSharedCalendarByMonth(User user, List<User> others, int month) {
        for (User other: others) {
            if (!user.getMySharedWithCalendars().contains(other)) {
                throw new IllegalOperationException(String.format("NO ACCESS to %s calendar", other.getName()));
            }
        }

        //events I want to return: the others' myOwnedEvents WITHOUT privates.
        Map<String,List<Event>> eventsByEmail = new HashMap<>();
        for (User other: others) {
            //filter out private events and events that the other user has been invited to.
            List<Event> otherSharedEvents = this.getEventsByMonth(other, month).stream().filter(event -> event.isPrivate() == false && event.getOrganizer().getId() == other.getId()).collect(Collectors.toList());
            eventsByEmail.put(other.getEmail(), otherSharedEvents);
        }
        return eventsByEmail;
    }

    public List<UserDTO> shareList(User user) {
      return user.getMySharedWithCalendars().stream().flatMap(u -> Stream.of(UserDTO.convertFromUser(u))).collect(Collectors.toList());
    }
    public UserRole getUserRole(User user,Long eventId){
        Event event = fetchEventById(eventId);
        if(event.getOrganizer().getId().equals(user.getId())){
            return UserRole.ORGANIZER;
        }
        Set<UserRolePair> userRoles = event.getUserRoles();
        UserRolePair useRole=userRoles.stream().filter((role)->role.getUser().getId()==user.getId()).findAny().get();
        return useRole.getRole();
    }

    public UserDTO makeAdmin(Event event, User user) {
        boolean alreadyAdmin = event.getUserRoles().stream().anyMatch(pair -> pair.getUser().getId() == user.getId() && pair.getRole() == UserRole.ADMIN);
        if (alreadyAdmin) {
            throw new UserAlreadyHaveRoleException("User already admin");
        }
        if(event.isGuest(user)) {
            UserRolePair rolePair = event.getUserRoles().stream().filter(pair -> pair.getUser().getId() == user.getId()).findAny().get();
            rolePair.setRole(UserRole.ADMIN);
            eventRepository.save(event);
            return UserDTO.convertFromUser(rolePair.getUser());
        } else {
            throw new IllegalOperationException("Admins must be chosen from the guests list");
        }
    }
}
