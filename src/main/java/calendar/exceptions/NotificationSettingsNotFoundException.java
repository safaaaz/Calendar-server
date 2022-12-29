package calendar.exceptions;

public class NotificationSettingsNotFoundException extends RuntimeException {
    public NotificationSettingsNotFoundException(String message) {
        super(message);
    }
}
