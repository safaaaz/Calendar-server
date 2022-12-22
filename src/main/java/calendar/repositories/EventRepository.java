package calendar.repositories;

import calendar.entities.Event;
import calendar.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    public Optional<Event> findById(Long id);

    //Optional<Event> findByUser(User user);
}
