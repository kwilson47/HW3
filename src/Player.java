// Keith Wilson
// Date: 2/17/22
// COP 3252 - Assignment #3

// This is a generic player class to be extended by the specific type of player.
abstract class Player {
    private final char symbol;
    private final String name;

    public Player(char symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

    public char getOpponentSymbol() {
        if (symbol == 'X') {
            return 'O';
        }
        else {
            return 'X';
        }
    }

    abstract int getMove(Board game);
}
