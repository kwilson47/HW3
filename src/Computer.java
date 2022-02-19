// Keith Wilson
// Date: 2/17/22
// COP 3252 - Assignment #3

// Class to represent a computer player in a game of tic-tac-toe
// Consists of a flag denoting whether it is in advanced mode, and a pair of maps
// used by the minimax methods to memoize previously calculated results.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Computer extends Player{

    private final boolean isAdvanced;

    private Map<String, UtilityMoves> maxValues = new HashMap<>();
    private Map<String, Integer> minValues = new HashMap<>();

    public Computer(char symbol, String name, boolean isAdvanced) {
        super(symbol, name);
        this.isAdvanced = isAdvanced;
    }

    // returns the utility of the given board configuration, from the perspective of the
    // player whose turn it is.
    // Winning state: 1
    // Losing state: -1
    // Tie: 0
    // Non-terminal state: null
    private Integer utility(Board configuration) {
        if (configuration.isWinningState(getSymbol())) {
            return 1;
        } else if (configuration.isWinningState(getOpponentSymbol())) {
                return -1;
        } else {
            if (configuration.getNumFilled() == 9) {
                return 0;
            }
            return null;
        }
    }

    // This method returns a string that is the given string transposed according to the
    // indexOrder.
    // For example, the string "hello" with indexOrder = {2, 1, 3, 0, 4} would return "lelho"
    private String transposeString(String original, int[] indexOrder) {
        StringBuilder sb = new StringBuilder();
        for (int i : indexOrder) {
            sb.append(original.charAt(i));
        }
        return sb.toString();
    }

    // This method returns an array of moves, transposed to account for the different orientation
    // of the game board. For example, if originalMoves is {1, 3} that represents moves in the upper
    // left and upper right of the game board. If the indexOrder of the transposition is
    // {6, 7, 8, 3, 4, 5, 0, 1, 2}, this represents a reflection about the middle row. So the corresponding
    // moves that would be returned would be {7, 9}.
    private ArrayList<Integer> getTransposedMoves(ArrayList<Integer> originalMoves, int[] indexOrder) {
        ArrayList<Integer> transposedMoves = new ArrayList<>();
        // transpose each move. Note, we subtract 1 from the index and add 1 to the result to
        // compensate for the fact the moves are stored [1, 9] but the transposition array contains
        // elements [0 - 8]
        for (Integer move : originalMoves) {
            transposedMoves.add(indexOrder[move - 1] + 1);
        }
        return transposedMoves;
    }

    // This method checks to see if we have previously calculated a MIN value for the given
    // string that represents a board, or any other string representing a board that is
    // isomorphic to the given board.
    private Integer getPreviouslyDeterminedMinValue(String configuration) {
        if (minValues.containsKey(configuration)) {
            return (minValues.get(configuration));
        } else {
            int[][] isomorphicConfigurations = {
                    {8, 7, 6, 5, 4, 3, 2, 1, 0}, // perimeter rotated 4 spaces
                    {2, 5, 8, 1, 4, 7, 0, 3, 6}, // perimeter rotated 2 spaces clockwise
                    {6, 3, 0, 7, 4, 1, 8, 5, 2}, // perimeter rotated 2 spaces counter-clockwise
                    {6, 7, 8, 3, 4, 5, 0, 1, 2}, // reflected about the middle row
                    {2, 1, 0, 5, 4, 3, 8, 7, 6}, // reflected about the middle column
                    {8, 5, 2, 7, 4, 1, 6, 3, 0}, // reflected about upper right-lower left diagonal
                    {0, 3, 6, 1, 4, 7, 2, 5, 8}}; // reflected about the upper left-lower right diagonal

            // Check the memoized map for each isomorphic configuration until an entry is found
            for (int[] isomorphicConfiguration : isomorphicConfigurations) {
                String iso = transposeString(configuration, isomorphicConfiguration);
                if (minValues.containsKey(iso)) {
                    int c = minValues.get(iso);
                    minValues.put(configuration, c);
                    return (c);
                }
            }
        }
        return null;
    }

    // This method checks to see if we have previously calculated a MAX value for the given
    // string that represents a board, or any other string representing a board that is
    // isomorphic to the given board.
    private UtilityMoves getPreviouslyDeterminedMaxValue(String configuration) {

        if (maxValues.containsKey(configuration)) {
            return (maxValues.get(configuration));
        } else {
            int[][] isomorphicConfigurations = {
                    {8, 7, 6, 5, 4, 3, 2, 1, 0}, // perimeter rotated 4 spaces
                    {2, 5, 8, 1, 4, 7, 0, 3, 6}, // perimeter rotated 2 spaces clockwise
                    {6, 3, 0, 7, 4, 1, 8, 5, 2}, // perimeter rotated 2 spaces counter-clockwise
                    {6, 7, 8, 3, 4, 5, 0, 1, 2}, // reflected about the middle row
                    {2, 1, 0, 5, 4, 3, 8, 7, 6}, // reflected about the middle column
                    {8, 5, 2, 7, 4, 1, 6, 3, 0}, // reflected about upper right-lower left diagonal
                    {0, 3, 6, 1, 4, 7, 2, 5, 8}}; // reflected about upper left-lower right diagonal
            for (int[] isomorphicConfiguration : isomorphicConfigurations) {
                UtilityMoves m = getTransposedMinimaxObject(configuration, isomorphicConfiguration);
                if (m != null) {
                    return m;
                }
            }
        }

        return null;
    }

    // Returns the MIN value in the minimax algorithm. MAX doesn't care what moves MIN made to obtain
    // its value, so we only need to return the utility.
    private int minValue(Board configuration, int alpha, int beta) {
        // check to see if we have already calculated the MIN value for this
        // board configuration
        String bAsStr = configuration.getBoardAsString();
        Integer existingValue = getPreviouslyDeterminedMinValue(bAsStr);
        if (existingValue != null) {
            return existingValue;
        }

        // Get the utility of the current board configuration and return it
        // if we are in a terminal state.
        // 1 = Winning State, -1 = Losing State, 0 = tie, null = Not a terminal state
        Integer utility = utility(configuration);
        if (utility != null) {
            return utility;
        }

        int minimumUtility = 5; // initialize to any number > the max utility in the game (1).
        int[] validMoves = configuration.getEmptySpaces();
        for (int x : validMoves) {
            Board newState = new Board(configuration);
            newState.setCell(x, getOpponentSymbol());
            UtilityMoves obj = maxValue(newState, alpha, beta);
            if (obj.getUtility() < minimumUtility) {
                beta = Math.min(beta, minimumUtility);
                minimumUtility = obj.getUtility();
            }
            if (minimumUtility < alpha) {
                minValues.put(bAsStr, minimumUtility);
                return minimumUtility;
            }
        }
        minValues.put(bAsStr, minimumUtility);
        return minimumUtility;
    }

    // Given a string representing a board state, and an array of integers representing
    // the transposition order, return the previously calculated MAX value of the transposed
    // string if it exists.
    private UtilityMoves getTransposedMinimaxObject(String originalString, int[] indexOrder) {

        String iso = transposeString(originalString, indexOrder);
        if (maxValues.containsKey(iso)) {
            ArrayList<Integer> transposedMoves = getTransposedMoves(maxValues.get(iso).getMoves(), indexOrder);
            UtilityMoves m = new UtilityMoves(maxValues.get(iso).getUtility(), transposedMoves);
            maxValues.put(originalString, m);
            return m;
        }
        return null;

    }

    // This method returns the max value in a minimax search. Note, that we want to return an array of
    // all moves with the same optimal value. It would be more efficient to just return a single optimal
    // move, but while the computer would still play optimally, it would play predictably. We will
    // sacrifice efficiency for the ability to find all optimal moves and let the computer
    // randomly pick one.
    // MIN needs to know the utility of MAX, and ultimately we need to know the moves used to obtain
    // that utility, so we will store the results in an object that contains both a utility and an
    // array of moves (implemented as an ArrayList, so we can more easily add to it as we go).
    private UtilityMoves maxValue(Board configuration, int alpha, int beta) {
        // First, get the string representation of the board and look up in our
        // memoized map to see if we have already calculated the max value for this board.

        String boardAsString = configuration.getBoardAsString();
        UtilityMoves m = getPreviouslyDeterminedMaxValue(boardAsString);
        if (m != null) {
            return m;
        }

        // Get the utility of the current board configuration and return it
        // if we are in a terminal state.
        // 1 = Winning State, -1 = Losing State, 0 = tie, null = Not a terminal state
        Integer utility = utility(configuration);
        if (utility != null) {
            return new UtilityMoves(utility, null);
        }
        int largestUtility = -5; // initialize to any value less than the lowest utility, which is -1
        ArrayList<Integer> moves = new ArrayList<>();
        int[] validMoves = configuration.getEmptySpaces();
        // loop through each possible move
        for (int x : validMoves) {
            Board newState = new Board(configuration);
            newState.setCell(x, getSymbol());
            // send the new board state to the min method
            int thisUtility = minValue(newState, alpha, beta);
            // if this move matches the most optimal so far, add it to the array
            if (thisUtility == largestUtility) {
                moves.add(x);
            }
            // if it is more optimal, then replace the existing array with a new one
            // update alpha if necessary
            else if (thisUtility > largestUtility) {
                alpha = Math.max(alpha, largestUtility);
                largestUtility = thisUtility;
                moves = new ArrayList<>();
                moves.add(x);
            }
            if (largestUtility > beta) {
                // memoize the result, and return
                UtilityMoves result = new UtilityMoves(largestUtility, moves);
                maxValues.put(boardAsString, result);
                return result;
            }

        }
        // memoize the result, and return
        UtilityMoves result = new UtilityMoves(largestUtility, moves);
        maxValues.put(boardAsString, result);
        return result;
    }

    // Implements the minimax algorithm, with alpha-beta pruning, to determine the optimal
    // move the computer should make.
    // Returns a random move among all moves found to be optimal.
    private int minimaxSearch(Board configuration) {
        UtilityMoves obj = maxValue(configuration, -1, 1);
        // we end up with an array of moves, each with the same optimal value.
        // return a random one of those moves
        return obj.getRandomMove();
    }

    // method that determines the computer's move and returns the integer corresponding
    // to its position on the game board.
    public int getMove(Board configuration) {
        // if the computer is in advanced mode, get the move by using the minimax algorithm
        if (isAdvanced) {
            return minimaxSearch(configuration);
        }

        // array that holds each valid move
        int[] validMoves = configuration.getEmptySpaces();

        // if a valid move is a winning move, then take that move
        for (int x : validMoves) {
            if (configuration.isWinningState(getSymbol(), x)) {
                return x;
            }
        }

        // if a valid move would be a winning move for the opponent, then take that move.
        for (int x : validMoves) {
            if (configuration.isWinningState(getOpponentSymbol(), x)) {
                return x;
            }
        }

        // if the center square is a valid move, take that move.
        for (int x : validMoves) {
            if (x == 5) {
                return 5;
            }
        }

        // generate a random number in the range [0, number of valid moves - 1]
        // use that random number as the index and return the move in that position
        Random rand = new Random();
        int randomIndex = rand.nextInt(validMoves.length);
        return validMoves[randomIndex];

    }

}
