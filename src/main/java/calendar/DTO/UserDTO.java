package calendar.DTO;

import calendar.entities.User;

public class UserDTO {

    public String name;

    public String email;

    public String password;

    public Long eventId;
    public static UserDTO convertFromUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.email = user.getEmail();
        userDTO.name = user.getName();

        return userDTO;
    }

    public UserDTO(String name, String email, Long eventId) {
        this.name = name;
        this.email = email;
        this.eventId = eventId;
    }

    public UserDTO(String name, String email, String password, Long eventId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.eventId = eventId;
    }
    public UserDTO(){}
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Long getEventId() {
        return eventId;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", eventId=" + eventId +
                '}';
    }
}
