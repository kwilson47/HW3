// Keith Wilson
// Date: 2/17/22
// COP 3252 - Assignment #3

// Class to represent a human player in the game of tic-tac-toe

import java.util.InputMismatchException;
import java.util.Scanner;

public class Human extends Player{

    public Human(char symbol, String name) {
        super(symbol, name);
    }

    // returns a move chosen by the human player
    public int getMove(Board configuration) {
        Scanner in = new Scanner(System.in);
        boolean invalidInput = true;
        int input = 0;
        // keep prompting the user until they enter valid input.
        // Valid input must be:
        // 1) An integer
        // 2) Between 1 and 9 inclusive
        // 3) Not be a space that is already taken.
        do {
            try {
                System.out.print("\n" + getName() + ", please enter a move(1-9): ");
                input = in.nextInt();
                invalidInput = false;
                if (input < 1 || input > 9) {
                    System.out.println(input + " not in the range [1, 9].");
                    invalidInput = true;
                } else if (!configuration.isSpotAvailable(input)) {
                    System.out.println(input + " is already taken.");
                    invalidInput = true;
                }
            } catch (InputMismatchException e) {
                System.out.print("Input was not an integer.");
                in.nextLine();
            }
        } while (invalidInput);
        return input;
    }

}
