public class Main {

    public static void main(String[] args) {
        for (int t = 0; t < 81; t++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(Cards.Cards().get(t)[i][j]);
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
