package net.orfjackal.experimental;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * @author Esko Luontola
 * @since 15.7.2009
 */
public class SystrayExperiment {

    private static SystemTray systray = SystemTray.getSystemTray();

    public static void main(String[] args) throws Exception {
        System.out.println("tray.getTrayIcons() = " + Arrays.toString(systray.getTrayIcons()));

        PopupMenu popup = new PopupMenu("Popup");
        MenuItem mi = new MenuItem("Woot?");
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Woot!");
            }
        });
        popup.add(mi);

        BufferedImage i1 = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        i1.getGraphics().drawOval(0, 0, 15, 15);
        i1.getGraphics().drawLine(5, 4, 5, 6);
        i1.getGraphics().drawLine(10, 4, 10, 6);
        i1.getGraphics().fillArc(3, 6, 9, 6, 180, 180);
        TrayIcon icon = new TrayIcon(i1);
        icon.setPopupMenu(popup);
        systray.add(icon);

        Thread.sleep(15 * 1000);
        System.exit(0);
    }
}
