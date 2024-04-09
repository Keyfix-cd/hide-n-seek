package me.gege.util;

import java.util.regex.Pattern;

public class UUIDHelper {

    private UUIDHelper() {
        // Privater Konstruktor, um Instanziierung zu verhindern
    }

    public static boolean isValidUUID(String uuidString) {
        // Regular expression for UUID pattern
        String uuidRegex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(uuidRegex, Pattern.CASE_INSENSITIVE);

        // Match the input string against the pattern
        return pattern.matcher(uuidString).matches();
    }
}
