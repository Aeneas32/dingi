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

import org.dolben.MarkN.*;

/**
 *  This class contains the <i>N</i> digit number game guess Generator
 *  that uses the Brute Force algorithm.
 *  <p></p>
 *  For each next guess it simply goes through a sequence of valid numbers
 *  to find one that could have given all of the scores that were obtained
 *  for the previous guesses.
 */
class BruteForceGenerator extends Generator {
    private int              _guesses;  // the number of guesses made so far
    private NumberSequence   _sequence; // a sequence generator
    private Numbah[]         _guess;    // the guesses that have been made
    private Score[]          _score;    // the scores give for the guesses made
    private static final int _MAX_GUESSES = 10; // maximum number of guesses
    
    /**
     *  constructs a BruteForceGenerator
     */
    BruteForceGenerator() {
        _sequence = new NumberSequence();
        _guess = new Numbah[_MAX_GUESSES];
        _score = new Score[_MAX_GUESSES];
        _guesses = 0;
    }
    
    /**
     *  generates the next guess
     *
     *  @param guess the next generated guess
     *
     *  @return true iff there is a next guess.
     *
     *  (It won't be possible to generate a guess if scores given for
     *  the previous guesses are logically inconsistent.)
     */
    public boolean nextGuess( Numbah guess ) {
        if ( _guesses > 0 ) {
            do  {
                if ( !_sequence.next() ) {
                    _guess[_guesses-1].copy(_sequence);
                    return false;
                }
            }
            while ( !possible() );
        }
        _sequence.copy(guess);
        _guess[_guesses++] = (Numbah)guess.clone();
        return true;
    }
    
    /**
     *  @return true iff current sequence gets scores equal to those for
     *  all previous guesses
     */
    private boolean possible( ) {
        for ( int i = 0; i < _guesses; ++i ) {
            if ( !_score[i].equal(_sequence.score(_guess[i])) ) {
                return false;
            }
        }
        return true;
    }
    
    /**
     *  is given a score for the last guess
     *
     *  @param score the score for the last guess
     */
    public void tellScore( Score score ) {
        _score[_guesses-1] = (Score)score.clone();
    }

    /**
     *  backs up to state before last nextGuess()
     */
    public void retractScore( ) {
        _guesses--;
        _guess[_guesses-1].copy(_sequence);
    }
    
    /**
     *  tests the class
     *
     *  @param arg ignored
     */
    public static void main( String[] arg ) throws Exception {
        GeneratorTest test = new GeneratorTest() {
            public Generator newGenerator() {
                return new BruteForceGenerator();
            }
        };
    test.test();
    }
    
}
