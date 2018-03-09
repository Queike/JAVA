import java.util.ArrayList;
import java.util.Random;

public class RandomSearch {

    private final int NUMBER_OF_LOOPS = 100000;

    private static int locationsNumber;
    private static int[][] distanceMatrix;
    private static int[][] flowMatrix;
    private int bestResult;

    RandomSearch(int locationsNumber, int[][] distanceMatrix, int[][] flowMatrix){
        RandomSearch.locationsNumber = locationsNumber;
        RandomSearch.distanceMatrix = distanceMatrix;
        RandomSearch.flowMatrix = flowMatrix;
    }

    public void search(){
        QualityCounter qualityCounter = new QualityCounter(locationsNumber, distanceMatrix, flowMatrix);

        int [] vector;
        int [] bestVector = new int[locationsNumber];
        int quality;
        Solution solution = new Solution();

        for(int i = 0; i < NUMBER_OF_LOOPS; i++){
            vector = generateVector(locationsNumber);

            solution.setVector(vector);
            if(solution.getCost() == -1)
                quality = qualityCounter.count(solution);
            else
                quality = solution.getCost();


            if(quality < bestResult || bestResult == 0){
                bestResult = quality;
                bestVector = vector;
            }
        }

        System.out.print("BEST RESULT OF RANDOM SEARCH FOR N = " + locationsNumber + " : " + bestResult + "\tvector -> ");
        printVector(bestVector);
    }

    private int[] generateVector(int locationsNumber){

        ArrayList<Integer> factories = new ArrayList<>();
        Random generator = new Random();
        int generatedNumber;

        for(int actualFactoryNumber = 0; actualFactoryNumber < locationsNumber; actualFactoryNumber++)
        {
            factories.add(actualFactoryNumber);
        }

        int [] vector = new int[locationsNumber];

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
