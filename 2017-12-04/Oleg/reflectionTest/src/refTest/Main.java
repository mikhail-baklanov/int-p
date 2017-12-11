package refTest;

import java.lang.reflect.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        while (true) {
            Scanner in = new Scanner(System.in);
            System.out.println("Введите название класса:");
            String clas = in.next();
            try {
                Class clazz = Class.forName("refTest." + clas);

                int modifiers = clazz.getModifiers();
                System.out.print(getModifiers(modifiers));

                System.out.print("class " + clazz.getSimpleName() + " ");

                System.out.print("extends " + clazz.getSuperclass().getSimpleName() + " ");

                Method[] methods = clazz.getDeclaredMethods();
                for (Method m : methods) {
                    System.out.print("\t" + getModifiers(m.getModifiers()) +
                            getType(m.getReturnType()) + " " + m.getName() + "(");
                    System.out.print(getParameters(m.getParameterTypes()));
                    System.out.println(") { }");
                }

                System.out.println("}");
                System.out.println("Выберите метод");
                clas=in.next();
                try {
                    Method method = clazz.getDeclaredMethod(clas);
                    method.invoke(clazz.newInstance());
                }
                catch (Exception e) {
                    System.out.println("Невозможно выполнить метод "+ clas);
                }
            }
            catch (Exception e) {
                System.out.println("Такого класса в данном контексте не существует");
            }

        }
    }
    static String getModifiers(int m) {
        String modifiers = "";
        if (Modifier.isPublic(m)) modifiers += "public ";
        if (Modifier.isProtected(m)) modifiers += "protected ";
        if (Modifier.isPrivate(m)) modifiers += "private ";
        if (Modifier.isStatic(m)) modifiers += "static ";
        if (Modifier.isAbstract(m)) modifiers += "abstract ";
        return modifiers;
    }

    static String getType(Class clazz) {
        String type = clazz.isArray()
                ? clazz.getComponentType().getSimpleName()
                : clazz.getSimpleName();
        if (clazz.isArray()) type += "[]";
        return type;
    }

    static String getParameters(Class[] params) {
        String p = "";
        for (int i = 0, size = params.length; i < size; i++) {
            if (i > 0) p += ", ";
            p += getType(params[i]) + " param" + i;
        }
        return p;
    }
}
