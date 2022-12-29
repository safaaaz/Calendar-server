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

    public void setTitle(String title) {
        this.title = title;
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
        isPrivate = aPrivate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getTitle() {
        return title;
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
