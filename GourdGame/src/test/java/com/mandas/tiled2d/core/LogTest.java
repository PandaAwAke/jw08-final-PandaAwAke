package com.mandas.tiled2d.core;

import org.junit.Test;

public class LogTest {

    @Test
    public void testMandasLogger() {
        Log.mandas().debug("Mandas Logger Test Debug");
        Log.mandas().info("Mandas Logger Test Info");
        Log.mandas().warn("Mandas Logger Test Warn");
        Log.mandas().error("Mandas Logger Test Error");
        Log.mandas().fatal("Mandas Logger Test Fatal");
        Log.mandas().trace("Mandas Logger Test Trace");
    }

}
