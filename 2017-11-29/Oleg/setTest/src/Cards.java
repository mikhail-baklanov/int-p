import java.util.ArrayList;

public class Cards {

    public static ArrayList<Integer[][]> Cards() {

        ArrayList<Integer[][]> cards=new ArrayList<>();
        int line=0;
        int column1, column2, column3, column4;

        for (int i=0;i<81;i++) {
            Integer [][] card = new Integer[4][3];
            for (int l=0;l<4;l++) {
                for (int c=0;c<3;c++)
                    card[l][c]=0;
            }
            column4=i%3;
            column3=(int)Math.floor((i%9)/3);
            column2=(int)Math.floor((i%27)/9);
            column1=(int)Math.floor(i/27);
            card[line++][column1]=1;
            card[line++][column2]=1;
            card[line++][column3]=1;
            card[line][column4]=1;
            line=0;
            cards.add(card);
        }

        return cards;
    }
}
