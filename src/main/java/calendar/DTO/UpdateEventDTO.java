package calendar.DTO;

public class UpdateEventDTO extends CreateEventDTO{
    public Long id;

    @Override
    public String toString() {
        return "UpdateEventDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dateTime=" + dateTime +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", isPrivate=" + isPrivate +
                ", location='" + location + '\'' +
                '}';
    }
}
