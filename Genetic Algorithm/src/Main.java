import java.io.IOException;

public class Main {

    private static final String BEGINNING_OF_TEST_DATA_PATH = "http://anjos.mgi.polymtl.ca/qaplib/data.d/";
    private static final String END_OF_TEST_DATA_PATH = ".dat";
    private static final String [] NAMES_OF_DATA_SETS = {"had12", "had14", "had16", "had18", "had20"};

    private static int N;
    private static int [][] distanceMatrix;
    private static int [][] flowMatrix;

    public static void main(String args[]) throws IOException {

        runRandomSearchForAllTestSets();
        System.out.println();
        runGreedySearchForAllTestSets();

    }


    private static void runRandomSearchForAllTestSets() throws IOException {

        for(int i = 0; i < NAMES_OF_DATA_SETS.length; i++){
            String path = BEGINNING_OF_TEST_DATA_PATH + NAMES_OF_DATA_SETS[i] + END_OF_TEST_DATA_PATH;
            WebReader webReader = new WebReader(path);
            WebReader readData = webReader.read();

            N = readData.getNumberN();
            distanceMatrix = readData.getMatrix1();
            flowMatrix = readData.getMatrix2();

            RandomSearch randomSearch = new RandomSearch(N, distanceMatrix, flowMatrix);
            randomSearch.search();
        }

    }


    private static void runGreedySearchForAllTestSets() throws IOException {

        for(int i = 0; i < NAMES_OF_DATA_SETS.length; i++){
            String path = BEGINNING_OF_TEST_DATA_PATH + NAMES_OF_DATA_SETS[i] + END_OF_TEST_DATA_PATH;
            WebReader webReader = new WebReader(path);
            WebReader readData = webReader.read();

            N = readData.getNumberN();
            distanceMatrix = readData.getMatrix1();
            flowMatrix = readData.getMatrix2();

            GreedySearch greedySearch = new GreedySearch(N, distanceMatrix, flowMatrix);
            greedySearch.search();
        }

    }

}
