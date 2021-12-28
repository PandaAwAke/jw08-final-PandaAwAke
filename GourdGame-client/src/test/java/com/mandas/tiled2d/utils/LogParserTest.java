package com.mandas.tiled2d.utils;

import com.mandas.tiled2d.Config;
import com.mandas.tiled2d.core.Log;
import org.junit.Test;

public class LogParserTest {

    @Test
    public void testLogParser() {
//        Log.file().trace("Mandas Logger Test Trace");
//        Log.file().debug("Mandas Logger Test Debug");
//        Log.file().info("Mandas Logger Test Info");
//        Log.file().warn("Mandas Logger Test Warn");
//        Log.file().error("Mandas Logger Test Error");
//        Log.file().fatal("Mandas Logger Test Fatal");
//
//        Log.file().trace("Mandas Logger Test - ");

        LogParser parser = new LogParser(Config.LogFilename);
        LogParser.LogLine logLine;
        while ((logLine = parser.getLine()) != null) {
            System.out.println(logLine.toString());
        }

    }

}
