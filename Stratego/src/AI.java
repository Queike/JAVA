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

    public SingleMove makeMoveWithMaxPoints(ArrayList<SingleMove> availavleMoves){
        int maxPoints = 0;
        SingleMove bestMove = availavleMoves.get(0);

        for (SingleMove move : availavleMoves) {
            int currentPoints = countPointsForPotentialMove(move);
            if(currentPoints > maxPoints){
                maxPoints = currentPoints;
                bestMove = move;
            }
        }

        if(maxPoints == 0)
            bestMove = makeRandomMove(availavleMoves);

        return bestMove;
    }

    private int countPointsForPotentialMove(SingleMove move){
        gameBoard[move.getRow()][move.getColumn()] = OCCUPIED_DESIGNATION;
        int potentialPoints = countPoints(move);
        gameBoard[move.getRow()][move.getColumn()] = EMPTY_DESIGNATION;

        return potentialPoints;
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
