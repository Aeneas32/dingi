/**
 *  Copyright (c) 2005-2006 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  A truncated dodecahedron.
 */
public class TruncatedDodecahedron extends Equilateral {
    
    /**
     *  Sets the vertices of a truncated dodecahedron.
     */
    public void create( ) {
        vertex =
            P3.concatenate(
                P3.allPluses(P3.evenPermutations(
                    new double[] {0,1/P3.PHI,2+P3.PHI}
                )),
            P3.concatenate(
                P3.allPluses(P3.evenPermutations(
                    new double[] {1/P3.PHI,P3.PHI,2*P3.PHI}
                )),
                P3.allPluses(P3.evenPermutations(
                    new double[] {P3.PHI,2,P3.PHI*P3.PHI}
                ))
            ));
    }

}
