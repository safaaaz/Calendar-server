package calendar.entities;

import calendar.enums.Provider;
import calendar.enums.TimeZone;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private Set<UserEnrolled> userEnrolled;
    @Column(name = "auth_provider")
    @Enumerated(EnumType.STRING)
    private Provider provider;
    User() {
    }

    public User(String email, Provider provider) {
        this.email = email;
        this.provider = provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Provider getProvider() {
        return provider;
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
    public NotificationSettings getNotificationSettings() {
        return notificationSettings;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!Objects.equals(id, user.id)) return false;
        if (!Objects.equals(name, user.name)) return false;
        if (!Objects.equals(email, user.email)) return false;
        if (!Objects.equals(password, user.password)) return false;
        if (timeZone != user.timeZone) return false;
        if (!Objects.equals(myOwnedEvents, user.myOwnedEvents))
            return false;
        return Objects.equals(notificationSettings, user.notificationSettings);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
        result = 31 * result + (myOwnedEvents != null ? myOwnedEvents.hashCode() : 0);
        result = 31 * result + (notificationSettings != null ? notificationSettings.hashCode() : 0);
        return result;
    }
}
