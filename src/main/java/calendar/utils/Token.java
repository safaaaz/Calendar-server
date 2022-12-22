package calendar.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class Token {
    public static String generate() {
        byte[] randomBytes = new byte[24];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }

    // another option ( didn't check)
    /*
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
     */
}