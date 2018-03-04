import static java.lang.Math.abs;

public class GeneticAlgorithm {

    private final int FINAL_DIFFERENCE_VALUE = 2;

    private int populationSize;
    private int locationsNumber;
    private static int [][] distanceMatrix;
    private static int [][] flowMatrix;

    public GeneticAlgorithm(int populationSize, int locationsNumber, int[][] distanceMatrix, int[][] flowMatrix){
        this.populationSize = populationSize;
        this.locationsNumber = locationsNumber;
        this.distanceMatrix = distanceMatrix;
        this.flowMatrix = flowMatrix;
    }

    public void run(){

        int actualResult;
        int previousResult = 0;

        QualityCounter qualityCounter = new QualityCounter(locationsNumber, distanceMatrix, flowMatrix);
        Population population = new Population(populationSize, locationsNumber, qualityCounter);
        System.out.println("Best generation --> " + qualityCounter.count(qualityCounter.findBestIndividual(population.actualGeneration)));
        actualResult = qualityCounter.count(qualityCounter.findBestIndividual(population.actualGeneration));

        System.out.println("To zwroci metoda isEnd " + isEnd(actualResult, previousResult));

        while(!isEnd(actualResult, previousResult)){
            previousResult = actualResult;

            population.cross(population.selectIndividualsWithRoulette());
            //printPopulation(population.cross(population.selectIndividualsWithRoulette()));
            population.swapMutation(population.actualGeneration);

            actualResult = qualityCounter.count(qualityCounter.findBestIndividual(population.actualGeneration));

            System.out.println("ACTUAL RESULT ---> " + actualResult);
        }




    }

    private boolean isEnd(int actualResult, int previousResult){
        System.out.println("Jestem w metodzie isEnd");
        if(abs(previousResult - actualResult) <= FINAL_DIFFERENCE_VALUE)
            return true;
        else return false;
    }

    public void printVector(int [] vector){
        for(int i = 0; i < vector.length ; i++) {
            System.out.print(vector[i] + " ");
        }
        System.out.println();
    }

    public void printPopulation(int[][] population){
        System.out.println("__THE POPULATION__");
        for(int i = 0; i < populationSize; i++){
            printVector(population[i]);
        }
    }
}
