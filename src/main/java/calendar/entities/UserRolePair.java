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
import java.io.Serializable;
import java.util.Objects;

@Entity
public class UserRolePair implements Serializable {
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

    private UserRolePair(User user, UserRole role) {
        this.user = user;
        this.role = role;
    }

    public static UserRolePair newGuest(User user) {
        return new UserRolePair(user, UserRole.GUEST);
    }

    public static UserRolePair newAdmin(User user) {
        return new UserRolePair(user, UserRole.ADMIN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRolePair)) return false;

        UserRolePair that = (UserRolePair) o;

        if (!user.equals(that.user)) return false;
        return role == that.role;
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public UserRole getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
