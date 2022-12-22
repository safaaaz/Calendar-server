package calendar.DTO;

import calendar.entities.Attachment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreateEventDTO {

    public String title;
    //public Long organizerId;
    public LocalDateTime dateTime;
    public int duration;
    public String description;
    public boolean isPrivate;
    public String location;
    public List<Attachment> attachments = new ArrayList<>();
    //public Set<UserEnrolled> userEnrolled;
}
