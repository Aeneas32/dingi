/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

import java.awt.Color;

/**
 *  A polyhedron drawn as shaded faces.
 */
public class ShadedFaces extends ColoredFaces {
    
    private Color    color;    // the base color of the faces
    
    /**
     *  Creates a new Solid for a given polyhedron.
     *
     *  @param poly the polyhedron to be drawn
     *  @param colo the color with which to draw
     */
    public ShadedFaces( Polyhedron poly, Color colo ) {
        super(poly);
        color = colo;
    }
    
    /**
     *  Gets the color of a face of the polyhedron.
     *
     *  @param index which face
     *
     *  @return the color of the indexed face
     */
    protected Color getColor( int index ) {
        return color;
    }

}
