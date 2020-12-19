package de.tuda.prg.battleship;

public class GridPlayer2 {

    final static boolean[] myGrid = new boolean[]{false, false, true, false, true, false, true, false, true, true};

    public static int applyOppsGuess(final int location) {
        int result = 0;
        if (location >= 0) {
            if (!myGrid[location]) {
                result = -1;    // Battleship absent
            }
            else {
                result = 1;    // Battleship present / Battleship hit by the opponent
                myGrid[location] = false; // Battleship hit by the opponent
            }
            if (allFalse()) {
                result = 2;  // All battleships hit
            }
        }
        return  result;
    }

    private static boolean allFalse() {
        boolean allFalse = true;
        for (int i = 0; i < myGrid.length; i++) {
            if (myGrid[i]) {
                allFalse = false;
                break;
            }
        }
        return allFalse;
    }
}