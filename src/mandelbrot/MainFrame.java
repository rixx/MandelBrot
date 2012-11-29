
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
import javax.swing.JFrame;

/**
 * The MainFrame handles everything else. It has the MenuBar and the
 * StatusBar, it calls on the Setter and acts as Parent for the SchemeSelection.
 * @author rix
 */
public class MainFrame extends JFrame implements ActionListener,MouseListener,ComponentListener{
 
    private ImagePanel Image;
    //private int width, height;
    private MandelMenuBar menuBar;
    private SchemeSelection ColorForm;
    private MandelSetter Setter;
    private MandelStatusBar StatusBar;
    private int[][] MandelArr;
    private int[] DragCoord;
    //private Boolean wasResized = false; 
    private boolean wasResized = false;
    
    public static void main(String[] args) {
        
        MainFrame frame = new MainFrame();
    }

    /*Initializing, initializing*/
    MainFrame() {
        //width = 800;
        //height = 600;
        
        DragCoord = new int[4];
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        setSize(800,600);
        this.setTitle("Mandelbrot Set");
        setBackground(Color.black);
        setLayout(new BorderLayout());
        addMouseListener(this);
        addComponentListener(this);
        
        menuBar = new MandelMenuBar(this);
        setJMenuBar(menuBar);
        
        StatusBar = new MandelStatusBar();
        add(StatusBar, BorderLayout.SOUTH);
        StatusBar.setPreferredSize(new Dimension(this.getWidth(), 16));
        
        Setter = new MandelSetter(getWidth(), getHeight());
        MandelArr = Setter.getMandelSet();
        
        ColorForm = new SchemeSelection(this);
        
        Image = new ImagePanel(getWidth(), getHeight(),MandelArr,this);
        getContentPane().add(Image, BorderLayout.CENTER);
        Image.addMouseListener(this);
        
        setVisible(true);
        StatusBar.setZoom(Setter.getZoom());

        
        
    }
    
    
    /*
     * Handles clicks on the menus.
     */
    //@Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("New")) {
            Setter.reload();
            MandelArr = Setter.getMandelSet();
            Image.reDo(MandelArr);
        }
        else if(ae.getActionCommand().equals("Quit")) {
            System.exit(0);
        } 
        else if (ae.getActionCommand().equals("Color …")) {
           ColorForm.Spectrum = Image.getSpectrum();
           ColorForm.showPanels();
           ColorForm.setVisible(true);
        } else if (ae.getActionCommand().equals("Help")){
            menuBar.showHelp();
        } else if (ae.getActionCommand().equals("About")) {
            menuBar.showAbout();
        }
        else if (ae.getActionCommand().equals("Save")) {
            Image.save();
        }
        else if (ae.getActionCommand().equals("1x")) {
            Setter.setZoom(1);
        }
        else if (ae.getActionCommand().equals("2x")) {
            Setter.setZoom(2);
        }
        else if (ae.getActionCommand().equals("4x")) {
            Setter.setZoom(4);
        }
        else if (ae.getActionCommand().equals("2 (default)")) {
            Setter.setMode(2);
            MandelArr = Setter.getMandelSet();
            Image.reDo(MandelArr);
        }
        else if (ae.getActionCommand().equals("3")) {
            Setter.setMode(3);
            MandelArr = Setter.getMandelSet();
            Image.reDo(MandelArr);
        }
        else if (ae.getActionCommand().equals("4")) {
            Setter.setMode(4);
            MandelArr = Setter.getMandelSet();
            Image.reDo(MandelArr);
        }else if (ae.getActionCommand().equals("5")) {
            Setter.setMode(5);
            MandelArr = Setter.getMandelSet();
            Image.reDo(MandelArr);
        }else if (ae.getActionCommand().equals("6")) {
            Setter.setMode(6);
            MandelArr = Setter.getMandelSet();
            Image.reDo(MandelArr);
        }else if (ae.getActionCommand().equals("7")) {
            Setter.setMode(7);
            MandelArr = Setter.getMandelSet();
            Image.reDo(MandelArr);
        }else if (ae.getActionCommand().equals("8")) {
            Setter.setMode(8);
            MandelArr = Setter.getMandelSet();
            Image.reDo(MandelArr);
        }else if (ae.getActionCommand().equals("9")) {
            Setter.setMode(9);
            MandelArr = Setter.getMandelSet();
            Image.reDo(MandelArr);
        }else if (ae.getActionCommand().equals("10")) {
            Setter.setMode(10);
            MandelArr = Setter.getMandelSet();
            Image.reDo(MandelArr);
        }
    }
    
    /*
     * Handles clicks on the Image.
     * Left click => zooming in; right click => zooming out.
     */
    //@Override
    public void mouseClicked(MouseEvent me) {
        int x = me.getX();
        int y = me.getY();
        int button = me.getButton();
        
        if (button == 1) {
            Setter.zoomIn(x, y);
        } else if (button == 3) {
            Setter.zoomOut(x, y);
        }
        
        MandelArr = Setter.getMandelSet();
        Image.reDo(MandelArr);
        StatusBar.setZoom(Setter.getZoom());
        
    }
    
    /*Gets the mouse coordinates when the mouse is pressed 
     * TODO: Image follows cursor when dragged.
     */
    //@Override
    public void mousePressed(MouseEvent me) {
        if (me.getSource() == Image) {
            DragCoord[0] = me.getX();
            DragCoord[1] = me.getY();
            
        }
            
    }
    
    /* Gets the mouse coordinates when the mouse is released.
     * If the mouse has moved while being pressed, the Image is dragged.
     */
    //@Override
    public void mouseReleased(MouseEvent me) {
        
        if (me.getSource() == Image) {
            DragCoord[2] = me.getX();
            DragCoord[3] = me.getY();

            if ((DragCoord[0] != DragCoord[2]) && (DragCoord[1] != DragCoord[3])) {
                Setter.drag(DragCoord);
                MandelArr = Setter.getMandelSet();
                Image.reDo(MandelArr);

            }
      
        } 
       
    }

    //@Override
    public void mouseEntered(MouseEvent me) {
        
        if (wasResized == true) {
            wasResized = false;
            
            reSize();
        }
    }

    //@Override
    public void mouseExited(MouseEvent me) {
        
    }
    
    //@Override
    public void repaint() {
        Image.setSpectrum(ColorForm.Spectrum);
        Image.reDo(MandelArr);
    }
    
   
    public void reSize() {
        
        Setter.resize(getWidth(), getHeight());
        MandelArr = Setter.getMandelSet();
        
        Image.resize(getWidth(), getHeight(), MandelArr);
        
        
    }

    //@Override
    public void componentResized(ComponentEvent ce) {
        wasResized = true;
        
    }

    //@Override
    public void componentMoved(ComponentEvent ce) {
        
    }

    //@Override
    public void componentShown(ComponentEvent ce) {
        
    }

    //@Override
    public void componentHidden(ComponentEvent ce) {
        
    }
    

}
