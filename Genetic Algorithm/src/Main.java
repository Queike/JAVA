import java.io.IOException;
import java.util.ArrayList;

public class Main {

    private static final String BEGINNING_OF_TEST_DATA_PATH = "http://anjos.mgi.polymtl.ca/qaplib/data.d/";
    private static final String END_OF_TEST_DATA_PATH = ".dat";
    private static final String [] NAMES_OF_DATA_SETS = {"had12", "had14", "had16", "had18", "had20"};

    private static final int PERCENTAGE_PROBABILITY_OF_CROSSING = 70;
    private static final int PERCENTAGE_PROBABILITY_OF_MUTATION = 25;
    private static final int POPULATION_SIZE = 200;
    private static final int GENERATION_LIMIT = 1000;
    private static final int THE_SAME_RESULT_MAX_COUNTER = 800;

    private static int locationsNumber;
    private static int [][] distanceMatrix;
    private static int [][] flowMatrix;

    public static void main(String args[]) throws IOException {
//
//        runRandomSearchForAllTestSets();
//        System.out.println();
//        runGreedySearchForAllTestSets();
//        System.out.println();
//        crossingTest();
//        generatingNewGenerationTest();


        System.out.println("_____GREEDY SEARCH_____");
        runGreedySearchForAllTestSets();
        System.out.println("\n\n_____RANDOM SEARCH_____");
        runRandomSearchForAllTestSets();
        System.out.println("\n\n_____GENETIC ALGORITHM BETA_____");
        runGaAlgorithm();

    }

    public static void runGaAlgorithm() throws IOException {

        for(int i = 0; i < NAMES_OF_DATA_SETS.length; i++){
            String path = BEGINNING_OF_TEST_DATA_PATH + NAMES_OF_DATA_SETS[i] + END_OF_TEST_DATA_PATH;
            WebReader webReader = new WebReader(path);
            WebReader readData = webReader.read();

            locationsNumber = readData.getNumberN();
            distanceMatrix = readData.getMatrix1();
            flowMatrix = readData.getMatrix2();

            QualityCounter qualityCounter = new QualityCounter(locationsNumber, distanceMatrix, flowMatrix);
            Population population = new Population(POPULATION_SIZE, locationsNumber, qualityCounter, PERCENTAGE_PROBABILITY_OF_MUTATION, PERCENTAGE_PROBABILITY_OF_CROSSING);

            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(POPULATION_SIZE, locationsNumber, qualityCounter, PERCENTAGE_PROBABILITY_OF_MUTATION, PERCENTAGE_PROBABILITY_OF_CROSSING, GENERATION_LIMIT, THE_SAME_RESULT_MAX_COUNTER, NAMES_OF_DATA_SETS[i]);
            geneticAlgorithm.run();
        }

    }


    private static void runGaForAllTestSets() throws IOException {
        for(int i = 0; i < NAMES_OF_DATA_SETS.length; i++){
            String path = BEGINNING_OF_TEST_DATA_PATH + NAMES_OF_DATA_SETS[i] + END_OF_TEST_DATA_PATH;
            WebReader webReader = new WebReader(path);
            WebReader readData = webReader.read();
            QualityCounter qualityCounter = new QualityCounter(locationsNumber, distanceMatrix, flowMatrix);

            locationsNumber = readData.getNumberN();
            distanceMatrix = readData.getMatrix1();
            flowMatrix = readData.getMatrix2();

            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(POPULATION_SIZE, locationsNumber, qualityCounter, PERCENTAGE_PROBABILITY_OF_MUTATION, PERCENTAGE_PROBABILITY_OF_CROSSING, GENERATION_LIMIT, THE_SAME_RESULT_MAX_COUNTER, NAMES_OF_DATA_SETS[i]);
            geneticAlgorithm.run();
        }
    }

    private static void runRandomSearchForAllTestSets() throws IOException {

        for(int i = 0; i < NAMES_OF_DATA_SETS.length; i++){
            String path = BEGINNING_OF_TEST_DATA_PATH + NAMES_OF_DATA_SETS[i] + END_OF_TEST_DATA_PATH;
            WebReader webReader = new WebReader(path);
            WebReader readData = webReader.read();

            locationsNumber = readData.getNumberN();
            distanceMatrix = readData.getMatrix1();
            flowMatrix = readData.getMatrix2();

            RandomSearch randomSearch = new RandomSearch(locationsNumber, distanceMatrix, flowMatrix);
            randomSearch.search();
        }
    }

    private static void runGreedySearchForAllTestSets() throws IOException {

        for(int i = 0; i < NAMES_OF_DATA_SETS.length; i++){
            String path = BEGINNING_OF_TEST_DATA_PATH + NAMES_OF_DATA_SETS[i] + END_OF_TEST_DATA_PATH;
            WebReader webReader = new WebReader(path);
            WebReader readData = webReader.read();

            locationsNumber = readData.getNumberN();
            distanceMatrix = readData.getMatrix1();
            flowMatrix = readData.getMatrix2();

            GreedySearch greedySearch = new GreedySearch(locationsNumber, distanceMatrix, flowMatrix);
            greedySearch.search();
        }
    }

    public static void printVector(int [] vector){
        for(int i = 0; i < vector.length ; i++) {
            System.out.print(vector[i] + " ");
        }
        System.out.println();
    }

//    public static void generatingNewGenerationTest() throws IOException{
//        String path = BEGINNING_OF_TEST_DATA_PATH + NAMES_OF_DATA_SETS[0] + END_OF_TEST_DATA_PATH;
//        WebReader webReader = new WebReader(path);
//        WebReader readData = webReader.read();
//
//        locationsNumber = readData.getNumberN();
//        distanceMatrix = readData.getMatrix1();
//        flowMatrix = readData.getMatrix2();
//
//        QualityCounter qualityCounter = new QualityCounter(locationsNumber, distanceMatrix, flowMatrix);
//        Population population = new Population(100, locationsNumber, qualityCounter, distanceMatrix, flowMatrix);
//
//        ArrayList<int[]> newGeneration = new ArrayList<>();
//        newGeneration = population.createNewGenerationWithRouletteAndCross();
//
//        for(int[] vector : newGeneration){
//            printVector(vector);
//        }
//    }

//    public static void crossingTest() throws IOException {
//        String path = BEGINNING_OF_TEST_DATA_PATH + NAMES_OF_DATA_SETS[0] + END_OF_TEST_DATA_PATH;
//        WebReader webReader = new WebReader(path);
//        WebReader readData = webReader.read();
//
//        locationsNumber = readData.getNumberN();
//        distanceMatrix = readData.getMatrix1();
//        flowMatrix = readData.getMatrix2();
//
//        QualityCounter qualityCounter = new QualityCounter(locationsNumber, distanceMatrix, flowMatrix);
//        Population population = new Population(100, locationsNumber, qualityCounter, distanceMatrix, flowMatrix);
//
//        System.out.print("Wektor pierwszy : ");
//        population.printVector(population.actualGeneration[0]);
//        System.out.println();
//        System.out.print("Wektor drugi : ");
//        population.printVector(population.actualGeneration[1]);
//        System.out.println();
//
//        ArrayList<ArrayList> crossedChildren;
//        crossedChildren = population.cross(population.actualGeneration[0], population.actualGeneration[1]);
//
//        System.out.println("Dziecko pierwsze : " + crossedChildren.get(0));
//        System.out.println("Dziecko drugie : " + crossedChildren.get(1));
//
//    }


}
