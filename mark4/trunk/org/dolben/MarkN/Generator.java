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
 *  This is the interface for an N digit number game guess generator.
 *  Derive a class from this one with a constructor and the methods
 *  given here to implement some algorithm for generating guesses.
 *  </p><p>
 *  A Guesser repeatedly calls nextGuess() to get the next guess
 *  and then tellScore() to give the Generator the score for the last guess.
 */
public abstract class Generator {
    /**
     *  makes the next guess
     *
     *  @param guess the guess that gets generated, if possible
     *
     *  @return true iff it was possible to generate a next guess,
     *  if not, the "guess" does not change, and the previous score
     *  is forgotten
     */
    public abstract boolean nextGuess( Numbah guess );
    
    /**
     *  is told a score for the last guess
     *
     *  @param score the score for the last guess -
     *  the score must be legit, though may be inconsistent with previous scores
     *  in which case nextGuess() will return false
     */
    public abstract void tellScore( Score score );
    
    /**
     *  backs up the generator to the state before the last nextGuess()
     */
    public abstract void retractScore( );
    
}
