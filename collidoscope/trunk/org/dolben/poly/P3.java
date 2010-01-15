/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  <p>
 *  Permutations of components and combinations of signs of components
 *  for 3D vectors: { even | odd | all } { Permutations | Pluses }.
 *  </p><p>
 *  For completeness there ought to be *Permutations( double[][] ),
 *  but they weren't used and so are left out.
 *  See evenPluses( double[][] ), e.g., for a template if need be.
 *  </p>
 */
public class P3 {
    
    // The Golden Mean is admittedly just tossed in here
    // because it needs to be somewhere.
    public static final double PHI = (1+Math.sqrt(5))/2;
    
    /**
     *  Generates all the even permutations
     *  of the components of a 3D vector.
     *
     *  @param v the vector
     *
     *  @return an array of 3D vectors
     */
    public static double[][] evenPermutations( double[] v ) {
        return new double[][] {
            {v[0],v[1],v[2]},
            {v[2],v[0],v[1]},
            {v[1],v[2],v[0]}
        };
    }
    
    /**
     *  Generates all the odd permutations
     *  of the components of a 3D vector.
     *
     *  @param v the vector
     *
     *  @return an array of 3D vectors
     */
    public static double[][] oddPermutations( double[] v ) {
        return new double[][] {
            {v[0],v[2],v[1]},
            {v[2],v[1],v[0]},
            {v[1],v[0],v[2]}
        };
    }
    
    /**
     *  Generates all the combinations of an even number of plus signs
     *  for the components of a 3D vector where no component is zero.
     *
     *  @param v the vector
     *
     *  @return an array of 3D vectors
     */
    public static double[][] evenPluses( double[] v ) {
        return new double[][] {
            { v[0], v[1],-v[2]},
            { v[0],-v[1], v[2]},
            {-v[0], v[1], v[2]},
            {-v[0],-v[1],-v[2]}
        };
    }
    
    /**
     *  Generates all the combinations of an odd number of plus signs
     *  for the components of a 3D vector where no component is zero.
     *
     *  @param v the vector
     *
     *  @return an array of 3D vectors
     */
    public static double[][] oddPluses( double[] v ) {
        return new double[][] {
            { v[0], v[1], v[2]},
            { v[0],-v[1],-v[2]},
            {-v[0], v[1],-v[2]},
            {-v[0],-v[1], v[2]}
        };
    }
    
    /**
     *  Generates all the combinations of signs
     *  for the components of a 3D vector.
     *  Handles all the cases of zero components.
     *
     *  @param v the vector
     *
     *  @return an array of 3D vectors
     */
    public static double[][] allPluses( double[] v ) {
        // There's probably some general algorithm for this
        // but for 3D there are only 8 cases, so...
        if ( v[0] != 0 && v[1] != 0 && v[2] != 0 ) {
            return new double[][] {
                { v[0], v[1], v[2]},
                { v[0], v[1],-v[2]},
                { v[0],-v[1], v[2]},
                { v[0],-v[1],-v[2]},
                {-v[0], v[1], v[2]},
                {-v[0], v[1],-v[2]},
                {-v[0],-v[1], v[2]},
                {-v[0],-v[1],-v[2]}
            };
        }
        if ( v[0] == 0 && v[1] != 0 && v[2] != 0 ) {
            return new double[][] {
                { v[0], v[1], v[2]},
                { v[0], v[1],-v[2]},
                { v[0],-v[1], v[2]},
                { v[0],-v[1],-v[2]}
            };
        }
        if ( v[0] != 0 && v[1] == 0 && v[2] != 0 ) {
            return new double[][] {
                { v[0], v[1], v[2]},
                { v[0], v[1],-v[2]},
                {-v[0], v[1], v[2]},
                {-v[0], v[1],-v[2]}
            };
        }
        if ( v[0] != 0 && v[1] != 0 && v[2] == 0 ) {
            return new double[][] {
                { v[0], v[1], v[2]},
                { v[0],-v[1], v[2]},
                {-v[0], v[1], v[2]},
                {-v[0],-v[1], v[2]}
            };
        }
        if ( v[0] != 0 && v[1] == 0 && v[2] == 0 ) {
            return new double[][] {
                { v[0], v[1], v[2]},
                {-v[0], v[1], v[2]}
            };
        }
        if ( v[0] == 0 && v[1] != 0 && v[2] == 0 ) {
            return new double[][] {
                { v[0], v[1], v[2]},
                { v[0],-v[1], v[2]}
            };
        }
        if ( v[0] == 0 && v[1] == 0 && v[2] != 0 ) {
            return new double[][] {
                { v[0], v[1], v[2]},
                { v[0], v[1],-v[2]}
            };
        }
        return new double[][] {v};
    }
    
    /**
     *  Generates all the permutations
     *  of the components of a 3D vector.
     *
     *  @param v the vector
     *
     *  @return an array of 3D vectors
     */
    public static double[][] allPermutations( double[] v ) {
        return concatenate(evenPermutations(v),oddPermutations(v));
    }
    
    
    /**
     *  Generates all the combinations of an even number of plus signs
     *  for the components of each 3D vector in an array
     *  where no component is zero.
     *
     *  @param a array of 3D vectors
     *
     *  @return an array of 3D vectors
     */
    public static double[][] evenPluses( double[][] a ) {
        double[][] p = evenPluses(a[0]);
        for ( int i = 1; i < a.length; ++i ) {
            p = concatenate(p,evenPluses(a[i]));
        }
        return p;
    }
    
    /**
     *  Generates all the combinations of an odd number of plus signs
     *  for the components of each 3D vector in an array
     *  where no component is zero.
     *
     *  @param a array of 3D vectors
     *
     *  @return an array of 3D vectors
     */
    public static double[][] oddPluses( double[][] a ) {
        double[][] p = oddPluses(a[0]);
        for ( int i = 1; i < a.length; ++i ) {
            p = concatenate(p,oddPluses(a[i]));
        }
        return p;
    }
    
    /**
     *  Generates all the combinations of an even number of plus signs
     *  for the components of each 3D vector in an array
     *  where no component is zero.
     *
     *  @param a array of 3D vectors
     *
     *  @return an array of 3D vectors
     */
    public static double[][] allPluses( double[][] a ) {
        double[][] p = allPluses(a[0]);
        for ( int i = 1; i < a.length; ++i ) {
            p = concatenate(p,allPluses(a[i]));
        }
        return p;
    }
    
    /**
     *  Concatenates two arrays of vectors.
     *
     *  @param a one array of vectors
     *  @param b another array of vectors
     *
     *  @return an array of vectors which is the concatenation of the two
     */
    public static double[][] concatenate( double[][] a, double[][] b ) {
        double[][] p = new double[a.length+b.length][];
        for( int i = 0; i < a.length; ++i ) {
            p[i] = a[i];
        }
        for( int i = 0; i < b.length; ++i ) {
            p[a.length+i] = b[i];
        }
        return p;
    }

}
