import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Experience {

    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String ENG_FILE_NAME = USER_DIR + "/eng.txt";
    private static final String RUS_FILE_NAME = USER_DIR + "/rus.txt";

    public static void main(String[] args) {
        try {
            System.out.println("For English symbols:");
            printSolve(ENG_FILE_NAME, StandardCharsets.UTF_8);
            printSolve(ENG_FILE_NAME, StandardCharsets.UTF_16);
            System.out.println();

            System.out.println("For Russian symbols:");
            printSolve(RUS_FILE_NAME, StandardCharsets.UTF_8);
            printSolve(RUS_FILE_NAME, StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getCountOfSymbols(String fileName, Charset charset) {
        int result = 0;
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), charset))) {
            String line;
            while( (line = bufferedReader.readLine()) != null) {
                result += line.length();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void printSolve(String fileName, Charset charset) throws IOException {
        System.out.println(charset + ": " + getCountOfSymbols(fileName, charset) + " symbols.");
    }
}
