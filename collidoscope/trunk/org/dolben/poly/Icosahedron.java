/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  An icosahedron.
 */
public class Icosahedron extends Equilateral {
    
    /**
     *  Sets the vertices of an icosahedron.
     */
    public void create( ) {
        vertex = P3.allPluses(
            P3.evenPermutations(new double[] {0,1,P3.PHI})
        );
    }

}
