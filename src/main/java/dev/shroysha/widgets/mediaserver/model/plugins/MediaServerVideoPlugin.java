package dev.shroysha.widgets.mediaserver.model.plugins;


import dev.shroysha.widgets.mediaserver.model.MediaServerPlugin;

public class MediaServerVideoPlugin extends MediaServerPlugin {


    public String getTitle() {
        return "Videos";
    }


    public void enterPlugin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void exitPlugin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public String[] getWantedExtensions() {
        return new String[0];
    }


    public String addFilesRemotely() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
