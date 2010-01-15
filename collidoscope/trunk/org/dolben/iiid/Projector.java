/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.iiid;

/**
 *  Projector is used for drawing a 3D facet in 2D,
 *  either as an outline or as a shaded, filled polygon.
 */
public class Projector {
    
    private double width;       // width of drawing
    private double height;      // height of drawing
    private double screen;      // z component of the screen plane
    private double[] viewer;    // 3D vector location of the viewer
    
    /**
     *  Creates a new Projector.
     *
     *  @param w the width of the drawing
     *  @param h the height of the drawing
     *  @param s the z component of the screen plane
     *  @param v the z component of the location of the viewer
     */
    public Projector( double w, double h, double s, double v ) {
        width = w;
        height = h;
        screen = s;
        viewer = new double[] {0,0,v};
    }
    
    /**
     *  Gets the location of the viewer.
     *
     *  @return the location of the viewer
     */
    public double[] getViewer( ) {
        return viewer;
    }
    
    /**
     *  Projects an array of 3D points onto the drawing plane.
     *
     *  @param point the array of points to be projected
     *
     *  @return array of x and array of y coordinates of projection
     */
    public int[][] project( double[][] point ) {
        double viewd = viewer[2]-screen;
        int[] x = new int[point.length];
        int[] y = new int[point.length];
        for ( int i = 0; i < x.length; ++i ) {
            double[] v = point[i];
            double scale = viewd/(viewer[2]-v[2]);
            x[i] = (int)( scale*v[0]+width/2);
            y[i] = (int)(-scale*v[1]+height/2);
        }
        return new int[][] { x, y };
    }

}
