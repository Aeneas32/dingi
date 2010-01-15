/**
 *  Copyright (c) 2005-2007 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.anim;

import java.awt.Graphics;
import org.dolben.iiid.Rn;
import org.dolben.iiid.Projector;
import org.dolben.poly.Polyhedron;
import org.dolben.poly.Solid;

/**
 *  Body moves a solid around in 3D space; rotating it, translating it,
 *  and bouncing it off of other bodies and the walls of its enclosure.
 */
public class Body implements Comparable {
    
    private Solid solid;            // the Solid that moves
    private double[][] rotation;    // the rotation for each step of motion
    private double[] velocity;      // the displacement per step
    private double[] maximum;       // the limit of the trap
    
    /**
     *  Creates a new Body for a given solid.
     *
     *  @param s the solid to be moved
     */
    public Body( Solid s ) {
        solid = s;
    }
    
    /**
     *  Draws the solid.
	 *
     *  @param projector the projector that maps 3D to Graphics coordinates
     *  @param graphics  the drawing context
     */
    public void paint( Projector projector, Graphics graphics ) {
        solid.paint(projector,graphics);
    }
    
    /**
     *  Gets the solid's polyhedron.
     */
    private Polyhedron getPolyhedron( ) {
        return solid.getPolyhedron();
    }
    
    /**
     *  Gets the solid's collision radius (a hack)
     */
    public double getCollisionRadius( ) {
        double sqrtn = Math.sqrt(getPolyhedron().getVertexCount());
        return getPolyhedron().getRadius()*(sqrtn-1)/sqrtn;
    }
    
    /**
     *  Orders Bodies from farthest to nearest to an observer,
     *  presumed to be at a large z coordinate, making the class
     *  Comparable.
     *
     *  @param object another body to compare to
     *
     *  @return -1 when this is farther than the other,
     *           1 when this is nearer,
     *        or 0 when they are the same distance
     */
    public int compareTo( Object object ) {
        Body other = (Body)object;
        double thisZ = getPolyhedron().getPosition()[2];
        double otherZ = other.getPolyhedron().getPosition()[2];
        if ( thisZ < otherZ ) {
            return -1;
        } else if ( thisZ > otherZ ) {
            return 1;
        } else {
            return 0;
        }
    }
    
    /**
     *  Sets the rotation to be done with each step of motion.
     *
     *  @param r the 3D rotation matrix
     */
    public void setRotation( double[][] r ) {
        rotation = r;
    }
    
    /**
     *  Sets the velocity and limit of translation.
     *
     *  @param v the 3D velocity vector
     *  @param limit the limit of displacement from the origin
     *              in each dimension
     */
    public void setVelocity( double[] v, double[] limit ) {
        velocity = v;
        maximum = limit;
    }
    
    /**
     *  Moves the polyhedron one step.
     */
    public void step( ) {
        if ( rotation  != null ) {
            getPolyhedron().rotate(rotation);
        }
        if ( velocity != null ) {
            getPolyhedron().translate(velocity);
            trap();
        }
    }
    
    /**
     *  Bounces the polyhedron off of the walls of an enclosing cube.
     */
    private void trap( ) {
        double[][] v = getPolyhedron().getVertices();
        for ( int i = 0; i < 3; ++i ) {
            for ( int j = 0; j < v.length; ++j ) {
                if ( v[j][i] > maximum[i] ) {
                    if ( velocity[i] > 0 ) {
                        velocity[i] = -velocity[i];
                    }
                    break;
                } else if ( v[j][i] < -maximum[i] ) {
                    if ( velocity[i] < 0 ) {
                        velocity[i] = -velocity[i];
                    }
                    break;
                }
            }
        }
    }
    
    /**
     *  <p> Bounces this body off of another when they collide.
     *  </p>
     *  <p> The objects are considered to be spheres of the same mass.
     *  </p>
     *  <p> The threshhold distance for a collision is less than the sum of
     *  the radii of the circumspheres, since the objects often appear
     *  to miss at greater distance.
     *  </p>
     *  @param other the other body in a collision
     */
    public void collide( Body other ) {
        double[] p = getPolyhedron().getPosition();
        double[] po = other.getPolyhedron().getPosition();
        
        // the vector from this solid to the other
        double[] disp = Rn.subtract(po,p);
        double dist = Rn.magnitude(disp); // distance between centers
        double r = getCollisionRadius();
        double ro = other.getCollisionRadius();
        // make them bounce only when they're close to each other
        if ( dist < r+ro ) {
            double[] deltav = Rn.subtract(other.velocity,velocity);
            // and moving toward each other
            if ( Rn.dot(deltav,disp) < 0 ) {
                // unit vector in the direction from this solid to the other
                double[] dir = Rn.multiply(1/dist,disp);
                
                // component of this solid's velocity in the direction
                double[] v = Rn.multiply(Rn.dot(dir,velocity),dir);
                
                // component of the other solid's velocity in the direction
                double[] vo = Rn.multiply(Rn.dot(dir,other.velocity),dir);
                
                velocity = Rn.add(Rn.subtract(velocity,v),vo);
                other.velocity = Rn.add(Rn.subtract(other.velocity,vo),v);
            }
        }
    }

}
