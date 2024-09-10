package org.hy.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgentLogger {
    private static final Logger LOGGER = Logger.getLogger(AgentLogger.class.getName());

    static {
        String logConfig = System.getProperty("java.util.logging.config.file");
        if (logConfig == null || logConfig.isEmpty()) {
            Formatter formatter = new HyLogFormatter();
            ConsoleHandler handler = new ConsoleHandler();
            handler.setFormatter(formatter);
            LOGGER.addHandler(handler);
        }
    }

    private static final boolean DEBUG =
            "true".equals(System.getenv("DEBUG"))
                    || "true".equals(System.getProperty("debug"));

    public static void log(Level level, String message, Object... objects) {
        if (LOGGER.isLoggable(level)) {
            LOGGER.log(level, String.format(message, objects));
        }

        if (DEBUG) {
            System.out
                    .format("[%s] %s %s", level, LOGGER.getName(), String.format(message, objects))
                    .println();
        }
    }
}