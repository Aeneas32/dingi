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
 *  This is field for entry of a score.
 */
class ScoreField extends TextField {
    
    public static final long serialVersionUID = 1;
    
    /**
     *  constructs a new score field
     */
    ScoreField( ) {
        super("",2);
    }
    
    /**
     *  gets the score from the field
     *
     *  @return the score
     *
     *  @exception Exception when the field does not contain a valid score
     */
    public Score getScore( ) throws Exception {
        Score score = new Score();
        
        String s = getText();
        String d = s.substring(0,1);
        score.setPlaced(Integer.valueOf(d).intValue());
        d = s.substring(1,2);
        score.setMisplaced(Integer.valueOf(d).intValue());
        if ( s.length() != 2 || !score.valid() ) {
            throw new Exception();
        }
        return score;
    }

}
