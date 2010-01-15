/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  A rhombicuboctahedron.
 */
public class Rhombicuboctahedron extends Equilateral {
    
    /**
     *  Sets the vertices of a rhombicuboctahedron.
     */
    public void create( ) {
        vertex = P3.allPluses(P3.evenPermutations(
            new double[] {1+Math.sqrt(2),1,1}
        ));
    }

}
