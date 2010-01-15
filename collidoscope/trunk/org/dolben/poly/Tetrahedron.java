/**
 *  Copyright (c) 2005-2006 by Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

/**
 *  A tetrahedron.
 */
public class Tetrahedron extends Equilateral {
    
    /**
     *  Sets the vertices of a tetrahedron.
     */
    public void create( ) {
        vertex = P3.oddPluses(new double[] {1,1,1});
    }

}
