/**
 *  Copyright (c) 2005-2006 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  A cuboid.
 */
public class Cuboid extends Polyhedron {
    
    /**
     *  Creates a cuboid, length x width x height.
     */
    public Cuboid( double length, double width, double height ) {
        create(length/2,width/2,height/2);
    }
    
    /**
     *  Creates a cube.
     */
    public Cuboid( ) {
        create();
    }
    
    /**
     *  Gets the corner with all positive components.
     */
    public double[] getExtent( ) {
        return vertex[0];
    }
    
    /**
     * Sets the vertices and faces of a cube.
     */
    public void create( ) {
        create(1,1,1);
    }
    
    /**
     *  Sets the vertices and faces of a cuboid.
     */
    public void create( double l, double w, double h ) {
        vertex = new double[][]{
            { l, w, h},
            {-l, w, h},
            {-l,-w, h},
            { l,-w, h},
            { l, w,-h},
            {-l, w,-h},
            {-l,-w,-h},
            { l,-w,-h}
        };
        face = new int[][]{
            {0,1,2,3},
            {7,6,5,4},
            {0,4,5,1},
            {1,5,6,2},
            {2,6,7,3},
            {3,7,4,0}
        };
    }

}
