package mandelbrot;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 * StatusBar, showing the current zoom level.
 * @author rix
 */
public class MandelStatusBar extends JPanel{
    
    private JLabel zoomLabel;
    
    public MandelStatusBar() {
        this.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        
        
        zoomLabel = new JLabel("status");
        zoomLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.add(zoomLabel);
        zoomLabel.setText("Zoom: ");
        
        

    }
    
    public void setZoom(int zoom) {
        zoomLabel.setText("Zoom: "+zoom+"x");
    }
}
