package calendar.services;

import calendar.entities.Event;
import calendar.exceptions.EventNotFoundException;
import calendar.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event fetchEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("event not found with id " + id));
    }
}
