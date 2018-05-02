import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.Random;

public class AI {

    private final char EMPTY_DESIGNATION = '_';
    private final char OCCUPIED_DESIGNATION = 'X';

    private Random random;
    private int gameBoardSize;
    private char[][] gameBoard;

    AI(char[][] gameBoard){
        random = new Random();
        this.gameBoard = gameBoard;
        gameBoardSize = gameBoard.length;
    }


    public SingleMove makeRandomMove(ArrayList<SingleMove> availableMoves){
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }

    public SingleMove makeMoveWithPoints(ArrayList<SingleMove> availavleMoves){
        SingleMove resultMove = availavleMoves.get(0);
        int resultPoints = 0;

        for (SingleMove move : availavleMoves) {
            int currentPoints = countPointsForPotentialMove(move, gameBoard);
            if(currentPoints > 0){
                resultPoints = currentPoints;
                resultMove = move;
                break;
            }
        }

        if(resultPoints == 0)
            resultMove = makeRandomMove(availavleMoves);

        return resultMove;

    }

    public SingleMove makeMoveWithMaxPoints(ArrayList<SingleMove> availableMoves){
        int maxPoints = 0;
        SingleMove bestMove = availableMoves.get(0);

        for (SingleMove move : availableMoves) {
            int currentPoints = countPointsForPotentialMove(move, gameBoard);
            if(currentPoints > maxPoints){
                maxPoints = currentPoints;
                bestMove = move;
            }
        }

        if(maxPoints == 0)
            bestMove = makeRandomMove(availableMoves);

        return bestMove;
    }

    public SingleMove makeMoveWithMinMax(){
        return minMax(gameBoard, 3, null, true);
    }

    private SingleMove minMax(char[][] board, int depth, SingleMove move, boolean isMaxing){
        if(depth == 0 || countEmptyPlaces(board) == 0){
            return move;
        }

        if(isMaxing){
            double best = Double.NEGATIVE_INFINITY;
            SingleMove returnMove = new SingleMove();
            ArrayList<SingleMove> availableMoves = getAvailableMoves(board);

            for (SingleMove currentMove : availableMoves) {
                char[][] newBoard = copyBoardWithNewMove(board, currentMove);
                SingleMove nextMove = minMax(newBoard, depth - 1, currentMove, false);

                int points = countPointsForMove(nextMove, newBoard);
                if(points > best){
//                    System.out.println(points + " > " + best);
                    returnMove = currentMove;
                    best = points;
                }
            }
//            Gui gui = new Gui();
//            gui.printGameBoardWithIndexes(copyBoardWithNewMove(gameBoard, returnMove));
            return returnMove;

        } else{
            double best = Double.POSITIVE_INFINITY;
            SingleMove returnMove = new SingleMove();
            ArrayList<SingleMove> availableMoves = getAvailableMoves(board);

            for (SingleMove currentMove : availableMoves) {
                char[][] newBoard = copyBoardWithNewMove(board, currentMove);
                SingleMove nextMove = minMax(newBoard, depth - 1, currentMove, true);

                int points = countPointsForMove(nextMove, newBoard);
                if(points < best){
                    returnMove = currentMove;
                    best = points;
                }
            }
//            Gui gui = new Gui();
//            gui.printGameBoardWithIndexes(copyBoardWithNewMove(gameBoard, returnMove));
            return returnMove;
        }
    }

    private char[][] copyBoardWithNewMove(char[][] oldBoard, SingleMove newMove){
        char[][] newBoard = new char[gameBoardSize][gameBoardSize];

        for(int rowIndex = 0; rowIndex < gameBoardSize; rowIndex++){
            for(int columnIndex = 0; columnIndex < gameBoardSize; columnIndex++){
                char currentSign = oldBoard[rowIndex][columnIndex];
                if(rowIndex == newMove.getRow() && columnIndex == newMove.getColumn()){
                    newBoard[rowIndex][columnIndex] = OCCUPIED_DESIGNATION;
                } else {
                    newBoard[rowIndex][columnIndex] = currentSign;
                }
            }
        }
        return newBoard;
    }


    private int countPointsForPotentialMove(SingleMove move, char[][] board){
        gameBoard[move.getRow()][move.getColumn()] = OCCUPIED_DESIGNATION;
        int potentialPoints = countPoints(move, board);
        gameBoard[move.getRow()][move.getColumn()] = EMPTY_DESIGNATION;

        return potentialPoints;
    }

    private int countPointsForMove(SingleMove move, char[][] board){
        board[move.getRow()][move.getColumn()] = OCCUPIED_DESIGNATION;
        int points = countPoints(move, board);

//        if(points > 0)
        board[move.getRow()][move.getColumn()] = EMPTY_DESIGNATION;

        return points;
    }


    private boolean checkRow(SingleMove move, char[][] board){
        for(int columnIndex = 0; columnIndex < gameBoardSize; columnIndex++) {
            if (board[move.getRow()][columnIndex] == EMPTY_DESIGNATION)
                return false;
        }
        return true;
    }

    private boolean checkColumn(SingleMove move, char[][] board){
        for(int rowIndex = 0; rowIndex < gameBoardSize; rowIndex++) {
            if (board[rowIndex][move.getColumn()] == EMPTY_DESIGNATION)
                return false;
        }
        return true;
    }

    private int countEmptyPlaces(char[][] board){
        int emptyPlacesCounter = 0;

        for(int rowIndex = 0; rowIndex < gameBoardSize; rowIndex++){
            for(int columnIndex = 0; columnIndex < gameBoardSize; columnIndex++){
                char currentSign = gameBoard[rowIndex][columnIndex];
                if(currentSign == EMPTY_DESIGNATION){
                    emptyPlacesCounter++;
                }
            }
        }
        return emptyPlacesCounter;
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

    private ArrayList<SingleMove> getAvailableMoves(char[][] board){
        ArrayList<SingleMove> availableMoves = new ArrayList<>();

        for(int rowIndex = 0; rowIndex < gameBoardSize; rowIndex++){
            for(int columnIndex = 0; columnIndex < gameBoardSize; columnIndex++){
                if(board[rowIndex][columnIndex] == EMPTY_DESIGNATION){
                    SingleMove singleMove = new SingleMove(rowIndex, columnIndex);
                    availableMoves.add(singleMove);
                }
            }
        }

        return availableMoves;
    }

    private int checkFirstDiagonal(SingleMove move, char[][] board){
        int pointsCounter = 0;

        int rowIndex = move.getRow();
        int columnIndex = move.getColumn();

        while(columnIndex >= 0 && rowIndex >= 0){
            if(board[rowIndex][columnIndex] == EMPTY_DESIGNATION)
                return 0;
            else pointsCounter++;

            rowIndex--;
            columnIndex--;
        }

        rowIndex = move.getRow() + 1;
        columnIndex = move.getColumn() + 1;

        while(columnIndex < gameBoardSize && rowIndex < gameBoardSize){
            if(board[rowIndex][columnIndex] == EMPTY_DESIGNATION)
                return 0;
            else pointsCounter++;

            rowIndex++;
            columnIndex++;
        }



        if(pointsCounter == 1)
            pointsCounter--;

        return pointsCounter;
    }

    private int checkSecondDiagonal(SingleMove move, char[][] board){
        int pointsCounter = 0;
        int rowIndex = move.getRow();
        int columnIndex = move.getColumn();

        while(columnIndex >= 0 && rowIndex < gameBoardSize){
            if(board[rowIndex][columnIndex] == EMPTY_DESIGNATION)
                return 0;
            else pointsCounter++;

            rowIndex++;
            columnIndex--;
        }

        rowIndex = move.getRow() - 1;
        columnIndex = move.getColumn() + 1;

        while(columnIndex < gameBoardSize && rowIndex >= 0){
            if(board[rowIndex][columnIndex] == EMPTY_DESIGNATION)
                return 0;
            else pointsCounter++;

            rowIndex--;
            columnIndex++;
        }

        if(pointsCounter == 1)
            pointsCounter--;

        return pointsCounter;
    }

    private int checkDiagonals(SingleMove move, char[][] board){
        return checkFirstDiagonal(move, board) + checkSecondDiagonal(move, board);
    }

    private int countPoints(SingleMove move, char[][] board){
        int earnedPoints = 0;

        if(checkRow(move, board))
            earnedPoints += countPointsFromRow();

        if(checkColumn(move, board))
            earnedPoints += countPointsFromColumn();

        int diagonalPoints = checkDiagonals(move, board);
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

}
