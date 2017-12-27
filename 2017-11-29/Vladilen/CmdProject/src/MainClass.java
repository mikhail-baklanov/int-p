import java.io.*;
import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        String[] ss = getFileNamesList(args);
        int[] counts = getFileLineCounts(ss);
        for (int i = 0; i < ss.length; i++)
            System.out.println("[" + counts[i] + "] " + ss[i]);
    }

    private static int[] getFileLineCounts(String[] args) {
        int[] counts = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            byte[] array = new byte[100];
            try (FileInputStream inputStream = new FileInputStream(args[i])){
                int count;
                while ((count = inputStream.read(array)) != -1)
                    for (int j = 0; j < count; j++) {
                        if (j + 1 == count && inputStream.available() == 0 || array[j] == '\n')
                            counts[i]++;
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return counts;
    }

    private static String[] getFileNamesList(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        for (String arg : args) {
            File f = new File(arg);
            if (f.exists() && f.canRead())
                list.add(arg);
        }
        String[] strings = new String[list.size()];
        list.toArray(strings);
        return strings;
    }
}