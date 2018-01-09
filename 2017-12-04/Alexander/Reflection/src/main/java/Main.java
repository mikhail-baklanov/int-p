import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;


/**
 * Основной класс программы, который позволяет пользователю узнать о существующих классах и получить доступ к их методам.
 */
public class Main {

    /**
     * Список зарегистрированных классов.
     */
    private static Class[] CLASSES = new Class[] {Dog.class, Student.class, EmptyClass.class};

    /**
     * Булиевская переменная для проверки выхода.
     */
    private static boolean ISEXIT = false;

    /**
     * Программа выводит список всех зарегистрированных классов пользователю и предлагает выбрать один из них.
     * После выбора пользователю отображаются список методов этого класса и также предлагает выбрать один из них.
     * После выбора пользователем метода, он выполняется.
     */
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        //Если список классов пуст, завершаем программу
        if (CLASSES.length == 0) {
            System.out.println("Нет зарегистрированных классов.");
            ISEXIT = true;
        }

        while (!ISEXIT) {
            System.out.println("Доступные классы для вызова методов:");
            for (int i = 0; i < CLASSES.length; i++)
                System.out.println(i + ". " + CLASSES[i].getName());
            System.out.println("Введите номер одного из классов, чтобы посмореть список его методов.");

            boolean isTrue = false;
            int indexOfClass = -1;
            while (!isTrue) {
                indexOfClass = getIntegerFromSystemIn();
                if (indexOfClass >= 0 && indexOfClass < CLASSES.length)
                    isTrue = true;
                else System.out.println("Класса с таким индексом не существует.");
            }

            Class currentClass = CLASSES[indexOfClass];
            Method[] methodsOfCurrentClass = currentClass.getDeclaredMethods();
            if (methodsOfCurrentClass.length > 0) {
                System.out.println("Методы класса " + currentClass.getName() + ":");
                for (int i = 0; i < methodsOfCurrentClass.length; i++)
                    System.out.println(i + ". " + methodsOfCurrentClass[i].getReturnType().getName() + " " + methodsOfCurrentClass[i].getName() + "( ) { ... }");
                System.out.println("Введите номер одного из методов, чтобы вызвать его.");

                isTrue = false;
                int indexOfMethod = -1;
                while (!isTrue) {
                    indexOfMethod = getIntegerFromSystemIn();
                    if (indexOfMethod >= 0 && indexOfMethod < methodsOfCurrentClass.length)
                        isTrue = true;
                    else System.out.println("Метода с таким индексом не существует.");
                }

                Method currentMethod = methodsOfCurrentClass[indexOfMethod];
                currentMethod.invoke(CLASSES[indexOfClass].newInstance());
            } else
                System.out.println("Методов в этом классе нет.");

            ISEXIT = isExit();
        }
    }

    /**
     * Узнает у пользователя, желает ли он выйти.
     * @return возвращает конечный ответ пользователя в приемлемом виде.
     */
    private static boolean isExit() {
        Scanner input = new Scanner(System.in);
        String answer = "";
        while(!answer.toLowerCase().equals("n") && !answer.toLowerCase().equals("y")) {
            System.out.print("Желаете продолжить? (y/n): ");
            answer = input.nextLine();
        }
        if (answer.toLowerCase().equals("y"))
            return false;
        return true;
    }

    /**
     * Получает от пользователя число.
     * @return возвращает конечный ответ пользователя в приемлемом виде.
     */
    private static int getIntegerFromSystemIn() {

        Scanner input = new Scanner(System.in);
        String answer;
        while(true) {
            answer = input.nextLine();
            if(answer.matches("[0-9]*"))
                break;
            else
                System.out.println("Введенный текст не является числом");
        }

        return Integer.parseInt(answer);
    }

}

