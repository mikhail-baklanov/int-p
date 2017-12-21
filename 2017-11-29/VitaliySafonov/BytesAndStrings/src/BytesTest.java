public class BytesTest
{
    /**
     * *
     * переводит number в 3 сс
     * @param number
     * @return
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
        for(int i=80;i>-1;i--)
            System.out.print(from10to3(i)+" ");
    }
}
