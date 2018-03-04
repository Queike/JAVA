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

    public int[] findBestIndividual(int [][] array){
        int result;
        int bestResult = 0;
        int[] bestIndividual = array[0];

        for(int actualIndividualNumber = 0; actualIndividualNumber < array.length; actualIndividualNumber++){

            result = 0;

            for (int actualLocationNumber = 0; actualLocationNumber < N; actualLocationNumber++){
                for (int actualFactoryNumber = 0; actualFactoryNumber < N; actualFactoryNumber++){
                    result += (distanceMatrix[actualLocationNumber][actualFactoryNumber]*flowMatrix[array[actualIndividualNumber][actualLocationNumber]][array[actualIndividualNumber][actualFactoryNumber]]);
                }
            }

            if(result < bestResult || bestResult == 0){
                bestIndividual = array[actualIndividualNumber];
            }
        }

        return bestIndividual;
    }
}
