import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SecondApplication {

    private static final byte END_OF_LINE = '\n';
    private static final char QUOTE_SYMBOL = '"';

    public static void main(String[] args) {
        List<String> fileNames = new ArrayList<>();

        int i = 0;
        while (i < args.length) {
            if (!isNameWithSpaces(args[i]))
                fileNames.add(args[i++]);
            else {
                String fileName = args[i].substring(1, args[i].length() - 2);
                fileNames.add(fileName);
            }
        }

        try {
            printCountOfStrings(fileNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isNameWithSpaces(String fileName) {
        return fileName.charAt(0) == QUOTE_SYMBOL &&
                fileName.charAt(fileName.length() - 1) == QUOTE_SYMBOL;
    }

    private static void printCountOfStrings(List<String> fileNames) throws IOException {
        for (String name: fileNames) {
            int currentByte, countOfLines = 0;
            byte[] arr = Files.readAllBytes(Paths.get(name));
            ByteArrayInputStream inputStream = new ByteArrayInputStream(arr);
            while ((currentByte = inputStream.read()) != -1) {
                if (currentByte == END_OF_LINE)
                    countOfLines++;
            }
            System.out.println("Количество строк в файле " + name + " равно " + countOfLines);
        }
    }
}
