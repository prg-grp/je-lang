package battleship;

@Enclave
public class Grid {

    @Secret
    static boolean[][] gridA;

    @Gateway
    public static int applyGuessA(Guess guess) {
        Guess guessE = endorse(guess);
        int result = applyA(guessE);
        return declassify(result);
    }

    private static boolean applyA(Guess guess) {
        int x = guess.x;
        int y = guess.y;
        boolean b = gridA[x][y];
        return b;
    }
}
