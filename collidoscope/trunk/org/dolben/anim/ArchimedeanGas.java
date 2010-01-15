/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.anim;

import java.awt.Color;
import org.dolben.poly.ColoredFaces;
import org.dolben.poly.ShadedFaces;
import org.dolben.poly.TruncatedTetrahedron;
import org.dolben.poly.TruncatedCube;
import org.dolben.poly.TruncatedOctahedron;
import org.dolben.poly.TruncatedDodecahedron;
import org.dolben.poly.TruncatedIcosahedron;
import org.dolben.poly.Cuboctahedron;
import org.dolben.poly.Icosidodecahedron;
import org.dolben.poly.TruncatedCuboctahedron;
import org.dolben.poly.TruncatedIcosidodecahedron;
import org.dolben.poly.Rhombicuboctahedron;
import org.dolben.poly.Rhombicosidodecahedron;
import org.dolben.poly.SnubCube;
import org.dolben.poly.SnubDodecahedron;

/**
 *  An animated simulation of Archimedean solids bouncing around in a box.
 <pre>
                                   faces with n sides   n sides
                          vertices   3  4  5  6  8 10  at vertex
                                --  -- -- -- -- -- --  ---------
   TruncatedTetrahedron         12   4        4        3,6,6
   TruncatedCube                24   8           6     3,8,8
   TruncatedOctahedron          24      6     8        4,6,6
   TruncatedDodecahedron        60  20             12  3,10,10
   TruncatedIcosahedron         60        12 20        5,6,6
   Cuboctahedron                12   8  6              3,4,3,4
   Icosidodecahedron            30  20    12           3,5,3,5
   TruncatedCuboctahedron       48     12    8   6     4,6,8
   TruncatedIcosidodecahedron  120     30    20    12  4,6,10
   Rhombicuboctahedron          24   8 18              3,4,4,4
   Rhombicosidodecahedron       60  20 30 12           3,4,5,4
   SnubCube                     24  32  6              3,3,3,3,4
   SnubDodecahedron             60  80    12           3,3,3,3,5
 </pre>
 */
public class ArchimedeanGas extends Collidoscope {
    
    private static final long serialVersionUID = 1;

    /**
     *  Creates the bodies in the simulation.
     */
    protected void createBodies( double[] limit ) {
        final int N = 13;  // number of solids
        // make the radius so that the volume taken up is
        // about the same as the Platonics
        double volume = 1.0/(10*N);
        for ( int i = 0; i < 3; i++ ) {
            volume *= 2*limit[i];
        }
        /* V = 4/3 ¹ r^3; r = 3/4/¹ V^(1/3) */
        double radius = Math.pow((3/4.0)/Math.PI*volume,1/3.0);
        initBody(new ColoredFaces(new TruncatedTetrahedron      ()),radius);
        initBody(new ColoredFaces(new TruncatedCube             ()),radius);
        initBody(new ColoredFaces(new TruncatedOctahedron       ()),radius);
        initBody(new ColoredFaces(new TruncatedDodecahedron     ()),radius);
        Color color = new Color(0.0f,1.0f,0.5f);
        initBody(new ShadedFaces(new TruncatedIcosahedron      (),color),radius);
        
        initBody(new ColoredFaces(new Cuboctahedron             ()),radius);
        initBody(new ColoredFaces(new TruncatedCuboctahedron    ()),radius);
        initBody(new ColoredFaces(new Rhombicuboctahedron       ()),radius);
        initBody(new ColoredFaces(new Icosidodecahedron         ()),radius);
        initBody(new ColoredFaces(new TruncatedIcosidodecahedron()),radius);
        initBody(new ColoredFaces(new Rhombicosidodecahedron    ()),radius);
        
        initBody(new ColoredFaces(new SnubCube                  ()),radius);
        initBody(new ColoredFaces(new SnubDodecahedron          ()),radius);
     }

}
