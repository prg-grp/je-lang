package battleship;

@Enclave
public class Grid {

    @Secret
    static boolean[][] gridA;

    @Gateway
    public static boolean applyGuessA(Guess guess) {
        Guess guessE = endorse(guess);
        boolean resultE = applyA(guessE);
        return declassify(resultE);
    }

    private static boolean applyA(Guess guess) {
        int x;
        {
            int f = 0;
            try {
                f = guess.x;
            } catch (NullPointerException e) {
            }
            x = f;
        }
        int y;
        {
            int l = 0;
            try {
                l = guess.y;
            } catch (NullPointerException e) {
            }
            y = l;
        }
        boolean b;
        {
            boolean k = false;
            try {
                k = gridA[x][y];
            } catch (NullPointerException e) {
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            b = k;
        }
        return b;
    }
}
