package calendar.entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@JsonIncludeProperties(value = {"id"})
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="event_id", referencedColumnName = "id")
//    private Event event;

    @Column(name = "attach", nullable = false)
    private String attachment;

    Attachment() {

    }

    public Long getId() {
        return id;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attachment)) return false;

        Attachment that = (Attachment) o;

        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(attachment, that.attachment);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (attachment != null ? attachment.hashCode() : 0);
        return result;
    }
}
