package root;

import java.io.*;

/**
 * Created by 1 on 30.11.2017.
 */
public class PathReader {

    public void linesCount(String path) {
        int count=0;
        try
        {
            File myFile = new File(path);
            FileReader fileReader = new FileReader(myFile);
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader);

            while (lineNumberReader.readLine() != null){
                count++;
            }

            System.out.println("В файле " + path + "  " + count + " строк");

            if(fileReader!=null)
                fileReader.close();
            lineNumberReader.close();
        } catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void symbolsCount(String path) {
        int count = 0;
        try {
            File myFile = new File(path);
            InputStreamReader is = new InputStreamReader(new FileInputStream(myFile), "Cp866");

            while(is.read() != -1){
                    count ++;
            }

            System.out.println("В файле " + path + "  " + count + " символов");

            is.close();

        } catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
