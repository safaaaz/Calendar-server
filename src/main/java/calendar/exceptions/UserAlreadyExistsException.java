package calendar.exceptions;

import calendar.enums.UserRole;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String email) {
        super(String.format("user %s  already exists", email));
    }
}
