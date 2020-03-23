package dev.shroysha.widgets.mediaserver.controller;

import dev.shroysha.widgets.mediaserver.model.MediaServerPlugin;

import java.io.File;


public class MediaServerController {

    private static final File resourceLibrary = new File(System.getProperty("user.home") + "/.MediaServer/");
    private static MediaServerPlugin running;



    public static void runPlugin(MediaServerPlugin plugin) {
        try {
            running = plugin;
            plugin.enterPlugin();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            try {
                plugin.exitPlugin();
            } catch (Exception ignored) {

            }
            running = null;
        }
    }

    public static MediaServerPlugin getRunningPlugin() {
        return running;
    }

    public static File getResourceLibrary() {
        return resourceLibrary;
    }

    public static void install() {
        resourceLibrary.mkdir();
    }

    public static boolean notInstalled() {
        return !resourceLibrary.exists();
    }

}
