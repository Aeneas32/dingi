/**
 *  Copyright (c) 2005-2007 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

import org.dolben.iiid.Rn;

/**
 *  Representation of a polyhedron, its vertices and faces in 3D,
 *  that can be scaled, translated and rotated.
 *  Override create() to set the vertices and faces for a particular
 *  geometric form.
 */
public abstract class Polyhedron {
    
    /**
     *  Each vertex is a 3D vector from the origin of the coordinate system.
     */
    protected double[][] vertex;
    
    /**
     *  Each element of the 'face' array is an array of indices
     *  into the 'vertex' array for the (co-planar) points of a
     *  face of the polyhedron, ordered such that an adjacent pair
     *  (and the first and last) are (the indices of) the end-points
     *  of an edge, and the direction of a cycle produces by the
     *  right-hand rule an outward pointing normal to the face.
     *  The faces are in no special order.
     */
    protected int[][] face;
    
    // the vector to the center of the polyhedron
    private double[] center = new double[]{0,0,0};
    
    /**
     *  Constructs a Polyhedron centered on the origin.
     */
    public Polyhedron( ) {
        create();
    }
    
    /**
     *  Sets the vertex and face elements. Override for a concrete class.
     */
    protected abstract void create( );
    
    /**
     *  Scales the polydron, setting the radius of its circumsphere
     *  to the given argument.
     *
     *  @param radius the new radius
     */
    public void setRadius( double radius ) {
        scale(radius/getRadius());
    }
    
    /**
     *  Gets the radius of the polyhedron's circumsphere.
     *
     *  @return the radius
     *
     *  Override if the distance between the center and the first vertex
     *  is not equal to the radius.
     */
    public double getRadius( ) {
        return Rn.magnitude(Rn.subtract(vertex[0],center));
    }
    
    /**
     *  Gets the position of the polyhedron, a 3D vector to its center.
     *
     *  @return the postion
     */
    public double[] getPosition( ) {
        return center;
    }
    
    /**
     *  Scales the polyhedron, multiplying the distance between the center
     *  and each vertex by the given argument.
     *
     *  @param factor the scale factor
     */
    public void scale( double factor ) {
        for ( int i = 0; i < vertex.length; ++i ) {
            vertex[i] = Rn.multiply(factor,Rn.subtract(vertex[i],center));
        }
    }
    
    /**
     *  Translates the polyhedron by the given 3D vector.
     *
     *  @param displacement the translation vector
     */
    public void translate( double[] displacement ) {
        center = Rn.add(center,displacement);
        for ( int i = 0; i < vertex.length; ++i ) {
            vertex[i] = Rn.add(vertex[i],displacement);
        }
    }
    
    /**
     *  Rotates the polyhedron by the given 3D rotation matrix.
     *
     *  @param rotation the rotation matrix
     */
    public void rotate( double[][] rotation ) {
        for ( int i = 0; i < vertex.length; ++i ) {
            double[] v = Rn.subtract(vertex[i],center);
            vertex[i] = Rn.add(Rn.multiply(rotation,v),center);
        }
    }
    
    /**
     *  Gets the number of vertices of the polyhedron.
     *
     *  @return the number of vertices
     */
    public int getVertexCount( ) {
        return vertex.length;
    }
    
    /**
     *  Gets the array of vertices of the polyhedron.
     *
     *  @return the array of vertices
     */
    public double[][] getVertices( ) {
        return vertex;
    }
    
    /**
     *  Gets an indexed vertex of the polyhedron.
     *
     *  @param index the index of the face to get
     *
     *  @return the indexed vertex
     */
    public double[] getVertex( int index ) {
        return vertex[index];
    }
    
    /**
     *  Gets the number of faces of the polyhedron.
     *
     *  @return the number of faces
     */
    public int getFaces( ) {
        return face.length;
    }
    
    /**
     *  Gets the vertex indices of an indexed face of the polyhedron.
     *
     *  @param index the index of the face to get
     *
     *  @return the vertex indices
     */
    public int[] getFaceIndices( int index ) {
        return face[index];
    }
    
    /**
     *  Gets an indexed face of the polyhedron, where each element
     *  of the array is a vertex of the face's polygon.
     *  See <a href="#face">the description of face</a> for the order
     *  of the array.
     *
     *  @param index the index of the face to get
     */
    public double[][] getFace( int index ) {
        int[] f = face[index];
        double[][] p = new double[f.length][];
        for ( int i = 0; i < f.length; ++i ) {
            p[i] = vertex[f[i]];
        }
        return p;
    }
    
    /**
     *  Turns the polyhedron inside-out, e.g., viewed from the inside
     *  when was viewed from the outside.
     */
    public void turnInsideOut( ) {
        for ( int f = 0; f < face.length; ++f ) {
            turnInsideOut(face[f]);
        }
    }
    
    /**
     *  Turns a face inside-out by reversing the order of its points.
     */
    private void turnInsideOut( int[] f ) {
        for ( int i = 0; i < f.length/2; ++i ) {
            int j = f.length-1-i;
            int k = f[i];
            f[i] = f[j];
            f[j] = k;
        }
    }
    
    /**
     *  Makes a String representation of the polyhedron.
     *
     *  @return the String
     */
    public String toString( ) {
        String s = "";
        for ( int i = 0; i < face.length; ++i ) {
            int[] f = face[i];
            for ( int j = 0; j < f.length; ++j ) {
                double[] v = vertex[f[j]];
                for ( int k = 0; k < 3; ++k ) {
                    s += v[k]+" ";
                }
                s += "\n";
            }
            s += "\n";
        }
        return s;
    }

}
