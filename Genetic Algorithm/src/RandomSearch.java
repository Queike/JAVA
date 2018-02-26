import java.util.ArrayList;
import java.util.Random;

public class RandomSearch {

    private final int NUMBER_OF_LOOPS = 1;

    private static int N;
    private static int[][] distanceMatrix;
    private static int[][] flowMatrix;
    private int bestResult;

    RandomSearch(int N, int[][] distanceMatrix, int[][] flowMatrix){
        RandomSearch.N = N;
        RandomSearch.distanceMatrix = distanceMatrix;
        RandomSearch.flowMatrix = flowMatrix;
    }

    public void search(){
        QualityCounter qualityCounter = new QualityCounter(N, distanceMatrix, flowMatrix);

        int [] vector;
        int quality;

        for(int i = 0; i < NUMBER_OF_LOOPS; i++){
            vector = generateVector(N);

            quality = qualityCounter.count(vector);

            if(quality < bestResult || bestResult == 0)
                bestResult = quality;
        }

        System.out.println("BEST RESULT OF RANDOM SEARCH FOR N = " + N + " : " + bestResult);
    }

    private int[] generateVector(int n){

        ArrayList<Integer> factories = new ArrayList<>();
        Random generator = new Random();
        int generatedNumber;

        for(int i = 0; i < n; i++)
        {
            factories.add(i);
        }

        int [] vector = new int[n];

        int position = 0;

        while(factories.size() > 0){
            generatedNumber = generator.nextInt(factories.size());
            vector[position] = factories.get(generatedNumber);
            factories.remove(generatedNumber);
            position++;
        }

//        printVector(vector);
        return vector;
    }

    public void printVector(int [] vector){
        for(int i = 0; i < vector.length ; i++) {
            System.out.print(vector[i] + " ");
        }
        System.out.println();
    }

}
