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

package org.dolben.MarkX;

/**
 *  A Monitor has a list of Requirements, one for each guess,
 *  and checks that a digit conforms to those Requirements,
 *  simply aggregating the effect of the individual Requirements.
 */
class Monitor {
    private int _guesses; // the number of guesses that have been made
    private Requirement[] _requirement; // the array of Requirements
            // note that this would properly be a List, but is an array as
            // an historical artifact of the translation from C
    
    /**
     *
     */
    Monitor( int maxGuesses ) {
        _guesses = 0;
        _requirement = new Requirement[maxGuesses];
    }
    
    /**
     *  adds a Requirement
     *
     *  @param requirement the Requirement added for the next guess
     */
    public void addRequirement( Requirement requirement ) {
        _requirement[_guesses++] = requirement;
    }
    
    /**
     *  removes the last Requirement, for backing up when guess scoring is inconsistent
     *
     *  @return true iff there is a Requirement to remove
     */
    public boolean removeRequirement( ) {
        if ( _guesses > 0 ) {
            --_guesses;
        }
        return _guesses == 0;
    }
    
    /**
     *  checks that the a digit can be picked with a number of places left in the guess
     *  and informs all the Requirements of the pick when they all approve
     *
     *  @param digit the digit to be checked
     *  @param left  the number of places left for digits to be picked in the guess
     *
     *  @return true iff it's OK with all of the requirements of this Monitor
     *  to pick the digit
     */
    public boolean pick( int digit, int left ) {
        for ( int guess = 0; guess < _guesses; ++guess ) {
            if ( !_requirement[guess].ok(digit,left) ) {
                return false;
            }
        }
        for ( int guess = 0; guess < _guesses; ++guess ) {
            _requirement[guess].pick(digit);
        }
        return true;
    }
    
    /**
     *  tells all of the Requirements for this Monitor to retract the pick of a digit
     *
     *  @param digit the digit that is no longer picked
     */
    public void unpick( int digit ) {
        for ( int guess = 0; guess < _guesses; ++guess ) {
            _requirement[guess].unpick(digit);
        }
    }
    
    /**
     *  makes a string of the last Requirement
     *
     *  @return a string representing the last Requirement
     */
    public String toString( ) {
        return ""+_requirement[_guesses-1];
    }

}
