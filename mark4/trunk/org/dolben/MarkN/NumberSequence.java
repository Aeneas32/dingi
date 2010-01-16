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

import java.util.BitSet;

/**
 *  This is a generator of a complete sequence of valid N Digit Numbers.
 */
public class NumberSequence extends Numbah {

    /**
     *  constructs a new sequence generator
     */
    public NumberSequence( ) {
        for ( int place = 0; place < Configuration.getPlaces(); ++place ) {
            setDigit( Configuration.getPlaces()-1-place, place );
        }
    }
    
    /**
     *  generates the next number
     *
     *  @return true iff there is a number left to generate 
     */
    public boolean next( ) {
        BitSet used = new BitSet(Configuration.getDigits());
        for ( int place = 0; place < Configuration.getPlaces(); ++place ) {
            used.set(getDigit(place));
        }
        return nextPlace(0,used);
    }
    
    /**
     *  recursively generates the next number for each place
     *
     *  @return true iff there is a number left to generate 
     */
    private boolean nextPlace( int place, BitSet used ) {
        if ( place == Configuration.getPlaces() ) {
            return false;
        }
        int digit = getDigit(place);
        used.clear(digit);
        do  {
            ++digit;
            if ( digit == Configuration.getDigits() ) {
                if ( !nextPlace(place+1,used) ) {
                    return false;
                }
                digit = 0;
            }
        }
        while ( used.get(digit) );
        used.set(digit);
        setDigit(digit,place);
        return true;
    }
    
    /**
     *  tests the class
     *
     *  @param arg ignored
     */
    public static void main( String[] arg ) {
        NumberSequence sequence = new NumberSequence();
        do  {
            System.out.println(sequence.toString());
        }
        while ( sequence.next() );
    }
    
}
