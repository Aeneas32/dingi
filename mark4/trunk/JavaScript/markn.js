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

function Scoreboard( id ) {
    this.scoreboard = document.getElementById(id);
    this.line = 1;
}

Scoreboard.prototype.clear = function( ) {
    while ( this.scoreboard.firstChild ) {
        this.scoreboard.removeChild(this.scoreboard.firstChild);
    }
    this.line = 1;
}

Scoreboard.prototype.putGuess = function( guess ) {
    this.scoreboard.appendChild(document.createTextNode(
        this.line+": "+guess+" "));
}

Scoreboard.prototype.retract = function( ) {
    this.line--;
    this.scoreboard.removeChild(this.scoreboard.lastChild);
    this.scoreboard.removeChild(this.scoreboard.lastChild);
    this.scoreboard.removeChild(this.scoreboard.lastChild);
}

Scoreboard.prototype.putScore = function( score ) {
    this.scoreboard.appendChild(document.createTextNode(score));
    this.scoreboard.appendChild(document.createElement("br"));
    this.line++;
    if ( this.scoreboard.scrollHeight > this.scoreboard.clientHeight ) {
        this.scoreboard.scrollTop =
            this.scoreboard.scrollHeight-this.scoreboard.clientHeight;
    }
}

function RandomDeal( n ) {
    this.digit = new Array(n);
    this.n = n;
    for ( var i = 0; i < n; i++ ) {
        this.digit[i] = i;
    }
}

RandomDeal.prototype.next = function( ) {
    var pick = Math.floor(this.n*Math.random());
    var digit = this.digit[pick];
    this.n--;
    for ( var i = pick; i < this.n; i++ ) {
        this.digit[i] = this.digit[i+1];
    }
    return digit;
}

RandomDeal.prototype.getMap = function( ) {
    var shuffle = new Array(this.n);
    for ( var i = 0; i < shuffle.length; i++ ) {
        shuffle[i] = this.next();
    }
    return shuffle;
}

function Guesser( ) {
    this.generator = new MonitorsGenerator(DIGITS,PLACES);
    var deal = new RandomDeal(DIGITS);
    this.digitMap = deal.getMap();
    var deal = new RandomDeal(PLACES);
    this.placeMap = deal.getMap();
    machine.clear();
}

Guesser.prototype.mapGuess = function( guess ) {
    var number = new Number();
    
    for ( var place = 0; place < PLACES; place++ ) {
        var digit = this.digitMap[guess.getDigit(place)];
        number.setDigit(digit,this.placeMap[place]);
    }
    return number;
}

Guesser.prototype.guess = function( ) {
    var guess = new Number();
    this.generator.nextGuess(guess);
    if ( guess.digit.length == 0 ) {
        machine.scoreboard.appendChild(
            document.createTextNode("inconsistent scores")
        );
        document.getElementById("score").disabled = true;
    } else {
        var number = this.mapGuess(guess);
        machine.putGuess(number);
        return number;
    }
    return guess;
}

Guesser.prototype.retract = function( ) {
    this.generator.retractScore();
}

var guesser;

function autoscore( target ) {
    do {
        var score = target.score(guesser.guess());
        machine.putScore(score);
        guesser.generator.tellScore(score);
    } while ( !score.correct(PLACES) );
    document.getElementById("retract").disabled = true;
    document.getElementById("score").disabled = true;
}

function retarget( ) {
    guesser = new Guesser();
    guesser.guess();
    document.getElementById("retract").disabled = true;
    validateScore();
}

function target( ) {
    guesser = new Guesser();
    autoscore(getNumber("target"));
}

function validateNumber( which ) {
    var number = getNumber(which);
    document.getElementById(which).disabled = !number.valid();
}

Number.prototype.random = function( ) {
    var deal = new RandomDeal(DIGITS);
    for ( var place = 0; place < PLACES; place++ ) {
        this.setDigit(deal.next(),place);
    }
}

function random( ) {
    var number = new Number();
    number.random();
    number.putNumber("target");
    document.getElementById("target").disabled = false;
    target();
}

function getScore( ) {
    var score = new Score();
    score.setPlaced(document.getElementById("placed").value[0]-'0');
    score.setMisplaced(document.getElementById("misplaced").value[0]-'0');
    return score;
}

function validateScore( ) {
    var score = getScore();
    document.getElementById("score").disabled = !score.valid(PLACES);
}

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

function retract( ) {
    machine.retract();
    guesser.retract();
    document.getElementById("retract").disabled = machine.line <= 1;
    validateScore();
}

var secret = new Number();

function newSecret( ) {
    human.clear();
    secret.random();
}

function getNumber( select ) {
    var number = new Number();
    for ( var place = 0; place < PLACES; place++ ) {
        var s = document.getElementById(select+place);
        number.setDigit(s.selectedIndex,place);
    }
    return number;
}

Number.prototype.putNumber = function( select ) {
    for ( var place = 0; place < PLACES; place++ ) {
        var s = document.getElementById(select+place);
        s.selectedIndex = this.getDigit(place);
    }
}

function guess( ) {
    var number = getNumber("guess");
    human.putGuess(number);
    human.putScore(secret.score(number));
}

function reveal( ) {
    secret.putNumber("guess");
    document.getElementById("guess").disabled = false;
}
