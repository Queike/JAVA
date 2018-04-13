import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    private static final String FILE_NOT_FOUND_INFORMATION = "File not found!";

    private ArrayList<String> readWords;
    private Scanner scanner;

    FileReader(String filePath)
    {
        scanner = null;
        readWords = new ArrayList<>();
        try
        {
            scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println(FILE_NOT_FOUND_INFORMATION);
        }
    }

    public ArrayList<String> loadWordsFromFileToArrayList()
    {
        assert (scanner != null);
        while (scanner.hasNext()) {
            readWords.add(scanner.next());
        }

        return readWords;
    }
}
