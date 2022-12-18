package calendar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "event")
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @JsonIncludeProperties(value = {"id"})
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="organizer_id", referencedColumnName = "id")
    private User organizer;

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

    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Attachment> attachments;

    @OneToMany (mappedBy = "event", cascade = CascadeType.ALL)
    private Set<UserEnrolled> userEnrolled;

    Event () {

    }

    public static class Builder {
        //---required fields---
        private String title;
        private User organizer;
        private LocalDateTime dateTime;
        private int duration;

        //---optional fields---
        private String description = "";
        private boolean isPrivate = false;
        private String location = null;
        private List<Attachment> attachments = null;
        private Set<UserEnrolled> userEnrolled = null;

        public Builder(String title, User organizer, LocalDateTime dateTime, int duration) {
            this.title = title;
            this.organizer = organizer;
            this.dateTime = dateTime;
            this.duration = duration;
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

        public Builder userEnrolled(Set<UserEnrolled> userEnrolled) {
            this.userEnrolled = userEnrolled;
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
       this.userEnrolled = builder.userEnrolled;
    }

    public Event createNewSimpleEvent(String title, User organizer, LocalDateTime dateTime, int duration) {
        return new Event.Builder(title, organizer, dateTime, duration).build();
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
}
