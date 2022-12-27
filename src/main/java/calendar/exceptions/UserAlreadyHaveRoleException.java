package calendar.exceptions;

import calendar.enums.UserRole;

public class UserAlreadyHaveRoleException extends RuntimeException{

    public UserAlreadyHaveRoleException(UserRole userRole) {
        super("user already has the role: " + userRole.name().toLowerCase());
    }

}
