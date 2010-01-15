/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  An icosidodecahedron.
 */
public class Icosidodecahedron extends Equilateral {
    
    /**
     *  Sets the vertices of an icosidodecahedron.
     */
    public void create( ) {
        vertex = P3.concatenate(
            P3.allPluses(P3.evenPermutations(
                new double[] {0,0,P3.PHI}
            )),
            P3.allPluses(P3.evenPermutations(
                new double[] {1.0/2,P3.PHI/2,(1+P3.PHI)/2}
            ))
        );
    }

}
