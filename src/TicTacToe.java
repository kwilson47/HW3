// Keith Wilson
// Date: 2/17/22
// COP 3252 - Assignment #3

// Class TicTacToe represents a game of tic-tac-toe.
// It consists of a board, two players, and a reference variable that will point to the active player.
public class TicTacToe {

    private Board boardState;
    private Player player1;
    private Player player2;
    private Player activePlayer;

    // Constructor for a game of tic-tac-toe
    // It uses the command line arguments to determine which type of players to create
    // and in the case of the computer, if it is playing in advanced mode.
    public TicTacToe(String[] args) {
        boardState = new Board();
        CLArgs gameArgs = new CLArgs(args);

        if (!gameArgs.getComputerFlag()) {
            player1 = new Human('X',  "Player 1");
            player2 = new Human('O',  "Player 2");
        } else if (gameArgs.getComputerValue().equals("1")) {
            player1 = new Computer('X',  "Player 1", gameArgs.getAdvancedFlag());
            player2 = new Human('O',  "Player 2");
        } else if (gameArgs.getComputerValue().equals("2")) {
            player1 = new Human('X',  "Player 1");
            player2 = new Computer('O',  "Player 2", gameArgs.getAdvancedFlag());
        } else {
            player1 = new Computer('X',  "Player 1", gameArgs.getAdvancedFlag());
            player2 = new Computer('O',  "Player 2", gameArgs.getAdvancedFlag());
        }
        activePlayer = player1;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Board getBoardState() {
        return boardState;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    // a single turn consists of determining a move, updating the board with that move,
    // and printing the move to the screen
    public void takeTurn() {
        int input = activePlayer.getMove(boardState);
        char symbol = activePlayer.getSymbol();

        boardState.setCell(input, symbol);
        System.out.println(activePlayer.getName() + " chooses " + input);
        boardState.print();
    }

    // swaps activePlayer, indicating it is the other person's turn.
    public void swapActivePlayer() {
        if (activePlayer == player1) {
            activePlayer = player2;
        }
        else {
            activePlayer = player1;
        }
    }

    public static void main(String[] args) {

        TicTacToe game = new TicTacToe(args);
        boolean winner = false;
        game.getBoardState().print();
        // The game loop will run 9 times, or until there is a winner, whichever comes first.
        for (int x = 1; x <= 9; x++) {
            game.takeTurn();
            // determine if the current player has won and end the game if so.
            if (game.boardState.isWinningState(game.activePlayer.getSymbol())) {
                winner = true;
                System.out.println(game.activePlayer.getName() + " is the winner!\n");
                break;
            }
            game.swapActivePlayer();
        }

        // if nine moves have been taken and there is no winner, it must be a tie.
        if (!winner) {
            System.out.println("Game ends in a draw.\n");
        }

    }
}
