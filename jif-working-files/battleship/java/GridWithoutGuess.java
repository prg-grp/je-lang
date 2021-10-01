package battleship;

@Enclave
public class GridWithoutGuess {

    @Secret
    static boolean[][] gridA;

    @Gateway
    public static boolean applyGuessA(int x, int y) {
        int xE = endorse(x);
        int yE = endorse(y);
        boolean resultE = applyA(xE, yE);
        return declassify(resultE);
    }

    private static boolean applyA(int x, int y) {
        boolean b;
        {
            boolean c = false;
            try {
                c = gridA[x][y];
            } catch (NullPointerException e) {
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            b = c;
        }
        return b;
    }
}
