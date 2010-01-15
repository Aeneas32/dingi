/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.anim;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.dolben.iiid.*;
import org.dolben.poly.Cuboid;
import org.dolben.poly.Polyhedron;
import org.dolben.poly.ShadedFaces;
import org.dolben.poly.Solid;

/**
 *  An animated simulation of solids bouncing around in a box.
 *  Override createBodies() in a concrete class.
 */
public abstract class Collidoscope extends AnimationApplet {
    
    private static final long serialVersionUID = 1;
    private Projector projector;    // the projector to draw 3D
    private List bodies;            // the list of moving objects
    private Solid trap;             // the box they're in
    
    /**
     *  Creates the bodies in the simulation.
     *
     *  @param limit the maxima in 3D of the box
     */
    protected abstract void createBodies( double[] limit );
    
    /**
     *  Creates the moving 3D objects, a cuboid that contains them,
     *  and a projector to draw them in 2D.
     */
    protected void initAnimation( ) {
        period = Math.round(1000.0f/24);
        double width = getSize().width-3;
        double height = getSize().height-3;
        double depth = ( width < height ) ? width : height;
        projector = new Projector(width+2,height+2,depth/2,2.5*depth);
        Cuboid cuboid = new Cuboid(width,height,depth);
        cuboid.turnInsideOut();
        trap = new ShadedFaces(cuboid,Color.gray);
        bodies = new LinkedList();
        double[] limit = cuboid.getExtent();
        createBodies(limit);
        double vmax =
            (period/1000.0)*Rn.magnitude(limit)/Math.sqrt(2*bodies.size());
        Iterator it = bodies.iterator();
        while ( it.hasNext() ) {
            Body body = (Body)it.next();
            body.setVelocity(vrandge(vmax),limit);
        }
    }
    
    /**
     *  Draws all of the objects, then moves them and handles collisions
     *  between them.
     *
     *  @param graphics the drawing context
     */
    public void paintFrame( Graphics graphics ) {
        Graphics2D g2d = (Graphics2D)graphics;
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setStroke(new BasicStroke(0.75f));
        trap.paint(projector,graphics);
        Collections.sort(bodies);
        Iterator it = bodies.iterator();
        while ( it.hasNext() ) {
            Body body = (Body)it.next();
            body.paint(projector,graphics);
        }
        it = bodies.iterator();
        while ( it.hasNext() ) {
            Body body = (Body)it.next();
            body.step();
        }
        for ( int i = 0; i < bodies.size()-1; ++i ) {
            Body body = (Body)bodies.get(i);
            for ( int j = i+1; j < bodies.size(); ++j ) {
                body.collide((Body)bodies.get(j));
            }
        }
    }
    
    /**
     *  Creates a Body, adding it the list, for the given polyhedron,
     *  normalizes the radius and randomizes position, velocity and spin
     */
    protected void initBody( Solid solid, double r ) {
        Cuboid cuboid = (Cuboid)trap.getPolyhedron();
        double[] limit = cuboid.getExtent();
        Polyhedron polyhedron = solid.getPolyhedron();
        polyhedron.setRadius(r);
        Body body = new Body(solid);
        double[] offset = new double[3];
        for ( int i = 0; i < 3; i++ ) {
            offset[i] = randge(limit[i]-r);
        }
        polyhedron.translate(offset);
        double scale = period/100.0;
        body.setRotation(
            rotationD(randge(Math.PI),randge(Math.PI/2),randge(scale*Math.PI/15))
        );
        bodies.add(body);
    }
    
    /**
     *  Generates a 3D matrix for a rotation by an angle around the direction
     *  given by spherical coordinate angles (theta,phi).
     *
     *  @param phi the angle from the x axis, around the z axis
     *  @param theta the angle from the z axis, around the y axis
     *  @param rho the angle around the x axis
     *
     *  @return the rotation matrix
     */
    private double[][] rotationD( double phi, double theta, double rho ) {
        // put x in the given direction
        double[][] d = Rn.multiply(R3.rotationY(-theta),R3.rotationZ(phi));
        return Rn.multiply(Rn.transpose(d),Rn.multiply(R3.rotationX(rho),d));
    }
    
    /*
     *  Generates a pseudo-random vector where each component is
     *  in the range -r to r.
     */
    private double[] vrandge( double r ) {
        double[] v = new double[3];
        for ( int i = 0; i < 3; ++i ) {
            v[i] = randge(r);
        }
        return v;
    }
    
    // Generates a pseudo-random number in the range -r to r.
    private double randge( double r ) {
        return 2*rand(r)-r;
    }
    
    // Generates a pseudo-random number in the range 0 to r.
    private double rand( double r ) {
        return r*Math.random();
    }

}
