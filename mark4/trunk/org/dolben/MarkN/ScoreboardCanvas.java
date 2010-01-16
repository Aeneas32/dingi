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

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.util.Vector;

/**
 *  This a display for a list of guesses and their scores,
 *  plus a message.
 */
class ScoreboardCanvas extends Canvas {
    public static final long serialVersionUID = 1;
    private Vector _guess;   // the guesses
    private Vector _score;   // the scores
    private String _message; // the message
    
    /**
     *  constructs a new, empty scoreboard
     */
    ScoreboardCanvas( ) {
        clear();
    }
    
    /**
     *  clears the scoreboard
     */
    public void clear( ) {
        _guess = new Vector();
        _score = new Vector();
        _message = "";
    }
    
    /**
     *  adds a guess to the scoreboard
     *
     *  @param guess the guess to add
     */
    public void addGuess( Numbah guess ) {
        _guess.addElement(guess.clone());
    }
    
    /**
     *  adds a score to the scoreboard
     *
     *  @param score the score to add
     */
    public void addScore( Score score ) {
        _score.addElement(score.clone());
    }

    /**
     *  removes a guess and score from the scoreboard
     *
     *  @return the guess that was removed
     */
    public Numbah retractScore( ) {
        if ( _score.size() > 0 ) {
            _score.removeElementAt(_score.size()-1);
            if ( _score.size() < _guess.size()-1 ) {
                _guess.removeElementAt(_guess.size()-1);
            }
        }
        Numbah guess = (Numbah)_guess.elementAt(_guess.size()-1);
        return (Numbah)guess.clone();
    }
    
    /**
     *  sets the scoreboard message
     *
     *  @param s the message to be set
     */
    public void setMessage( String s ) {
        _message = s;
    }
    
    /**
     *  draws the scoreboard
     *
     *  @param g the graphics environment
     */
    public void paint( Graphics g ) {
        final int DISPLAY_LINES = 9;
        FontMetrics fm = g.getFontMetrics();
        int v = fm.getHeight();
        int h = fm.stringWidth("1234");
        g.drawRect(
            h-v/2,v/2,
            fm.stringWidth("76:  1 0")+
            fm.stringWidth("4")*Configuration.getPlaces()+v,
            v*10
        );
        int i = 0;
        if ( _guess.size() > DISPLAY_LINES ) {
            i = _guess.size()-DISPLAY_LINES;
        }
        for ( ; i < _guess.size(); ++i ) {
            v += fm.getHeight();
            String s = (i+1)+": "+_guess.elementAt(i);
            if ( i < _score.size() ) {
                s += " "+_score.elementAt(i);
            }
            int d = 0;
            if ( i+1 < 10 ) {
                d = fm.stringWidth("1");
            }
            g.drawString(s,h+d,v);
        }
        g.drawString(_message,10,(DISPLAY_LINES+3)*fm.getHeight());
    }

}
