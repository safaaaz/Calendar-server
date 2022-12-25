package calendar.entities;

import calendar.enums.UserRole;
import calendar.enums.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class UserStatusPair implements Serializable {
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

    private UserStatusPair(User user, UserStatus status) {
        this.user = user;
        this.status = status;
    }

    public static UserStatusPair newApproved(User user){
        return new UserStatusPair(user, UserStatus.APPROVED);
    }

    public static UserStatusPair newRejected(User user){
        return new UserStatusPair(user, UserStatus.REJECTED);
    }

    public static UserStatusPair newTentative(User user){
        return new UserStatusPair(user, UserStatus.TENTATIVE);
    }

    public static UserStatusPair newPending(User user){
        return new UserStatusPair(user, UserStatus.PENDING);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserStatusPair)) return false;

        UserStatusPair that = (UserStatusPair) o;

        if (id != that.id) return false;
        if (!Objects.equals(user, that.user)) return false;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserStatus getStatus() {
        return status;
    }
}
