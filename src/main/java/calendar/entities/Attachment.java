package calendar.entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import javax.persistence.*;

@Entity
@Table(name = "attachment")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@JsonIncludeProperties(value = {"id"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id", referencedColumnName = "id")
    private Event event;

    @Column(name = "attach", nullable = false)
    private String attachment;

    Attachment() {

    }

    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
