package com.mandas.tiled2d.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static final Logger mandasLogger = LogManager.getLogger("MandasLogger");
    private static final Logger appLogger = LogManager.getLogger("AppLogger");
    private static final Logger fileLogger = LogManager.getLogger("FileLogger");

    public static Logger mandas() {
        return mandasLogger;
    }

    public static Logger app() {
        return appLogger;
    }

    public static Logger file() {
        return fileLogger;
    }

}
