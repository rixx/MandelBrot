
package mandelbrot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.JProgressBar;


/**
 * The MainFrame handles everything. It has the MenuBar and the
 * StatusBar, it calls on the Setter and acts as Parent for the SchemeSelection
 * and, of course, the ImagePanel.
 * 
 * Also, the different Listeners lie here.
 * 
 * @author rix
 */
public class MainFrame extends JFrame implements ActionListener,MouseListener,ComponentListener,MouseMotionListener{
 
    private ImagePanel Image;
    private MandelMenuBar menuBar;
    private SchemeSelection ColorForm;
    private ModeSelection ModeForm;
    private MandelSetter Setter;
    private MandelStatusBar StatusBar;
    private int[][] MandelArr;
    private int[] DragCoord;
    private boolean wasResized = false;
    private JProgressBar ProgBar;
    
    
    public static void main(String[] args) {
        
        MainFrame frame = new MainFrame();
    }

    
    MainFrame() {
        
        DragCoord = new int[4];
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        this.setSize(800,600);
        this.setTitle("MandelBrot");
        this.setBackground(Color.black);
        this.setLayout(new BorderLayout());
        this.addMouseListener(this);
        this.addComponentListener(this);
        
        menuBar = new MandelMenuBar(this);
        setJMenuBar(menuBar);
        
        StatusBar = new MandelStatusBar();
        StatusBar.setPreferredSize(new Dimension(this.getWidth(), 20));
        this.add(StatusBar, BorderLayout.SOUTH);
        
        ProgBar = new JProgressBar(1,100);
        StatusBar.add(ProgBar, BorderLayout.SOUTH);
        
        Image = new ImagePanel(getWidth(), getHeight(),MandelArr,this);
        this.getContentPane().add(Image, BorderLayout.CENTER);
        Image.addMouseListener(this);
        
        Setter = new MandelSetter(getWidth(), getHeight());
        Setter.setProgBar(ProgBar);
        Setter.setImage(Image);
        Setter.reload();
        
        MandelArr = Setter.getMandelSet();
        StatusBar.setZoom(Setter.getZoom());

        ColorForm = new SchemeSelection(this);
        ModeForm = new ModeSelection(this);
        
        this.setVisible(true);
        
    }
    
    
    /*
     * Handles clicks on the menus.
     * 
     * If I hadn't to experiment with older JDK versions, this
     * would be a switch(ae.getActionCommand()){}
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        
        if (ae.getActionCommand().equals("New")) {
            Setter.reload();
        
        } else if(ae.getActionCommand().equals("Quit")) {
            System.exit(0);
        
        } else if (ae.getActionCommand().equals("Color …")) {
           ColorForm.Spectrum = Image.getSpectrum();
           ColorForm.showPanels();
           ColorForm.setVisible(true);
        
        } else if (ae.getActionCommand().equals("Help")){
            menuBar.showHelp();
        
        } else if (ae.getActionCommand().equals("About")) {
            menuBar.showAbout();
        
        } else if (ae.getActionCommand().equals("Save")) {
            Image.save();
        
        } else if (ae.getActionCommand().equals("1x")) {
            Setter.setZoom(1);
        
        } else if (ae.getActionCommand().equals("2x")) {
            Setter.setZoom(2);
        
        } else if (ae.getActionCommand().equals("4x")) {
            Setter.setZoom(4);
        
        } else if (ae.getActionCommand().equals("Mode …")) {
            ModeForm.setVisible(true);
        }
    }
    
    
    /*
     * Handles clicks on the Image.
     * Left click => zooming in; right click => zooming out.
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        
        int x = me.getX();
        int y = me.getY();
        int button = me.getButton();
        
        if (button == 1) {
            Setter.zoomIn(x, y);
        } else if (button == 3) {
            Setter.zoomOut(x, y);
        }
        
        StatusBar.setZoom(Setter.getZoom());
        
    }
    
    
    /* 
     * Gets the mouse coordinates when the mouse is pressed 
     * TODO: Image follows cursor when dragged. Or not.
     */
    @Override
    public void mousePressed(MouseEvent me) {
    
        if (me.getSource() == Image) {
            DragCoord[0] = me.getX();
            DragCoord[1] = me.getY();
            
        }
    }
    
    
    /* 
     * Gets the mouse coordinates when the mouse is released.
     * If the mouse has moved while being pressed, the Image is dragged.
     */
    @Override
    public void mouseReleased(MouseEvent me) {
        
        if (me.getSource() == Image) {
            DragCoord[2] = me.getX();
            DragCoord[3] = me.getY();

            if ((DragCoord[0] != DragCoord[2]) && (DragCoord[1] != DragCoord[3])) {
                Setter.drag(DragCoord);
            }
        } 
       
    }

    
    /*
     * Resize if the size of the window has changed, upon entering the image.
     */
    @Override
    public void mouseEntered(MouseEvent me) {
        
        if (wasResized == true) {
            wasResized = false;
            reSize();
        }
    }

    @Override
    public void mouseExited(MouseEvent me) { }
    
    
    /*
     * It's called repaint(), but it's really used to re-color.
     */
    @Override
    public void repaint() {
        Image.setSpectrum(ColorForm.Spectrum);
        Image.reDo(MandelArr);
    }
    
    
    /*
     * Resizing.
     */
    public void reSize() {
        
        Setter.resize(getWidth(), getHeight());
        MandelArr = Setter.getMandelSet();
        Image.resize(getWidth(), getHeight(), MandelArr);
        
    }

    
    @Override
    public void componentResized(ComponentEvent ce) {
        wasResized = true;
    }

    @Override
    public void componentMoved(ComponentEvent ce) { }

    @Override
    public void componentShown(ComponentEvent ce) { }

    @Override
    public void componentHidden(ComponentEvent ce) { }
    
    
    public void setMode(int mode) {
        Setter.setMode(mode);
    }
    
    public void setMode(int mode, double X, double Y) {
        Setter.setMode(mode,X,Y);
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        me.translatePoint(me.getComponent().getLocation().x, me.getComponent().getLocation().y);
        Image.setLocation(me.getX(),me.getY());
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        
    }
    
}
