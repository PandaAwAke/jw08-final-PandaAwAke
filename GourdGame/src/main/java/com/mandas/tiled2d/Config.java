package com.mandas.tiled2d;

import java.awt.*;
import java.text.SimpleDateFormat;

public class Config {
    // Core settings
    public static final String Version = "v1.2";
    public static final int MaxFrameRate = 60;

    // Log settings
    public static final String LogFilename = "test.log";
    public static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final boolean EnableLogging = false;

    //      - Render settings
    public static final Font ScoreboardTextFont = new Font("宋体", Font.PLAIN, 36);
    public static final Color FontColor = Color.black;
    public static final Color DefaultBackgroundColor = Color.WHITE;


}
