/**
 *  Copyright (c) 2005-2006 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  A truncated cube.
 */
public class TruncatedCube extends Equilateral {
    
    /**
     *  Sets the vertices of a truncated cube.
     */
    public void create( ) {
        vertex = P3.allPluses(P3.evenPermutations(
            new double[] {Math.sqrt(2)-1,1,1}
        ));
    }

}
