/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

import java.awt.Color;
import java.awt.Graphics;
import org.dolben.iiid.*;
import org.dolben.poly.Polyhedron;

/**
 *  A polyhedron drawn as colored faces.
 */
public class ColoredFaces extends Solid {
    
    protected Lighting lighting; // the lighting of the scene
    
    /**
     *  Creates a new Solid for a given polyhedron.
     *
     *  @param poly the polyhedron to be drawn
     */
    public ColoredFaces( Polyhedron poly ) {
        super(poly);
        lighting = new Lighting();
    }
    
    /**
     *  Gets the color of a polygon based on the number of its sides.
     */
    protected Color getColor( int index ) {
        int[] f = polyhedron.getFaceIndices(index);
        switch ( f.length ) {
        case 3:
            return Color.blue;
        case 4:
            return Color.red;
        case 5:
            return Color.green;
        case 6:
            return Color.cyan;
        case 8:
            return Color.pink;
        case 10:
            return Color.orange;
        default:
            return Color.gray;
        }
    }
    
    /**
     *  Draws the polyhedron.
     *
     *  @param projector the projector that maps 3D to Graphics coordinates
     *  @param graphics  the drawing context
     */
    public void paint( Projector projector, Graphics graphics ) {
        /*
         * Each face polygon is drawn and then filled.
         * If it is only filled, thin gaps are left along the edges.
         *
         * All of the common heavy lifting for both draw and fill
         * is done up front.
         */
        int p[][] = projector.project(polyhedron.getVertices());
        double normal[][] = new double[polyhedron.getFaces()][];
        Color color[] = new Color[normal.length];
        boolean showing[] = new boolean[normal.length];
        for ( int i = 0; i < normal.length; ++i ) {
            double point[][] = polyhedron.getFace(i);
            normal[i] = R3.cross(
                Rn.subtract(point[1],point[0]),Rn.subtract(point[2],point[1])
            );
            showing[i] = Rn.dot(
                    normal[i],Rn.subtract(projector.getViewer(),point[0])
                ) > 0;
            if ( showing[i] ) {
                color[i] = lighting.getShade(normal[i],getColor(i));
            }
        }
        for ( int i = 0; i < polyhedron.getFaces(); ++i ) {
            if ( showing[i] ) {
                graphics.setColor(color[i]);
                int[][] xy = getFacePoints(p,i);
                graphics.drawPolygon(xy[0],xy[1],xy[0].length);
            }
        }
        for ( int i = 0; i < polyhedron.getFaces(); ++i ) {
            if ( showing[i] ) {
                graphics.setColor(color[i]);
                int[][] xy = getFacePoints(p,i);
                graphics.fillPolygon(xy[0],xy[1],xy[0].length);
            }
        }
    }
    
    /**
     *  Returns the x and y coordinates of a projected face
     */
    private int[][] getFacePoints( int[][] vertex, int index ) {
        int[] f = polyhedron.getFaceIndices(index);
        int[][] point = new int[2][f.length];
        for ( int i = 0; i < f.length; ++i ) {
            int j = f[i];
            point[0][i] = vertex[0][j];
            point[1][i] = vertex[1][j];
        }
        return point;
    }

}
