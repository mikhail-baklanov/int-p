public class BytesTest
{
    /**
     * *
     * переводит число number в 3 сс
     * @param number
     * @return возвращает
     */
    public static int from10to3(int number)

    {

        int value = number;

        String result = "";

        if(number==0) return 0;
        while (number >= 1)
        {
            number/=3;

            int p = value / 3;
            int q = value % 3;

            result = q + result;

            value = p;
        }
        return Integer.parseInt( result );
    }

    public static void main(String[] args)
    {
        for(int i=0;i<=80;i++)
            System.out.print(i+"="+from10to3(i)+" ");
    }
}
