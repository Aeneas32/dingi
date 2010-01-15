/**
 *  Copyright (c) 2005-2006 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  A dodecahedron.
 */
public class Dodecahedron extends Equilateral {
    
    /**
     *  Sets the vertices of a dodecahedron.
     */
    public void create( ) {
        vertex = P3.concatenate(
            P3.allPluses(P3.evenPermutations(new double[] {0,1/P3.PHI,P3.PHI})),
            P3.allPluses(new double[] {1,1,1})
        );
    }

}
