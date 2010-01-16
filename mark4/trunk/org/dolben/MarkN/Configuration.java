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
 *  This is the configuration for the <i>N Digit Number Game</i>:
 *  the number (<i>N</i>) of places in a target or guess,
 *  and the number of digits that are used (e.g., 10, ('0'-'9')).
 *
 *  Note that the terminology is conflicting,
 *  as "<i>N Digit</i>" in the name of the game,
 *  denotes the number of <i>places</i> in a <i>Number</i>.
 */
public class Configuration {
    private static int     _DIGITS = 10;   // the number of digits
    private static int     _PLACES =  4;   // the number of places
    private static boolean _used = false;  // whether or not Configuration has been used
    
    /**
     *  sets the number of digits
     *
     *  @param s the number of digits as represented by a string
     *
     *  @exception Exception when s cannot be parsed as an integer,
     *                  or the number doesn't result in a valid configuration,
     *                  or the configuration has already been used
     */
    public static void setDigits( String s )
        throws Exception {
        if ( s == null ) {
            return;
        }
        int digits = Integer.parseInt(s);
        validateConfiguration(digits,_PLACES);
        if ( digits > 10 ) {
            throw new Exception("digits > 10");
        }
        _DIGITS = digits;
    }
    
    /**
     *  sets the number of places
     *
     *  @param s the number of places, <i>N</i>, as represented by a string
     *
     *  @exception Exception when s cannot be parsed as an integer,
     *                  or the number doesn't result in a valid configuration,
     *                  or the configuration has already been used
     */
    public static void setPlaces( String s )
        throws Exception {
        if ( s == null ) {
            return;
        }
        int places = Integer.parseInt(s);
        validateConfiguration(_DIGITS,places);
        if ( places < 1 ) {
            throw new Exception("places < 1");
        }
        _PLACES = places;
    }
    
    /**
     *  checks that the given configuration is valid and has not been used,
     *  so that it's OK to set
     *
     *  @param digits the number of digits
     *  @param places the number of places
     *
     *  @exception when the configuration is invalid or has been used
     */
    private static void validateConfiguration( int digits, int places )
        throws Exception {
        if ( _used ) {
            throw new Exception("reconfiguration after use");
        }
        if ( digits < places ) {
            throw new Exception("digits < places");
        }
    }
    
    /**
     *  gets the number of digits
     *
     *  @return the number of digits
     */
    public static int getDigits( ) {
        _used = true;
        return _DIGITS;
    }
    
    /**
     *  gets the number of places
     *
     *  @return the number of places
     */
    public static int getPlaces( ) {
        _used = true;
        return _PLACES;
    }
    
}
