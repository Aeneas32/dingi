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
 *  This is a scrambler of <i>N Digit Numbers</i>,
 *  which pseudorandomly remaps the places and digits of a number to disguise
 *  the underlying sequence of some generator.
 */
public class NumberScrambler {
    private int[] _placeMap;  // the map of places
    private int[] _digitMap;  // the map of digits
    
    /**
     *  constructs a new number scrambler
     */
    NumberScrambler( ) {
        _placeMap = randomMap(Configuration.getPlaces());
        _digitMap = randomMap(Configuration.getDigits());
    }
    
    /**
     *  produces a pseudorandom map
     *
     *  @param size the size of the map to be produced
     *
     *  @return the map
     */
    private int[] randomMap( int size ) {
        int [] map = new int[size];
        RandomDeal deal = new RandomDeal(size);
        for ( int i = 0; i < size; ++i ) {
            map[i] = deal.next();
        }
        return map;
    }
    
    /**
     *  scrambles the given number
     *
     *  @param s the number to be scrambled
     */
    public void scramble( Numbah s ) {
        Numbah n = (Numbah)s.clone();
        
        for ( int place = 0; place < Configuration.getPlaces(); ++place ) {
            s.setDigit(_digitMap[n.getDigit(place)],_placeMap[place]);
        }
    }

}
