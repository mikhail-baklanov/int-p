import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class refl
{
    /**
     * Вызов метода methodname из класса classname
     * @param classname
     * @param methodname
     */

    public static void useMethodFromClass(String classname, String methodname)
    {
        try
        {
            Class myClass=Class.forName(classname);
            Method method = myClass.getDeclaredMethod(methodname);
            method.invoke(myClass.newInstance());
        }
        catch (ClassNotFoundException e) { e.printStackTrace(); } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


    }

    /**
     * Сообщает всю информацию о классе
     * Принимает в качестве параметра объект данного класса
     * @param o
     */
    public static void getClassess(Object o)
    {

        Class aclass = o.getClass();
        System.out.println("Class name:" + aclass.getName());
        System.out.println("Class fields:");
        for (Field field : aclass.getFields()) {
            Class fieldType = field.getType();
            System.out.print("field name: " + field.getName());
            System.out.print("field type: " + fieldType.getName());
        }
        System.out.println("Class fields:");
        for (Method method : aclass.getDeclaredMethods())
        {
            System.out.println("method name: " + method.getName());
            System.out.println("return type: " + method.getReturnType().getName());
        }

    }

    public static void main(String[] args)
    {
        getClassess(new BytesTest());


        Scanner in = new Scanner(System.in);
        System.out.println("Название класса:");
        String classname = in.nextLine();

        System.out.println("Название метода:");
        String methodname = in.nextLine();
        useMethodFromClass(classname,methodname);
    }


}