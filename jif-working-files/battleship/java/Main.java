import java.util.Random;

public class Main {

    static boolean[][] gridA = { {false, false , false, false, false}, {false, false , false, false, false}, {false, false , false, false, false}, {false, false , false, false, false}, {false, false , false, false, false}};
    static int gridSize;
    static int numberShips;

    public static void main(String[] args) {
        System.out.println("Started Game");
        boolean gameOver = false;
        if (args.length > 0) {
            gridSize = Integer.valueOf(args[0]);
            numberShips = gridSize/2;
        } else {
            gridSize = 5;
            numberShips = 5;
        }

        int resultA = 0;

        while(!gameOver) {
            Guess g1 = getGuessA();
            updateGridA(g1);
            boolean b = Grid.applyGuessA(g1);
            if (b) resultA += 1;
            if (resultA == numberShips) {
                gameOver = true;
            }
            System.out.println("ResultA : "+resultA);
        }

        System.out.println("GAME is Over! A destroyed: "+resultA);
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
