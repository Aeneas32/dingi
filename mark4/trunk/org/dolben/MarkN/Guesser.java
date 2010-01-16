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
 *  This is an applet that guesses an N digit number.
 *
 *  A Generator must be supplied in a derived class by overriding
 *  the newGenerator factory method.
 */
public abstract class Guesser extends Applet {
    
    public static final long serialVersionUID = 1;
    //  Derivation Interface
    
    /**
     *  makes a guess Generator
     *
     *  @return the new Generator
     */
    public abstract Generator newGenerator();
    
    private ScoreboardCanvas    _scoreboard;  // display of guesses and scores
    private NumberField         _target;      // target entry field
    private ScoreField          _score;       // score entry field
    private Button              _scoreButton; // button to confirm score
    private Numbah              _guess;       // the last guess made
    private Generator           _generator;   // the guess generator
    private NumberScrambler     _s;           // the guess scrambler
    
    /**
     *  initializes the applet
     */
    public synchronized void init( ) {
        /*
         *  for target, set up
         *      a field to enter a number
         *      a "New" button to set a new target
         *      a "Random" button to set a random target
         *
         *  setup a scoreboard to display guesses and scores
         *
         *  for guess, set up
         *      a field to enter a score
         *      a "Score" button to give the score
         *      a "Retract" button to undo the last score
         */
        setBackground(Color.white);
        setLayout(new BorderLayout());
        
        try {
            Configuration.setPlaces(getParameter("NDNG.PLACES"));
            Configuration.setDigits(getParameter("NDNG.DIGITS"));
        } catch ( Exception e ) {
            System.out.println( e.toString() );
        }
        Button b;
        Panel north = new Panel();
        _target = new NumberField();
        north.add(_target);

        north.add(b = new Button("New"));
        b.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    retarget();
                }
            }
        );

        north.add(b = new Button("Random"));
        b.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    random();
                }
            }
        );
        add("North",north);
        
        _scoreboard = new ScoreboardCanvas();
        add("Center",_scoreboard);
        
        Panel south = new Panel();
        _score = new ScoreField();
        south.add(_score);

        south.add(_scoreButton = new Button("Score"));
        _scoreButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    score();
                }
            }
        );

        south.add(b = new Button("Retract"));
        b.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    retract();
                }
            }
        );
        add("South",south);

        _guess = new Numbah();
        startGuessing();
    }
        
    /**
     *  the action performed by the "New" button
     *  automatically score and guess the target when given
     *  otherwise just make the first guess, allowing player to score
     */
    private void retarget( ) {
        startGuessing();
        solve();
        _scoreboard.repaint();
    }
    
    /**
     *  the action performed by the "Random" button
     *  automatically score and guess a randomly generated target
     */
    private void random( ) {
        startGuessing();
        _target.setNumber(Numbah.random());
        solve();
        _scoreboard.repaint();
    }
    
    /**
     *  the action performed by the "Score" button
     *  make another guess given the score of the last guess
     */
    private void score( ) {
        _scoreboard.setMessage("");
        try {
            guess( _score.getScore() );
        } catch ( Exception e ) {
            _scoreboard.setMessage("Score must be legit two digits only");
        }
        _scoreboard.repaint();
    }
    
    /**
     *  the action performed by the "Retract" button
     *  retract the last given score
     */
    private void retract( ) {
        _scoreButton.setEnabled(true);
        _generator.retractScore();
        _guess = _scoreboard.retractScore();
        _scoreboard.setMessage("");
        _scoreboard.repaint();
    }

    /**
     *  starts the program's guessing over
     */
    private void startGuessing( ) {
        _generator = newGenerator();
        _s = new NumberScrambler();
        _scoreButton.setEnabled(true);
        _scoreboard.clear();
        scrambledGuess();
        _scoreboard.addGuess(_guess);
    }
    
    /**
     *  automatically scores and guesses the given target
     */
    private void solve( ) {
        if ( _target.getText().length() != 0 ) {
            try {
                Numbah target = _target.getNumber();
                while ( !guess(target.score(_guess)) );
            } catch ( Exception e ) {
                _scoreboard.setMessage(
                    "Numbah must have "+
                    Configuration.getPlaces()+" unique digits"
                );
            }
        }
    }
    
    /**
     *  makes the next guess given the score to the previous guess
     *  unless the score says that the previous guess was correct
     *  don't update the scoreboard if a next guess can't be generated
     *
     *  @return true when scored guess is correct
     */
    private boolean guess( Score score ) {
        _generator.tellScore(score);
        if ( score.correct() ) {
            _scoreButton.setEnabled(false);
            _scoreboard.addScore(score);
            return true;
        }
        if ( scrambledGuess() ) {
            _scoreboard.addScore(score);
            _scoreboard.addGuess(_guess);
        } else {
            _scoreboard.setMessage("Scores are inconsistent");
        }
        return false;
    }
    
    /**
     *  scrambles the next guess to disguise the strategy ;-)
     */
    private boolean scrambledGuess( ) {
        Numbah plain = new Numbah();
        if ( _generator.nextGuess(plain) ) {
            _guess = (Numbah)plain.clone();
            _s.scramble(_guess);
            return true;
        } else {
            return false;
        }
    }
    
}
