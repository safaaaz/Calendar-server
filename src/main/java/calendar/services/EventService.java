package calendar.services;

import calendar.DTO.CreateEventDTO;
import calendar.DTO.UserDTO;
import calendar.DTO.UpdateEventDTO;
import calendar.controllers.EventController;
import calendar.entities.Attachment;
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
    @Autowired
    private UserRepository userRepository;

    public static final Logger logger = LogManager.getLogger(EventService.class);
    /**
     * get event from the database that have the id
     * @param id - event id
     * @return event
     * @throws EventNotFoundException - if event not found
     */
    public Event fetchEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("event not found with id " + id));
    }

    /**
     * add new event to the event's table
     * @param eventDTO - the event that the server received from the client
     * @param organizer - the user that add the event
     * @return the new event
     */
    public Event add(CreateEventDTO eventDTO, User organizer) {
        if (!Validate.isValidDuration(eventDTO.duration)) {
            throw new InvalidEventDurationException(eventDTO.duration);
        }

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


    /**
     * Updates an existing event with new details.
     * @param updateEventDTO A data transfer object containing the new event details.
     * @param user The user that is making the request.
     * @return An object to represent the newly updated event.
     */
    public EventController.ResponseUpdatedEvent updateEvent(UpdateEventDTO updateEventDTO, User user) {


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
        Event currentEvent = eventRepository.findById(updateEventDTO.id).get();
        currentEvent.setEvent(updateEventDTO);

        eventRepository.save(currentEvent);
        EventController.ResponseUpdatedEvent resEvent = new EventController.ResponseUpdatedEvent(currentEvent.getId(),currentEvent.getTitle(),user.getEmail());
        return resEvent;
    }

    /**
     * Get events from the database by a specific month
     * @param user
     * @param month
     * @return list of events
     */
    public List<Event> getEventsByMonth(User user, int month) {
        logger.info("EventService: get event by month " + month);
        List<Event> events = Stream.concat(user.getMyOwnedEvents().stream(), user.getSharedEvents().stream())
                .filter(event -> event.getDateTime().getMonth().getValue() == month)
                .collect(Collectors.toList());
        return events;
    }

    /**
     * Delete Event By Id
     * @param id
     * @return success message
     */
    public String deleteEventById(Long id) {

        Event event = fetchEventById(id);
        
        List<User> userList = userRepository.findAll();
        userList.stream().forEach((user) -> {user.getSharedEvents().remove(event);});
        eventRepository.deleteById(id);
        return "Event has been deleted";
    }

    /**
     * Invite guest to an event
     * @param event
     * @param user - the guest
     * @return guest's details
     */
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

        UserDTO userDTO = UserDTO.convertFromUser(user);
        userDTO.setEventId(event.getId());
        return userDTO;
    }

    /**
     * remove guest from the event and delete the event from the shared event
     * @param event
     * @param user
     * @return the removed user
     */
    public User removeGuest(Event event, User user) {
        if (event.removeGuest(user) != null) {
            eventRepository.save(event);
            //user.removeSharedEvent(event);
            return user;
        } else {
            throw new IllegalOperationException("user is not a guest in this event");
        }
    }

    /**
     * function that traverses a list of users and return all of their public events
     * @param user
     * @param others
     * @param month
     * @return map<userEmail,list<events></events>
     */
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

    /**
     * share a list of users
     * @param user
     * @return List<UserDTO>
     */
    public List<UserDTO> shareList(User user) {
      return user.getMySharedWithCalendars().stream().flatMap(u -> Stream.of(UserDTO.convertFromUser(u))).collect(Collectors.toList());
    }

    /**
     * Get user role in the event
     * @param user
     * @param eventId
     * @return UserRole
     */
    public UserRole getUserRole(User user,Long eventId){
        Event event = fetchEventById(eventId);
        if(event.getOrganizer().getId().equals(user.getId())){
            return UserRole.ORGANIZER;
        }
        Set<UserRolePair> userRoles = event.getUserRoles();
        UserRolePair useRole=userRoles.stream().filter((role)->role.getUser().getId()==user.getId()).findAny().get();
        logger.info(useRole);
        return useRole.getRole();
    }

    /**
     * make an invited guest an admin (that can change some of the event's details and invite guests
     * @param event
     * @param user - the guest
     * @return admin user
     */
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
