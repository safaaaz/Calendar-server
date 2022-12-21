package calendar.entities;

import calendar.enums.UserRole;
import calendar.enums.UserStatus;

import javax.persistence.*;

@Entity
public class UserStatusPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    UserStatusPair() {

    }
}
