package de.tuda.stg.battleshipLinkedList;

import java.util.Random;

public class Player1 {

    private static int gridSize = 5;
    private static Boolean[] gridStatusPlayer2 = new Boolean[gridSize];
    private static Random random = new Random();

    public static void main(String[] args) {
        GridPlayer1.initializeTheList();
        GridPlayer2.initializeTheList();

        boolean gameOver = false;
        GuessAndResult g2 = Player2.accept(new GuessAndResult(generateGuess(), -1, 0));  // First guess from player1

        while(!gameOver) {
            if (g2.resultForLocation == 2) {
                gameOver = true;
                System.out.println("Player 1 wins");
                break;
            }
            updateOpponentsGridForMyPreviousGuess(g2); // Update player2's grid status at Player1's location

            System.out.print("Player2's grid status at player1 = ");
            printOppGrid();

            final int resultOfP2sGuess = GridPlayer1.applyOppsGuess(g2.proposedGuess);
            if (resultOfP2sGuess == 2) {
                System.out.println("Player 2 wins");
                break;
            }
            g2 = Player2.accept(new GuessAndResult(generateGuess(), g2.proposedGuess, resultOfP2sGuess));
        }
    }

    public static int generateGuess() {
        int location;
        do {
            location = random.nextInt(gridSize);
        } while (gridStatusPlayer2[location] != null);
        System.out.println("Player 1 : sending the proposed guess: "+location);
        return location;
    }

    private static void updateOpponentsGridForMyPreviousGuess(GuessAndResult oppsGuessAndResult) {
        if (oppsGuessAndResult.resultForLocation == -1) {
            gridStatusPlayer2[oppsGuessAndResult.locationFromYou] = false;
        } else if (oppsGuessAndResult.resultForLocation == 1) {
            gridStatusPlayer2[oppsGuessAndResult.locationFromYou] = true;
        }
    }

    private static void printOppGrid() {
        for (int i = 0; i < gridStatusPlayer2.length; i++) {
            System.out.print(gridStatusPlayer2[i]+"  ");
        }
        System.out.println("\n");
    }

}