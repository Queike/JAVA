import java.net.URL;
import java.io.*;
import java.util.Arrays;
import java.util.regex.*;


public class WebReader {

    private final int LINES_TO_FIRST_MATRIX = 2;
    private final int LINES_BETWEEN_MATRIXES = 1;
    private final int THE_BEGINNING_OF_THE_FIRST_MATRIX = LINES_TO_FIRST_MATRIX - 1;
//    private final int THE_BEGINNING_OF_THE_SECOND_MATRIX = ;


    private String path;
    private StringBuilder stringBuilder;
    private int [][] matrix1;
    private int [][] matrix2;
    private int numberN;


    public WebReader(String path){
        this.path = path;
    }

    public WebReader(int numberN, int[][] matrix1, int[][] matrix2){
        this.numberN = numberN;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
    }

    public int getNumberN(){
        return numberN;
    }

    public int[][] getMatrix1(){
        return matrix1;
    }

    public int[][] getMatrix2(){
        return matrix2;
    }

    public WebReader read() throws IOException {

        boolean firstLineKnown = false;
        boolean matrixInitialized = false;

        URL oracle = new URL(path);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        stringBuilder = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null){
            stringBuilder.append(inputLine + "\n");
        }
        in.close();

        BufferedReader inn = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        int loopNumber = 0;

        while ((inputLine = inn.readLine()) != null){
            if(!firstLineKnown) {
                numberN = getFirstNumber();
                firstLineKnown = true;
            }
            if(!matrixInitialized){
                matrix1 = new int[numberN][numberN];
                matrix2 = new int[numberN][numberN];
                matrixInitialized = true;
            }

            String line = regexChecker("\\d+", inputLine);

            if(THE_BEGINNING_OF_THE_FIRST_MATRIX < loopNumber && loopNumber <= THE_BEGINNING_OF_THE_FIRST_MATRIX + numberN)
            {
                matrix1[loopNumber-2] = Arrays.stream(line.split(","))
                        .map(String::trim).mapToInt(Integer::parseInt).toArray();
            }
            else if(LINES_TO_FIRST_MATRIX + LINES_BETWEEN_MATRIXES - 1 + numberN < loopNumber && loopNumber < LINES_TO_FIRST_MATRIX + LINES_BETWEEN_MATRIXES + 2*numberN){
                matrix2[loopNumber-numberN-3] = Arrays.stream(line.split(","))
                        .map(String::trim).mapToInt(Integer::parseInt).toArray();
            }
            loopNumber++;
        }

        in.close();

        WebReader webReader = new WebReader(numberN, matrix1, matrix2);
        return webReader;
    }

    public static String regexChecker(String theRegex, String str2Check){

        String result = "";

        Pattern checkRegex = Pattern.compile(theRegex);

        Matcher regexMatcher = checkRegex.matcher(str2Check);

        while(regexMatcher.find()){
            if(regexMatcher.group().length() != 0){
                result += regexMatcher.group().trim() + ",";
            }
        }

        return result;
    }

    public int getFirstNumber(){

        String [] lines = stringBuilder.toString().split("\\n");
        String line = lines[0].replaceAll("\\s+","");
        int firstNumber = Integer.parseInt(line);

        return firstNumber;
    }

    public void removeSubstringFromStringBuilder(String substring){
        int i = stringBuilder.indexOf(substring);
        if (i != -1) {
            stringBuilder.delete(i, i + substring.length());
        }
        System.out.println(stringBuilder);
    }


    public void printMatrix(int [][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

}
