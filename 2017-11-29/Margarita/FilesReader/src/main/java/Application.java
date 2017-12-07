import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Application {

    private static final String FILE_EXTENSION = ".txt";

    public static void main(String[] args) {
        List<String> fileNames = new ArrayList<>();

        int i = 0;
        while (i < args.length) {
            if (isTextFileName(args[i]))
                fileNames.add(args[i++]);
            else {
                StringBuilder nameWithSpaces = new StringBuilder(args[i++]);
                boolean found = false;
                while (i < args.length && !found) {
                    String currentArg = args[i++];
                    found = isTextFileName(currentArg);
                    if (!found)
                        nameWithSpaces.append(currentArg);
                }
                if (found)
                    fileNames.add(nameWithSpaces.toString());
            }
        }

        try {
            printCountOfStrings(fileNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isTextFileName(String fileName) {
        return fileName.endsWith(FILE_EXTENSION);
    }

    private static void printCountOfStrings(List<String> fileNames) throws IOException {
        for (String name: fileNames) {
            int count = Files.readAllLines(Paths.get(name), StandardCharsets.UTF_8).size();
            System.out.println("Количество строк в файле " + name + " равно " + count);
        }
    }
}
