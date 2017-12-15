package ru.relex.intertrust.suppression.vladilen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Deprecated
public class Result {
    private String author;
    private String methodName;
    private double timeInMs = Double.MAX_VALUE;
    private String additionalInfo;
    private boolean completeness = true;
    private boolean isNormal = true;
    private Map<String, Boolean> additionalFlags = new HashMap<>();

    public Map<String, Boolean> getAdditionalFlags() {
        return additionalFlags;
    }

    public void setAdditionalFlags(Map<String, Boolean> additionalFlags) {
        this.additionalFlags = additionalFlags;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setTimeInMs(double timeInMs) {
        this.timeInMs = timeInMs;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void setCompleteness(boolean completeness) {
        this.completeness = completeness;
    }

    public void setNormal(boolean normal) {
        isNormal = normal;
    }

    public Result(String author, String methodName) {

        this.author = author;
        this.methodName = methodName;
    }

    public String getAuthor() {
        return author;
    }

    public String getMethodName() {
        return methodName;
    }

    public double getTimeInMs() {
        return timeInMs;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public boolean isCompleteness() {
        return completeness;
    }

    public boolean isNormal() {
        return isNormal;
    }

    String[] getResult(){
        List<String> result = new ArrayList<>();
        result.add("Автор: " + getAuthor());
        result.add("Имя метода: " + getMethodName());
        result.add("Статус: завершился " + (isNormal ? "без ошибок" : "с ошибками"));
        if (additionalInfo!=null)
            result.add("Доп. информация: " + additionalInfo);
        if (isNormal) {
            result.add("Правильность результата: " + completeness);
            result.add("Среднее время работы: " + timeInMs + "ms");
        }
        String[] array = new String[result.size()];
        result.toArray(array);
        return array;
    }

    public void clear(String fileName){
        try (BufferedWriter BW = new BufferedWriter(new FileWriter(fileName))){
            BW.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print(){
        System.out.println("Результаты тестирования:");
        Arrays.stream(this.getResult()).forEach(System.out::println);
        System.out.println(); //empty line
    }

    public void printIntoFile(String fileName){
        try (BufferedWriter BW = new BufferedWriter(new FileWriter(fileName))){
            String[] result = getResult();
            for (int i = 0; i < result.length; i++)
                BW.append(result[i]);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
