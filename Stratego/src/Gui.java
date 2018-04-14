import java.util.Scanner;

public class Gui {

    private final char EMPTY_DESIGNATION = '_';
    private final char SEPARATOR = '\t';
    private final String GAME_TITLE = "_________..:: STRATEGO ::.._________";
    private final String GAME_OPTIONS = "1) Player vs Player\n2) Player vs Computer\n3) Computer vs Computer\n\n";
    private final String GAME_MODE_REQUEST = "Please choose the game mode: ";
    private final String GAME_SIZE_REQUEST = "Please enter the gameboard size: ";
    private final String BAD_INPUT_MESSAGE = "\nBad input!";
    private final String TAKEN_PLACE_MESSAGE = "This place is taken! Please choose another. ";

    private final char PLAYER1_DESIGNATION = 'M';
    private final char PLAYER2_DESIGNATION = 'D';

    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RED = "\u001B[31m";

    private int gameBoardSize;
    private int gameBoardSizeChosenByUser = 0;
    private int chosenGameMode = 0;
    private char[][] gameBoard;


    Gui(char[][] gameBoard){
        this.gameBoard = gameBoard;
        gameBoardSize = gameBoard.length;
    }

    Gui(){
    }

    public void printGameBoard(){
        for(int rowIndex = 0; rowIndex < gameBoardSize; rowIndex++){
            for(int columnIndex = 0; columnIndex < gameBoardSize; columnIndex++){
                char currentSign = gameBoard[rowIndex][columnIndex];
                if(currentSign == PLAYER1_DESIGNATION){
                    System.out.print(currentSign);
                }

                System.out.print(SEPARATOR);
            }
            System.out.println();
        }
    }

    public void printGameBoardWithIndexes(){
        System.out.print("\t");
        for(int index = 0; index < gameBoardSize; index++){
            System.out.print(index);
            System.out.print(SEPARATOR);
        }

        System.out.println();

        for(int rowIndex = 0; rowIndex < gameBoardSize; rowIndex++){
            System.out.print(rowIndex);
            System.out.print("\t");

            for(int columnIndex = 0; columnIndex < gameBoardSize; columnIndex++){
                System.out.print(gameBoard[rowIndex][columnIndex]);
                System.out.print(SEPARATOR);
            }
            System.out.println("\n");
        }
    }

    public void printGameBoardWithIndexes(char[][] gameBoard){
        System.out.println();
        System.out.print("\t");
        for(int index = 0; index < gameBoard.length; index++){
            System.out.print(index);
            System.out.print(SEPARATOR);
        }

        System.out.println();

        for(int rowIndex = 0; rowIndex < gameBoard.length; rowIndex++){
            System.out.print(rowIndex);
            System.out.print("\t");

            for(int columnIndex = 0; columnIndex < gameBoard.length; columnIndex++){
                char currentSign = gameBoard[rowIndex][columnIndex];
                if(currentSign == PLAYER1_DESIGNATION){
                    System.out.print(ANSI_BLUE + currentSign + ANSI_RESET);
                }
                else if(currentSign == PLAYER2_DESIGNATION){
                    System.out.print(ANSI_RED + currentSign + ANSI_RESET);
                }
                else System.out.print(gameBoard[rowIndex][columnIndex]);

                System.out.print(SEPARATOR);
            }
            System.out.println("\n");
        }
    }

    public void setup(){
        System.out.println(GAME_TITLE);
        System.out.println(GAME_OPTIONS);

        Scanner scanner = new Scanner(System.in);


        while (chosenGameMode == 0){
            System.out.print(GAME_MODE_REQUEST);
            try {
                chosenGameMode = Integer.parseInt(scanner.nextLine());
            } catch (Exception e){
                System.err.print(BAD_INPUT_MESSAGE);
            }
        }

        while (gameBoardSizeChosenByUser == 0){
            System.out.print(GAME_SIZE_REQUEST);
            try {
                gameBoardSizeChosenByUser = Integer.parseInt(scanner.nextLine());
            } catch (Exception e){
                System.err.print(BAD_INPUT_MESSAGE);
            }
        }
    }

    public SingleMove getMove(String playerName, char playerDesignation){
        System.out.print(playerName + " moves: ");
        Scanner scanner = new Scanner(System.in);
        int chosenRow = scanner.nextInt();
        int chosenColumn = scanner.nextInt();

        SingleMove singleMove = new SingleMove(chosenRow, chosenColumn);

        return singleMove;
    }

    public void showPlayerScoredMessage(String playerName, int points){
        System.out.println("\n" + ANSI_GREEN + playerName + " scored " + points + " points!" + ANSI_RESET);
    }

    public void showPlayersPoints(String player1Name, int player1Points, String player2Name, int player2Points){
        System.out.println(ANSI_BLUE + player1Name + " points: " + player1Points + ANSI_RESET);
        System.out.println(ANSI_RED + player2Name + " points: " + player2Points + ANSI_RESET);
    }

    public void showPlaceTakenMessage(){
        System.out.println(TAKEN_PLACE_MESSAGE);
    }



    public int getGameBoardSizeChosenByUser(){
        return gameBoardSizeChosenByUser;
    }

    public int getGameModeChosenByUser(){
        return chosenGameMode;
    }

    public void showBadGameModeMessage(){
        System.out.println(BAD_INPUT_MESSAGE);
    }
}
