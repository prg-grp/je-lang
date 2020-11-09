package de.tuda.prg.battleship;

public class GuessAndResult {
    int proposedGuess;
    int locationFromYou;
    int resultForLocation;  // 0 - nothing (unusual condition), -1 = battleship absent, 1 = battleship present

    public GuessAndResult(int proposedGuess, int locationfromYou, int resultForLocation) {
        this.proposedGuess = proposedGuess;
        this.locationFromYou = locationfromYou;
        this.resultForLocation = resultForLocation;
    }
}