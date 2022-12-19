package calendar.exceptions;

public class InvalidEventDurationException extends RuntimeException{

    public InvalidEventDurationException(int duration) {
        super("Invalid event duration " + duration);
    }
}
