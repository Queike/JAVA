public class Game {

    private final char EMPTY_DESIGNATION = '_';
    private final char PLAYER1_DESIGNATION = 'M';
    private final char PLAYER2_DESIGNATION = 'D';

    private int gameBoardSize;
    private char[][] gameBoard;
    private static char currentPlayer;
    Gui gui;
    private int freeSpaces, player1Points, player2Points;


    Game(int gameBoardSize){
        this.gameBoardSize = gameBoardSize;
        this.gameBoard = new char[gameBoardSize][gameBoardSize];
        clearGameBoard();
        currentPlayer = PLAYER1_DESIGNATION;
        gui = new Gui(gameBoard);
        freeSpaces = gameBoardSize * gameBoardSize;
        player1Points = 0;
        player2Points = 0;
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
            makePlayerMove();
        }
    }

    private void makePlayerMove(){
        gui.printGameBoardWithIndexes(gameBoard);

        SingleMove move = gui.getMove(String.valueOf(currentPlayer), currentPlayer);
        if(gameBoard[move.getRow()][move.getColumn()] == EMPTY_DESIGNATION){
            gameBoard[move.getRow()][move.getColumn()] = currentPlayer;

            if(checkMoveForPoints(move)){
                int earnedPoints = countPoints(move);
                gui.showPlayerScoredMessage(String.valueOf(currentPlayer), earnedPoints);
                addPointsToPlayerAccount(earnedPoints);
                gui.showPlayersPoints(String.valueOf(PLAYER1_DESIGNATION), player1Points, String.valueOf(PLAYER2_DESIGNATION), player2Points);
            }


            freeSpaces--;
            switchCurrentPlayer();
        } else {
            gui.showPlaceTakenMessage();
            makePlayerMove();
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

    private boolean checkMoveForPoints(SingleMove move){
        return checkRow(move) || checkColumn(move) || checkDiagonal(move) != 0;
    }

    private boolean checkRow(SingleMove move){
        for(int columnIndex = 0; columnIndex < gameBoardSize; columnIndex++) {
            if (gameBoard[move.getRow()][columnIndex] == EMPTY_DESIGNATION)
                return false;
        }
        return true;
    }

    private boolean checkColumn(SingleMove move){
        for(int rowIndex = 0; rowIndex < gameBoardSize; rowIndex++) {
            if (gameBoard[rowIndex][move.getColumn()] == EMPTY_DESIGNATION)
                return false;
        }
        return true;
    }

    private int checkDiagonal(SingleMove move){
        int pointsCounter = 0;

        int rowIndex = move.getRow();
        int columnIndex = move.getColumn();

        while(columnIndex >= 0 && rowIndex >= 0){
            if(gameBoard[rowIndex][columnIndex] == EMPTY_DESIGNATION)
                return 0;
            else pointsCounter++;

            rowIndex--;
            columnIndex--;
        }

        rowIndex = move.getRow() + 1;
        columnIndex = move.getColumn() + 1;

        while(columnIndex < gameBoardSize && rowIndex < gameBoardSize){
            if(gameBoard[rowIndex][columnIndex] == EMPTY_DESIGNATION)
                return 0;
            else pointsCounter++;

            rowIndex++;
            columnIndex++;
        }

        if(pointsCounter == 1)
            pointsCounter--;

        return pointsCounter;
    }

    private int countPoints(SingleMove move){
        int earnedPoints = 0;

        if(checkRow(move))
            earnedPoints += countPointsFromRow();

        if(checkColumn(move))
            earnedPoints += countPointsFromColumn();

        int diagonalPoints = checkDiagonal(move);
        if(diagonalPoints != 0)
            earnedPoints += countPointsFromDiagonal(diagonalPoints);

        return earnedPoints;
    }

    private int countPointsFromRow(){
        return gameBoardSize;
    }

    private int countPointsFromColumn(){
        return gameBoardSize;
    }

    private int countPointsFromDiagonal(int diagonalPoints){
        return diagonalPoints;
    }

    private void addPointsToPlayerAccount(int points){
        if(currentPlayer == PLAYER1_DESIGNATION)
            player1Points += points;
        else player2Points += points;
    }


}
