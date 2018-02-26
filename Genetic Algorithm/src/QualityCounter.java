public class QualityCounter {

    private static int N;
    private static int[][] distanceMatrix;
    private static int[][] flowMatrix;

    public QualityCounter(int N, int[][] distanceMatrix, int[][] flowMatrix){
        this.N = N;
        this.distanceMatrix = distanceMatrix;
        this.flowMatrix = flowMatrix;
    }

    public int count(int [] array){
        int result = 0;
        for (int i=0; i<N; i++){
            for (int j=0; j<N; j++){
                result += (distanceMatrix[i][j]*flowMatrix[array[i]][array[j]]);
            }

        }
        return result;
    }
}
