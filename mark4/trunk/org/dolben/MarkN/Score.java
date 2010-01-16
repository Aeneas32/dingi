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

/**
 *  This is the score for some guess compared to a target.
 */
public class Score implements Cloneable {
    private int _placed;     // the number of digits in the right place
    private int _misplaced;  // the number of digits in the wrong place
    
    /**
     *  constructs a new score
     */
    Score( ) {
        _placed = 0;
        _misplaced = 0;
    }
    
    /**
     *  makes a new copy of this score
     *
     *  @return the copy
     */
    public Object clone( ) {
        try {
            return super.clone();
        } catch ( CloneNotSupportedException e ) {
            return null;
        }
    }
    
    /**
     *  sets the number of digits which are in the right place
     *
     *  @param placed the number of digits in the right place
     */
    public void setPlaced( int placed ) {
        _placed = placed;
    }

    /**
     *  gets the number of digits which are in the right place
     *
     *  @return the number of digits in the right place
     */
    public int getPlaced( ) {
        return _placed;
    }
    
    /**
     *  sets the number of digits which are in the wrong place
     *
     *  @param misplaced the number of digits in the wrong place
     */
    public void setMisplaced( int misplaced ) {
        _misplaced = misplaced;
    }

    /**
     *  gets the number of digits which are in the wrong place
     *
     *  @return the number of digits in the wrong place
     */
    public int getMisplaced( ) {
        return _misplaced;
    }
    
    /**
     *  tests whether or not this score indicates a correct guess
     *
     *  @return true iff this score is a correct guess
     */
    public boolean correct( ) {
        return _placed == Configuration.getPlaces();
    }
    
    /**
     *  tests whether or not this score is valid
     *
     *  @return true iff this score is valid
     */
    public boolean valid( ) {
        if ( _placed + _misplaced > Configuration.getPlaces() ) {
            return false;
        }
        if ( _placed == Configuration.getPlaces()-1 && _misplaced != 0 ) {
            return false;
        }
        return true;
    }
    
    /**
     *  tests whether or not this score is equal to the given one
     *
     *  @param s the score to check for equality
     *
     *  @return true iff this score is equal to the given one
     */
    public boolean equal( Score s ) {
        return s._placed == _placed && s._misplaced == _misplaced;
    }
    
    /**
     *  increments one of the score's counters
     *
     *  @param inplace true iff the counter for digits in the right place is
     *                 to be incremented
     */
    public void count( boolean inplace ) {
        if ( inplace ) {
            ++_placed;
        } else {
            ++_misplaced;
        }
    }
    
    /**
     *  makes a string of the score
     *
     *  @return the string of the score
     */
    public String toString( ) {
        String s = _placed+" "+_misplaced;
        return s;
    }
    
}
