package root;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 1 on 05.12.2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ClassFinder classFinder = new ClassFinder();
        classFinder.getClassesForPackage(Main.class.getPackage());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите полное имя класса: ");
        String userClass = br.readLine();
        try {
            Class UserClass = Class.forName(userClass);
            System.out.println("Введите имя метода: ");
            String userMethod = br.readLine();
            Method[] methods = UserClass.getDeclaredMethods();
            for (Method method : methods) {
                if (userMethod.equals(method.getName())){
                    Class[] parameterTypes = method.getParameterTypes();
                    method.invoke(UserClass, parameterTypes);
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
