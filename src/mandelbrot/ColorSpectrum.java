
package mandelbrot;

import java.awt.Color;


/**
 * The ColorSpectrum calculates Colors by a given iteration and maximum
 * iteration. It also holds the ColorArray containing all colors in order
 * of usage.
 * 
 * @author rix
 */
public class ColorSpectrum {
    
    private int[][] ColorArray;
    private int[][] CompleteArray;
    private int colorCount;
    
    
    public ColorSpectrum() {
        
        ColorArray = new int[2][3];
        colorCount = 2;
        
        /* Let's just start with the nice blue-to-white scheme */
        ColorArray[0][0] = 0;
        ColorArray[1][0] = 255;
        ColorArray[0][1] = 0;
        ColorArray[1][1] = 255;
        ColorArray[0][2] = 80;
        ColorArray[1][2] = 255;
        
    }
    
    /*
     * Retrieves the color for a certain iteration.
     */
    public Color getColor(int iteration, int maxiteration) {

        int r,g,b;
        int colorNumber;
        double factor;
        Boolean found;

        if (iteration > 0) {

            r = CompleteArray[iteration][0];
            g = CompleteArray[iteration][1];
            b = CompleteArray[iteration][2];
       
       /* an iteration of -1 indicates that this series is divergent */
        } else {
            r = 0;
            g = 0;
            b = 0;
        }
       
        return new Color(r,g,b);
    }

    
    /*
     * Returns the nth Color in the current scheme.
     * Used by the ColorSelection.
     */
    public Color getColorByNum(int number) {
        return new Color(ColorArray[number-1][0],ColorArray[number-1][1],ColorArray[number-1][2]);
    }

    
    /* Empties the array */
    public void clear() {
        ColorArray = new int[0][3];
        colorCount = 0;
    }

    
    /* 
     * Adds a color. Copies arrays around.
     * Probably the worst possible way to do this.
     */
    public void add(int r, int g, int b) {
        colorCount++;

        int[][] placeHolder;
        placeHolder = new int[colorCount][3];
        
        /* copies ColorArray to placeHolder */
        for (int i = 0; i < colorCount - 1; i++) {
            for (int j = 0; j < 3; j++) {
                placeHolder[i][j] = ColorArray[i][j];
            }
        }

        placeHolder[colorCount - 1][0] = r;
        placeHolder[colorCount - 1][1] = g;
        placeHolder[colorCount - 1][2] = b;

        ColorArray = new int[colorCount][3];
        ColorArray = placeHolder;
    }

    
    public int getColorCount() {
        return colorCount;
    }

    
    /* Changes a specific color */
    public void change(int number, int r, int g, int b) {
        ColorArray[number-1][0] = r;
        ColorArray[number-1][1] = g;
        ColorArray[number-1][2] = b;
    }

    /* 
     * Calculates the color for every iteration between 0 and maxIteration.
     * Insane amounts of energy have been wasted on this cute little algorithm.
     */
    public void calculate(int maxIteration) {
        
        double factor,iStart,iEnd;
        int colorNumber,iteration,r,g,b;

        CompleteArray = new int[maxIteration+1][3];

        for (iteration = 0; iteration <= maxIteration; iteration ++) {
             
            factor = ((double)(iteration)/(double)(maxIteration));
            factor = Math.pow(((2*factor) - (factor * factor)),0.5);
            
            colorNumber = -1;
            iStart = 0;
            iEnd = 0.5;
            
            /* divides the iterations into color areas (logarithmically) */
            for (int i = 0; i < colorCount -2; i++) {

                if ( (factor >= iStart) && (factor< iEnd)) {
                    colorNumber  = i;
                    break;
                }

                iStart = iEnd;
                iEnd += Math.pow(0.5,i+2);

            }

            /* colorNumber remains -1 if it is in the last subset. */
            if (colorNumber == -1) {
                colorNumber = colorCount - 2;
                iEnd = 1;
            }

            factor = (factor - iStart)/(iEnd-iStart);

            r = (int)((1-factor)*ColorArray[colorNumber][0] + factor*ColorArray[colorNumber + 1][0]);
            g = (int)((1-factor)*ColorArray[colorNumber][1] + factor*ColorArray[colorNumber + 1][1]);
            b = (int)((1-factor)*ColorArray[colorNumber][2] + factor*ColorArray[colorNumber + 1][2]);

            CompleteArray[iteration][0] = r;
            CompleteArray[iteration][1] = g;
            CompleteArray[iteration][2] = b;
        }
    }

}
