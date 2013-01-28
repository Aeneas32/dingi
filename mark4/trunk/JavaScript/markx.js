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

//  checks that picking some digit with a number of places left in the
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

//  makes a String of the object
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

Score.prototype.setPlaced = function( placed ) {
    this.placed = placed;
}

Score.prototype.getPlaced = function( ) {
    return this.placed;
}

Score.prototype.setMisplaced = function( misplaced ) {
    this.misplaced = misplaced;
}

Score.prototype.getMisplaced = function( ) {
    return this.misplaced;
}

Score.prototype.correct = function( places ) {
    return this.placed == places;
}

Score.prototype.valid = function( places ) {
    if ( this.placed+this.misplaced > places ) {
        return false;
    }
    if ( this.placed == places-1 && this.misplaced != 0 ) {
        return false;
    }
    return true;
}

Score.prototype.equal = function( s ) {
    return s.placed == this.placed && s.misplaced == this.misplaced;
}

Score.prototype.count = function( inplace ) {
    if ( inplace ) {
        this.placed++;
    } else {
        this.misplaced++;
    }
}

Score.prototype.toString = function( ) {
    return this.placed+" "+this.misplaced;
}

function Number( ) {
    this.digit = new Array();
}

Number.prototype.setDigit = function( digit, place ) {
    this.digit[place] = digit;
}

Number.prototype.getDigit = function( place ) {
    return this.digit[place];
}

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

Number.prototype.toString = function( ) {
    var s = "";
    for ( var i = 0; i < this.digit.length; i++ ) {
        s += this.digit[i];
    }
    return s;
}

function Monitor( ) {
    this.requirement = new Array();
}

Monitor.prototype.addRequirement = function( requirement ) {
    this.requirement[this.requirement.length] = requirement;
}

Monitor.prototype.removeRequirement = function( ) {
    if ( this.requirement.length > 0 ) {
        this.requirement.length--;
    }
    return this.requirement.length == 0;
}

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

Monitor.prototype.unpick = function( digit ) {
    for ( var guess = 0; guess < this.requirement.length; guess++ ) {
        this.requirement[guess].unpick(digit);
    }
}

Monitor.prototype.toString = function( ) {
    return "["+this.requirement+"]";
}

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

MonitorsGenerator.prototype.tellScore = function( score ) {
    var digits = this.monitor.length;
    var places = this.monitor[0].length;
    var inGuess = new Array(digits);
    var notInGuess = new Array(digits);
    var inReq = new Requirement(inGuess,score.getPlaced());
    var misReq = new Requirement(inGuess,score.getMisplaced());
    var otherReq = new Requirement(
        notInGuess,places-score.getPlaced()-score.getMisplaced());
    for ( var digit = 0; digit < digits; digit++ ) {
        inGuess[digit] = false;
    }
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

MonitorsGenerator.prototype.firstGuess = function( guess ) {
    for ( var place = 0; place < this.monitor[0].length; place++ ) {
        guess.setDigit(place,place);
    }
}

MonitorsGenerator.prototype.retractScore = function( ) {
    this.removeRequirements();
    this.nextGuess(new Number());
}

MonitorsGenerator.prototype.removeRequirements = function( ) {
    for ( var digit = 0; digit < this.monitor.length; digit++ ) {
        for ( var place = 0; place < this.monitor[0].length; place++ ) {
            this.first = this.monitor[digit][place].removeRequirement();
        }
    }
}

MonitorsGenerator.prototype.toString = function( ) {
    return "["+this.monitors+"]";
}
