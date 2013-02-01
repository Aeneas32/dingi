//  Controller of the four digit number game
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

var DIGITS = 10;
var PLACES =  4;

// the area where the sequence of guesses and their scores are displayed
function Scoreboard( id ) {
    this.scoreboard = document.getElementById(id);
    this.clear();
}

// clears a scoreboard
Scoreboard.prototype.clear = function( ) {
    while ( this.scoreboard.firstChild ) {
        this.scoreboard.removeChild(this.scoreboard.firstChild);
    }
    for ( var i = 0; i < 8; i++ ) {
        this.scoreboard.appendChild(document.createElement("br"));
    }
    this.line = 1;
}

// scrolls the last line to the bottom of the scoreboard
Scoreboard.prototype.scroll = function( ) {
    this.scoreboard.scrollTop =
        this.scoreboard.scrollHeight-this.scoreboard.clientHeight;
}

// puts text (normally a guess) on the next line in the scoreboard
Scoreboard.prototype.putText = function( text ) {
    this.scoreboard.appendChild(document.createTextNode(text));
    this.scroll();
}

// puts a guess in the scoreboard
Scoreboard.prototype.putGuess = function( guess ) {
    this.putText(this.line+": "+guess+" ");
}

// puts a score in the scoreboard
Scoreboard.prototype.putScore = function( score ) {
    this.scoreboard.appendChild(document.createTextNode(score));
    this.scoreboard.appendChild(document.createElement("br"));
    this.line++;
    this.scroll();
}

// retracts a guess (or message) and the previous score from the scoreboard
Scoreboard.prototype.retract = function( ) {
    this.line--;
    this.scoreboard.removeChild(this.scoreboard.lastChild);
    this.scoreboard.removeChild(this.scoreboard.lastChild);
    this.scoreboard.removeChild(this.scoreboard.lastChild);
    this.scroll();
}

// a permuter of n digits, 0 to n-1
function RandomDeal( n ) {
    this.digit = new Array(n);
    this.n = n;
    for ( var i = 0; i < n; i++ ) {
        this.digit[i] = i;
    }
}

// returns the next digit in a random sequence
RandomDeal.prototype.next = function( ) {
    var pick = Math.floor(this.n*Math.random());
    var digit = this.digit[pick];
    this.n--;
    for ( var i = pick; i < this.n; i++ ) {
        this.digit[i] = this.digit[i+1];
    }
    return digit;
}

// returns an array of a permuted sequence
RandomDeal.prototype.getMap = function( ) {
    var shuffle = new Array(this.n);
    for ( var i = 0; i < shuffle.length; i++ ) {
        shuffle[i] = this.next();
    }
    return shuffle;
}

// an automatic number guesser
function Guesser( ) {
    this.generator = new MonitorsGenerator(DIGITS,PLACES);
    var deal = new RandomDeal(DIGITS);
    this.digitMap = deal.getMap();
    var deal = new RandomDeal(PLACES);
    this.placeMap = deal.getMap();
    this.inconsistentScores = false;
    machine.clear();
}

// scramble a guess
Guesser.prototype.mapGuess = function( guess ) {
    var number = new Number();
    
    for ( var place = 0; place < PLACES; place++ ) {
        var digit = this.digitMap[guess.getDigit(place)];
        number.setDigit(digit,this.placeMap[place]);
    }
    return number;
}

// returns the next generated guess and puts in the scoreboard
// 0 length array if a guess cannot be generated
Guesser.prototype.guess = function( ) {
    var guess = new Number();
    this.generator.nextGuess(guess);
    if ( guess.digit.length == 0 ) {
        machine.putText("inconsistent scores");
        this.inconsistentScores = true;
        document.getElementById("score").disabled = true;
    } else {
        var number = this.mapGuess(guess);
        machine.putGuess(number);
        return number;
    }
    return guess;
}

// retract the previous score
Guesser.prototype.retract = function( ) {
    this.generator.retractScore();
    this.inconsistentScores = false;
}

var guesser; // the automatic number guesser

// generates guess and scores them until the target is matched
function autoscore( target ) {
    do {
        var score = target.score(guesser.guess());
        machine.putScore(score);
        guesser.generator.tellScore(score);
    } while ( !score.correct(PLACES) );
    document.getElementById("retract").disabled = true;
    document.getElementById("score").disabled = true;
}

// start guessing a new target that the user will score
function retarget( ) {
    guesser = new Guesser();
    guesser.guess();
    document.getElementById("retract").disabled = true;
    validateScore();
}

// generates guesses and scores them for a user supplied target
function target( ) {
    guesser = new Guesser();
    autoscore(getNumber("target"));
}

// returns that a number supplied the user is valid; has no repeated digits
function validateNumber( which ) {
    var number = getNumber(which);
    document.getElementById(which).disabled = !number.valid();
}

// randomly sets the digits of this number
Number.prototype.random = function( ) {
    var deal = new RandomDeal(DIGITS);
    for ( var place = 0; place < PLACES; place++ ) {
        this.setDigit(deal.next(),place);
    }
}

// randomly sets a target and the automatically guesses it
function random( ) {
    var number = new Number();
    number.random();
    number.putNumber("target");
    document.getElementById("target").disabled = false;
    target();
}

// returns the score set by the user
function getScore( ) {
    var score = new Score();
    score.setPlaced(document.getElementById("placed").value[0]-'0');
    score.setMisplaced(document.getElementById("misplaced").value[0]-'0');
    return score;
}

// enables or disables the Score button depending on the validity of the score
// set by the user
function validateScore( ) {
    var score = getScore();
    document.getElementById("score").disabled =
        guesser.inconsistentScores || !score.valid(PLACES);
}

// tells the guess generator the score of its last guess and generates the next
function score( ) {
    var score = getScore();
    machine.putScore(score);
    document.getElementById("retract").disabled = false;
    guesser.generator.tellScore(score);
    if ( !score.correct(PLACES) ) {
        var guess = guesser.guess();
    } else {
        document.getElementById("retract").disabled = true;
        document.getElementById("score").disabled = true;
    }
}

// retracts the last guess and score
function retract( ) {
    machine.retract();
    guesser.retract();
    document.getElementById("retract").disabled = machine.line <= 1;
    validateScore();
}

var secret = new Number(); // the target that the user is guessing

// clears the user's guesses and randomly sets a new target for the user
function newSecret( ) {
    human.clear();
    secret.random();
}

// returns a number set by the user
function getNumber( select ) {
    var number = new Number();
    for ( var place = 0; place < PLACES; place++ ) {
        var s = document.getElementById(select+place);
        number.setDigit(s.selectedIndex,place);
    }
    return number;
}

// puts a number in the user's interface
Number.prototype.putNumber = function( select ) {
    for ( var place = 0; place < PLACES; place++ ) {
        var s = document.getElementById(select+place);
        s.selectedIndex = this.getDigit(place);
    }
}

// puts the user's guess and its score into the scoreboard
function guess( ) {
    var number = getNumber("guess");
    human.putGuess(number);
    human.putScore(secret.score(number));
}

// puts the user's target into the 'Guess' interface
function reveal( ) {
    secret.putNumber("guess");
    document.getElementById("guess").disabled = false;
}
