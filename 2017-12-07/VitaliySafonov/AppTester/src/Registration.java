import java.util.ArrayList;
import java.util.List;

public class Registration
{

    private static List<SuppressionChecker> list = new ArrayList<>();

    private Registration() {};
    public static void registration(SuppressionChecker obj)
    {
        list.add(obj);
    }

    public static List<SuppressionChecker> getList()
    {
        return list;
    }

    public static void main(String[] args)
    {
        FindingFilesWithoutRegExp t1=new FindingFilesWithoutRegExp();

        for(int i=0;i<list.size();i++)
        {
            long timeStart = System.currentTimeMillis() ;

            int n=1;


            for(String e:list.get(i).parseSuppression("supp.xml"))
            {
                System.out.println();
                System.out.println("------------------------------------------------------");
                System.out.println("Deleted file №"+(n++)+" : ");
                System.out.print(e);
            }

            long finishTime =System.currentTimeMillis() ;
            System.out.println();
            System.out.println("------------------------------------------------------");
            System.out.println("Время работы программы "+list.get(i).getDeveloperName()+" = "+(finishTime-timeStart)+"ms");
        }

    }

}
