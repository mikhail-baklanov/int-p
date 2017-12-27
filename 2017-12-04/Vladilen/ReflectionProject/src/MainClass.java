import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.*;

public class MainClass {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Show(MathManager.class);
        Show(ConsoleManager.class);
        System.out.println("Напишите полное имя метода и аргументы:");

        Action();
    }

    private static void Show(Class<?> c) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method[] ms = c.getDeclaredMethods();
        for (Method m : ms) {
            System.out.println(getFullMethodName(m));
        }
    }

    private static void Action() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        try (BufferedReader BR = new BufferedReader(new InputStreamReader(System.in)))
        {
            String line = BR.readLine();
            String className = line.substring(0, line.indexOf('.'));
            String methodName = line.substring(line.indexOf('.')+1, line.indexOf('('));
            Class<?> classForName = Class.forName(className);
            Method method = null;
            for (int i = 0; i < classForName.getDeclaredMethods().length; i++) {
                if (classForName.getDeclaredMethods()[i].getName().equals(methodName))
                {
                    method = classForName.getDeclaredMethods()[i];
                    break;
                }
            }
            if (method==null)
                return;
            Object[] args = new Object[method.getParameterCount()];
            String paramsstring = line.substring(line.indexOf('(')+1, line.indexOf(')'));
            String[] classValues = paramsstring.split(", ");
            for (int i = 0; i < args.length; i++) {
                //я бы хотел запустить метод, принимающий int-аргументы

                //Class<?> tmpClass = method.getParameters()[i].getType(); //here
                //switch (tmpClass.getName()){
                //    case "int":
                //        tmpClass = Integer.class;
                //    break;
                //}
                args[i] = classValues[i];
            }
            method.invoke(null, args);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private static String getFullMethodName(Method m) {
        StringBuilder SB = new StringBuilder(m.getDeclaringClass().getSimpleName());
        SB.append('.').append(m.getName()).append('(');
        Parameter[] parameters = m.getParameters();
        for (int i = 0; i < m.getParameterCount()-1; i++)
            SB.append(parameters[i].getType().getSimpleName()).append(' ').append(parameters[i].getName()).append(", ");
        SB.append(parameters[parameters.length - 1].getType().getSimpleName()).append(' ').
                append(parameters[parameters.length - 1].getName()).append(')');
        return SB.toString();
    }
}

class ConsoleManager {
    public static void PrintToConsole(String print){
        System.out.println(print);
    }
}

class MathManager {
    public static int IncrementValue(int a){
        return a+1;
    }

    public static int DecrementValue(int a){
        return a-1;
    }
}
