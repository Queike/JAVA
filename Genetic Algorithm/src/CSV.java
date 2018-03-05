import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CSV {

    private StringBuilder sb;
    private PrintWriter pw;

    public CSV(String fileName) throws FileNotFoundException {
        sb = new StringBuilder();
        pw = new PrintWriter(new File("test" + ".csv"));
    }

    public void appendToFile(String string){
        sb.append(string);
    }

    public void nextColumn(){
        sb.append(';');
    }

    public void nextLine(){
        sb.append('\n');
    }

    public void saveFile(){
        pw.write(sb.toString());
        pw.close();
    }
}
