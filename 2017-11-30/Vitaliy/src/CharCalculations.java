import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class CharCalculations
{/**
 *Считывает посимвольно файл
 * и выдает их количество
 * */

    public static int CharCalculator(String filename)
    {
    	int counter=0;

            try(FileReader reader = new FileReader(filename))
            {
                int c;
                while((c=reader.read())!=-1)
                    counter++;
                

                reader.close();
            }
            catch (IOException e)
            {System.out.println(e.getMessage());}

            return counter;

    }


    public static void main(String[] args)
    {
        System.out.println("Chars in UNICODE file="+CharCalculator("TableTextServiceDaYiInUnicode.txt"));
        System.out.println("Chars in ANSI file="+CharCalculator("TableTextServiceDaYiInAnsi.txt"));
    }

}
