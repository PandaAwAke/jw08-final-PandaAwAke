package com.mandas.tiled2d.core;

import com.mandas.tiled2d.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static final Logger mandasLogger = LogManager.getLogger("MandasLogger");
    private static final Logger appLogger = LogManager.getLogger("AppLogger");
    private static final Logger fileLogger = LogManager.getLogger("FileLogger");
    private static final Logger invalidLogger = LogManager.getLogger("InvalidLogger");

    public static Logger mandas() {
        if (Config.EnableLogging) {
            return mandasLogger;
        }
        return invalidLogger;
    }

    public static Logger app() {
        if (Config.EnableLogging) {
            return appLogger;
        }
        return invalidLogger;
    }

    public static Logger file() {
        if (Config.EnableLogging) {
            return fileLogger;
        }
        return invalidLogger;
    }

}
