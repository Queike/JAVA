import java.io.FileNotFoundException;

import static java.lang.Math.abs;

public class GeneticAlgorithm {

    private final int FINAL_DIFFERENCE_VALUE = 0;
    private final int FINAL_TEST_LOOPS = 1;

    private int populationSize;
    private int locationsNumber;
    private QualityCounter qualityCounter;
    private int percentageProbabilityOfMutation;
    private int percentageProbabilityOfCrossing;
    private int generationLimit;
    private int theSameResultMaxCounter;
    private String dataSetName;
    long searchingTime;

    GeneticAlgorithm(int populationSize, int locationsNumber, QualityCounter qualityCounter, int percentageProbabilityOfMutation, int percentageProbabilityOfCrossing, int generationLimit, int theSameResultMaxCounter, String dataSetName){
        this.populationSize = populationSize;
        this.locationsNumber = locationsNumber;
        this.qualityCounter = qualityCounter;
        this.percentageProbabilityOfMutation = percentageProbabilityOfMutation;
        this.percentageProbabilityOfCrossing = percentageProbabilityOfCrossing;
        this.generationLimit = generationLimit;
        this.theSameResultMaxCounter = theSameResultMaxCounter;
        this.dataSetName = dataSetName;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getPercentageProbabilityOfMutation(){
        return percentageProbabilityOfMutation;
    }

    public int getPercentageProbabilityOfCrossing(){
        return percentageProbabilityOfCrossing;
    }

    public int getGenerationLimit(){
        return generationLimit;
    }

    public void run() throws FileNotFoundException {

        int totalCostSum = 0;
        int sumCount = 0;

        for(int actualTestLoop = 0; actualTestLoop < FINAL_TEST_LOOPS; actualTestLoop++){

            int bestResult;
            int worstResult;
            int previousResult = 0;
            int generationNumber = 1;
            int theSameResultCounter = 0;
            long startTime = System.currentTimeMillis();

            Population population = new Population(populationSize, locationsNumber, qualityCounter, percentageProbabilityOfMutation, percentageProbabilityOfCrossing);
            CSV csv = new CSV(actualTestLoop + dataSetName);

            bestResult = qualityCounter.count(qualityCounter.findBestSolution(population.actualGeneration));

            csv.appendToFile(Integer.toString(generationNumber));
            csv.nextColumn();
            csv.appendToFile(Integer.toString(bestResult));
            csv.nextLine();

            while(!isEnd3(generationNumber, theSameResultCounter)){
//           while(!isEnd3(generationNumber, theSameResultCounter)){
                previousResult = bestResult;

                // no special selection
//                population.makeNextGeneration(population.actualGeneration);

                // selection with roulette
                population.makeNextGenerationWithRoulette(population.actualGeneration);

                // selection with tournament
//                population.makeNextGenerationWithTournament(population.actualGeneration, 10);

                generationNumber++;

                bestResult = qualityCounter.count(qualityCounter.findBestSolution(population.actualGeneration));
                worstResult = qualityCounter.count(qualityCounter.findBestSolution(population.actualGeneration));
                totalCostSum += bestResult;
                sumCount++;

                if(bestResult == previousResult)
                    theSameResultCounter++;
                else
                    theSameResultCounter = 0;

                csv.appendToFile(Integer.toString(generationNumber));
                csv.nextColumn();
                csv.appendToFile(Integer.toString(bestResult));
                csv.nextLine();
//                System.out.println("BEST RESULT ---> " + bestResult);
//                System.out.println("WORST RESULT --> " + worstResult);
            }

            long finishTime = System.currentTimeMillis();
            searchingTime = finishTime - startTime;
            csv.nextColumn();
            csv.nextColumn();
            csv.nextColumn();
            csv.appendToFile(Long.toString(searchingTime));
            csv.saveFile();
        }

        int totalAverage = totalCostSum / sumCount;
        System.out.println("TOTAL AVERAGE FOR " + locationsNumber + " locations number : " + totalAverage);

    }

    private boolean isEnd(int actualResult, int previousResult){
        if(abs(previousResult - actualResult) <= FINAL_DIFFERENCE_VALUE)
            return true;
        else return false;
    }

    private boolean isEnd3(int actualGenerationNumber, int theSameResultCounter){
        return actualGenerationNumber > generationLimit || theSameResultCounter > theSameResultMaxCounter;
    }

    private boolean isEnd2(int actualResult, int previousResult){
        if(actualResult == 1652)
            return true;
        else return false;
    }

    public void printVector(int [] vector){
        for(int indexOfFactory = 0; indexOfFactory < vector.length ; indexOfFactory++) {
            System.out.print(vector[indexOfFactory] + " ");
        }
        System.out.println();
    }

    public void printPopulation(int[][] population){
        for(int indexOfVector = 0; indexOfVector < populationSize; indexOfVector++){
            printVector(population[indexOfVector]);
        }
    }


//    public void run(){
//
//        int actualResult;
//        int previousResult = 0;
//
//        QualityCounter qualityCounter = new QualityCounter(locationsNumber, distanceMatrix, flowMatrix);
//        Population population = new Population(populationSize, locationsNumber, qualityCounter, distanceMatrix, flowMatrix);
//        System.out.println("Best generation --> " + qualityCounter.count(qualityCounter.findBestIndividual(population.actualGeneration)));
//        actualResult = qualityCounter.count(qualityCounter.findBestIndividual(population.actualGeneration));
//
//        System.out.println("To zwroci metoda isEnd " + isEnd(actualResult, previousResult));
//
//        while(!isEnd(actualResult, previousResult)){
//            previousResult = actualResult;
//
//            population.cross(population.selectIndividualsWithRoulette());
//            //printPopulation(population.cross(population.selectIndividualsWithRoulette()));
//            population.swapMutation(population.actualGeneration);
//
//            actualResult = qualityCounter.count(qualityCounter.findBestIndividual(population.actualGeneration));
//
//            System.out.println("ACTUAL RESULT ---> " + actualResult);
//        }
//
//
//
//
//    }

//    public void run(){
//        int actualResult;
//        int previousResult = 0;
//
//        QualityCounter qualityCounter = new QualityCounter(locationsNumber, distanceMatrix, flowMatrix);
//        Population population = new Population(populationSize, locationsNumber, qualityCounter, distanceMatrix, flowMatrix);
//
//
//        System.out.println("Best generation --> " + qualityCounter.count(qualityCounter.findBestIndividual(population.actualGeneration)));
//        actualResult = qualityCounter.count(qualityCounter.findBestIndividual(population.actualGeneration));
//
//        System.out.println("To zwroci metoda isEnd " + isEnd(actualResult, previousResult));
//
//
//        while(!isEnd2(actualResult, previousResult)){
//            previousResult = actualResult;
//
//            population.mutate(population.createNewGenerationWithBestAndCross());
//
//            actualResult = qualityCounter.count(qualityCounter.findBestIndividual(population.actualGeneration));
//
//            System.out.println("ACTUAL RESULT ---> " + actualResult);
//        }
//    }


}
