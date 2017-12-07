package root;

import java.util.ArrayList;

/**
 * Created by 1 on 30.11.2017.
 */
public class Main {
    public static void main(String[] args) {

        int n = 80;
        int dim = 3;
        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            if (i<dim) {
                arr.add(i,i);
            } else {
                int value = i;

                String result = "";

                while ( value > 0 ) {

                    int p = value / dim;
                    int q = value % dim;

                    result = q + result;

                    value = p;
                }
                arr.add(i,Integer.parseInt(result));
            }
        }

        for (int j = n; j >= 0; j--) {
            System.out.print(arr.get(j) + " ");
        }
    }
}
