/**
 *  Copyright (c) 2005-2006 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  A rhombicosidodecahedron.
 */
public class Rhombicosidodecahedron extends Equilateral {
    
    /**
     *  Sets the vertices of a rhombicosidodecahedron.
     */
    public void create( ) {
        final double PHI2 = P3.PHI*P3.PHI;
        final double PHI3 = P3.PHI*PHI2;
        vertex = 
            P3.concatenate(
                P3.allPluses(P3.evenPermutations(
                    new double[] {PHI3,1,1}
                )),
            P3.concatenate(
                P3.allPluses(P3.evenPermutations(
                    new double[] {PHI2,P3.PHI,2*P3.PHI}
                )),
                P3.allPluses(P3.evenPermutations(
                    new double[] {2+P3.PHI,0,PHI2}
                ))
            ));
    }

}
