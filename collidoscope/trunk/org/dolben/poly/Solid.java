/**
 *  Copyright (c) 2005-2006 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

import java.awt.Graphics;
import org.dolben.iiid.Projector;

/**
 *  Solid is used for drawing a projection of a polyhedron.
 */
public abstract class Solid {
    
    protected Polyhedron polyhedron;  // the polyhedron that is drawn
    
    /**
     *  Creates a new Solid for a given polyhedron.
     *
     *  @param poly the polyhedron to be drawn
     */
    public Solid( Polyhedron poly ) {
        polyhedron = poly;
    }
    
    /**
     *  Gets this Solid's polyhedron.
     *
     *  @return the polyhedron
     */
    public Polyhedron getPolyhedron( ) {
        return polyhedron;
    }
        
    /**
     *  Draws the polyhedron.
	 *
     *  @param projector the projector that maps 3D to Graphics coordinates
     *  @param graphics  the drawing context
     */
    public abstract void paint( Projector projector, Graphics graphics );
    
}
