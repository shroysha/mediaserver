package dev.shroysha.widgets.mediaserver.view;

import dev.shroysha.widgets.mediaserver.controller.MediaServerController;
import dev.shroysha.widgets.mediaserver.model.MediaServerPlugin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;


public class MediaServerTitlePanel extends JPanel implements FocusListener, KeyListener, MouseListener {

    private final MediaServerPlugin plugin;
    private JLabel label;

    public MediaServerTitlePanel(MediaServerPlugin plugin) {
        super();
        this.plugin = plugin;
        init();
    }

    private void init() {
        this.setBackground(Color.black);
        this.setBorder(new EmptyBorder(10, 50, 10, 50));
        this.setOpaque(false);

        label = new JLabel(plugin.getTitle());

        Font font = new Font(Font.MONOSPACED, Font.BOLD, 24);
        label.setFont(font);
        label.setForeground(Color.white);

        this.setLayout(new BorderLayout());
        this.add(label, BorderLayout.CENTER);

        this.addKeyListener(this);
        this.addFocusListener(this);
        this.addMouseListener(this);
    }


    public void focusGained(FocusEvent fe) {
        label.setForeground(Color.green.brighter());
        System.out.println(plugin.getTitle() + " got focus");
    }


    public void focusLost(FocusEvent fe) {
        label.setForeground(Color.white);
        System.out.println(plugin.getTitle() + " lost focus");
    }


    public void keyTyped(KeyEvent ke) {
    }


    public void keyPressed(KeyEvent ke) {
        keyHit(ke);
    }


    public void keyReleased(KeyEvent ke) {
    }

    public void keyHit(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER || ke.getKeyCode() == KeyEvent.VK_SPACE) {
            MediaServerController.runPlugin(plugin);
        } else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
            MediaServerFrame.getMediaServerFrame().goDownPluginLabel();
        } else if (ke.getKeyCode() == KeyEvent.VK_UP) {
            MediaServerFrame.getMediaServerFrame().goUpPluginLabel();
        }
    }


    public void mouseClicked(MouseEvent me) {
        MediaServerController.runPlugin(plugin);
    }


    public void mousePressed(MouseEvent me) {
    }


    public void mouseReleased(MouseEvent me) {
    }


    public void mouseEntered(MouseEvent me) {
        this.requestFocus();
    }


    public void mouseExited(MouseEvent me) {
    }


}
