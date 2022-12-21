//package calendar.entities;
//
//import calendar.enums.UserRole;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "event_roles")
//public class UserRoles {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "event_id")
//    private Event event;
//
//    @Enumerated(EnumType.STRING)
//    private UserRole role;
//
//    UserRoles() {
//
//    }
//}


package calendar.entities;

import calendar.enums.UserRole;

import javax.persistence.*;

@Entity
public class UserRolePair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    UserRolePair() {

    }

    public UserRolePair(User user, UserRole role) {
        this.user = user;
        this.role = role;
    }

    public static UserRolePair newGuest(User user) {
        return new UserRolePair(user, UserRole.GUEST);
    }

    public static UserRolePair newAdmin(User user) {
        return new UserRolePair(user, UserRole.ADMIN);
    }
}
