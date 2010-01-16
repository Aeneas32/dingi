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

import java.awt.TextField;

/**
 *  This is a field for entry of an <i>N Digit Number</i>.
 */
public class NumberField extends TextField {
    public static final long serialVersionUID = 1;

    /**
     *  constructs a new number entry field
     */
    NumberField( ) {
        super("",Configuration.getPlaces());
    }
    
    /**
     *  gets a number from the field
     *
     *  @return the number
     *
     *  @exception Exception when the field does not have a valid number
     */
    public Numbah getNumber( ) throws Exception {
        Numbah n = new Numbah();
        
        String s = getText();
        for ( int place = 0; place < Configuration.getPlaces(); ++place ) {
            String d = s.substring(place,place+1);
            n.setDigit(Integer.valueOf(d).intValue(),place);
        }
        if ( s.length() != Configuration.getPlaces() || !n.valid() ) {
             throw new Exception();
        }
        return n;
    }
    
    /**
     *  sets the number in the field
     *
     *  @param n the number to be set
     */
    public void setNumber( Numbah n ) {
        setText(n.toString());
    }

}
