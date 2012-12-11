
package mandelbrot;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Yay, this is where the magic takes form and rises in form of a beautiful
 * image.
 * 
 * Or ugly, depending on the colors.
 * 
 * Damn, it's like 3 a.m. on a Thursday. This is screwed up.
 * 
 * @author rix
 */
public class ImagePanel extends JPanel {
    BufferedImage image; 
    Graphics2D graphics;
    private ColorSpectrum Spectrum;
    int[][] MandelArr;
    public int width, height, maxIteration;
    private boolean firstTime;
    MainFrame parent;
    
    ImagePanel(int width, int height, int[][] MandelArr, MainFrame parent) {
        this.MandelArr = MandelArr;
        this.width = width;
        this.height = height;
        this.parent = parent;
        
        Spectrum = new ColorSpectrum();
        firstTime = true;
        
    }
    
    /* It's a-painting.
     * Getting the colors for every entry in the array, and setting
     * the pixels by drawing a 1x1 rectangle ... Java.
     */
    @Override
    public void paint(Graphics g) {
        
        if (firstTime == true) {
            image = (BufferedImage) createImage(width, height);
            graphics = image.createGraphics();
            firstTime = false;
        }
            
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(image, 0, 0, this);
        
    }
    
    
    public final void reDo(int[][] Array) {
        int x,y;
        
        this.MandelArr = Array;
        
        if (firstTime == false) {
            Spectrum.calculate(MandelArr[width][0]);

            for (x = 0; x < width; x++) {
                for (y = 0; y < height; y++) {

                    graphics.setColor(Spectrum.getColor(MandelArr[x][y],MandelArr[width][0]));
                    graphics.drawRect(x, y, 1, 1);
                }
            }
        }
        
        repaint();
        
    }
    
    /* 
     * ooooh, image-saving.
     * TODO: add option for .JPG 
     */
    public void save() {
        
        String filename;
        
        filename = JOptionPane.showInputDialog("Dateiname: ");
        filename = filename + ".png";
        
        File file = new File(filename);
        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException ex) {
            System.out.println("Saving doesn't work.");
        }
    }
    
    public void setSpectrum(ColorSpectrum Spectrum) {
        this.Spectrum = Spectrum;
    }
    
    public ColorSpectrum getSpectrum() {
        return Spectrum;
    }
    
    public void resize(int width, int height, int[][] MandelArr) {
        this.width = width;
        this.height = height;
        this.MandelArr = MandelArr;
        
        setSize(width,height);
        
        image = (BufferedImage) createImage(this.width, this.height);
        graphics = image.createGraphics();
        
        reDo(this.MandelArr);
        
    }
}