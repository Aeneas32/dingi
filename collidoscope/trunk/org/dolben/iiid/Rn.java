/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.iiid;

/**
 *  <p>
 *  These are collected functions on generic vectors and matrices.
 *  </p><p>
 *  A scalar is represented by a double. (gratuitous comment;-)
 *  </p><p>
 *  A vector is represented by a one dimensional array of doubles.
 *  </p><p>
 *  A matrix is represented by a two dimensional array of doubles
 *  where the row is indexed first.
 *  </p><p>
 *  None of these functions modify their arguments. A returned
 *  vector or matrix result is a new array.
 *  </p>
 */
public class Rn {
    
    /**
     *  Multiplies a vector by a scalar
     *
     *  @param s a scalar
     *  @param v a vector
     *
     *  @return the scaled vector
     */
    public static double[] multiply( double s, double[] v ) {
        double[] u = new double[v.length];
        for ( int i = 0; i < v.length; ++i ) {
            u[i] = s*v[i];
        }
        return u;
    }
    
    /**
     *  Computes the dot product of two vectors
     *
     *  @param u one vector
     *  @param v another vector
     *
     *  @return the dot product
     */
    public static double dot( double[] u, double[] v ) {
        double sum = 0.0;
        for ( int i = 0; i < u.length; ++i ) {
            sum += u[i]*v[i];
        }
        return sum;
    }
    
    /**
     *  Computes the magnitude of a vector
     *
     *  @param v a vector
     *
     *  @return the magnitude
     */
    public static double magnitude( double[] v ) {
        return Math.sqrt(dot(v,v));
    }
    
    /**
     *  Computes the angle between two vectors
     *
     *  @param u one vector
     *  @param v another vector
     *
     *  @return the angle in radians
     */
    public static double angle( double[] u, double[] v ) {
        return Math.acos(dot(u,v)/magnitude(u)/magnitude(v));
    }
    
    /**
     *  Adds two vectors
     *
     *  @param u one vector
     *  @param v another vector
     *
     *  @return the vector sum
     */
    public static double[] add( double[] u, double[] v ) {
        double[] t = new double[u.length];
        for ( int i = 0; i < u.length; ++i ) {
            t[i] = u[i]+v[i];
        }
        return t;
    }
    
    /**
     *  Subtracts one vector from another
     *
     *  @param u another vector
     *  @param v one vector
     *
     *  @return the vector difference, u-v
     */
    public static double[] subtract( double[] u, double[] v ) {
        double[] t = new double[u.length];
        for ( int i = 0; i < u.length; ++i ) {
            t[i] = u[i]-v[i];
        }
        return t;
    }
    
    /**
     *  Multiplies a vector by a matrix
     *
     *  @param a the matrix
     *  @param v the vector
     *
     *  @return the transformed vector
     */
    public static double[] multiply( double[][] a, double[] v ) {
        double[] u = new double[v.length];
        for ( int i = 0; i < a[0].length; ++i ) {
            u[i] = dot(a[i],v);
        }
        return u;
    }
    
    /**
     *  Multiplies two matrices
     *
     *  @param b one matrix
     *  @param a another matrix
     *
     *  @return the matrix product
     */
    public static double[][] multiply( double[][] b, double[][] a ) {
        double[][] c = new double[b.length][a[0].length];
        for ( int i = 0; i < b.length; ++i ) {
            for ( int j = 0; j < a[0].length; ++j ) {
                c[i][j] = 0.0;
                for ( int k = 0; k < a.length; ++k ) {
                    c[i][j] += b[i][k]*a[k][j];
                }
            }
        }
        return c;
    }
    
    /**
     *  Transposes a matrix
     *
     *  @param a a matrix
     *
     *  @return the transpose
     */
    public static double[][] transpose( double[][] a ) {
        double[][] t = new double[a[0].length][a.length];
        for ( int i = 0; i < a.length; ++i ) {
            for ( int j = 0; j < a[0].length; ++j ) {
                t[i][j] = a[j][i];
            }
        }
        return t;
    }

}
    