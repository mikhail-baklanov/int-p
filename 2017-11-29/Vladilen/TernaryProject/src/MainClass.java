public class MainClass {
    public static void main(String[] args) {
        for (int i = 0; i < 81; i++) {
            PrintTernaryInteger(i);
        }
    }

    private static void PrintTernaryInteger(int toPrint){
        int toPrintCopy = toPrint;
        StringBuilder SB = new StringBuilder();
        do {
            SB.append(""+(toPrint % 3));
            toPrint /= 3;
        }
        while (toPrint!=0);
        while (SB.length()<4)
            SB.append("0");
        //SB.reverse(); //Сказали убрать
        System.out.println(toPrintCopy + ": " + SB.toString());
    }
}