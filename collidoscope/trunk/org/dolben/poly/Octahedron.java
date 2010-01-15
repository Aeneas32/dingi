/**
 *  Copyright (c) 2005-2006 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  An octahedron.
 */
public class Octahedron extends Equilateral {
    
    /**
     *  Sets the vertices of an octahedron.
     */
    public void create( ) {
        vertex = P3.allPluses(P3.evenPermutations(new double[] {1,0,0}));
    }

}
