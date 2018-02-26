import java.util.ArrayList;
import java.util.Random;

public class GreedySearch {

    private static int N;
    private static int[][] distanceMatrix;
    private static int[][] flowMatrix;
    private int bestResult;
    private Integer numberOfBestFactory;

    GreedySearch(int N, int[][] distanceMatrix, int[][] flowMatrix){
        GreedySearch.N = N;
        GreedySearch.distanceMatrix = distanceMatrix;
        GreedySearch.flowMatrix = flowMatrix;
    }

    public void search(){
        QualityCounter qualityCounter = new QualityCounter(N, distanceMatrix, flowMatrix);

        int [] vector;

        ArrayList<Integer> factories = new ArrayList<>();
        for(int i = 0; i < N; i++)
        {
            factories.add(i);
        }

        vector = getRandomlyFirstFactory();
        factories.remove(vector[0]);
        int cost;

        for(int i = 1; i < N; i++){

            for(int j = 0; j < factories.size(); j++){
                vector[i] = factories.get(j);
                cost = qualityCounter.count(vector);

                if(j == 0 || cost < bestResult){
                    bestResult = cost;
                    numberOfBestFactory = factories.get(j);
                }
            }

            vector[i] = numberOfBestFactory;
            factories.remove(numberOfBestFactory);
        }

        int finalCost = qualityCounter.count(vector);

        System.out.print("BEST RESULT OF GREEDY SEARCH FOR N = " + N + " : " + finalCost + "\tvector -> ");
        printVector(vector);
    }

    private int[] getRandomlyFirstFactory(){
        int [] vector = new int[N];
        Random generator = new Random();
        vector[0] = generator.nextInt(N);

        return vector;
    }

    public void printVector(int [] vector){
        for(int i = 0; i < vector.length ; i++) {
            System.out.print(vector[i] + " ");
        }
        System.out.println();
    }
}
