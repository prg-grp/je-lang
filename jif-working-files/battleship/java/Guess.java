package battleship;

public class Guess {
    private int x;
    private int y;

    public Guess(int xCord, int yCord) {
        x = xCord;
        y = yCord;
    }

    public Guess(int gridSize) {
        x = 1;
        y = 2;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
