// Keith Wilson
// Date: 2/17/22
// COP 3252 - Assignment #3

// This class stores information pertaining to the command line arguments provided
// when the application is run.
public class CLArgs {
    private boolean computerFlag = false;
    private String computerValue = "";
    private boolean advancedFlag = false;

    public CLArgs(String[] args) {
        boolean invalidArgs = false;
        for (int x = 0; x < args.length; x++) {
            switch (args[x]) {
                case "-c":
                    computerFlag = true;
                    // check the following arg to see if it is the optional value for the
                    // computer flag
                    if ((x + 1 != args.length) && (!args[x + 1].equals("-a"))) {
                        computerValue = args[x + 1];
                        x++;
                    }
                    if (!computerValue.equals("1") && !computerValue.equals("2") && !computerValue.equals("")) {
                        invalidArgs = true;
                    }
                    break;
                case "-a":
                    if (!computerFlag) {
                        invalidArgs = true;
                    }
                    advancedFlag = true;
                    break;
                default:
                    invalidArgs = true;
            }
        }
        if (invalidArgs) {
            System.out.println("Usage: java TicTacToe [-c [1|2] [-a]]");
            System.exit(1);
        }
    }

    public boolean getComputerFlag() {
        return computerFlag;
    }

    public boolean getAdvancedFlag() {
        return advancedFlag;
    }

    public String getComputerValue() {
        return computerValue;
    }
}

