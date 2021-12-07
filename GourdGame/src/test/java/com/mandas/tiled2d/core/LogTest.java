package com.mandas.tiled2d.core;

import org.junit.Test;

public class LogTest {

    @Test
    public void testMandasLogger() {
        // If Config.EnableLogging is false, these are invalid
        Log.mandas().trace("Mandas Logger Test Trace");
        Log.mandas().debug("Mandas Logger Test Debug");
        Log.mandas().info("Mandas Logger Test Info");
        Log.mandas().warn("Mandas Logger Test Warn");
        Log.mandas().error("Mandas Logger Test Error");
        Log.mandas().fatal("Mandas Logger Test Fatal");

        Log.app().trace("Mandas Logger Test Trace");
        Log.app().debug("Mandas Logger Test Debug");
        Log.app().info("Mandas Logger Test Info");
        Log.app().warn("Mandas Logger Test Warn");
        Log.app().error("Mandas Logger Test Error");
        Log.app().fatal("Mandas Logger Test Fatal");

        Log.file().trace("Mandas Logger Test Trace");
        Log.file().debug("Mandas Logger Test Debug");
        Log.file().info("Mandas Logger Test Info");
        Log.file().warn("Mandas Logger Test Warn");
        Log.file().error("Mandas Logger Test Error");
        Log.file().fatal("Mandas Logger Test Fatal");
    }

}
