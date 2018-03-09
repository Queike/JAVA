import java.util.ArrayList;
import java.util.Random;

public class GreedySearch {

    private static int locationsNumber;
    private static int[][] distanceMatrix;
    private static int[][] flowMatrix;
    private int bestResult;
    private int bestBestResult;
    private Integer numberOfBestFactory;
    private int NUMBER_OF_LOOPS;

    GreedySearch(int locationsNumber, int[][] distanceMatrix, int[][] flowMatrix){
        GreedySearch.locationsNumber = locationsNumber;
        GreedySearch.distanceMatrix = distanceMatrix;
        GreedySearch.flowMatrix = flowMatrix;
    }

    public void search(){

        NUMBER_OF_LOOPS = locationsNumber;
        Solution bestSolution = new Solution();

        for(int i = 0; i < NUMBER_OF_LOOPS; i++){
            QualityCounter qualityCounter = new QualityCounter(locationsNumber, distanceMatrix, flowMatrix);
            Solution solution = new Solution();

            ArrayList<Integer> factories = new ArrayList<>();
            for(int actualFactoryNumber = 0; actualFactoryNumber < locationsNumber; actualFactoryNumber++)
            {
                factories.add(actualFactoryNumber);
            }

            int[] vector = new int[locationsNumber];
//            vector = getRandomlyFirstFactory();
            solution.setVector(getFirstFactory(i));
            vector = solution.getVector();
            factories.remove(solution.getVector()[0]);

            int cost;

            for(int actualLocationNumber = 1; actualLocationNumber < locationsNumber; actualLocationNumber++){

                for(int actualFactoryNumber = 0; actualFactoryNumber < factories.size(); actualFactoryNumber++){
                    vector[actualLocationNumber] = factories.get(actualFactoryNumber);
                    solution.setVector(vector);
                        cost = qualityCounter.count(solution);

                    if(actualFactoryNumber == 0 || cost < bestResult){
                        bestResult = cost;
                        numberOfBestFactory = factories.get(actualFactoryNumber);
                    }
                }

                vector[actualLocationNumber] = numberOfBestFactory;
                factories.remove(numberOfBestFactory);
            }

            solution.setVector(vector);

            int finalCost = qualityCounter.count(solution);

            if(finalCost < bestBestResult || bestBestResult == 0) {
                bestBestResult = finalCost;
                bestSolution.setVector(solution.getVector());
            }


        }
        System.out.print("BEST RESULT OF GREEDY SEARCH FOR N = " + locationsNumber + " : " + bestBestResult + "\tvector -> ");
        printVector(bestSolution.getVector());
    }




    private int[] getRandomlyFirstFactory(){
        int [] vector = new int[locationsNumber];
        Random generator = new Random();
        vector[0] = generator.nextInt(locationsNumber);

        return vector;
    }

    public void printVector(int [] vector){
        for(int i = 0; i < vector.length ; i++) {
            System.out.print(vector[i] + " ");
        }
        System.out.println();
    }

    public int[] getFirstFactory(int n){
        int [] vector = new int[locationsNumber];
        vector[0] = n;

        return vector;
    }
}
