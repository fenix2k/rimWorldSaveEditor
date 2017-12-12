package RimSaveEditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Logger {
    public static List<String> messages = new ArrayList<>();
    public static List<String> logLevel = Arrays.asList("INFO", "WARNING", "ERROR", "FATAL");


    public static void log(int level, String msg) {
        int lev = level;
        if(lev < 0 || lev > 3) lev = 0;
        String date = new Date().toString();
        String message = date + " [" + logLevel.get(lev) + "] " + msg;

        messages.add(message);
    }

    public static List<String> getLog() {
        return messages;
    }

    public static void clear() {
        messages.clear();
    }
}
