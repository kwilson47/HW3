// Keith Wilson
// Date: 2/17/22
// COP 3252 - Assignment #3

// Class used to represent a tic-tac-toe board
// Consists of a 2d array of characters that store the symbols on the board
// and an integer that stores how many cells on the board are currently taken.
class Board {
    private char[][] cells;
    private int numFilled;

    // default constructor. initializes the board to all spaces
    public Board() {
        cells = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j] = ' ';
            }
        }
        numFilled = 0;
    }

    // copy constructor
    public Board(Board b) {
        cells = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(b.cells[i], 0, this.cells[i], 0, 3);
        }
        numFilled = b.numFilled;
    }

    // prints the board as well as the integer representations of each position.
    public void print() {
        System.out.println("\nGame Board:      Positions:");
        System.out.println(" " + cells[0][0] + " | " + cells[0][1] + " | " + cells[0][2]
                + "        1 | 2 | 3 ");
        System.out.println("-----------      -----------");
        System.out.println(" " + cells[1][0] + " | " + cells[1][1] + " | " + cells[1][2]
                + "        4 | 5 | 6 ");
        System.out.println("-----------      -----------");
        System.out.println(" " + cells[2][0] + " | " + cells[2][1] + " | " + cells[2][2]
                + "        7 | 8 | 9 \n");
    }

    // set the given position on the board to the given symbol
    public void setCell(int x, char symbol) {
        int r = (x - 1) / 3;
        int c = (x - 1) % 3;
        cells[r][c] = symbol;
        numFilled++;
    }

    // return whether the given position on the board is empty
    public boolean isSpotAvailable(int x) {
        int r = (x - 1) / 3;
        int c = (x - 1) % 3;
        return cells[r][c] == ' ';
    }

    // returns an array containing the positions of each empty space.
    public int[] getEmptySpaces() {
        int count = 0;
        // loop through the board, counting the total number of empty spaces, so
        // we know the size of the array that we need.
        for (int x = 1; x <= 9; x++) {
            if (isSpotAvailable(x)) {
                count++;
            }
        }
        int[] emptySpaces = new int[count];
        count = 0;
        // loop through again, this time adding the position to the array if it is empty
        for (int x = 1; x <= 9; x++) {
            if (isSpotAvailable(x)) {
                emptySpaces[count++] = x;
            }
        }
        return emptySpaces;
    }

    // determine if the board state is a winning state for the given symbol
    // A winning state is if any of the three rows, three columns, or two diagonals contain the
    // same symbol.
    public boolean isWinningState(char symbol) {
        return (cells[0][0] == symbol && cells[0][1] == symbol && cells[0][2] == symbol) ||
                (cells[1][0] == symbol && cells[1][1] == symbol && cells[1][2] == symbol) ||
                (cells[2][0] == symbol && cells[2][1] == symbol && cells[2][2] == symbol) ||
                (cells[0][0] == symbol && cells[1][0] == symbol && cells[2][0] == symbol) ||
                (cells[0][1] == symbol && cells[1][1] == symbol && cells[2][1] == symbol) ||
                (cells[0][2] == symbol && cells[1][2] == symbol && cells[2][2] == symbol) ||
                (cells[0][0] == symbol && cells[1][1] == symbol && cells[2][2] == symbol) ||
                (cells[0][2] == symbol && cells[1][1] == symbol && cells[2][0] == symbol);
    }

    // overloaded method
    // this time, we first apply the given move to a copy of the current board, and then determine
    // if the resulting board is a winning state for the given symbol.
    public boolean isWinningState(char symbol, int move) {
        Board newState = new Board(this);
        newState.setCell(move, symbol);
        return newState.isWinningState(symbol);
     }

    public int getNumFilled() {
        return numFilled;
    }

    // return the board representation as a string
    public String getBoardAsString() {
        String s1 = new String(cells[0]);
        String s2 = new String(cells[1]);
        String s3 = new String(cells[2]);
        return s1 + s2 + s3;
    }
}
