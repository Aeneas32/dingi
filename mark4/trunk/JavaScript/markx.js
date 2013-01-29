//  Model of the N digit number game
//  Copyright (c) 2013 Hank Dolben
//
//  This file is part of Mark4.
//
//  Mark4 is free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program. if not, see <http://www.gnu.org/licenses/>

//  A Requirement, as part of the Monitors algorithm for generating
//  guesses in the N digit number game, keeps track of a set of available digits
//  and the number of digits that must be picked from that set, and provides
//  a check that those constraints are satisfied.
//
function Requirement( available, needs ) {
    this.available = available; // the set digits tracked
    this.needs = needs;         // the number of digits from the set
                                // still needed in the guess
}

//  returns that picking some digit with a number of places left in the
//  guess satisfies the constraints
//
Requirement.prototype.ok = function( digit, left ) {
    return this.available[digit] && (1 <= this.needs) && (this.needs <= left );
}

//  tracks the picking of a digit
//
Requirement.prototype.pick = function( digit ) {
    this.available[digit] = false;
    this.needs--;
}

//  tracks the backtracking of picking a digit
//
Requirement.prototype.unpick = function( digit ) {
    this.available[digit] = true;
    this.needs++;
}

//  returns a String of the object
//
Requirement.prototype.toString = function( ) {
    return "Requirement(["+this.available+"],"+this.needs+")";
}

//  the score for some guess compared to a target
//
function Score( ) {
    this.placed = 0;
    this.misplaced = 0;
}

//  sets the number of digits which are in the right place
//
Score.prototype.setPlaced = function( placed ) {
    this.placed = placed;
}

//  returns the number of digits which are in the right place
//
Score.prototype.getPlaced = function( ) {
    return this.placed;
}

//  sets the number of digits which are in the wrong place
//
Score.prototype.setMisplaced = function( misplaced ) {
    this.misplaced = misplaced;
}

//  returns the number of digits which are in the wrong place
//
Score.prototype.getMisplaced = function( ) {
    return this.misplaced;
}

//  tests whether or not this score indicates a correct guess
//
Score.prototype.correct = function( places ) {
    return this.placed == places;
}

//  returns whether or not this score is valid
//
Score.prototype.valid = function( places ) {
    if ( this.placed+this.misplaced > places ) {
        return false;
    }
    if ( this.placed == places-1 && this.misplaced != 0 ) {
        return false;
    }
    return true;
}

//  returns whether or not this score is equal to the given one
//
Score.prototype.equal = function( s ) {
    return s.placed == this.placed && s.misplaced == this.misplaced;
}

//  increments one of the score's counters
//
Score.prototype.count = function( inplace ) {
    if ( inplace ) {
        this.placed++;
    } else {
        this.misplaced++;
    }
}

//  returns a string of the score
//
Score.prototype.toString = function( ) {
    return this.placed+" "+this.misplaced;
}

//  an N digit number
//
function Number( ) {
    this.digit = new Array();
}

//  sets a digit of this number
//
Number.prototype.setDigit = function( digit, place ) {
    this.digit[place] = digit;
}

//  returns the digit in a given place
//
Number.prototype.getDigit = function( place ) {
    return this.digit[place];
}

//  returns the validity of this number (no two digits the same)
//
Number.prototype.valid = function( ) {
    for ( var i = 1; i < this.digit.length; i++ ) {
        for ( var j = 0; j < i; j++ ) {
            if ( this.digit[i] == this.digit[j] ) {
                return false;
            }
        }
    }
    return true;
}

//  returns the score of a guess with this number as the target
//
Number.prototype.score = function( guess ) {
    s = new Score();
    
    for ( var i = 0; i < this.digit.length; i++ ) {
        for ( var j = 0; j < guess.digit.length; j++ ) {
            if ( guess.digit[j] == this.digit[i] ) {
                s.count( i == j );
            }
        }
    }
    return s;
}

//  returns a string of the digits in this number
//
Number.prototype.toString = function( ) {
    var s = "";
    for ( var i = 0; i < this.digit.length; i++ ) {
        s += this.digit[i];
    }
    return s;
}

//  A Monitor has a list of Requirements, one for each guess,
//  and checks that a digit conforms to those Requirements,
//  simply aggregating the effect of the individual Requirements.
//
function Monitor( ) {
    this.requirement = new Array();
}

//  adds a Requirement
//
Monitor.prototype.addRequirement = function( requirement ) {
    this.requirement[this.requirement.length] = requirement;
}

//  removes the last Requirement, for backtracking when scoring is inconsistent
//
Monitor.prototype.removeRequirement = function( ) {
    if ( this.requirement.length > 0 ) {
        this.requirement.length--;
    }
    return this.requirement.length == 0;
}

//  checks that the a digit can be picked with a number of places left in the
//  guess and informs all the Requirements of the pick when they all approve
//
Monitor.prototype.pick = function( digit, left ) {
    for ( var guess = 0; guess < this.requirement.length; guess++ ) {
        if ( !this.requirement[guess].ok(digit,left) ) {
            return false;
        }
    }
    for ( var guess = 0; guess < this.requirement.length; guess++ ) {
        this.requirement[guess].pick(digit);
    }
    return true;
}

//  tells all of the Requirements for this Monitor to retract a pick of a digit
//
Monitor.prototype.unpick = function( digit ) {
    for ( var guess = 0; guess < this.requirement.length; guess++ ) {
        this.requirement[guess].unpick(digit);
    }
}

//  makes a string of the Requirements
//
Monitor.prototype.toString = function( ) {
    return "["+this.requirement+"]";
}

//  The guts of the N digit number game guess generator,
//  particularly, the "Monitors algorithm" for generating guesses.
//  
//  Each time a guess is generated, it is a number which could have produced
//  the scores of the previous guesses.
//  
//  There is a Monitor for each (digit, place) containing the requirements
//  created by the guesses made before and their scores.
//  To generate the digit in each place of the next guess,
//  find a digit that, according to the Monitor for that digit and place,
//  satisfies the requirements.
//  When there is no digit that satisfies the requirements
//  at some place in the guess, backtrack and try a different digit in the
//  previous place (a recursive operation).
//  
//  A requirement says that some number of digits must be included in the guess
//  from a given set.
//  When a guess is scored with the number of 'placed' digits and the
//  number of 'misplaced' digits, first, two sets of digits are created:
//  the digits in the guess, and the digits not in the guess.
//  Then, three requirements are created and added to the Monitors:
//  
//      There must be 'placed' digits, from the set of digits in the
//      guess, in a subsequent guess that are in the same
//      place as digits in this guess
//  
//      There must be 'misplaced' digits, from the set of digits in the
//      guess, in a subsequent guess that are in a
//      different place than digits in this guess
//  
//      There must be N - 'placed' - 'misplaced' digits,
//      from the set of digits not in the guess, in a subsequent guess in
//      any place.
//
function MonitorsGenerator( digits, places ) {
    this.monitor = new Array(digits);
    for ( var digit = 0; digit < digits; digit++ ) {
        this.monitor[digit] = new Array(places);
        for ( var place = 0; place < places; place++ ) {
            this.monitor[digit][place] = new Monitor();
        }
    }
    this.first = true;
}

//  score the last guess
//
MonitorsGenerator.prototype.tellScore = function( score ) {
    var digits = this.monitor.length;
    var places = this.monitor[0].length;
    
    // create to sets of digits: those in the guess, and those not
    var inGuess = new Array(digits);
    var notInGuess = new Array(digits);
    
    // make the three kinds of requirements
    //   for "placed"    digits
    //   for "misplaced" digits
    //   for "other"     digits
    var inReq = new Requirement(inGuess,score.getPlaced());
    var misReq = new Requirement(inGuess,score.getMisplaced());
    var otherReq = new Requirement(
        notInGuess,places-score.getPlaced()-score.getMisplaced());
    for ( var digit = 0; digit < digits; digit++ ) {
        inGuess[digit] = false;
    }
    // add the requirements to the monitor for each (digit, place)
    for ( var i = 0; i < places; i++ ) {
        var digit = this.lastGuess.getDigit(i);
        inGuess[digit] = true;
        for ( var place = 0; place < places; place++ ) {
            if ( i == place ) {
                this.monitor[digit][place].addRequirement(inReq);
            } else {
                this.monitor[digit][place].addRequirement(misReq);
            }
        }
    }
    for ( var digit = 0; digit < digits; digit++ ) {
        notInGuess[digit] = !inGuess[digit];
        if ( notInGuess[digit] ) {
            for ( var place = 0; place < places; place++ ) {
                this.monitor[digit][place].addRequirement(otherReq);
            }
        }
    }   
}

//  returns the next guess, zero length when not possible (inconsistent scores)
//
MonitorsGenerator.prototype.nextGuess = function( guess ) {
    if ( this.first ) {
        this.first = false;
        this.firstGuess(guess);
    } else if ( !this.nextPlace(guess,0) ) {
        return false;
    }
    this.lastGuess = guess;
    return true;
}

//  recursively generates a guess place by place.
//  returns true iff successful, otherwise caller must backtrack
//
MonitorsGenerator.prototype.nextPlace = function( guess, place ) {
    var places = this.monitor[0].length;
    if ( place == places ) {
        return true;
    }
    for ( var digit = 0; digit < this.monitor.length; digit++ ) {
        var monitor = this.monitor[digit][place];
        if ( monitor.pick(digit,places-place) ) {
            var done = this.nextPlace(guess,place+1);
            monitor.unpick(digit);
            if ( done ) {
                guess.setDigit(digit,place);
                return true;
            }
        }
    }
    return false;
}

//  fills in the "standard" first guess, e.g., 0123
//
MonitorsGenerator.prototype.firstGuess = function( guess ) {
    for ( var place = 0; place < this.monitor[0].length; place++ ) {
        guess.setDigit(place,place);
    }
}

//  backs up to state before last nextGuess()
//
MonitorsGenerator.prototype.retractScore = function( ) {
    this.removeRequirements();
    this.nextGuess(new Number());
}

//  removes the last requirement for the monitor of each digit, place
//
MonitorsGenerator.prototype.removeRequirements = function( ) {
    for ( var digit = 0; digit < this.monitor.length; digit++ ) {
        for ( var place = 0; place < this.monitor[0].length; place++ ) {
            this.first = this.monitor[digit][place].removeRequirement();
        }
    }
}

//  makes a string made of the strings of all the monitors
//
MonitorsGenerator.prototype.toString = function( ) {
    return "["+this.monitors+"]";
}
