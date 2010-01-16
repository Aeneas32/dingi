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
import java.util.BitSet;

/**
 *  This class contains the guts of the <i>N</i> digit number game guess generator,
 *  particularly, the "Monitors algorithm" for generating guesses.
 *  </p><p>
 *  Each time a guess is generated, it is a number which could have produced
 *  the scores of the previous guesses.
 *  </p><p>
 *  There is a Monitor for each (digit, place) containing the requirements
 *  created by the guesses made before and their scores.
 *  To generate the digit in each place of the next guess,
 *  find a digit that, according to the Monitor for that digit and place,
 *  satisfies the requirements.
 *  When there is no digit that satisfies the requirements
 *  at some place in the guess, backtrack and try a different digit in the
 *  previous place (a recursive operation).
 *  </p><p>
 *  A requirement says that some number of digits must be included in the guess
 *  from a given set.
 *  When a guess is scored with the number of <i>placed</i> digits and the
 *  number of <i>misplaced</i> digits, first, two sets of digits are created:
 *  the digits in the guess, and the digits not in the guess.
 *  Then, three requirements are created and added to the Monitors:
 *  </p>
 *  <ul>
 *    <li>
 *      There must be <i>placed</i> digits, from the set of digits in the
 *      guess, in a subsequent guess that are in the same
 *      place as digits in this guess
 *    </li>
 *    <li>
 *      There must be <i>misplaced</i> digits, from the set of digits in the
 *      guess, in a subsequent guess that are in a
 *      different place than digits in this guess
 *    </li>
 *    <li>
 *      There must be <i>N</i> - <i>placed</i> - <i>misplaced</i> digits,
 *      from the set of digits not in the guess, in a subsequent guess in
 *      any place.
 *    </li>
 *  </ul>
 *  <p>
 */
class MonitorsGenerator extends Generator {
    private static final int _MAX_GUESSES = 10; // maximum number of guesses
    private Monitor[][]      _monitor;  // one Monitor for each (digit, place)
    private boolean          _first;    // true before the first guess is made
    private Numbah           _lastGuess;// the last guess that was generated
    
    /**
     *  makes one Monitor for each digit in each place and
     *  sets that the next guess is the first one
     */
    MonitorsGenerator( ) {
        _monitor =
            new Monitor[Configuration.getDigits()][Configuration.getPlaces()];
        for ( int digit = 0; digit < Configuration.getDigits(); ++digit ) {
            for ( int place = 0; place < Configuration.getPlaces(); ++place ) {
                _monitor[digit][place] = new Monitor(_MAX_GUESSES);
            }
        }
        _first = true;
    }
    
    /**
     *  is given a score for the last guess
     *
     *  @param score the score for the last guess
     */
    public void tellScore( Score score ) {
        // make the two sets of digits: of those in the guess, and of those not
        BitSet inGuess = new BitSet(Configuration.getDigits());
        BitSet notInGuess = new BitSet(Configuration.getDigits());
        
        // make the three kinds of requirements
        //   for "placed"    digits
        //   for "misplaced" digits
        //   for "other"     digits
        Requirement inReq = new Requirement(inGuess,score.getPlaced());
        Requirement misReq = new Requirement(inGuess,score.getMisplaced());
        Requirement otherReq = new Requirement(
            notInGuess,
            Configuration.getPlaces()-score.getPlaced()-score.getMisplaced()
        );
        
        // add the requirements to the monitor for each (digit, place)
        for ( int i = 0; i < Configuration.getPlaces(); ++i ) {
            int digit = _lastGuess.getDigit(i);
            inGuess.set(digit);
            for ( int place = 0; place < Configuration.getPlaces(); ++place ) {
                if ( i == place ) {
                    _monitor[digit][place].addRequirement(inReq);
                } else {
                    _monitor[digit][place].addRequirement(misReq);
                }
            }
        }
        for ( int digit = 0; digit < Configuration.getDigits(); ++digit ) {
            if ( !inGuess.get(digit) ) {
                notInGuess.set(digit);
                for ( int place = 0;
                        place < Configuration.getPlaces(); ++place ) {
                    _monitor[digit][place].addRequirement( otherReq );
                }
            }
        }
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
        if ( _first ) {
            _first = false;
            firstGuess(guess);
        } else if ( !nextPlace(guess,0) ) {
            removeRequirements();
            return false;
        }
        _lastGuess = guess;
        return true;
    }
    
    /**
     *  recursively generates a guess place by place.
     *  If a monitor says its OK to pick a particular digit in this place,
     *  go on to generate the digit for the next place.
     *
     *  @param guess the number being generated
     *  @param place the place in the number to generate here
     *
     *  @return true iff a guess can be found
     */
    private boolean nextPlace( Numbah guess, int place ) {
        if ( place == Configuration.getPlaces() ) {
            return true;
        }
        for ( int digit = 0; digit < Configuration.getDigits(); ++digit ) {
            Monitor monitor = _monitor[digit][place];
            if ( monitor.pick(digit,Configuration.getPlaces()-place) ) {
                boolean done = nextPlace(guess,place+1);
                monitor.unpick(digit);
                if ( done ) {
                    guess.setDigit(digit,place);
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     *  fills in the "standard" first guess, e.g., 0123
     */
    private void firstGuess( Numbah guess ) {
        for ( int place = 0; place < Configuration.getPlaces(); ++place ) {
            guess.setDigit(place,place);
        }
    }
    
    /**
     *  backs up to state before last nextGuess()
     */
    public void retractScore( ) {
        removeRequirements();
        nextGuess(new Numbah());
    }
    
    /**
     *  removes the last requirement for the monitor of each digit, place
     */
    private void removeRequirements( ) {
        for ( int digit = 0; digit < Configuration.getDigits(); ++digit ) {
            for ( int place = 0; place < Configuration.getPlaces(); ++place ) {
                _first = _monitor[digit][place].removeRequirement();
            }
        }
    }
    
    /**
     *  makes a string made of the strings of all the monitors
     *
     *  @return the string of all the monitors
     */
    public String toString( ) {
        String s;
        
        s = "";
        for ( int digit = 0; digit < Configuration.getDigits(); ++digit ) {
            s += _monitor[digit][0];
            for ( int place = 1; place < Configuration.getPlaces(); ++place ) {
                s += ", "+_monitor[digit][place];
            }
            s += "\n";
        }
        return s;
    }
    
    /**
     *  tests the class
     *
     *  @param arg ignored
     */
    public static void main( String[] arg ) throws Exception {
        GeneratorTest test = new GeneratorTest() {
            public Generator newGenerator() {
                return new MonitorsGenerator();
            }
        };
        test.test();
    }
    
}
