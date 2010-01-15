/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.iiid;

import java.awt.Color;

/**
 *  A representation of the lighting of a scene, with a method for
 *  finding the shading of a surface depending on its angle to the light.
 */
public class Lighting {
    
    private float background;   // level of background light
    private double[] light;     // 3D vector direction of the light source
    
    /**
     *  Creates new Lighting.
     */
    public Lighting( ) {
        background = 0.2f;
        setLight(new double[]{-0.6,0.8,1},0.2f);
    }
    
    /**
     *  Sets the location of the light source
     *  and the background light level as a fraction of the source light.
     *  The light is presumed to be very far away so that only direction
     *  is used to effect shading.
     *
     *  @param l the 3D vector location of the light source
     *  @param bg the background light level (0,1)
     */
    public void setLight( double[] l, float bg ) {
        light = Rn.multiply(1/Rn.magnitude(l),l);
        background = bg;
    }
    
    /**
     *  Gets the shade for a color produced by the lighting.
     *
     *  @param normal the vector normal to the plane to be shaded
     *  @param color base color to be shaded
     *
     *  @return the shaded color
     */
    public Color getShade( double[] normal, Color color ) {
        double fraction = (1+Rn.dot(normal,light)/Rn.magnitude(normal))/2;
        float brightness = (float)fraction*(1-background)+background;
        float[] hsb = Color.RGBtoHSB(
            color.getRed(),color.getGreen(),color.getBlue(),null
        );
        return Color.getHSBColor(hsb[0],hsb[1],brightness);
    }

}
