package calendar.DTO;

import calendar.entities.User;

public class UserDTO {

    public String name;

    public String email;

    public String password;


    public static UserDTO convertFromUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.email = user.getEmail();
        userDTO.name = user.getName();

        return userDTO;
    }
}
