/**
 *  Copyright (c) 2005-2006 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

import java.awt.Color;
import java.awt.Graphics;
import org.dolben.iiid.Projector;
import org.dolben.poly.Polyhedron;

/**
 *  Used for drawing a projection of a polyhedron as a wire frame.
 */
public class WireFrame extends Solid {
    
    private Color color;
    
    /**
     *  Creates a new WireFrame for a given polyhedron.
     *
     *  @param poly the polyhedron to be drawn
     *  @param colo the color with which to draw
     */
    public WireFrame( Polyhedron poly, Color colo ) {
        super(poly);
        color = colo;
    }
        
    /**
     *  Draws the polyhedron.
	 *
     *  @param projector the projector that maps 3D to Graphics coordinates
     *  @param graphics  the drawing context
     */
    public void paint( Projector projector, Graphics graphics ) {
        for ( int i = 0; i < polyhedron.getFaces(); ++i ) {
            paintFace(polyhedron.getFace(i),projector,graphics);
        }
    }
    
    /**
     *  Draws the outline that is the projection of a facet,
     *  given by an array of 3D points.
     *
     *  @param point the array of 3D points that are the vertices of
     *      the facet
     *  @param projector the projector that maps 3D to Graphics coordinates
     *  @param graphics the drawing context
     */
    private void paintFace( double[][] point, Projector projector, Graphics g ) {
        int[][] p = projector.project(point);
        g.setColor(color);
        g.drawPolygon(p[0],p[1],p[0].length);
    }

}
