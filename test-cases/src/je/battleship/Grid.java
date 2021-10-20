package battleship;

@Enclave
public class Grid {

    @Secret
    static boolean[][] gridA = {{true, false}, {true, true}};

    @Gateway
    public static boolean applyGuessA(Guess guess) {
        Guess guessE = endorse(guess);
        boolean resultE = applyA(guessE);
        return declassify(resultE);
    }

    private static boolean applyA(Guess guess) {
        int x = guess.x;
        int y = guess.y;
        boolean b = gridA[x][y];
        return b;
    }
}
