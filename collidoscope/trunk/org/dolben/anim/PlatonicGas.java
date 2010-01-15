/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.anim;

import java.awt.Color;
import org.dolben.poly.ShadedFaces;
import org.dolben.poly.Tetrahedron;
import org.dolben.poly.Cube;
import org.dolben.poly.Octahedron;
import org.dolben.poly.Dodecahedron;
import org.dolben.poly.Icosahedron;

/**
 *  An animated simulation of Platonic solids bouncing around in a box
 */
public class PlatonicGas extends Collidoscope {
    
    private static final long serialVersionUID = 1;
    
    /**
     *  Creates the bodies in the simulation.
     */
    protected void createBodies( double[] limit ) {
        final int N = 5;
        double volume = 1.0/(10*N);
        for ( int i = 0; i < 3; i++ ) {
            volume *= 2*limit[i];
        }
        /* V = 4/3 ¹ r^3; r = 3/4/¹ V^(1/3) */
        double radius = Math.pow((3/4.0)/Math.PI*volume,1/3.0);
        initBody(new ShadedFaces(new Tetrahedron (),Color.magenta),radius);
        initBody(new ShadedFaces(new Cube        (),Color.orange ),radius);
        initBody(new ShadedFaces(new Octahedron  (),Color.blue   ),radius);
        initBody(new ShadedFaces(new Dodecahedron(),Color.green  ),radius);
        initBody(new ShadedFaces(new Icosahedron (),Color.red    ),radius);
    }

}
