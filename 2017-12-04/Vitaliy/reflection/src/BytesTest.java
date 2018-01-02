public class BytesTest
{
    /**
     * *
     * переводит число number в 3 сс
     * @param
     * @return возвращает
     */
    public static void from10to3()

    {
        for(int number1=0;number1<=80;number1++)
        {

            int number=number1;
            int value = number;

            String result = "";

            if (number == 0) result="0" ;
            while (number >= 1)
            {
                number /= 3;

                int p = value / 3;
                int q = value % 3;

                result = q + result;

                value = p;
            }
            System.out.println("result="+result);
        }



    }


}
