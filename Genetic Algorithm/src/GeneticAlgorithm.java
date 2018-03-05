import java.io.FileNotFoundException;

import static java.lang.Math.abs;

public class GeneticAlgorithm {

    private final int FINAL_DIFFERENCE_VALUE = 0;



    private int populationSize;
    private int locationsNumber;
    private QualityCounter qualityCounter;
    private int percentageProbabilityOfMutation;
    private int percentageProbabilityOfCrossing;
    private int generationLimit;

    GeneticAlgorithm(int populationSize, int locationsNumber, QualityCounter qualityCounter, int percentageProbabilityOfMutation, int percentageProbabilityOfCrossing, int generationLimit){
        this.populationSize = populationSize;
        this.locationsNumber = locationsNumber;
        this.qualityCounter = qualityCounter;
        this.percentageProbabilityOfMutation = percentageProbabilityOfMutation;
        this.percentageProbabilityOfCrossing = percentageProbabilityOfCrossing;
        this.generationLimit = generationLimit;
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
        int actualResult;
        int previousResult = 0;
        int generationNumber = 1;

        Population population = new Population(populationSize, locationsNumber, qualityCounter, percentageProbabilityOfMutation, percentageProbabilityOfCrossing);
        CSV csv = new CSV("test");

        actualResult = qualityCounter.count(qualityCounter.findBestIndividual(population.actualGeneration));


        while(!isEnd3(generationNumber)){
            previousResult = actualResult;

            population.makeNextGeneration(population.actualGeneration);
            generationNumber++;

            actualResult = qualityCounter.count(qualityCounter.findBestIndividual(population.actualGeneration));

            csv.appendToFile(Integer.toString(generationNumber));
            csv.nextColumn();
            csv.appendToFile(Integer.toString(actualResult));
            csv.nextLine();
            System.out.println("ACTUAL RESULT ---> " + actualResult);
        }

        csv.saveFile();
    }

    private boolean isEnd(int actualResult, int previousResult){
        System.out.println("Jestem w metodzie isEnd");
        if(abs(previousResult - actualResult) <= FINAL_DIFFERENCE_VALUE)
            return true;
        else return false;
    }

    private boolean isEnd3(int actualGenerationNumber){
        return actualGenerationNumber > generationLimit;
    }

    private boolean isEnd2(int actualResult, int previousResult){
        System.out.println("Jestem w metodzie isEnd");
        if(actualResult == 1652)
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
