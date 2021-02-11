package de.tuda.prg.battleship;

import java.util.Random;

public class Player2 {
    private static int gridSize = 10;
    private static Boolean[] gridStatusPlayer1 = new Boolean[gridSize];
    private static Random random = new Random();

    public static GuessAndResult accept(GuessAndResult guessAndResultFromP1) {
        updateOpponentsGridForMyPreviousGuess(guessAndResultFromP1); // Update player1's grid status at Player2's location for the previous guess from Player2

        // System.out.print("Player1's grid status at player2 = ");
        printOppGrid();

        return new GuessAndResult(generateGuess(), guessAndResultFromP1.proposedGuess, GridPlayer2.applyOppsGuess(guessAndResultFromP1.proposedGuess));
    }

    private static void updateOpponentsGridForMyPreviousGuess(GuessAndResult oppsGuessAndResult) {
        if (oppsGuessAndResult.resultForLocation == -1) {
            gridStatusPlayer1[oppsGuessAndResult.locationFromYou] = false;
        } else if (oppsGuessAndResult.resultForLocation == 1) {
            gridStatusPlayer1[oppsGuessAndResult.locationFromYou] = true;
        }

    }

    private static int generateGuess() {
        int location;
        do {
            location = random.nextInt(gridSize);
        } while (gridStatusPlayer1[location] != null);
        // System.out.println("Player 2 : sending the proposed guess: "+location);
        return location;
    }

    private static void printOppGrid() {
        for (int i = 0; i < gridStatusPlayer1.length; i++) {
            // System.out.print(gridStatusPlayer1[i]+"  ");
        }
        System.out.println("\n");
    }
}
