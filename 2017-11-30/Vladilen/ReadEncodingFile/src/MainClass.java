import java.io.*;
import java.nio.charset.Charset;

public class MainClass {
    public static void main(String[] args) {
        Charset[] charsets = new Charset[]{
                Charset.forName("UTF-8"),           //123 abcde абвгд .,!
                Charset.forName("UTF-16"),          //ㄲ㌠慢捤攠킰킱킲킳킴‮Ⱑ
                Charset.forName("cp866")            //123 abcde ╨░╨▒╨▓╨│╨┤ .,!
        };
        int[][] counts = getCharCount(args, charsets);
        for (int i = 0; i < args.length; i++)
            for (int j = 0; j < charsets.length; j++)
                System.out.println("[" + charsets[j].displayName() + ": " + counts[i][j] + "] " + args[i]);
        for (int i = 0; i < charsets.length; i++) //testPtint
            PrintFile(args[0], charsets[i]);
    }

    private static void PrintFile(String fileName, Charset Encoding) {
        try (BufferedReader BR = new BufferedReader
                (new InputStreamReader
                        (new FileInputStream(fileName), Encoding))) {
            while (BR.ready())
                System.out.println(BR.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[][] getCharCount(String[] args, Charset[] charsets) {
        int totalCount[][] = new int[args.length][charsets.length];
        for (int i = 0; i < args.length; i++) {
            char[] array = new char[100];
            for (int set = 0; set < charsets.length; set++) {
                try (BufferedReader BR = new BufferedReader
                        (new InputStreamReader
                                (new FileInputStream(args[i]), charsets[set]))) {
                    int count;
                    while ((count = BR.read(array)) != -1)
                        totalCount[i][set] += count;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return totalCount;
    }
}