package battleship;

import java.util.Random;

public class Main {

    static boolean[][] gridA;
    static int gridSize;
    static int numberShips;

    public static void main(String[] args) {
        boolean gameOver = false;
        if (args.length > 0) {
            gridSize = Integer.valueOf(args[0]);
            numberShips = gridSize/2;
        } else {
            gridSize = 7;
            numberShips = 5;
        }
        init();

        int resultA = 0;

        while(!gameOver) {
            Guess g1 = getGuessA();
            updateGridA(g1);
            if (Grid.applyGuessA(g1)) resultA += 1;
            if (resultA == numberShips) {
                gameOver = true;
            }
        }

        System.out.println("GAME is Over! A destroyed: "+resultA);
    }

    private static void init() {
        gridA = new boolean[gridSize][gridSize];
    }

    private static void updateGridA(Guess guess) {
        gridA[guess.x][guess.y] = true;
    }

    private static Guess getGuessA() {
        Random rnd = new Random();
        int x = rnd.nextInt(gridSize);
        int y = rnd.nextInt(gridSize);
        Guess guess = new Guess(x,y);
        while (gridA[guess.x][guess.y]) {
            x = rnd.nextInt(gridSize);
            y = rnd.nextInt(gridSize);
            guess = new Guess(x,y);
        }
        return guess;
    }


}
