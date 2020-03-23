package dev.shroysha.widgets.mediaserver.model.plugins;


import dev.shroysha.widgets.mediaserver.controller.MediaServerController;
import dev.shroysha.widgets.mediaserver.model.MediaServerPlugin;
import dev.shroysha.widgets.mediaserver.view.MediaServerFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class MediaServerPicturePlugin extends MediaServerPlugin {

    private static final File imageLibraryFile = new File(MediaServerController.getResourceLibrary().getPath() + "Picture");
    private File[] imageSourceFiles;
    private ImageLibrary library;
    private Image currentImages;
    private int currentImageIndex;

    public static File getImageLibraryFile() {
        return imageLibraryFile;
    }


    public String getTitle() {
        return "Pictures";
    }


    public void enterPlugin() throws IOException {
        if (notInstalled()) {
            install();
        }

        library = new ImageLibrary();

        JPanel panel = createPanel();
        MediaServerFrame.getMediaServerFrame().setViewedPanel(panel);
    }

    private JPanel createPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.black);

        PictureScroller scroller = new PictureScroller();
        CurrentPicturePanel cpp = new CurrentPicturePanel();

        contentPanel.add(cpp, BorderLayout.CENTER);
        contentPanel.add(scroller, BorderLayout.SOUTH);

        return contentPanel;
    }


    public void exitPlugin() throws IOException {
        writeImagesToCache();
    }

    private void writeImagesToCache() throws IOException {
        library.writeLibrary();
    }

    public void addFiles() throws IOException {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new FileNameExtensionFilter("Image Files", "gif", "jpeg", "jpg", "png"));
        int n = fc.showOpenDialog(MediaServerFrame.getMediaServerFrame());

        if (n == JFileChooser.APPROVE_OPTION) {
            library.addFiles(fc.getSelectedFiles());
        }
    }

    private boolean notInstalled() {
        return !imageLibraryFile.exists();
    }

    private void install() throws IOException {
        imageLibraryFile.createNewFile();
    }


    public String addFilesRemotely() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public String[] getWantedExtensions() {
        return new String[]{".gif", ".jpg", ".jpeg", ".png"};
    }

    public static class ImageWithSource {

        private final File location;
        private Image image;

        public ImageWithSource(File location) throws IOException {
            super();
            this.location = location;
            image = ImageIO.read(location);
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public String getPath() {
            return location.getPath();
        }
    }

    private class PictureScroller extends JPanel {

        private int imagePanelWidth, imagePanelHeight;
        private JScrollPane scroller;

        public PictureScroller() {
            super(new BorderLayout());
            init();
        }

        private void init() {
            JPanel picturesPanel = new JPanel(new GridLayout(1, library.size()));
            int width = MediaServerFrame.getMediaServerFrame().getWidth();
            int height = MediaServerFrame.getMediaServerFrame().getHeight() / 4;
            picturesPanel.setPreferredSize(new Dimension(width, height));
            picturesPanel.setOpaque(false);

            imagePanelWidth = width / 5;
            imagePanelHeight = height;

            ImagePanel[] imagePanels = new ImagePanel[library.size()];

            for (int i = 0; i < library.size(); i++) {
                ImagePanel ip = new ImagePanel(library.get(i));
                imagePanels[i] = ip;
                picturesPanel.add(ip);
            }

            scroller = new JScrollPane();
            scroller.setViewportView(picturesPanel);

            this.add(scroller, BorderLayout.CENTER);
        }

        public void gotoPicture(int i) {
            int height = (int) ((((double) i - 2.0) / library.size()) * scroller.getHorizontalScrollBar().getMaximum());
            scroller.getHorizontalScrollBar().getModel().setValue(height);
        }

        private class ImagePanel extends JPanel {

            private final ImageWithSource image;

            public ImagePanel(ImageWithSource image) {
                super(new BorderLayout());
                this.image = image;
            }

            private void init() {
                this.setPreferredSize(new Dimension(imagePanelWidth, imagePanelHeight));
                this.setOpaque(false);

                int ratio;
                if (image.getImage().getWidth(this) >= image.getImage().getHeight(this)) {
                    ratio = image.getImage().getWidth(this) / imagePanelWidth;
                } else {
                    ratio = image.getImage().getWidth(this) / imagePanelWidth;
                }

                Image displayPicture = resizeImage(image);
                ImageIcon icon = new ImageIcon(displayPicture);
                JLabel pictureLabel = new JLabel(icon);

                this.add(pictureLabel, BorderLayout.CENTER);
            }

            private Image resizeImage(ImageWithSource iws) {


                return iws.getImage();
            }
        }
    }

    public class ImageLibrary {

        private final File imageLibraryFile;
        private ImageWithSource[] images;

        public ImageLibrary() throws IOException {
            this(MediaServerPicturePlugin.getImageLibraryFile());
        }

        private ImageLibrary(File imageLibraryFile) throws IOException {
            super();
            this.imageLibraryFile = imageLibraryFile;
            init();
        }

        private void init() throws IOException {
            images = getImages();
        }

        private ImageWithSource[] getImages() throws IOException {
            String[] paths = getImagePathsFromFile();
            ImageWithSource[] images2 = new ImageWithSource[paths.length];
            for (int i = 0; i < paths.length; i++) {
                images2[i] = new ImageWithSource(new File(paths[i]));
            }

            return images2;
        }

        private String[] getImagePathsFromFile() throws FileNotFoundException {
            String[] paths;

            Scanner reader = new Scanner(imageLibraryFile);
            ArrayList<String> pathList = new ArrayList<>();
            while (reader.hasNextLine()) {
                pathList.add(reader.nextLine());
            }

            paths = pathList.toArray(new String[0]);

            return paths;
        }

        public void addFiles(File[] files) throws IOException {
            ImageWithSource[] newImages = new ImageWithSource[files.length + images.length];
            for (int i = 0; i < newImages.length; i++) {
                if (i < images.length) {
                    newImages[i] = images[i];
                } else {
                    int position = i - images.length;
                    File file = files[position];
                    newImages[i] = new ImageWithSource(file);
                }
            }
            images = newImages;
        }

        public void writeLibrary() throws IOException {
            BufferedWriter writer = new BufferedWriter(new FileWriter(imageLibraryFile));
            for (int i = 0; i < images.length; i++) {
                writer.write(images[i].getPath());
                if (i != images.length - 1) {
                    writer.write("\n");
                }
            }
        }

        public ImageWithSource get(int i) {
            return images[i];
        }

        public int size() {
            return images.length;
        }

    }


    public class CurrentPicturePanel extends JPanel {

        private static final int margin = 50;
        private ImageWithSource image;

        public CurrentPicturePanel() {
            super();
            init();
        }

        private void init() {
            this.setOpaque(true);
        }

        public void setImage(ImageWithSource image) {
            this.image = image;
            repaint();
        }


        protected void paintComponent(Graphics grphcs) {
            super.paintComponent(grphcs);

            if (image == null)
                return;

            int midPanelX = CurrentPicturePanel.this.getWidth() / 2;
            int midPanelY = CurrentPicturePanel.this.getHeight() / 2;
            int midImageX = image.getImage().getWidth(this);
            int midImageY = image.getImage().getHeight(this);

            int x = midPanelX - midImageX;
            int y = midPanelY - midImageY;

            grphcs.drawImage(image.getImage(), x, y, this);
        }

    }
}
