import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.Random;

public class AI {

    private final char EMPTY_DESIGNATION = '_';
    private final char OCCUPIED_DESIGNATION = 'X';

    private Random random;
    private int gameBoardSize;
    private char[][] gameBoard;
    private int setDepth;

    AI(char[][] gameBoard, int depth){
        random = new Random();
        this.gameBoard = gameBoard;
        gameBoardSize = gameBoard.length;
        setDepth = depth;
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
//        return minMax(gameBoard, 2, null, true);
        return (minMax3(gameBoard, setDepth, null, true, 0)).getMove();
    }

    public SingleMove makeMoveWithAlphaBeta(){
        return alphaBeta(gameBoard, 3, null, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
//        return alphaBeta3(gameBoard, 5, null, true,0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).getMove();
    }

    public SingleMove runMinMaxWithHeuristic(){
        if(getAvailableMoves().size() > 0.5 * gameBoardSize * gameBoardSize){
            return (minMax(gameBoard, 2, null, true));
        } else {
            return (minMax3(gameBoard, setDepth, null, true, 0)).getMove();
        }
    }

    private SingleMove minMax(char[][] board, int depth, SingleMove move, boolean isMaxing){
        if(depth == 0 || countEmptyPlaces(board) == 0){
            return move;
        }

        if(isMaxing){
            Double best = null;

            ArrayList<SingleMove> availableMoves = getAvailableMoves(board);
            SingleMove returnMove;
            if(availableMoves.size() != 0){
                returnMove = availableMoves.get(0);
            }
            else
                returnMove = new SingleMove();

//            System.out.println("aV Moves 1 : " + availableMoves.toString());

            for (SingleMove currentMove : availableMoves) {
                char[][] newBoard = copyBoardWithNewMove(board, currentMove);

                SingleMove nextMove = minMax(newBoard, depth - 1, currentMove, false);
//                System.out.println("____AI____");
//                Gui gui = new Gui();
//                gui.printGameBoardWithIndexes(copyBoardWithNewMove(newBoard, currentMove));
                int points = countPointsForMove(nextMove, newBoard);
//                System.out.println("points = " + points);
//                if(currentMove.getRow() == 2 && currentMove.getColumn() == 1)
//                if(points > 0) System.out.println("___________------___________");
                if(best == null)
                    best = (double) points;
                if(currentMove.equals(nextMove) && points > best){
//                    System.out.println(points + " < " + best);
                    returnMove = currentMove;
                    best = (double) points;
                }
                else if(points < best){
                    returnMove = currentMove;
                    best = (double) points;
                }
            }
//            Gui gui = new Gui();
//            gui.printGameBoardWithIndexes(copyBoardWithNewMove(gameBoard, returnMove));

            return returnMove;

        } else{
            double best = Double.NEGATIVE_INFINITY;
            SingleMove returnMove = new SingleMove();
            ArrayList<SingleMove> availableMoves = getAvailableMoves(board);
//            System.out.println("aV Moves 2 : " + availableMoves.toString());

            for (SingleMove currentMove : availableMoves) {
                char[][] newBoard = copyBoardWithNewMove(board, currentMove);
//                System.out.println("____JA____");
//                Gui gui = new Gui();
//                gui.printGameBoardWithIndexes(copyBoardWithNewMove(newBoard, currentMove));
                SingleMove nextMove = minMax(newBoard, depth - 1, currentMove, true);

                int points = countPointsForMove(nextMove, newBoard);
                if(points > best){
//                    System.out.println(points + " > " + best);
                    returnMove = currentMove;
                    best = points;
                }
            }
//            Gui gui = new Gui();
//            gui.printGameBoardWithIndexes(copyBoardWithNewMove(gameBoard, returnMove));
//            if(returnMove.getRow() == 3 && returnMove.getColumn() == 1)
            return returnMove;
        }
    }


    private SingleMove minMax2(char[][] board, int depth, SingleMove move, boolean isMaxing){
        if(depth == 0 || countEmptyPlaces(board) == 0){
            return move;
        }

        if(isMaxing){
            double best = Double.NEGATIVE_INFINITY;
            SingleMove returnMove = new SingleMove();
            ArrayList<SingleMove> availableMoves = getAvailableMoves(board);

            for (SingleMove currentMove : availableMoves) {
                char[][] newBoard = copyBoardWithNewMove(board, currentMove);
                SingleMove nextMove = minMax2(newBoard, depth - 1, currentMove, false);

                int points = countPointsForMove(nextMove, newBoard);
                if(points > best){
                    returnMove = currentMove;
                    best = points;
                }
            }
            return returnMove;

        } else{
            double best = Double.POSITIVE_INFINITY;
            SingleMove returnMove = new SingleMove();
            ArrayList<SingleMove> availableMoves = getAvailableMoves(board);

            for (SingleMove currentMove : availableMoves) {
                char[][] newBoard = copyBoardWithNewMove(board, currentMove);
                SingleMove nextMove = minMax2(newBoard, depth - 1, currentMove, true);

                int points = - countPointsForMove(nextMove, newBoard);
                if(points < best){
                    returnMove = currentMove;
                    best = points;
                }
            }
            return returnMove;
        }
    }

    private ReturnPair minMax3(char[][] board, int depth, SingleMove move, boolean isMaxing, int sum){
        ArrayList<SingleMove> availableMoves = getAvailableMoves(board);

        if(depth == 0 || countEmptyPlaces(board) == 0 || availableMoves.size() == 0){
            return new ReturnPair( countPointsForMove(move, board), move);
        }

        int score;
        if(depth == setDepth)
            score = 0;
        else
            score = countPointsForMoveWithoutClear(move, board);

        if(isMaxing){
            double best = Double.NEGATIVE_INFINITY;
            SingleMove returnMove = availableMoves.get(0);

            for (SingleMove currentMove : availableMoves) {
//                while (availableMoves.size() > 0) {

//                char[][] newBoard = copyBoardWithNewMove(board, currentMove);
//                    SingleMove currentMove = availableMoves.remove(availableMoves.size()/2);

                    char[][] newBoard = copyBoardWithNewMove(board,currentMove);

                ReturnPair pair = minMax3(newBoard, depth - 1, currentMove, false, sum - countPointsForMove(currentMove, newBoard));

//                if(countPointsForMoveWithoutClear(pair.getMove(), board) > best){
                if(sum + countPointsForMove(pair.getMove(), newBoard) > best){
                    returnMove = pair.getMove();
//                    best = pair.getPoints();
                    best = sum + countPointsForMove(pair.getMove(), newBoard);
                }
            }

//            return new ReturnPair((int)best - score, returnMove);
            return new ReturnPair(sum, returnMove);
        } else{
            double best = Double.POSITIVE_INFINITY;
            SingleMove returnMove = availableMoves.get(0);

            for (SingleMove currentMove : availableMoves) {
                char[][] newBoard = copyBoardWithNewMove(board, currentMove);

                ReturnPair pair = minMax3(newBoard, depth - 1, currentMove, true,  sum + countPointsForMove(currentMove, newBoard));

//                if(countPointsForMoveWithoutClear(pair.getMove(), board) < best){
                if(sum - countPointsForMove(pair.getMove(), newBoard) < best){
                    returnMove = pair.getMove();
//                    best = pair.getPoints();
                    best = sum - countPointsForMove(pair.getMove(), newBoard);
                }
            }

//            return new ReturnPair((int)best + score, returnMove);
            return new ReturnPair(sum, returnMove);
        }
    }

//    private ReturnPair alphaBeta3(char[][] board, int depth, SingleMove move, boolean isMaxing, int sum, Double alpha, Double beta){
//        if(depth == 0 || countEmptyPlaces(board) == 0){
//            return new ReturnPair((double) countPointsForMove(move, board), move);
//        }
//
//        if(isMaxing){
//            double best = Double.NEGATIVE_INFINITY;
//            SingleMove returnMove = new SingleMove();
//            ArrayList<SingleMove> availableMoves = getAvailableMoves(board);
//
//            for (SingleMove currentMove : availableMoves) {
//                char[][] newBoard = copyBoardWithNewMove(board, currentMove);
//                ReturnPair pair = alphaBeta3(newBoard, depth - 1, currentMove, false, sum + countPointsForMove(currentMove, newBoard), alpha, beta);
//
//                Double points = countPointsForMove(pair.getMove(), newBoard) + pair.getPoints();
//                if(sum + countPointsForMove(pair.getMove(), newBoard) > best){
//                    returnMove = pair.getMove();
//                    best = sum + countPointsForMove(pair.getMove(), newBoard);
//                }
//                if(points > alpha){
//                    alpha = (double) points;
//                }
//                if(alpha >= beta)
//                    return new ReturnPair(best, currentMove);
//            }
//
//            return new ReturnPair(best, returnMove);
//
//        } else{
//            double best = Double.POSITIVE_INFINITY;
//            SingleMove returnMove = new SingleMove();
//            ArrayList<SingleMove> availableMoves = getAvailableMoves(board);
//
//            for (SingleMove currentMove : availableMoves) {
//                char[][] newBoard = copyBoardWithNewMove(board, currentMove);
//                ReturnPair pair = alphaBeta3(newBoard, depth - 1, currentMove, true, sum - countPointsForMove(currentMove, newBoard), alpha, beta);
//
//                Double points = countPointsForMove(pair.getMove(), newBoard) - pair.getPoints();
//                if(sum - countPointsForMove(pair.getMove(), newBoard) < best){
//                    returnMove = pair.getMove();
//                    best = sum - countPointsForMove(pair.getMove(), newBoard);
//                }
//
//                if(points < beta){
//                    beta = (double) points;
//                }
//                if(alpha >= beta)
//                    return new ReturnPair(best, currentMove);
//            }
//
//            return new ReturnPair(best, returnMove);
//        }
//    }


    private SingleMove alphaBeta(char[][] board, int depth, SingleMove move, boolean isMaxing, Double alpha, Double beta){
        if(depth == 0 || countEmptyPlaces(board) == 0){
            return move;
        }

        if(isMaxing){
            double best = Double.NEGATIVE_INFINITY;
            SingleMove returnMove = new SingleMove();
            ArrayList<SingleMove> availableMoves = getAvailableMoves(board);

            for (SingleMove currentMove : availableMoves) {
                char[][] newBoard = copyBoardWithNewMove(board, currentMove);
                SingleMove nextMove = minMax2(newBoard, depth - 1, currentMove, false);

                int points = countPointsForMove(nextMove, newBoard);
                if(points > best){
//                    System.out.println(points + " > " + best);
                    returnMove = currentMove;
                    best = points;
                }

                if(points > alpha){
                    alpha = (double) points;
                }
                if(alpha >= beta)
                    return currentMove;
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
                SingleMove nextMove = minMax2(newBoard, depth - 1, currentMove, true);

                int points = - countPointsForMove(nextMove, newBoard);
                if(points < best){
                    returnMove = currentMove;
                    best = points;
                }

                if(points < beta){
                    beta = (double) points;
                }

                if(alpha >= beta)
                    return currentMove;
            }

//            Gui gui = new Gui();
//            gui.printGameBoardWithIndexes(copyBoardWithNewMove(gameBoard, returnMove));
            return returnMove;
        }
    }

    public SingleMove smartMove(ArrayList<SingleMove> availableMoves){
        SingleMove returnMove = new SingleMove();

        for (SingleMove currentMove : availableMoves) {
            SingleMove proposalMove = getSimetricMove(currentMove);
//            System.out.println(proposalMove);

            if(gameBoard[proposalMove.getRow()][proposalMove.getColumn()] == EMPTY_DESIGNATION)
                returnMove = proposalMove;
        }

        System.out.println(returnMove);
        return returnMove;
    }

    public SingleMove smartMove(SingleMove playerMove){

        return getSimetricMove(playerMove);
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

    private char[][] copyBoard(char[][] oldBoard){
        char[][] newBoard = new char[gameBoardSize][gameBoardSize];

        for(int rowIndex = 0; rowIndex < gameBoardSize; rowIndex++){
            for(int columnIndex = 0; columnIndex < gameBoardSize; columnIndex++){
                char currentSign = oldBoard[rowIndex][columnIndex];

                    newBoard[rowIndex][columnIndex] = currentSign;
            }
        }
        return newBoard;
    }

    private SingleMove getSimetricMove(SingleMove move){
        int row = gameBoardSize - move.getRow() - 1;
        int column = gameBoardSize - move.getColumn() - 1;

//        System.out.println(new SingleMove(row, column));

        return new SingleMove(row, column);
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

    private int countPointsForMoveWithoutClear(SingleMove move, char[][] board){
        char[][] tempBoard = new char[gameBoardSize][gameBoardSize];
        tempBoard = copyBoard(board);
        tempBoard[move.getRow()][move.getColumn()] = OCCUPIED_DESIGNATION;
        int points = countPoints(move, board);

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




    // _____________________________________________________


//    private ReturnPair getMinMaxMove(int depth, boolean maxingMode, char[][] board) {
//        ReturnPair moveToReturn = new ReturnPair(maxingMode ? -1 : 100000, null);
//        ArrayList<SingleMove> moves = getAvailableMoves(); // < ---
//        moveToReturn.setMove(moves.get(0));
//        int bestScore = maxingMode ? -1 : 100000;
//        for (SingleMove move : moves) {
////            addMoveToPlayer();
//            if(depth == 0 || moves.size() == 1) {
//                int currentScore = countPointsForMove(move, board);
//                if(maxingMode) {
//                    if (currentScore > moveToReturn.getPoints()) {
//                        moveToReturn.setMove(move);
//                        moveToReturn.setPoints(currentScore);
//                    }
//                }
//                else {
//                    if (currentScore < moveToReturn.getPoints()) {
//                        moveToReturn.setMove(move);
//                        moveToReturn.setPoints(currentScore);
//                    }
//                }
//            }
//            else{
//                int currentScore = countPointsForMove(move, board);
//                board = copyBoardWithNewMove(board, move);
//                ReturnPair nextMove = getMinMaxMove(depth -1, !maxingMode);
//                int nextScore = nextMove.getPoints();
//                clearPosition(move);
//                if(maxingMode) {
//                    if (currentScore - nextScore  > bestScore) {
//                        moveToReturn.setMove(move);
//                        moveToReturn.setPoints(currentScore);
//                        bestScore = nextScore;
//                    }
//                }
//                else {
//                    if (currentScore - nextScore < bestScore) {
//                        moveToReturn.setMove(move);
//                        moveToReturn.setPoints(currentScore);
//                        bestScore = nextScore;
//                    }
//                }
//            }
//        }
//        return moveToReturn;
//    }
}
