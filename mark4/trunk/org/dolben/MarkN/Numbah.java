/**
 *  @author  Hank Dolben
 *  @version 2004 Mar 12
 *
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
 *  This is an <i>N Digit Number</i>.
 *
 *  Note that it is called "Numbah" (the New England pronunciation) to
 *  disambiguate it from java.lang.Number without having to use the whole name.
 */
public class Numbah implements Cloneable {
    private int[] _digit;
    
    /**
     *  constructs a number
     */
    public Numbah( ) {
        _digit = new int[Configuration.getPlaces()];
    }
    
    /**
     *  makes a new copy of this number
     *
     *  @return the copy
     */
    public Object clone( ) {
        Numbah n = new Numbah();
        copy(n);
        return n;
    }
    
    /**
     *  copies a given number into this one
     *
     *  @param n the number to be copied
     */
    public void copy( Numbah n ) {
        for ( int place = 0; place < Configuration.getPlaces(); ++place ) {
            n._digit[place] = _digit[place];
        }
    }
    
    /**
     *  sets a digit of this number
     *
     *  @param digit the new digit
     *  @param place the place to set
     */
    public void setDigit( int digit, int place ) {
        _digit[place] = digit;
    }

    /**
     *  tests the validity of this number (no two digits the same)
     *
     *  @return true iff this number is valid
     */
    public boolean valid( ) {
        for ( int i = 0; i < Configuration.getPlaces(); ++i ) {
            for ( int j = 0; j < i; ++j ) {
                if ( _digit[i] == _digit[j] ) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     *  gets the digit in a given place
     *
     *  @param place which digit to get
     *
     *  @return the digit
     */
    public int getDigit( int place ) {
        return _digit[place];
    }

    /**
     *  makes a pseudorandom number
     *
     *  @return the pseudorandom number
     */
    public static Numbah random( ) {
        RandomDeal deal = new RandomDeal(Configuration.getDigits());
        Numbah n = new Numbah();
        for ( int place = 0; place < Configuration.getPlaces(); ++place ) {
            n.setDigit(deal.next(),place);
        }
        return n;
    }
    
    /**
     *  makes a string of the digits in this number
     *
     *  @return the string of the digits in this number
     */
    public String toString( ) {
        String s = new String();
        
        for ( int place = 0; place < Configuration.getPlaces(); ++place ) {
            s += getDigit(place);
        }
        return s;
    }
    
    /**
     *  scores a guess with this number as the target
     *
     *  @param guess the guess to score
     *
     *  @return the score
     */
    public Score score( Numbah guess ) {
        Score s = new Score();
        
        for ( int i = 0; i < Configuration.getPlaces(); ++i ) {
            for ( int j = 0; j < Configuration.getPlaces(); ++j ) {
                if ( guess.getDigit(i) == getDigit(j) ) {
                    s.count( i == j );
                }
            }
        }
        return s;
    }

}
