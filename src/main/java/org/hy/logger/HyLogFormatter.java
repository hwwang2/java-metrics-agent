package org.hy.logger;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class HyLogFormatter extends SimpleFormatter {
    private final String format = "[%1s] [%2$-7s] %3$s %n";
    static ZoneId zoneId = ZoneId.systemDefault(); // Use the system default time zone
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public synchronized String format(LogRecord lr) {
        return String.format(format, formatter.format(Instant.ofEpochMilli(lr.getMillis()).atZone(zoneId).toLocalDateTime()),
                lr.getLevel().getLocalizedName(), lr.getMessage());
    }
}
