import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ArgsTest
{
    /**
     * считает количество строк в файле filename
     * @param название файла
     * @return количество строк
     */
    public static int linesCounter(String filename)
    {
        int counter = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            while (reader.readLine()!=null)
                counter++;
        }
        catch (IOException ex)
        {System.out.println(ex.getMessage());}

        return counter;

    }

    public static void main(String[] args )
    {
        for(int i=0;i<args.length;i++)
            System.out.println(linesCounter(args[i]));
    }
}
