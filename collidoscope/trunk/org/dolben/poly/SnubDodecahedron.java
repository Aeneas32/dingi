/**
 *  Copyright (c) 2005-2006 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  A snub dodecahedron.
 */
public class SnubDodecahedron extends Equilateral {
    
    /**
     *  Sets the vertices of a snub dodecahedron.
     */
    public void create( ) {
        final double p = P3.PHI;    // the golden mean
        final double t = root(2,p-5.0/27)/2;
        final double eta = root(3,p/2+t)+root(3,p/2-t);
        final double a = eta-1/eta;
        final double b = eta*p+p*p+p/eta;
        vertex =
            P3.concatenate(
                P3.evenPluses(P3.evenPermutations(
                    new double[] {2*a,2,2*b}
                )),
            P3.concatenate(
                P3.evenPluses(P3.evenPermutations(
                    new double[] {a+b/p+p,-a*p+b+1/p,a/p+b*p-1}
                )),
            P3.concatenate(
                P3.evenPluses(P3.evenPermutations(
                    new double[] {-a/p+b*p+1,-a+b/p-p,a*p+b-1/p}
                )),
            P3.concatenate(
                P3.evenPluses(P3.evenPermutations(
                    new double[] {-a/p+b*p-1,a-b/p-p,a*p+b+1/p}
                )),
                P3.evenPluses(P3.evenPermutations(
                    new double[] {a+b/p-p,a*p-b+1/p,a/p+b*p+1}
                ))
            ))));
    }
    
    // returns the nth root of arg
    private double root( int n, double arg ) {
        return Math.pow(arg,1.0/n);
    }

}
