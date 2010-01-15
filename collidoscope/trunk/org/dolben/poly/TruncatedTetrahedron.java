/**
 *  Copyright (c) 2005-2006 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  A truncated tetrahedron.
 */
public class TruncatedTetrahedron extends Equilateral {
    
    /**
     *  Sets the vertices of a truncated tetrahedron.
     */
    public void create( ) {
        vertex = P3.oddPluses(P3.evenPermutations(
            new double[] {3,1,1}
        ));
    }

}
