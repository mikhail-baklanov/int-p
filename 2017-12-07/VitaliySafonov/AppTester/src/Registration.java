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

            list.get(i).parseSuppression("supp.xml");

            long finishTime =System.currentTimeMillis() ;
            System.out.println("------------------------------------------------------");
            System.out.println("Время работы программы "+list.get(i).getDeveloperName()+" = "+(finishTime-timeStart)+"ms");
        }

    }

}
