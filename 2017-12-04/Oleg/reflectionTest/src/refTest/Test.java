package refTest;

/**
 * Тестовый класс с одним методом и одной переменной, принадлежащей классу.
 */
public class Test {
    private static int field=0;

     public void method() {
       System.out.println(field++);
    }
}