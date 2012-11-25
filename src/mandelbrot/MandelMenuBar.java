package mandelbrot;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Is it an Image? Is it a Label? No, it is a MenuBar!
 * 
 * @author rix
 */
public class MandelMenuBar extends JMenuBar {
 
    private JMenu imageMenu, setMenu, frzMenu, zoomMenu;
    private JMenuItem newMenu,quitMenu,colorMenu,helpMenu,aboutMenu,saveMenu,zoom2,zoom4,zoom1;
    private MainFrame parent;
    
    public MandelMenuBar(MainFrame parent) {
        
        imageMenu = new JMenu("Image");
        this.add(imageMenu);
        imageMenu.setMnemonic('I');
        
        newMenu = new JMenuItem("New");
        saveMenu = new JMenuItem("Save");
        quitMenu = new JMenuItem("Quit");
        
        newMenu.addActionListener(parent);
        saveMenu.addActionListener(parent);
        quitMenu.addActionListener(parent);
        
        imageMenu.add(newMenu);
        imageMenu.add(saveMenu);
        imageMenu.add(quitMenu);
        
        
        setMenu = new JMenu("Settings");
        this.add(setMenu);
        setMenu.setMnemonic('S');
        
        colorMenu = new JMenuItem("Color …");
        zoomMenu = new JMenu("Zoom");
        
        colorMenu.addActionListener(parent);
        
        setMenu.add(colorMenu);
        setMenu.add(zoomMenu);
        
        zoom1 = new JMenuItem("1x");
        zoom2 = new JMenuItem("2x");
        zoom4 = new JMenuItem("4x");
        
        zoom1.addActionListener(parent);
        zoom2.addActionListener(parent);
        zoom4.addActionListener(parent);
        
        zoomMenu.add(zoom1);
        zoomMenu.add(zoom2);
        zoomMenu.add(zoom4);
        
        
        frzMenu = new JMenu("Help");
        this.add(frzMenu);
        frzMenu.setMnemonic('H');
        
        helpMenu = new JMenuItem("Help");
        helpMenu.addActionListener(parent);
        aboutMenu = new JMenuItem("About");
        aboutMenu.addActionListener(parent);
        frzMenu.add(helpMenu);
        frzMenu.add(aboutMenu);
        
    }
    
    public void showHelp() {
        String myString;
        myString = "It's fairly easy. Click (left) to zoom in, click (right) to zoom out. Drag to ... er ... drag.\n\n";
        myString += "In case something is broken (or for feature requests), feel free to contact \nme at rike.kunze@gmail.com\n\n";
        myString += "Don't panic and have fun.";
        JOptionPane.showMessageDialog(this, myString, "Help", WIDTH);
    }
    
    public void showAbout() {
        String myString;
        myString = "Copyright (c) 2012 rix (rike.kunze@gmail.com)\n\n";
        myString += "Permission is hereby granted, free of charge, to any person obtaining a copy of this software";
        myString += "and associated \ndocumentation files (the \"Software\"), to deal in the Software without restriction, ";
        myString += "including without limitation \nthe rights to use, copy, modify, merge, publish, distribute, sublicense, ";
        myString += "and/or sell copies of the Software, and \nto permit persons to whom the Software is furnished to do so, ";
        myString += "subject to the following conditions:\n\nThe above copyright notice and this permission notice shall ";
        myString += "be included in all copies or substantial portions \nof the Software.\n\nTHE SOFTWARE IS PROVIDED \"AS IS\", ";
        myString += "WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING \nBUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, ";
        myString += "FITNESS FOR A PARTICULAR PURPOSE AND \nNONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS ";
        myString += "BE LIABLE FOR ANY CLAIM, \nDAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING ";
        myString += "FROM, \nOUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.";
        
        JOptionPane.showMessageDialog(this, myString, "About", WIDTH);
    }
    
}
