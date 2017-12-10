import CommonElements.Author;
import CommonElements.Registrator;
import CommonElements.SuppressionChecker;
import DenisovVladilen.DenisovSuppressionCheckerAdapter;

import javax.swing.text.html.HTML;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainClass {
    private final static String SUPPRESSIONS_PATH = "recourses\\suppressions.xml";
    private final static String FILE_SYSTEM_PATH = "recourses\\fileSystem.txt";

    static {
        setFileList(FILE_SYSTEM_PATH);
        setSuppressionsPath();
    }

    public static void main(String[] args) throws Exception {
        Registration();

        TestAndMakeTable();
    }

    private static void Registration() {
        Registrator.register(new DenisovSuppressionCheckerAdapter());
        Registrator.register(new DenisovSuppressionCheckerAdapter());
        Registrator.register(new DenisovSuppressionCheckerAdapter());
    }

    private static void Testing() {
        try {
            TestMethodForEachClass(SuppressionChecker.class.getDeclaredMethod("parseSuppression", String.class),
                    SUPPRESSIONS_PATH);
            TestClassForEachMethod(Registrator.getList().get(0));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static void TestAndMakeTable() {
        Result[][] results = new Result[3][Registrator.getList().size()];
        for (int i = 0; i < Registrator.getList().size(); i++) {
            SuppressionChecker SC = Registrator.getList().get(i);
            try {
                results[0][i] = Test(SC, SuppressionChecker.class.getDeclaredMethod
                        ("parseSuppression", String.class), SUPPRESSIONS_PATH);
            } catch (NoSuchMethodException e) {
                results[0][i] = new Result
                        (SC.getClass().getDeclaredAnnotation(Author.class).value(),
                                "parseSuppression", false);
            }
            try {
                results[1][i] = Test(SC, SuppressionChecker.class.getDeclaredMethod
                        ("dir", String.class), "D:\\");
            } catch (NoSuchMethodException e) {
                results[1][i] = new Result
                        (SC.getClass().getDeclaredAnnotation(Author.class).value(),
                                "dir", false);
            }

            try {
                results[2][i] = Test(SC, SuppressionChecker.class.getDeclaredMethod
                        ("findDeletedFiles", List.class, List.class), suppList, fileList);
            } catch (NoSuchMethodException e) {
                results[2][i] = new Result
                        (SC.getClass().getDeclaredAnnotation(Author.class).value(),
                                "findDeletedFiles", false);
            }
        }
        writeIntoHTML(makeTable(results));
    }

    private static String makeTable(Result[][] results) {
        StringBuilder SB = new StringBuilder("<td>Метод\\Автор</td>");
        for (int i = 0; i < Registrator.getList().size(); i++) {
            SB.append("<td>")
                    .append(Registrator.getList().get(i).getClass().getDeclaredAnnotation(Author.class).value())
                    .append("</td>");
        }
        SB.append("</tr>");
        for (int i = 0; i < 3; i++) {
            SB.append("<tr><td>")
                    .append(results[i][0].getMethodName())
                    .append("</td>");
            int idx = 0;
            for (int k = 1; k < results[i].length; k++)
                if (results[i][k].getTimeInMs() < results[i][idx].getTimeInMs())
                    idx = k;
            for (int k = 0; k < results[i].length; k++) {
                SB.append((k==idx) ? "<td bgcolor=\"yellow\">" : "<td>")
                        .append(results[i][k].getTimeInMs())
                        .append("ms")
                        .append("</td>");
            }
            SB.append("</tr>");
        }
        return SB.toString();
    }

    @Deprecated
    private static void TestMethodForEachClass(Method method, Object... args) {
        for (int i = 0; i < Registrator.getList().size(); i++)
            try {
                Test(Registrator.getList().get(i), method, args).print();
            } catch (Exception e) {
                System.out.println("Ошибка при тестировании. Доп информация: { Автор:" +
                        Registrator.getList().get(i).getClass().getDeclaredAnnotation(Author.class) +
                        "; метод: " + method.getName() + "}");
            }
    }

    @Deprecated
    private static void TestClassForEachMethod(SuppressionChecker SC) {
        try {
            Test(SC, SuppressionChecker.class.getDeclaredMethod
                    ("parseSuppression", String.class), SUPPRESSIONS_PATH).print();
        } catch (NoSuchMethodException e) {
            new Result(SC.getClass().getDeclaredAnnotation(Author.class).value(),
                    "parseSuppression", false).print();
        }

        try {
            Test(SC, SuppressionChecker.class.getDeclaredMethod
                    ("dir", String.class), "D:\\").print();
        } catch (NoSuchMethodException e) {
            new Result(SC.getClass().getDeclaredAnnotation(Author.class).value(),
                    "dir", false).print();
        }

        List<String> suppList = null, fileList = null;
        try {
            suppList = SC.parseSuppression(SUPPRESSIONS_PATH);
            fileList = SC.dir("D:\\");
        } catch (Exception e) {
            System.out.println("Неудачное выполнение предыдущих операций -> " +
                    "невозможно вызвать соответствующий метод findDeletedFiles");
        }

        try {
            if (suppList != null && fileList != null)
                Test(SC, SuppressionChecker.class.getDeclaredMethod
                        ("findDeletedFiles", List.class, List.class), suppList, fileList).print();
        } catch (Exception e) {
            new Result(SC.getClass().getDeclaredAnnotation(Author.class).value(),
                    "findDeletedFiles", false).print();
        }
    }

    private static Result Test(SuppressionChecker SC, Method testing, Object... params) {
        int count = 10;
        long[] startTime = new long[count + 1];
        startTime[0] = System.currentTimeMillis();
        int[] timeDeltas = new int[count];
        for (int i = 0; i < count; i++) {
            try {
                testing.invoke(SC, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return new Result(SC.getClass().getDeclaredAnnotation(Author.class).value(),
                        testing.getName(),
                        false);
            }
            startTime[i + 1] = System.currentTimeMillis();
        }
        for (int i = 0; i < timeDeltas.length; i++)
            timeDeltas[i] = (int) (startTime[i + 1] - startTime[i]);
        double avg = Arrays.stream(timeDeltas).sum() / (1d * count);
        return new Result(SC.getClass().getDeclaredAnnotation(Author.class).value(),
                testing.getName(), avg);
    }

    private static List<String> fileList = null;
    private static List<String> suppList = null;
    private static void setFileList(String fileName) {
        fileList = new ArrayList<>();
        try (BufferedReader BR = new BufferedReader(new FileReader(new File(fileName)))) {
            while (BR.ready())
                fileList.add(BR.readLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void setSuppressionsPath(){
        suppList = new ArrayList<>();
        for (int i = 0; i < fileList.size(); i+=3)
            suppList.add(fileList.get(i));
    }

    private static void writeIntoHTML(String s) {
        try (BufferedReader BR = new BufferedReader(new FileReader("recourses\\result.html"))) {
            StringBuilder SB = new StringBuilder();
            while (BR.ready())
                SB.append(BR.readLine());
            SB.insert(SB.lastIndexOf("<table>") + 7, s);
            File file = File.createTempFile("sup", ".html");
            try (BufferedWriter BW = new BufferedWriter
                    (new FileWriter(file))) {
                BW.write(SB.toString());
            }
            Desktop.getDesktop().open(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}