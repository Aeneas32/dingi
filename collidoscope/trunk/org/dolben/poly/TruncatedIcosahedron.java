/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  A truncated icosahedron.
 */
public class TruncatedIcosahedron extends Equilateral {
    
    /**
     *  Sets the vertices of a truncated icosahedron.
     */
    public void create( ) {
        vertex =
            P3.concatenate(
                P3.allPluses(P3.evenPermutations(
                    new double[] {0,1,3*P3.PHI}
                )),
            P3.concatenate(
                P3.allPluses(P3.evenPermutations(
                    new double[] {2,1+2*P3.PHI,P3.PHI}
                )),
                P3.allPluses(P3.evenPermutations(
                    new double[] {1,2+P3.PHI,2*P3.PHI}
                ))
            ));
    }

}
