import java.util.*;

public class Main {

    private static final String FILE_PATH = "slowa.txt";
    private static final int MAXIMUM_NUMBER_OF_WORDS_TO_PRINT = 5;
    private static final String REQUEST_TO_ENTER_SERIES_OF_NUMBERS = "Wprowadź ciąg liczb: ";
    private static final String NUMBER_OF_FOUND_TEXTONYMS_HEADER = "\nLiczba znalezionych tekstonimów : ";
    private static final String EXAMPLES_OF_FOUND_TEXTONYMS_HEADER = "Przykłady znalezionych tekstonimów (max. 5) : ";

    public static void main(String[] args) {

        ArrayList<String> dictionary = readFileWithPolishWords();
        String wordAsNumbers = readNumbersFromUser();

        Encoder encoder = new Encoder(dictionary);
        HashMap<String, String> encodedWords = encoder.getEncodedDictionary();

        ArrayList<String> valuesSet = new ArrayList<>(encodedWords.values());

        System.out.println(NUMBER_OF_FOUND_TEXTONYMS_HEADER + countFoundWords(valuesSet, wordAsNumbers));
        ArrayList<String> matchedPolishWords = getKeysByValue(encodedWords, wordAsNumbers);

        System.out.print(EXAMPLES_OF_FOUND_TEXTONYMS_HEADER);
        if(matchedPolishWords.size() > MAXIMUM_NUMBER_OF_WORDS_TO_PRINT)
            System.out.println(matchedPolishWords.subList(0, MAXIMUM_NUMBER_OF_WORDS_TO_PRINT).toString());
        else
            System.out.println(matchedPolishWords.toString());
    }

    private static String readNumbersFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(REQUEST_TO_ENTER_SERIES_OF_NUMBERS);
        return scanner.nextLine();
    }

    private static int countFoundWords(ArrayList<String> valuesSet, String wordToCount){
        int result = 0;

        for (String word : valuesSet) {
            if(word.equals(wordToCount))
                result++;
        }
        return result;
    }

    private static <T, E> ArrayList<T> getKeysByValue(Map<T, E> map, E value){
        ArrayList<T> keys = new ArrayList<>();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    private static ArrayList<String> readFileWithPolishWords(){
        FileReader fileReader = new FileReader(FILE_PATH);
        return fileReader.loadWordsFromFileToArrayList();
    }
}
