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
 *  This is a pseudorandom order generator for some number integers.
 *  For example, if the number of items is 10, the sequence obtained by
 *  calls of next() will be some pseudorandom permutation of 0 to 9.
 */
class RandomDeal {
    private int[] _digit;  // the numbers left to deal
    private int   _n;      // how many numbers are left to deal

    /**
     *  constructs a new pseudorandom order generator
     *
     *  @param n the number of items in the sequence
     */
    RandomDeal( int n ) {
        _digit = new int[n];
        _n = n;
        for ( int i = 0; i < _n; ++i ) {
            _digit[i] = i;
        }
    }
    
    /**
     *  gets the next item in the sequence
     *
     *  @return the next item
     */
    int next( ) {
        int pick = (int)(_n*Math.random());
        int digit = _digit[pick];
        --_n;
        for ( ; pick < _n; ++pick ) {
            _digit[pick] = _digit[pick+1];
        }
        return digit;
    }

}
