/**
 *  Copyright (c) 2005-2006 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  A truncated cuboctahedron.
 */
public class TruncatedCuboctahedron extends Equilateral {
    
    /**
     *  Sets the vertices of a truncated cuboctahedron.
     */
    public void create( ) {
        vertex = P3.allPluses(P3.allPermutations(
            new double[] {1,1+Math.sqrt(2),1+Math.sqrt(8)}
        ));
    }

}
