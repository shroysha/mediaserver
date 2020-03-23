package dev.shroysha.widgets.mediaserver.controller;


import dev.shroysha.widgets.mediaserver.model.MediaServerPlugin;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MediaServerNetwork {

    private static final int PORT = 4729;
    private static ServerSocket ss;

    static {
        try {
            initServer();
        } catch (IOException ex) {
            Logger.getLogger(MediaServerNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void initServer() throws IOException {
        ss = new ServerSocket(PORT);
    }

    public static void listenForClients() {
        Thread thread = new Thread() {

            public void run() {
                //noinspection InfiniteLoopStatement
                while (true) {
                    try {
                        Socket socket = ss.accept();
                        new SocketThread(socket).start();
                    } catch (Exception ex) {
                        ex.printStackTrace(System.err);
                    }
                }
            }
        };
        thread.start();
    }


    interface ServerActions {
        String EXTENSIONS = "Extensions";
        String FILETRANSFER = "FT";
    }

    private static class SocketThread extends Thread {

        private final Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        public SocketThread(Socket socket) throws IOException {
            super();
            this.socket = socket;
            openStreams();
        }

        private void openStreams() throws IOException {
            ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("Opened input");
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Opened output");
            System.out.println("Opened streams");
        }


        public void run() {
            boolean connected = true;
            while (connected) {
                try {
                    String message = (String) ois.readObject();
                    if (message.equals(ServerActions.EXTENSIONS)) {
                        String extensions = getAllWantedExtensions();
                        oos.writeObject(extensions);
                    } else if (message.equals(ServerActions.FILETRANSFER)) {
                        //Get how many files they are transfering
                        Integer num = (Integer) ois.readObject();
                        String[] files = new String[num];
                        for (int i = 0; i < files.length; i++) {
                            String fileContent = (String) ois.readObject();
                            files[i] = fileContent;
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(MediaServerNetwork.class.getName()).log(Level.SEVERE, null, ex);
                    connected = false;
                } catch (ClassNotFoundException ignored) {
                }
            }

        }

        private String getAllWantedExtensions() {
            ArrayList<String> extensions = new ArrayList<>();
            for (MediaServerPlugin plugin : MediaServerPlugin.ALL_PLUGINS) {
                String[] pluginExts = plugin.getWantedExtensions();
                for (String ext : pluginExts) {
                    if (!extensions.contains(ext)) {
                        extensions.add(ext);
                    }
                }
            }
            StringBuilder allExts = new StringBuilder();
            while (!extensions.isEmpty()) {
                allExts.append(extensions.remove(0));
                if (!extensions.isEmpty())
                    allExts.append(" ");
            }

            return allExts.toString();
        }
    }
}
