package dev.shroysha.widgets.mediaserver.model.plugins;


import dev.shroysha.widgets.mediaserver.model.MediaServerPlugin;

public class MediaServerQuitPlugin extends MediaServerPlugin {


    public String getTitle() {
        return "Quit";
    }


    public void enterPlugin() {
        System.exit(42);
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
