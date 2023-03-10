package calendar.entities;

import calendar.enums.Provider;
import calendar.enums.TimeZone;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "user")
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

    @Column(name = "time_zone")
    @Enumerated(EnumType.STRING)
    private TimeZone timeZone;

    @JsonIgnore
    @OrderBy("dateTime asc")
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
    private List<Event> myOwnedEvents;

    @JsonIgnore
    @ManyToMany(targetEntity = Event.class)
    private List<Event> sharedEvents;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private NotificationSettings notificationSettings;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> mySharedWithCalendars;

    @Column(name = "auth_provider")
    @Enumerated(EnumType.STRING)
    private Provider provider;
    User() {
    }

    public User(String email, Provider provider) {
        this.email = email;
        this.provider = provider;
    }

    public static User newUserWithDefaultSettings( String email, String password) {
        return new User(email,password, TimeZone.UTC, NotificationSettings.defaultSettings());
    }
    private User(String email, String password, TimeZone timeZone, NotificationSettings notificationSettings) {
        this.email = email;
        this.password = password;
        this.timeZone = timeZone;
        this.notificationSettings = notificationSettings;
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

        if (!id.equals(user.id)) return false;
        if (!name.equals(user.name)) return false;
        if (!email.equals(user.email)) return false;
        return password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password);
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

    public List<Event> getSharedEvents() {
        return sharedEvents;
    }

    public void setSharedEvents(List<Event> sharedEvents) {
        this.sharedEvents = sharedEvents;
    }

    public void addSharedEvent(Event event){
        if(sharedEvents==null){
            sharedEvents = new ArrayList<>();
        }
        sharedEvents.add(event);
    }
}