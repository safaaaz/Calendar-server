package calendar.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {

    private static final Logger logger = LogManager.getLogger(Validate.class.getName());
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^[0-9]{5,10}$", Pattern.CASE_INSENSITIVE);

    public static boolean email(String email) {

        logger.info("email validation has begun: ");
        boolean status = VALID_EMAIL_ADDRESS_REGEX.matcher(email).find();

        if (status) {
            logger.info("email is valid");
        } else {
            logger.warn("email is not valid");
        }
        return status;
    }

    public static boolean password(String password) {
        logger.info("password validation has begun: ");
        boolean status = VALID_PASSWORD_REGEX.matcher(password).find();
/*
        if (status) {
            logger.info("password is valid");
        } else {
            logger.warn("password is not valid");
        }*/
        return status;
    }

    public static boolean isInPast(LocalDateTime dateTime) {
       return dateTime.isBefore(LocalDateTime.now());
    }

    public static boolean isValidDuration(int duration) {
        return 1 <= duration && duration <= 24;
    }
}