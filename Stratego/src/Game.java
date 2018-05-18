import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Game {

    private final char EMPTY_DESIGNATION = '_';
    private final char PLAYER1_DESIGNATION = 'M';
    private final char PLAYER2_DESIGNATION = 'D';
    private final int AI_SLEEP_TIME_IN_SECONDS = 0;

    private int gameBoardSize;
    private char[][] gameBoard;
    private static char currentPlayer;
    private Gui gui;
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

    public void playPvP(){

        while(freeSpaces > 0){
            gui.printGameBoardWithIndexes(gameBoard);
            SingleMove move = gui.getMove(String.valueOf(currentPlayer), currentPlayer);
            makeMove(move);
        }
    }

    public void playAvP() throws InterruptedException {
        AI ai = new AI(gameBoard, 2);

        while (freeSpaces > 0){
            gui.printGameBoardWithIndexes(gameBoard);
            SingleMove playerMove = gui.getMove(String.valueOf(currentPlayer), currentPlayer);
            makeMove(playerMove);
            gui.printGameBoardWithIndexes(gameBoard);

            if(freeSpaces > 0){
                System.out.print("AI moves: ");
                TimeUnit.SECONDS.sleep(AI_SLEEP_TIME_IN_SECONDS);

                // ARTIFICIAL INTELIGENCE PLAYER ALGORITHM

//            SingleMove aiMove = ai.makeRandomMove(getAvailableMoves());               // random move
//            SingleMove aiMove = ai.makeMoveWithPoints(getAvailableMoves());           // first move with points or random if no with points
                SingleMove aiMove = ai.makeMoveWithMaxPoints(getAvailableMoves());    // move with max points or random if no with points
//                SingleMove aiMove = ai.makeMoveWithMinMax();                          // move with min max algorithm
//                SingleMove aiMove = ai.makeMoveWithAlphaBeta();                       // move with alpha beta algorithm
//                SingleMove aiMove = ai.runMinMaxWithHeuristic();                      // move with min max algorithm with heuristic improve
//                SingleMove aiMove = ai.smartMove(getAvailableMoves());                // move with min max algorithm with heuristic improve
//                SingleMove aiMove = ai.smartMove(playerMove);                         // beta version - you dont need this :D

                System.out.print(aiMove.getRow() + " " + aiMove.getColumn());
                makeMove(aiMove);
            }

        }
    }

    public void playAvA() throws InterruptedException {
        AI ai = new AI(gameBoard, 3);

        while (freeSpaces > 0){
            gui.printGameBoardWithIndexes(gameBoard);
            System.out.print("AI moves: ");
            TimeUnit.SECONDS.sleep(AI_SLEEP_TIME_IN_SECONDS);

            // ARTIFICIAL INTELIGENCE PLAYER 1 ALGORITHM

//            SingleMove ai1Move = ai.makeRandomMove(getAvailableMoves());       // random move
//            SingleMove ai1Move = ai.makeMoveWithPoints(getAvailableMoves());   // first move with points or random if no with points
            SingleMove ai1Move = ai.makeMoveWithMaxPoints(getAvailableMoves()); // move with max points or random if no with points
//            SingleMove ai1Move = ai.makeMoveWithMinMax();                           // move with min max algorithm
//            SingleMove ai1Move = ai.makeMoveWithAlphaBeta();                        // move with alpha beta algorithm
//            SingleMove ai1Move = ai.runMinMaxWithHeuristic();                        // move with min max algorithm with heuristic improve

            System.out.print(ai1Move.getRow() + " " + ai1Move.getColumn());
            makeMove(ai1Move);

            gui.printGameBoardWithIndexes(gameBoard);

            if(freeSpaces > 0){
                System.out.print("AI moves: ");
                TimeUnit.SECONDS.sleep(AI_SLEEP_TIME_IN_SECONDS);

                // ARTIFICIAL INTELIGENCE PLAYER 2 ALGORITHM

//            SingleMove ai2Move = ai.makeRandomMove(getAvailableMoves());          // random move
//                SingleMove ai2Move = ai.makeMoveWithPoints(getAvailableMoves());  // first move with points or random if no with points
//            SingleMove ai2Move = ai.makeMoveWithMaxPoints(getAvailableMoves());   // move with max points or random if no with points
                SingleMove ai2Move = ai.makeMoveWithMinMax();                     // move with min max algorithm
//                SingleMove ai2Move = ai.makeMoveWithAlphaBeta();                  // move with alpha beta algorithm
//                SingleMove ai2Move = ai.runMinMaxWithHeuristic();                 // move with min max algorithm with heuristic improve
//                SingleMove ai2Move = ai.smartMove(getAvailableMoves());           // beta version - you dont need this :D
//                SingleMove ai2Move = ai.smartMove(ai1Move);                       // beta version - you dont need this :D


                System.out.print(ai2Move.getRow() + " " + ai2Move.getColumn());
                makeMove(ai2Move);
            }

        }
    }


    private void makeMove(SingleMove move){

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
            move = gui.getMove(String.valueOf(currentPlayer), currentPlayer);
            makeMove(move);
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

        return checkRow(move) || checkColumn(move) || checkDiagonals(move) != 0;
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


    private int checkFirstDiagonal(SingleMove move){
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

    private int checkSecondDiagonal(SingleMove move){
        int pointsCounter = 0;
        int rowIndex = move.getRow();
        int columnIndex = move.getColumn();

        while(columnIndex >= 0 && rowIndex < gameBoardSize){
            if(gameBoard[rowIndex][columnIndex] == EMPTY_DESIGNATION)
                return 0;
            else pointsCounter++;

            rowIndex++;
            columnIndex--;
        }

        rowIndex = move.getRow() - 1;
        columnIndex = move.getColumn() + 1;

        while(columnIndex < gameBoardSize && rowIndex >= 0){
            if(gameBoard[rowIndex][columnIndex] == EMPTY_DESIGNATION)
                return 0;
            else pointsCounter++;

            rowIndex--;
            columnIndex++;
        }

        if(pointsCounter == 1)
            pointsCounter--;

        return pointsCounter;
    }

    private int checkDiagonals(SingleMove move){
        return checkFirstDiagonal(move) + checkSecondDiagonal(move);
    }

    private int countPoints(SingleMove move){
        int earnedPoints = 0;

        if(checkRow(move))
            earnedPoints += countPointsFromRow();

        if(checkColumn(move))
            earnedPoints += countPointsFromColumn();

        int diagonalPoints = checkDiagonals(move);
        if(diagonalPoints != 0)
            earnedPoints += countPointsFromDiagonal(diagonalPoints);

        return earnedPoints;
    }

    private ArrayList<SingleMove> getAvailableMoves(){
        ArrayList<SingleMove> availableMoves = new ArrayList<>();

        for(int rowIndex = 0; rowIndex < gameBoardSize; rowIndex++){
            for(int columnIndex = 0; columnIndex < gameBoardSize; columnIndex++){
                if(gameBoard[rowIndex][columnIndex] == EMPTY_DESIGNATION){
                    SingleMove singleMove = new SingleMove(rowIndex, columnIndex);
                    availableMoves.add(singleMove);
                }
            }
        }

        return availableMoves;
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