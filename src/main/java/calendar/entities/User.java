package calendar.entities;

import calendar.enums.TimeZone;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
//@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "FieldHandler"})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "time_zone") // , nullable = false
    @Enumerated(EnumType.STRING)
    private TimeZone timeZone;

    @JsonIgnore
    @OrderBy("dateTime asc")
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
    private List<Event> myOwnedEvents;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private NotificationSettings notificationSettings;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> mySharedWithCalendars;

    User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password, TimeZone timeZone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.timeZone = timeZone;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public List<Event> getMyOwnedEvents() {
        return myOwnedEvents;
    }

//    public List<Permission> getPermissions() {
//        return permissions;
//    }

    public NotificationSettings getNotificationSettings() {
        return notificationSettings;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public void setMyOwnedEvents(List<Event> myOwnedEvents) {
        this.myOwnedEvents = myOwnedEvents;
    }

    public void setNotificationSettings(NotificationSettings notificationSettings) {
        this.notificationSettings = notificationSettings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!name.equals(user.name)) return false;
        if (!email.equals(user.email)) return false;
        return password.equals(user.password);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    public User addUserToMyCalendars(User user) {
        if (!this.mySharedWithCalendars.contains(user)) {
            this.mySharedWithCalendars.add(user);
            return user;
        }

        return null;
    }

    public Set<User> getMySharedWithCalendars() {
        return mySharedWithCalendars;
    }

    public void setMySharedWithCalendars(Set<User> mySharedWithCalendars) {
        this.mySharedWithCalendars = mySharedWithCalendars;
    }
}