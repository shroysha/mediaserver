package dev.shroysha.widgets.mediaserver.view;

import dev.shroysha.widgets.mediaserver.controller.MediaServerController;
import dev.shroysha.widgets.mediaserver.model.MediaServerPlugin;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MediaServerFrame extends JFrame {
    private static MediaServerFrame msf;

    private MediaServerTitlePanel[] pluginLabels;
    private boolean atMain = true;
    private int pluginLabelIndex = 0;
    private MediaServerTitlePanel currentLabel;
    private JPanel pluginsPanel;
    private JPanel currentPanel;
    private JToolBar toolbar;

    public MediaServerFrame() {
        super("Media Server");
        init();
    }

    public static MediaServerFrame getMediaServerFrame() {
        return msf;
    }

    private void init() {
        if (MediaServerController.notInstalled()) {
            MediaServerController.install();
        }

        atMain = true;
        JPanel contentPanel = new JPanel(new BorderLayout());
        currentPanel = new JPanel(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        makeToolBar();
        makePluginsPanel();

        contentPanel.add(toolbar, BorderLayout.NORTH);
        contentPanel.add(currentPanel, BorderLayout.CENTER);

        showPluginsPanel();

        this.add(contentPanel, BorderLayout.CENTER);

        enterFullScreen();

        pluginLabels[0].requestFocusInWindow();
        msf = this;
    }

    public void enterFullScreen() {
        //wholeThing();
        aLittle();
    }

    public void aLittle() {
        this.setUndecorated(true);
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0, 50, screenSize.width, screenSize.height);
    }

    public void wholeThing() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        this.setUndecorated(true);

        gs.setFullScreenWindow(this);

        //this.setSize(400,600);
        this.setResizable(false);
        System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported());
    }

    public void setIsAtMain(boolean isAtMain) {
        atMain = isAtMain;
    }

    void goDownPluginLabel() {
        pluginLabelIndex++;

        if (pluginLabelIndex >= pluginLabels.length) {
            pluginLabelIndex = 0;
        }

        pluginLabels[pluginLabelIndex].requestFocus();
        currentLabel = pluginLabels[pluginLabelIndex];
    }

    void goUpPluginLabel() {
        pluginLabelIndex--;

        if (pluginLabelIndex < 0) {
            pluginLabelIndex = pluginLabels.length - 1;
        }

        pluginLabels[pluginLabelIndex].requestFocus();
        currentLabel = pluginLabels[pluginLabelIndex];
    }

    private void makePluginsPanel() {
        pluginsPanel = new JPanel(new BorderLayout());
        pluginsPanel.setBackground(Color.red);

        GridLayout layout = new GridLayout(MediaServerPlugin.ALL_PLUGINS.size(), 1);
        JPanel wherePluginsGo = new JPanel(layout) {


            protected void paintComponent(Graphics grphcs) {
                try {
                    super.paintComponent(grphcs);
                    Image image = ImageIO.read(MediaServerFrame.class.getResource("background.jpeg"));
                    grphcs.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
                } catch (IOException ex) {
                    Logger.getLogger(MediaServerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };


        pluginLabels = new MediaServerTitlePanel[MediaServerPlugin.ALL_PLUGINS.size()];
        for (int i = 0; i < MediaServerPlugin.ALL_PLUGINS.size(); i++) {
            pluginLabels[i] = new MediaServerTitlePanel(MediaServerPlugin.ALL_PLUGINS.get(i));
            wherePluginsGo.add(pluginLabels[i]);
        }

        JScrollPane scroller = new JScrollPane();
        scroller.setViewportView(wherePluginsGo);


        pluginsPanel.add(scroller, BorderLayout.CENTER);

        wherePluginsGo.repaint();
    }

    public void setViewedPanel(final JPanel panel) {
        currentPanel.removeAll();
        currentPanel.add(panel, BorderLayout.CENTER);

        SwingUtilities.updateComponentTreeUI(this);
    }

    private void makeToolBar() {
        toolbar = new JToolBar("Actions", JToolBar.HORIZONTAL);

        JButton goHomeButton = new JButton("Home");
        goHomeButton.addActionListener(ae -> goHome());

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(ae -> {
            try {
                if (MediaServerController.getRunningPlugin() != null)
                    MediaServerController.getRunningPlugin().exitPlugin();
                System.exit(43);
            } catch (Exception ignored) {
            }
        });

        toolbar.add(goHomeButton);
        toolbar.add(quitButton);
        toolbar.setFloatable(false);
    }

    public void showPluginsPanel() {
        setViewedPanel(pluginsPanel);

        atMain = true;

        boolean yese = pluginLabels[0].requestFocusInWindow();
        System.out.println(yese);
        try {
            System.out.println(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner().getClass());
        } catch (Exception ex) {
            System.out.println("null");
        }
    }

    public void goHome() {
        try {
            if (!atMain) {
                MediaServerController.getRunningPlugin().exitPlugin();
                showPluginsPanel();
                System.out.println("Went home");
            } else
                pluginLabels[pluginLabelIndex].requestFocusInWindow();
        } catch (Exception ignored) {

        }
    }
}
