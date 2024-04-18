package club.libridge.libridgebackend.gui.frames;

import static club.libridge.libridgebackend.gui.constants.FrameConstants.TABLE_COLOR;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import club.libridge.libridgebackend.gui.constants.FrameConstants;
import club.libridge.libridgebackend.gui.main.ClientApplicationState;
import club.libridge.libridgebackend.gui.painters.Painter;

@SuppressWarnings("serial")
public class SBKingClientJFrame extends JFrame {

    public SBKingClientJFrame() {
        super();
        ClientApplicationState.startAppState();
        initializeFrame();
        initializeContentPane(this);
    }

    private void initializeFrame() {
        this.setVisible(true);
        this.setSize(FrameConstants.getTableWidth(), FrameConstants.getTableHeight());

        this.setApplicationIcon();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Might have to fix this later
        // import java.awt.event.WindowAdapter;
        // import java.awt.event.WindowEvent;
        // this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // this.addWindowListener(new WindowAdapter() {
        // public void windowClosing(WindowEvent e) {
        // System.exit(0);
        // }
        // });
    }

    private void initializeContentPane(SBKingClientJFrame screen) {
        getContentPane().setLayout(null);
        getContentPane().setBackground(TABLE_COLOR);

        Timer resizeDebounceTimer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ClientApplicationState.checkWindowResize(screen.getWidth(), screen.getHeight());
            }
        });
        resizeDebounceTimer.setRepeats(false);

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                ClientApplicationState.invalidateGUIScale();
                resizeDebounceTimer.restart();
            }
        });
    }

    private void setApplicationIcon() {
        String imagePath = "/images/logo/logo.jpg";
        URL logoUrl = getClass().getResource(imagePath);

        // Prevents an application crash in case image is non-existent.
        if (logoUrl != null) {
            ImageIcon img = new ImageIcon(logoUrl);
            this.setIconImage(img.getImage());
        }
    }

    public void paintPainter(Painter painter) {
        this.getContentPane().removeAll();
        painter.paint(this.getContentPane());
        ClientApplicationState.setGUIHasChanged(false);
    }

}
