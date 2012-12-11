package mandelbrot;

import javax.swing.JProgressBar;


/**
 * The setter computes the actual Mandel Set. 
 * It computes the iterations a point needs to exceed the boundary
 * and stores the value in an array.
 * @author rix
 */
public final class MandelSetter {
    
    private int[][] mandelSet;
    private int[][] Pascal;
    private int width, height;
    private double realWidth, realHeight;
    private double xStart, yStart, xEnd, yEnd;
    private double increment;
    private double juliaX, juliaY;
    private int zoom, zoomFactor = 2;
    private JProgressBar ProgBar;
    private ImagePanel Image;
    
    /*
     * int mode: 2 is standard, everything else are different sets.
     */
    private int mode = 2;
    
    MandelSetter(int width, int height) {
        this.width = width;
        this.height = height;
        
        mandelSet = new int[width+1][height];
        
        generatePascal();
        
        
        
    }
    
    public void reload() {
        
        zoom = 0;
        
        xStart = -3;
        yStart = 2;
        xEnd = 1;
        yEnd = -2;
        
        calculate();
        
        
        
    }
    
    /* To be implemented. 
     * A new array is generated and the old one is
     *      a) copied to the new one; in case the new one
     *         is larger, the missing parts are calculated.
     *      b) copied to the center of the new one.
     */
    public void resize(int width, int height) {
        
        mandelSet = new int[width+1][height];
        
        xEnd = xStart + getIncrement()*width;
        yEnd = yStart - getIncrement()*height;
        
        this.width = width;
        this.height = height;
        
        calculate();
        
        
    }
    
    /* Zooming in. Calls zoom(x,y,factor)*/
    public void zoomIn(int x, int y) {
        double factor;
        switch (zoomFactor) {
            case 1: factor = 1/(2*Math.sqrt(2));
                break;
            case 2: factor = 0.25;
                break;
            case 4: factor = 0.125;
                break;
            default: factor = 1/(2*Math.sqrt(2));
        }
        
        zoom += zoomFactor;
        zoom(x,y,factor);
        
    }
    
    /* Zooming out. Calls zoom(x,y,factor)*/
    public void zoomOut(int x, int y) {
        double factor;
        
        switch (zoomFactor) {
            case 1: factor = 0.5 + ((Math.sqrt(2)-1)/2);
                break;
            case 2: factor = 1;
                break;
            case 4: factor = 2;
                break;
            default: factor = 0.5 + ((Math.sqrt(2)-1)/2);
        }
        
        zoom -= zoomFactor;
        zoom(x,y,factor);
    }
    
    /* Zooming
     * Determines the new range and calls calculate();     
     */
    private void zoom(int x, int y, double factor) {
        double xMiddle,yMiddle;
        
        xMiddle = xStart + x * increment;
        yMiddle = yStart - y * increment;

        xStart = xMiddle - factor * realWidth;
        xEnd = xMiddle + factor * realWidth;
        yStart = yMiddle + factor * realHeight;
        yEnd = yMiddle - factor * realHeight;
        
        calculate();

    }
    
    /*
     * Dragging.
     * Sets the new range and calls calculate()
     * 
     * TODO: copy relevant parts of the array; only calculate 
     * those parts that weren't visible beforehand.
     */
    public void drag(int[] DragArr) {
        
        double dragX, dragY;
        
        dragX = increment*(DragArr[2] - DragArr[0]);
        dragY = increment*(DragArr[3] - DragArr[1]);
        
        xStart -= dragX;
        xEnd -= dragX;
        yStart += dragY;
        yEnd += dragY;
        
        calculate();
    }
    
    /*
     * The actual calculation.
     */
    private void calculate() {
        
        Thread thread = new Thread(new Runnable() {
            double x,y,xPara,yPara,xSave,period,absVal;
            int iteration,maxIteration,periodCount,xIter,yIter;
            double[] XY;
            boolean done;

        
        
            @Override
            public void run(){
                ProgBar.setVisible(true);
        
                maxIteration = 255 + 2*(int)(zoom);
                mandelSet[width][0] = maxIteration;

                realWidth = Math.abs(xStart-xEnd);
                realHeight = Math.abs(yStart-yEnd);
                increment = getIncrement();


                for (yIter = 0; yIter < height; yIter++){

                    for (xIter = 0; xIter < width; xIter++) {

                        /*set x and y to their actual values*/
                        x = xStart + xIter * increment;
                        y = yStart - yIter * increment;

                        xPara = x;
                        yPara = y;

                        iteration = 0;
                        done = false;
                        period = 0;
                        periodCount = 0;

                        /*calculate the sequence; stop if the absolute value has exceeded  
                         * the boundary of 2*/
                        while ((iteration < maxIteration) && (done == false)) {


                            if (mode ==2) {
                                double temp = xPara;
                                xPara = xPara * xPara - yPara * yPara + x;
                                yPara = 2 * temp * yPara +y;
                            } else {
                                XY = getNextXY(xPara,yPara,x,y,mode);

                                xPara = XY[0];
                                yPara = XY[1];
                            }

                            absVal = xPara * xPara + yPara * yPara;

                            if (absVal > 4) {

                                mandelSet[xIter][yIter] = iteration;
                                done = true;

                            /*Stop if periodivity is found */    
                            } else {

                                if (periodCount == 0) {
                                    periodCount = ((iteration / 8) + 1) * 8;
                                    period = absVal;

                                } else {
                                    periodCount --;

                                    if (period == absVal) {
                                        done = true;
                                        mandelSet[xIter][yIter] = -1;
                                    }
                                }

                                if (iteration + 1 == maxIteration) {
                                    mandelSet[xIter][yIter] = -1;
                                }
                            } // END if

                            iteration++;

                        } //END while

                        //int percent = (int)(100* (xIter*width + yIter)   /(width*height)  );


                    } //END for(y)
                    int percent = (int)(100*(1+yIter)/height);
                    ProgBar.setValue(percent);


                } //END for (x)

                ProgBar.setVisible(false);
                ProgBar.setValue(0);

                Image.reDo(mandelSet);
            }});
            thread.start();
    
    }
    
    /*returns the increment to be used for the actual values */
    private double getIncrement() {

         double reVal;

         if (height/width >= 0.8) {
             reVal = realWidth/width;
         } else {
             reVal = realHeight/height;
         }

         return reVal;
     }
    
    public int getZoom() {
        return (int) zoom;
    }
    
    public void setZoom(int zoom) {
        this.zoomFactor = zoom;
    }
    
    public final int[][] getMandelSet() {
        return mandelSet;
    }
    
    public void setMode(int mode) {
        this.mode = mode;
        reload();
    }
    
    public void setMode(int mode, double X, double Y) {
        this.mode = 32 + mode;
        this.juliaX = X;
        this.juliaY = Y;
        reload();
    }
    
    
    /*
     * Seriously, bow before me.
     */
    private double[] getNextXY(double xPara, double yPara, double x, double y, int mode) {
        
        double xTotal, yTotal, temp;
        int outerIterator, innerIterator;
        Boolean julia = false;
        
        if (mode > 32) {
            mode -= 32;
            julia = true;
        } 
        
        xTotal = 0;
        yTotal = 0;
        
        for (outerIterator = 1; outerIterator <= mode + 1; outerIterator++) {
            
            temp = 1;
            
            for (innerIterator = 1; innerIterator <= mode; innerIterator++) {
                
                if (innerIterator <= mode - outerIterator + 1) {
                    temp *= xPara;
                } else {
                    temp *= yPara;
                }
            }
            
            temp *= Pascal[mode][outerIterator - 1];
            
            if ((outerIterator % 4 == 3) || (outerIterator % 4 == 0)) {
                temp *= (-1);
            }
            
            if (outerIterator % 2 == 1) {
                xTotal += temp;
            } else {
                yTotal += temp;
            }
        }
        
        if (julia == false) {
            xTotal += x;
            yTotal += y;
        } else {
            xTotal += this.juliaX;
            yTotal += this.juliaY;
        }
        
        
        return new double[]{xTotal,yTotal};
        
    }
    
    /* 
     * Generates the Pascal Triangle, stored in the private global var Pascal[][]
     */
    private void generatePascal() {
        
        Pascal = new int[16][17];
        
        Pascal[0][0] = 1;
        Pascal[1][0] = 1;
        Pascal[1][1] = 1;
        
        
        for (int row = 2; row  <= 15; row++) {
            
            for(int column = 0; column <= row + 1; column++) {
                
                if ((column == 0) || (column == row)) {
                    Pascal[row][column] = 1;
                } else {
                    Pascal[row][column] = Pascal[row - 1][column - 1] + Pascal[row - 1][column];
                }
            }
        }
        
        System.out.println(Pascal[3][4]);
        
        
    }
    
    public void setProgBar(JProgressBar ProgBar) {
        this.ProgBar = ProgBar;
    }
    
    public void setImage(ImagePanel Image) {
        this.Image = Image;
    }
}
