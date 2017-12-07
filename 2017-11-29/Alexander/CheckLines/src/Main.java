import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        for(String item: args) {
            File file = new File(item);
            System.out.println("File: " + item);
            try (Scanner input = new Scanner(file)) {
                int count = 0;
                while(input.hasNextLine()){
                    count++;
                    input.nextLine();
                }
                input.close();
                System.out.println("Lines: " + count);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
