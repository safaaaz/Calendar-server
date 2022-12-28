package calendar.entities;

import calendar.DTO.UpdateEventDTO;
import calendar.enums.UserRole;
import calendar.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "event")
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @JsonIncludeProperties(value = {"id", "email"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizer_id", referencedColumnName = "id")
    private User organizer;

    @ManyToMany(targetEntity = User.class)
    private List<User> users;
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "duration")
    private int duration;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "access")
    private boolean isPrivate;

    @Column(name = "location")
    private String location;

    //@JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UserRolePair> userRoles;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserStatusPair> userStatuses;

    Event() {

    }

    public UserRolePair inviteGuest(User user) {
        boolean alreadyGuestOrAdmin = userRoles.stream().anyMatch(pair -> pair.getUser().getId() == user.getId() && (pair.getRole() == UserRole.GUEST || pair.getRole() == UserRole.ADMIN));
        if (!alreadyGuestOrAdmin) {
            UserRolePair guestUser = UserRolePair.newGuest(user);
            UserStatusPair pendingUser = UserStatusPair.newPending(user);
            this.userRoles.add(guestUser);
            this.userStatuses.add(pendingUser);
            return guestUser;
        }

        return null;
    }

    public User removeGuest(User user) {
        UserRolePair guest = UserRolePair.newGuest(user);
        if (this.userRoles.contains(guest)) {
            this.userRoles.remove(guest);
            UserStatusPair statusPair = findUserStatusPair(this.userStatuses, user);
            this.userStatuses.remove(statusPair);

            return user;
        }

        return null;
    }

    //This method gets a specific user and return its current arrival status
    public UserStatusPair findUserStatusPair(final Set<UserStatusPair> statusPairs, User user) {
        return statusPairs.stream().filter(pair -> pair.getUser().equals(user)).findFirst().get();
    }

    public boolean isGuest(User user) {
        return userRoles.stream().anyMatch(role -> role.getUser().getId() == user.getId() && role.getRole() == UserRole.GUEST);
    }

    public static class Builder {
        //---required fields---
        private String title;
        private User organizer;
        private LocalDateTime dateTime;


        //---optional fields---
        private int duration = 1;
        private String description = "";
        private boolean isPrivate = false;
        private String location = null;
        private List<Attachment> attachments = null;

        public Builder(String title, User organizer, LocalDateTime dateTime) {
            this.title = title;
            this.organizer = organizer;
            this.dateTime = dateTime;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder isPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder attachments(List<Attachment> attachments) {
            this.attachments = attachments;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }

    private Event(Builder builder) {
        this.title = builder.title;
        this.organizer = builder.organizer;
        this.dateTime = builder.dateTime;
        this.duration = builder.duration;
        this.description = builder.description;
        this.isPrivate = builder.isPrivate;
        this.location = builder.location;
        this.attachments = builder.attachments;
    }

    public Event createNewSimpleEvent(String title, User organizer, LocalDateTime dateTime) {
        return new Event.Builder(title, organizer, dateTime).build();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getOrganizer() {
        return organizer;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getLocation() {
        return location;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrivate(boolean aPrivate) {
        this.isPrivate = aPrivate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Set<UserRolePair> getUserRoles() {
        return userRoles;
    }

    public Set<UserStatusPair> getUserStatuses() {
        return userStatuses;
    }

    public void setUserRoles(Set<UserRolePair> userRoles) {
        this.userRoles = userRoles;
    }

    public void setUserStatuses(Set<UserStatusPair> userStatuses) {
        this.userStatuses = userStatuses;
    }

    public void setEvent(UpdateEventDTO updateEventDTO) {
        title = updateEventDTO.title;
        dateTime = updateEventDTO.dateTime;
        duration = updateEventDTO.duration;
        description = updateEventDTO.description;
        isPrivate = updateEventDTO.isPrivate;
        location = updateEventDTO.location;
        //attachments = updateEventDTO.attachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        if (duration != event.duration) return false;
        if (isPrivate != event.isPrivate) return false;
        if (!Objects.equals(id, event.id)) return false;
        if (!Objects.equals(title, event.title)) return false;
        if (!Objects.equals(organizer, event.organizer)) return false;
        if (!Objects.equals(dateTime, event.dateTime)) return false;
        if (!Objects.equals(description, event.description)) return false;
        if (!Objects.equals(location, event.location)) return false;
        if (!Objects.equals(attachments, event.attachments)) return false;
        if (!Objects.equals(userRoles, event.userRoles)) return false;
        return Objects.equals(userStatuses, event.userStatuses);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (organizer != null ? organizer.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + duration;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (isPrivate ? 1 : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (attachments != null ? attachments.hashCode() : 0);
        result = 31 * result + (userRoles != null ? userRoles.hashCode() : 0);
        result = 31 * result + (userStatuses != null ? userStatuses.hashCode() : 0);
        return result;
    }
}
