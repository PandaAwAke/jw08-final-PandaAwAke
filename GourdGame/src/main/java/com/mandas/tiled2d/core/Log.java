package com.mandas.tiled2d.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static final Logger mandasLogger = LogManager.getLogger("Mandas Logger");
    private static final Logger appLogger = LogManager.getLogger("App Logger");

    public static Logger mandas() {
        return mandasLogger;
    }

    public static Logger app() {
        return appLogger;
    }

}
