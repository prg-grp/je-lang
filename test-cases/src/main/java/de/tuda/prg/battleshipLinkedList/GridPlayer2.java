package de.tuda.prg.battleshipLinkedList;

import java.util.LinkedList;
import java.util.List;

public class GridPlayer2 {

    private static int gridSize = 5;
    private final static List myGrid = new LinkedList();



    public static int applyOppsGuess(final int location) {
        int result = 0;
        if (location >= 0) {
            if (!(Boolean) myGrid.get(location)) {
                result = -1;    // Battleship absent
            }
            else {
                result = 1;    // Battleship present / Battleship hit by the opponent
                myGrid.set(location, false); // Battleship hit by the opponent
            }
            if (allFalse()) {
                result = 2;  // All battleships hit
            }
        }
        return  result;
    }

    private static boolean allFalse() {
        boolean allFalse = true;
        for (int i = 0; i < myGrid.size(); i++) {
            if ((Boolean) myGrid.get(i)) {
                allFalse = false;
                break;
            }
        }
        return allFalse;
    }

    public static void initializeTheList() {
        myGrid.add(false);
        myGrid.add(false);
        myGrid.add(false);
        myGrid.add(true);
        myGrid.add(true);
    }
}
