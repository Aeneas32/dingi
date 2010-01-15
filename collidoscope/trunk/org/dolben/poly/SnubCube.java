/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  A snub cube.
 */
public class SnubCube extends Equilateral {
    
    /**
     *  Sets the vertices of a snub cube.
     */
    public void create( ) {
        final double ETA = (root(3,17+root(2,297))-root(3,-17+root(2,297))-1)/3;
        double[] v = {1,ETA,1/ETA};
        vertex = P3.concatenate(
            P3.evenPluses(P3.evenPermutations(v)),
            P3.oddPluses(P3.oddPermutations(v))
        );
    }
    
    // returns the nth root of arg
    private double root( int n, double arg ) {
        return Math.pow(arg,1.0/n);
    }

}
