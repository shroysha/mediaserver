package dev.shroysha.widgets.mediaserver.model;

import java.util.ArrayList;

public abstract class MediaServerPlugin {

    public static final ArrayList<MediaServerPlugin> ALL_PLUGINS = new ArrayList<>();

    public abstract String getTitle();

    public abstract void enterPlugin() throws Exception;

    public abstract void exitPlugin() throws Exception;

    public abstract String addFilesRemotely();

    public abstract String[] getWantedExtensions();


}
