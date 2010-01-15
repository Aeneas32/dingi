/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  A truncated octahedron.
 */
public class TruncatedOctahedron extends Equilateral {
    
    /**
     *  Sets the vertices of a truncated octahedron.
     */
    public void create( ) {
        vertex = P3.allPluses(P3.allPermutations(
            new double[] {0,1,2}
        ));
    }

}
