package calendar.DTO;

import calendar.entities.Attachment;
import calendar.entities.User;
import calendar.entities.UserEnrolled;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CreateEventDTO {

    public String title;
    public Long organizerId;
    public LocalDateTime dateTime;
    public int duration;
    public String description;
    public boolean isPrivate;
    public String location;
    public List<Attachment> attachments = new ArrayList<>();
    //public Set<UserEnrolled> userEnrolled;
}
