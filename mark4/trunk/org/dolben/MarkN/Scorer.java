/**
 *  MarkN: interactive n digit number game
 *  Copyright (c) 2000-2010 Hank Dolben
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.dolben.MarkN; 

import java.applet.Applet;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.Button;
import java.awt.event.*;

/**
 *  This is an applet that scores a user's guesses of an <i>N Digit Number</i>
 */
public class Scorer extends Applet {
    public static final long serialVersionUID = 1;
    private ScoreboardCanvas _scoreboard; // display of guesses and scores
    private Numbah           _target;     // number the user tries to guess
    private NumberField      _guess;      // the user's guess
    
    /**
     *  initializes the applet
     */
    public synchronized void init( ) {
        /*
         *  for target, set up
         *      a "New" button to set a new target
         *      a "Reveal" button to show the target
         *
         *  setup a scoreboard to display guesses and scores
         *
         *  for guess, set up
         *      a field to enter a guess
         *      a "Guess" button to give the guess
         */
        setBackground(Color.white);
        setLayout(new BorderLayout());
        
        try {
            Configuration.setPlaces( getParameter( "NDNG.PLACES" ) );
            Configuration.setDigits( getParameter( "NDNG.DIGITS" ) );
        } catch ( Exception e ) {
            System.out.println(e.toString());
        }
        Button b;
        Panel north = new Panel();
        
        north.add( b = new Button("New"));
        b.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    retarget();
                }
            }
        );
        
        north.add(b = new Button("Reveal"));
        b.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    reveal();
                }
            }
        );
        
        add("North",north);
        
        _scoreboard = new ScoreboardCanvas();
        add("Center",_scoreboard);
        
        Panel south = new Panel();
        _guess = new NumberField();
        south.add(_guess);
        
        south.add(b = new Button("Guess"));
        b.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    guess();
                }
            }
        );
        
        add("South",south);

        _target = new Numbah();
        startGuessing();
    }
    
    /**
     *  starts guessing over with a new number
     */
    private void startGuessing( ) {
        _scoreboard.clear();
        _target = Numbah.random();
    }
    
    /**
     *  gets a guess and scores it
     */
    private void guessAgain( ) {
        try {
            Numbah guess = _guess.getNumber();
            _scoreboard.addGuess(guess);
            _scoreboard.addScore(_target.score(guess));
        } catch ( Exception e ) {
            _scoreboard.setMessage(
                "Number must have "+
                Configuration.getPlaces()+" unique digits"
            );
        }
    }
    
    /**
     *  the action performed by the "New" button
     *  gets a new number for guessing
     */
    private void retarget( ) {
        startGuessing();
        _scoreboard.repaint();
    }
    
    /**
     *  the action performed by the "Reveal" button
     *  shows the magic number
     */
    private void reveal( ) {
        _scoreboard.setMessage("");
        _guess.setNumber(_target);
        _scoreboard.repaint();
    }
    
    /**
     *  the action performed by the "Guess" button
     *  tries another guess
     */
    private void guess( ) {
        _scoreboard.setMessage("");
        guessAgain();
        _scoreboard.repaint();
    }
    
}
