import java.io.IOException;

public class Main {

    private static final String TEST_DATA_PATH = "http://anjos.mgi.polymtl.ca/qaplib/data.d/had18.dat";
    private static final String [] NAMES_OF_DATA_SETS = {"had12", "had14", "had16", "had18", "had20"};

    private static int N;
    private static int [][] distanceMatrix;
    private static int [][] flowMatrix;

    public static void main(String args[]) throws IOException {
        WebReader webReader = new WebReader(TEST_DATA_PATH);
        WebReader readData = webReader.read();

        N = readData.getNumberN();
        distanceMatrix = readData.getMatrix1();
        flowMatrix = readData.getMatrix2();

        RandomSearch randomSearch = new RandomSearch(N, distanceMatrix, flowMatrix);
        randomSearch.search();

    }

//    public static int countQuality(int [] array){
//        int result = 0;
//        for (int i=0; i<N; i++){
//            for (int j=0; j<N; j++){
//                result += (distanceMatrix[i][j]*flowMatrix[array[i]][array[j]]);
//            }
//        }
//        return result;
//    }

}
