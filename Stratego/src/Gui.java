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

    private int gameBoardSize;
    private int gameBoardSizeChosenByUser = 0;
    private char[][] gameBoard;
    private String chosenOption;


    Gui(char[][] gameBoard){
        this.gameBoard = gameBoard;
        gameBoardSize = gameBoard.length;
    }

    Gui(){
    }

    public void printGameBoard(){
        for(int rowIndex = 0; rowIndex < gameBoardSize; rowIndex++){
            for(int columnIndex = 0; columnIndex < gameBoardSize; columnIndex++){
                System.out.print(gameBoard[rowIndex][columnIndex]);
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
                System.out.print(gameBoard[rowIndex][columnIndex]);
                System.out.print(SEPARATOR);
            }
            System.out.println("\n");
        }
    }

    public void setup(){
        System.out.println(GAME_TITLE);
        System.out.println(GAME_OPTIONS);

        System.out.print(GAME_MODE_REQUEST);
        Scanner scanner = new Scanner(System.in);
        chosenOption = scanner.nextLine();

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
        System.out.println(playerName + " scored " + points + " points!");
    }

    public void showPlayersPoints(String player1Name, int player1Points, String player2Name, int player2Points){
        System.out.println(player1Name + " points: " + player1Points);
        System.out.println(player2Name + " points: " + player2Points);
    }

    public void showPlaceTakenMessage(){
        System.out.println(TAKEN_PLACE_MESSAGE);
    }

    public String getChosenGameOption(){
        return chosenOption;
    }

    public int getGameBoardSizeChosenByUser(){
        return gameBoardSizeChosenByUser;
    }
}
