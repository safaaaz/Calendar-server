package calendar.exceptions;


public class MissingEventFieldException extends RuntimeException {
    public MissingEventFieldException(String fieldName) {
        super("Can't create event. A required field is Missing: " + fieldName);
    }
}

