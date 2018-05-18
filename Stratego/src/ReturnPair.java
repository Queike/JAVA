public class ReturnPair {
    private int points;
    private SingleMove move;

    public ReturnPair(int points, SingleMove move){
        this.points = points;
        this.move = move;
    }

    public ReturnPair(){}

    public int getPoints() {
        return points;
    }

    public SingleMove getMove() {
        return move;
    }

    public void setMove(SingleMove move){
        this.move = move;
    }

    public void setPoints(int points){
        this.points = points;
    }
}
