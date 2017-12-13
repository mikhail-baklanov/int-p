package ru.relex.intertrust.suppression.vladilen;

import ru.relex.intertrust.suppression.Registrator;
import ru.relex.intertrust.suppression.interfaces.Controller;
import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainClass implements Controller {
    private final static String SUPPRESSIONS_MOCK_FOR_CLASSES_PATH = "recourses\\suppressionsMockForClasses.xml";
    private final static String FILE_SYSTEM_PATH = "recourses\\fileSystem.txt";
    private final static String SUPPRESSIONS_MOCK_PATH = "recourses\\suppressionsMock.xml";

    private static List<String> fileList = null;
    private static List<String> suppList = null;

    static {
        setFileList(FILE_SYSTEM_PATH);
        setSuppList(SUPPRESSIONS_MOCK_PATH);
    }

    public static void main(String[] args) {
        new DenisovSuppressionCheckerAdapter();
        new MainClass().start("", "", Registrator.getCheckers());
    }

    private Result[][] results;

    @Override
    public void start(String suppressionFilename, String dir, List<SuppressionChecker> listOfChekers) {
        results = new Result[3][listOfChekers.size()];
        Method[] methods = getMethods();
        Object[][] params = new Object[3][];
        params[0] = new Object[]{SUPPRESSIONS_MOCK_FOR_CLASSES_PATH};
        params[1] = new Object[]{FILE_SYSTEM_PATH};
        params[2] = new Object[]{suppList, fileList};
        for (int i = 0; i < methods.length; i++) {
            for (int j = 0; j < listOfChekers.size(); j++) {
                SuppressionChecker SC = listOfChekers.get(j);
                results[i][j] = Test(SC, methods[i], (i==2) ? (results[0][j].getAdditionalFlags().get("Refactor")) : false, params[i]);
            }
        }
        try (BufferedWriter BW = new BufferedWriter(new FileWriter("DenisovFileResult.txt"))) {
            for (int i = 0; i < results[0].length; i++)
                for (int j = 0; j < results.length; j++) {
                    String[] re = results[j][i].getResult();
                    for (int k = 0; k < re.length; k++) {
                        BW.write(re[k]+'\n');
                    }
                    BW.write('\n');
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Random rnd = new Random();

    //public static void DO(){
    //    GenerateSuppressionFile(SUPPRESSIONS_MOCK_FOR_CLASSES_PATH, suppList);
    //}

    //it was invoked once
    private static void GenerateSuppressionFile(String fileName, List<String> Suppressions) {
        try (BufferedWriter BW = new BufferedWriter(new FileWriter(fileName))) {
            BW.write("<suppressions>\n");
            for (int i = 0; i < Suppressions.size(); i++) {
                String s = Suppressions.get(i);
                BW.write("<suppress files=\"");
                BW.write(s);
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

    @Deprecated
    private static String makeTable(Result[][] results) {
        StringBuilder SB = new StringBuilder("<td>Метод\\Автор</td>");
        for (int i = 0; i < Registrator.getCheckers().size(); i++) {
            SB.append("<td>")
                    .append(Registrator.getCheckers().get(i).getDeveloperName())
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

    private static Result Test(SuppressionChecker SC, Method testing, boolean additionalFlag, Object... params) {
        int count = 1;
        long[] startTime = new long[count + 1];
        startTime[0] = System.currentTimeMillis();
        int[] timeDeltas = new int[count];
        Object resultOfInvoking = null;
        Result result = new Result(SC.getDeveloperName(), testing.getName());
        for (int i = 0; i < count; i++) {
            try {
                if (additionalFlag&&testing.getName().equals("findDeletedFiles"))
                    params[0] = getRefactorSuppList((List<String>)params[0]);
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

    private static Method[] getMethods() {
        final Method[] methods = new Method[3];
        try {
            methods[0] = SuppressionChecker.class.getDeclaredMethod("parseSuppression", String.class);
            methods[1] = SuppressionChecker.class.getDeclaredMethod("dir", String.class);
            methods[2] = SuppressionChecker.class.getDeclaredMethod("findDeletedFiles", List.class, List.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return methods;
    }

    private static void setResult(Method m, Result result, Object resultObject) throws NoSuchMethodException {
        Method[] methods = getMethods();
        List<String> list;
        if (m.getName().equals(methods[0].getName()))
            result.getAdditionalFlags().put("Refactor", false); //default
        if (!(resultObject instanceof Exception)) {
            list = (List<String>) resultObject;
            if (list == null) {
                result.setNormal(false);
                result.setAdditionalInfo("Метод завершился с ошибкой: " + NullPointerException.class.getName());
                return;
            }
            result.setNormal(true);
        } else {
            result.setNormal(false);
            result.setAdditionalInfo("Метод завершился с ошибкой: " + ((Exception) resultObject).getClass().getName());
            return;
        }
        if (m.getName().equals(methods[0].getName())) {
            try {
                Boolean b = list.get(0).contains("[\\\\/]");
                result.setAdditionalInfo(b ? "Метод не преобразовывает разделители вида [\\\\/]" :
                        "Метод преобразовывает разделители вида [\\\\/]");
                result.getAdditionalFlags().replace("Refactor", !b);
                List<String> absolut = b ? suppList : getRefactorSuppList(suppList);
                result.setCompleteness(CheckCompleteness((Object) list, (Object) absolut));
            } catch (IndexOutOfBoundsException IOOBE) {
                result.setAdditionalInfo("Метод вернул пустой список");
                result.setNormal(false);
            }
        } else if (m.getName().equals(methods[1].getName())) {
            if (list.size() == 0) {
                result.setAdditionalInfo("Метод вернул пустой список");
                result.setNormal(false);
            } else {
                result.setCompleteness(CheckCompleteness((Object) list, fileList));
            }
        } else if (m.getName().equals(methods[2].getName())) {
            if (list.size() == 0) {
                result.setAdditionalInfo("Метод вернул пустой список");
                result.setNormal(false);
            } else if (list.size() == 3)
                result.setCompleteness(true);
            else {
                result.setCompleteness(false);
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
        boolean flag = true;
        for (int i = 0; i < absolutList.size() && flag; i++)
            if (!forCheckList.contains(absolutList.get(i)) && !forCheckList.contains(absolutList.get(i)+' '))
                flag = false;
        for (int i = 0; i < forCheckList.size() && flag; i++)
            if (!absolutList.contains(forCheckList.get(i)) && !absolutList.contains(forCheckList.get(i).trim()))
                flag = false;
        return flag;
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