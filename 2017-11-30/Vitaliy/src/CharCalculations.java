import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class CharCalculations
{/**
 *Считывает посимвольно файл
 * и выдает их количество
 * */

    public static void main(String[] args)
    {
        try(FileReader reader = new FileReader("chars.txt"))
        {
            int c;
            int counter=0;
            while((c=reader.read())!=-1)
                counter++;
            System.out.println("counter="+counter);
//73135 в ANSI
//73136 в UTF 8
//146270 в UTF 16
            reader.close();
        }
        catch (IOException e)
        {System.out.println(e.getMessage());}
    }
}
