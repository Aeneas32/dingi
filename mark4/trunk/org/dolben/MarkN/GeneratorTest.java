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

import java.text.DecimalFormat;

/**
 *  This tests an N digit number game guess generator.
 *
 *  Override the newGenerator factory method in a derived class.
 */
public abstract class GeneratorTest {

    /**
     *  constructs a GeneratorTest
     */
    public GeneratorTest( ) {
        try {
            Configuration.setPlaces(System.getProperty("NDNG.PLACES"));
            Configuration.setDigits(System.getProperty("NDNG.DIGITS"));
        } catch ( Exception e ) {
            System.out.println(e.toString());
        }
    }
    
    /**
     *  makes a new Generator
     *
     *  @return the new Generator
     */
    public abstract Generator newGenerator( );
        
    /**
     *  trys guessing all valid targets and
     *  prints how many targets required a number of guesses, the distribution
     */
    public void test( ) throws Exception {
        System.out.println(
            "places = "+Configuration.getPlaces()+
            ", digits = "+Configuration.getDigits()
        );
        final int MAX_GUESSES = 10;
        int[] count = new int[MAX_GUESSES];
        NumberSequence target = new NumberSequence();
        do  {
            count[guesses(target)-1] += 1;
        }
        while ( target.next() );
        for ( int i = 0; i < MAX_GUESSES; ++i ) {
            System.out.print(format(i+1,2)+": ");
            System.out.println(format(count[i],5));
        }
    }
    
    /**
     *  formats "n", right justified, in a String of "width"
     */
    private String format( int n, int width ) {
        DecimalFormat f = new DecimalFormat();
        String s = f.format(n);
        for ( int col = s.length(); col < width; ++col ) {
            s = ' '+s;
        }
        return s;
    }
    
    /**
     *  returns the number of guesses the generator needs
     *  to match the given "target"
     */
    private int guesses( Numbah target ) throws Exception {
        Generator generator = newGenerator();
        Numbah guess = new Numbah();
        Score score;
        int n = 0;
        do  {
            n += 1;
            if ( !generator.nextGuess(guess) ) {
                throw new Exception("Generator failed");
            }
            score = target.score(guess);
            generator.tellScore(score);
        }
        while ( !score.correct() );
        return n;
    }
    
}
