public class GreedySearch {

    private static int N;
    private static int[][] distanceMatrix;
    private static int[][] flowMatrix;

    public GreedySearch(int N, int[][] distanceMatrix, int[][] flowMatrix){
        this.N = N;
        this.distanceMatrix = distanceMatrix;
        this.flowMatrix = flowMatrix;
    }
}
