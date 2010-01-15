/**
 *  Copyright (c) 2005-2006 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.iiid;

/**
 *  Cross product and rotation matrix generators for 3D.
 *  <br />
 *  The components of a 3D vector are { x, y, z }.
 *  The generated matrices are "active rotations", i.e.,
 *  applying one to a vector results in a vector rotated by the
 *  given angle around the axis in the original coordinate system.
 */
public class R3 {
    
    /**
     *  Computes the cross product of two 3D vectors
     *
     *  @param u one vector
     *  @param v another vector
     *
     *  @return the cross product
     */
    public static double[] cross( double[] u, double[] v ) {
        return new double[] {
            u[1]*v[2] - u[2]*v[1],
            u[2]*v[0] - u[0]*v[2],
            u[0]*v[1] - u[1]*v[0]
        };
    }
    
    /**
     *  Generates a 3D matix for a rotation by an angle around the x axis.
     *
     *  @param theta an angle
     *
     *  @return the rotation matrix
     */
    public static double[][] rotationX( double theta ) {
        double sine = Math.sin(theta);
        double cosine = Math.cos(theta);
        return new double[][] {
            {      1,      0,      0 },
            {      0, cosine,  -sine },
            {      0,   sine, cosine }
        };
    }
    
    /**
     *  Generates a 3D matix for a rotation by an angle around the y axis.
     *
     *  @param theta an angle
     *
     *  @return the rotation matrix
     */
    public static double[][] rotationY( double theta ) {
        double sine = Math.sin(theta);
        double cosine = Math.cos(theta);
        return new double[][] {
            { cosine,      0,   sine },
            {      0,      1,      0 },
            {  -sine,      0, cosine }
        };
    }
    
    /**
     *  Generates a 3D matix for a rotation by an angle around the z axis.
     *
     *  @param theta an angle
     *
     *  @return the rotation matrix
     */
    public static double[][] rotationZ( double theta ) {
        double sine = Math.sin(theta);
        double cosine = Math.cos(theta);
        return new double[][] {
            { cosine,  -sine,      0 },
            {   sine, cosine,      0 },
            {      0,      0,      1 }
        };
    }

}
