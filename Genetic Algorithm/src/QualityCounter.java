public class QualityCounter {

    private static int N;
    private static int[][] distanceMatrix;
    private static int[][] flowMatrix;

    QualityCounter(int N, int[][] distanceMatrix, int[][] flowMatrix){
        QualityCounter.N = N;
        QualityCounter.distanceMatrix = distanceMatrix;
        QualityCounter.flowMatrix = flowMatrix;
    }

    public int count(int [] array){
        int result = 0;
        for (int actualLocationNumber = 0; actualLocationNumber < N; actualLocationNumber++){
            for (int actualFactoryNumber = 0; actualFactoryNumber < N; actualFactoryNumber++){
                result += (distanceMatrix[actualLocationNumber][actualFactoryNumber]*flowMatrix[array[actualLocationNumber]][array[actualFactoryNumber]]);
            }
        }
        return result;
    }
}
