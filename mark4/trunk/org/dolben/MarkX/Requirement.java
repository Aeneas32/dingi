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

import java.util.BitSet;

/**
 *  A Requirement, as part of the Monitors algorithm for generating
 *  guesses in the N digit number game, keeps track of a set of available digits
 *  and the number of digits that must be picked from that set, and provides
 *  a check that those constraints are satisfied.
 */
class Requirement {
    private BitSet _available; // the set digits tracked
    private int    _needs;     // the number of digits from the set
                               // still needed in the guess
    
    /**
     *  constructs a Requirement with a set of available digits and a number
     *  of digits from that set that are in the target number
     *
     *  @param available the set of digits to track
     *  @param needs     the number of digits from the set in the target
     */
    Requirement( BitSet available, int needs ) {
        _available = available;
        _needs = needs;
    }
    
    /**
     *  checks that picking some digit with a number of places left in the
     *  guess satisfies the constraints
     *
     *  @param digit the digit to check
     *  @param left  the number of digits left in the guess
     *
     *  @return true iff the digit is in the set and picking it
     *  does not preclude getting the needed number of digits from the set;
     *  the guess still needs digits from the set, and there are enough digits
     *  left to use the rest of the digits needed from the set
     */
    public boolean ok( int digit, int left ) {
        return _available.get(digit) && (1 <= _needs) && (_needs <= left);
    }
    
    /**
     *  tracks the picking of a digit
     *
     *  @param digit the digit picked
     */
    public void pick( int digit ) {
        _available.clear(digit);
        --_needs;
    }
    
    /**
     *  tracks the backtracking of picking a digit
     *
     *  @param digit the digit which was picked before
     */
    public void unpick( int digit ) {
        _available.set(digit);
        ++_needs;
    }
    
    /**
     *  makes a String of the object
     *
     *  @return the String representing the object
     */
    public String toString( ) {
        return _available+" "+_needs;
    }

}
