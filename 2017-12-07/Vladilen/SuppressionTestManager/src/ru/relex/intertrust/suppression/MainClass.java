package ru.relex.intertrust.suppression;

import ru.relex.intertrust.suppression.CommonElements.Author;
import ru.relex.intertrust.suppression.CommonElements.Registrator;
import ru.relex.intertrust.suppression.CommonElements.SuppressionChecker;
import ru.relex.intertrust.suppression.DenisovVladilen.DenisovSuppressionCheckerAdapter;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainClass {
    private final static String SUPPRESSIONS_MOCK_FOR_CLASSES_PATH = "recourses\\suppressionsMockForClasses.xml";
    private final static String FILE_SYSTEM_PATH = "recourses\\fileSystem.txt";
    private final static String SUPPRESSIONS_MOCK_PATH = "recourses\\suppressionsMock.xml";

    private static List<String> fileList = null;
    private static List<String> suppList = null;

    static {
        setFileList(FILE_SYSTEM_PATH);
        setSuppList(SUPPRESSIONS_MOCK_PATH);
    }

    public static void main(String[] args) throws Exception {
        Registration();

        //TestAndMakeTable();
        Testing();
        //GenerateSuppressionFile("recourses\\suppressionsMockForClasses.xml", suppList);
    }

    private static void Registration() {
        //todo переделать регистрацию, регистрируя из статиков в самих классах
        Registrator.register(new DenisovSuppressionCheckerAdapter());
    }

    private static void Testing() {
        try {
            Test(Registrator.getList().get(0), SuppressionChecker.class.getDeclaredMethod("parseSuppression", String.class), SUPPRESSIONS_MOCK_FOR_CLASSES_PATH).print();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    //it was invoked once
    private static void GenerateSuppressionFile(String fileName, List<String> Suppressions) {
        try (BufferedWriter BW = new BufferedWriter(new FileWriter(fileName))) {
            BW.write("<suppressions>\n");
            for (int i = 0; i < Suppressions.size(); i++) {
                String s = Suppressions.get(i);
                BW.write("<suppress files=\"");
                BW.write(s + '\n');
                BW.write("\"\nchecks=\"^((?!LineLength).)*$\"/>\n");
            }
            BW.write("</suppressions>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void TestAndMakeTable() {
        //Result[][] results = new Result[3][Registrator.getList().size()];
        //for (int i = 0; i < Registrator.getList().size(); i++) {
        //    SuppressionChecker SC = Registrator.getList().get(i);
        //    try {
        //        results[0][i] = Test(SC, SuppressionChecker.class.getDeclaredMethod
        //                ("parseSuppression", String.class), SUPPRESSIONS_PATH);
        //    } catch (NoSuchMethodException e) {
        //        results[0][i] = new Result
        //                (SC.getClass().getDeclaredAnnotation(Author.class).value(),
        //                        "parseSuppression", false);
        //    }
        //    try {
        //        results[1][i] = Test(SC, SuppressionChecker.class.getDeclaredMethod
        //                ("dir", String.class), "D:\\");
        //    } catch (NoSuchMethodException e) {
        //        results[1][i] = new Result
        //                (SC.getClass().getDeclaredAnnotation(Author.class).value(),
        //                        "dir", false);
        //    }
//
        //    try {
        //        results[2][i] = Test(SC, SuppressionChecker.class.getDeclaredMethod
        //                ("findDeletedFiles", List.class, List.class), suppList, fileList);
        //    } catch (NoSuchMethodException e) {
        //        results[2][i] = new Result
        //                (SC.getClass().getDeclaredAnnotation(Author.class).value(),
        //                        "findDeletedFiles", false);
        //    }
        //}
        //writeIntoHTML(makeTable(results));
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
            int idx = 0; //todo сделать нормальный вывод при отрицательном результате у всех
            for (int k = 1; k < results[i].length; k++)
                if (results[i][k].getTimeInMs() < results[i][idx].getTimeInMs())
                    idx = k;
            for (int k = 0; k < results[i].length; k++) {
                SB.append((k == idx) ? "<td bgcolor=\"yellow\">" : "<td>")
                        .append(results[i][k].getTimeInMs()) //todo сделать вывод результата с ошибкой
                        .append("ms")
                        .append("</td>");
            }
            SB.append("</tr>");
        }
        return SB.toString();
    }

    private static Result Test(SuppressionChecker SC, Method testing, Object... params) {
        int count = 20;
        long[] startTime = new long[count + 1];
        startTime[0] = System.currentTimeMillis();
        int[] timeDeltas = new int[count];
        Object resultOfInvoking = null;
        Result result = new Result(SC.getClass().getDeclaredAnnotation(Author.class).value(), testing.getName());
        for (int i = 0; i < count; i++) {
            try {
                resultOfInvoking = testing.invoke(SC, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                resultOfInvoking = e;
            }
            startTime[i + 1] = System.currentTimeMillis();
        }
        try {
            setResult(testing, result, resultOfInvoking);
        } catch (NoSuchMethodException e) {
            e.printStackTrace(); //зачем это?
        }
        for (int i = 0; i < timeDeltas.length; i++)
            timeDeltas[i] = (int) (startTime[i + 1] - startTime[i]);
        double avg = Arrays.stream(timeDeltas).sum() / (1d * count);
        result.setTimeInMs(avg);
        return result;
    }

    private static void setResult(Method m, Result result, Object resultObject) throws NoSuchMethodException {
        final Method[] methods = new Method[]{
                SuppressionChecker.class.getDeclaredMethod("parseSuppression", String.class),
                SuppressionChecker.class.getDeclaredMethod("dir", String.class),
                SuppressionChecker.class.getDeclaredMethod("findDeletedFiles", List.class, List.class)
        };
        if (m.getName().equals(methods[0].getName())) {
            List<String> list;
            if (!(resultObject instanceof Exception)) {

                list = (List<String>) resultObject;
                result.setNormal(true);
            } else {
                result.setNormal(false);
                result.setAdditionalInfo("Метод завершился с ошибкой: " + ((Exception)resultObject).getClass().getName());
                return;
            }
            try {
                Boolean b = list.get(0).contains("[\\\\/]");
                result.setAdditionalInfo(b ? "Метод не преобразовывает разделители вида [\\\\/]" : "Метод преобразовывает разделители вида [\\\\/]");
                result.getAdditionalFlags().put("Refactor", b);
                List<String> absolut = b ? suppList : getRefactorSuppList(suppList);
                result.setCompleteness(CheckCompleteness((Object) list, (Object) absolut));
            } catch (IndexOutOfBoundsException IOOBE) {
                result.setAdditionalInfo("Метод вернул пустой список");
                result.setNormal(false);
            }
        }
    }

    private static List<String> getRefactorSuppList(List<String> suppList) {
        List<String> list = new ArrayList<>(suppList.size());
        for (int i = 0; i < suppList.size(); i++)
            list.add(suppList.get(i).replaceAll("\\[\\\\\\\\/\\]", "\\\\"));
        return list;
    }

    private static boolean CheckCompleteness(Object forCheck, Object absolut) {
        List<String> forCheckList = (List<String>) forCheck;
        List<String> absolutList = (List<String>) absolut;
        if (forCheckList.size() != absolutList.size())
            return false;
        for (int i = 0; i < ((List<String>) forCheck).size(); i++)
            if (!forCheckList.get(i).equals(absolutList.get(i))) {
                System.out.println(forCheckList.get(i));
                System.out.println(absolutList.get(i));
                return false;
            }
        return true;
    }

    private static void setFileList(String fileName) {
        fileList = new ArrayList<>();
        printIntoLists(fileName, fileList);
    }

    private static void setSuppList(String fileName) {
        suppList = new ArrayList<>();
        printIntoLists(fileName, suppList);
    }

    private static void printIntoLists(String fileName, List<String> list) {
        try (BufferedReader BR = new BufferedReader(new FileReader(new File(fileName)))) {
            while (BR.ready())
                list.add(BR.readLine().trim());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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