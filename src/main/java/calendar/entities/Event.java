package calendar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "event")
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //---required fields---
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

    //---optional fields---
    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "access")
    private boolean accessibility;

    @Column(name = "location")
    private String location;

    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Attachment> attachments;

//    private Set<User> guestsAndAdmins;

    Event () {

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

    public boolean isAccessibility() {
        return accessibility;
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

    public void setAccessibility(boolean accessibility) {
        this.accessibility = accessibility;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
