package calendar.exceptions;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PastDateException extends RuntimeException{

    public PastDateException(LocalDateTime dateTime) {
        super("Can't add event in past time " + dateTime);
    }

}
