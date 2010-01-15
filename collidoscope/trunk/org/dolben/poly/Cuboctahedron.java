/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  A cuboctahedron.
 */
public class Cuboctahedron extends Equilateral {
    
    /**
     *  Sets the vertices of a cuboctahedron.
     */
    public void create( ) {
        vertex = P3.allPluses(P3.evenPermutations(
            new double[] {1,1,0}
        ));
    }

}
