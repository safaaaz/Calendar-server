package calendar.repositories;

import calendar.entities.PreConfirmed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PreConfirmedRepository extends JpaRepository<PreConfirmed, Long> {

    @Query("SELECT u FROM PreConfirmed u WHERE u.token = :token")
    public PreConfirmed findByToken(@Param("token") String token);

    @Query("SELECT u FROM PreConfirmed u WHERE u.email = :email")
    public PreConfirmed findByEmail(@Param("email") String email);
}