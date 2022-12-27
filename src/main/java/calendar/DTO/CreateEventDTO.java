package calendar.DTO;

import calendar.entities.Attachment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreateEventDTO {
    public String title;
    public LocalDateTime dateTime;
    public int duration;
    public String description;
    public boolean isPrivate;
    public String location;

    public List<Attachment> attachments;

    @Override
    public String toString() {
        return "CreateEventDTO{" +
                "title='" + title + '\'' +
                ", dateTime=" + dateTime +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", isPrivate=" + isPrivate +
                ", location='" + location + '\'' +
                ", attachmentsList=" + attachments +
                '}';
    }
}
