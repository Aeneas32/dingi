/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.anim;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

/**
 *  AnimationApplet is used for running an animation in an Applet.
 *  Override initAnimation() and paintFrame() to derive a concrete class.
 */
public abstract class AnimationApplet extends Applet {
    
    private static final long serialVersionUID = 1;
    private Timer sprocket = new Timer(true);   // advances animation
    private TimerTask shutter = new Shutter();  // animation shutter
    private static final int DEFAULT_PERIOD = 100;
    private Image buffer;                       // off screen drawing buffer
    private Graphics offScreen;                 // off screen graphics context
    private Graphics onScreen;                  // on screen graphics context
    
    /**
     *  The interval of time between frames of the animation.
     */
    protected int period;
    
    /**
     *  Initializes the particular animation. Override for derivation.
     */
    protected abstract void initAnimation( );
    
    /**
     *  Paints a frame of the animation. Override for derivation.
     *
     *  @param g the graphics context
     */
    protected abstract void paintFrame( Graphics g );
    
    /**
     *  Initializes the Applet.
     */
    public void init( ) {
        synchronized(sprocket) {
            shutter.cancel();
            period = DEFAULT_PERIOD;
            buffer = createImage(getSize().width,getSize().height);
            offScreen = buffer.getGraphics();
            onScreen = this.getGraphics();
            initAnimation();
        }
    }
    
    /**
     *  Paints a frame of the animation.
     *
     *  @param g the graphics context
     */
    public void paint( Graphics g ) {
        synchronized(sprocket) {
            g.drawImage(buffer,0,0,null);
        }
    }
    
    /**
     *  Starts the Applet.
     */
    public void start( ) {
        synchronized(sprocket) {
            shutter = new Shutter();
            sprocket.schedule(shutter,0,period);
        }
    }
    
    /**
     *  Stops the Applet.
     */
    public void stop( ) {
        shutter.cancel();
    }
    
    /**
     *  The task that shows each frame.
     */
    private class Shutter extends TimerTask {
        
        public void run( ) {
            synchronized(sprocket) {
                offScreen.clearRect(0,0,getSize().width,getSize().height);
                paintFrame(offScreen);
                paint(onScreen);
            }
        }
        
    }
 
}
