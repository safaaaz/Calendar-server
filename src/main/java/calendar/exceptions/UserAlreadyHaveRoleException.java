package calendar.exceptions;

import calendar.enums.UserRole;

public class UserAlreadyHaveRoleException extends RuntimeException{

    public UserAlreadyHaveRoleException(String message) {
        super(message);
    }

}
