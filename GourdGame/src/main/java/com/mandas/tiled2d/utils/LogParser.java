package com.mandas.tiled2d.utils;

import com.mandas.tiled2d.Config;
import com.mandas.tiled2d.core.Log;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.text.ParseException;
import java.util.Date;

public class LogParser {

    public static final String ContentSeparator = " - ";

    File logFile;
    BufferedReader reader;

    public LogParser(String logFilename) {
        logFile = new File(logFilename);
        try {
            reader = new BufferedReader(new FileReader(logFile));
        } catch (FileNotFoundException e) {
            Log.mandas().error(this.getClass().getName() + ": Log file not found!");
            System.exit(1);
        }
    }

    public LogLine getLine() {
        String line = "";
        try {
            line = reader.readLine();
        } catch (IOException e) {
            Log.mandas().error(this.getClass().getName() + ": getLine - IOException!");
            System.exit(1);
        }
        if (line == null) {
            return null;
        }
        String[] strings = line.split(ContentSeparator, 2);
        if (strings.length < 2) {
            Log.mandas().error(this.getClass().getName() + ": Illegal log line format!");
            System.exit(1);
        }
        String content = strings[1];
        String info = strings[0];
        String[] infoStringsTemp = info.split(" ");
        String[] infoStrings = new String[5];
        int index = 0;
        for (String str : infoStringsTemp) {
            if (str.isEmpty()) {
                continue;
            }
            if (index >= 5) {
                Log.mandas().error(this.getClass().getName() + ": Illegal log line format!");
                System.exit(1);
            }
            infoStrings[index++] = str;
        }

        String timeStr = infoStrings[0] + " " + infoStrings[1];
        Date time = null;
        try {
            time = Config.DateFormat.parse(timeStr);
        } catch (ParseException e) {
            Log.mandas().error(this.getClass().getName() + ": Illegal date time format!");
            System.exit(1);
        }


        Level level = Level.OFF;
        switch (infoStrings[3]) {
            case "TRACE":
                level = Level.TRACE;
                break;
            case "DEBUG":
                level = Level.DEBUG;
                break;
            case "INFO":
                level = Level.INFO;
                break;
            case "WARN":
                level = Level.WARN;
                break;
            case "ERROR":
                level = Level.ERROR;
                break;
            case "FATAL":
                level = Level.FATAL;
                break;
        }
        return new LogLine(content, time, level, infoStrings[4]);
    }

    public void Close() throws IOException {
        reader.close();
    }



    public static class LogLine {
        public boolean valid = false;
        public String content = "";
        public Level level = Level.OFF;
        public String loggerName = "";
        public Date time = null;

        public LogLine() {}

        public LogLine(String content, Date time, Level level, String loggerName) {
            this.valid = true;
            this.content = content;
            this.time = time;
            this.level = level;
            this.loggerName = loggerName;
        }

        public String toString() {
            String result = Config.DateFormat.format(time);
            result += " " + level.toString() + " " + loggerName + ": " + content;
            return result;
        }

    }

}
