package root;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by 1 on 28.11.2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        PathReader pathReader = new PathReader();
        for (int i = 0; i < args.length; i++) {
            pathReader.linesCount(args[i]);
            pathReader.symbolsCount(args[i]);
        }
    }
}
