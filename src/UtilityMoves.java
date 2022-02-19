// Keith Wilson
// Date: 2/17/22
// COP 3252 - Assignment #3

// Class to represent the utility of a list of moves
// The minimax algorithm uses this class to store the utility of a set of moves
import java.util.ArrayList;
import java.util.Random;

public class UtilityMoves {
    private final int utility;
    private final ArrayList<Integer> moves;

    public UtilityMoves(int utility, ArrayList<Integer> moves) {
        this.utility = utility;
        this.moves = moves;
    }

    public int getUtility() {
        return utility;
    }

    public ArrayList<Integer> getMoves() {
        return moves;
    }

    public int getRandomMove() {
        Random rand = new Random();
        int randomNum = rand.nextInt(moves.size());
        return moves.get(randomNum);
    }
}
