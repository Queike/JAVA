public class Game {

    private final char EMPTY_DESIGNATION = '_';
    private final char PLAYER1_DESIGNATION = 'M';
    private final char PLAYER2_DESIGNATION = 'D';

    private int gameBoardSize;
    private char[][] gameBoard;
    private static char currentPlayer;
    Gui gui;
    private int freeSpaces;


    Game(int gameBoardSize){
        this.gameBoardSize = gameBoardSize;
        this.gameBoard = new char[gameBoardSize][gameBoardSize];
        clearGameBoard();
        currentPlayer = PLAYER1_DESIGNATION;
        gui = new Gui(gameBoard);
        freeSpaces = gameBoardSize * gameBoardSize;
    }

    private void clearGameBoard(){
        for(int rowIndex = 0; rowIndex < gameBoardSize; rowIndex++){
            for(int columnIndex = 0; columnIndex < gameBoardSize; columnIndex++){
                gameBoard[rowIndex][columnIndex] = EMPTY_DESIGNATION;
            }
        }
    }

    public void play(){
        while(freeSpaces > 0){
            makeMove();
        }
    }

    private void makeMove(){
        gui.printGameBoardWithIndexes(gameBoard);

        SingleMove move = gui.getMove(String.valueOf(currentPlayer), currentPlayer);
        if(gameBoard[move.getRow()][move.getColumn()] == EMPTY_DESIGNATION){
            gameBoard[move.getRow()][move.getColumn()] = currentPlayer;
            freeSpaces--;
            switchCurrentPlayer();
        } else {
            gui.showPlaceTakenMessage();
            makeMove();
        }
    }

    public char[][] getGameBoard(){
        return gameBoard;
    }

    private void switchCurrentPlayer(){
        if(currentPlayer == PLAYER1_DESIGNATION)
            currentPlayer = PLAYER2_DESIGNATION;
        else currentPlayer = PLAYER1_DESIGNATION;
    }
}
