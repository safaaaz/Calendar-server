package calendar.services;

import calendar.DTO.CreateEventDTO;
import calendar.entities.Event;
import calendar.entities.User;
import calendar.exceptions.InvalidEventDurationException;
import calendar.exceptions.EventNotFoundException;
import calendar.exceptions.PastDateException;
import calendar.repositories.EventRepository;
import calendar.utils.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

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
}
