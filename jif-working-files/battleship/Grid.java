package battleship;

@Enclave
public class Grid {

    @Secret
    static boolean[][] gridA;

    @Gateway
    public static int applyGuessA(Guess guess) {
        uess guessE = endorse(guess);
        int result = applyA(guessE);
        return declassify(result);;
    }

    private static boolean applyA(Guess guess) {
            int x = guess.getX();
            int y = guess.getY();
            boolean b = gridA[x][y];
            return b;
    }

    public static void init(int gridSize, int numberShips) {
        int count = 0;
        gridA = new boolean[gridSize][gridSize];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (i == 0 && count < numberShips) {
                    gridA[i][j] = true;
                    count = count + 1;
                }
            }
        }
    }
}