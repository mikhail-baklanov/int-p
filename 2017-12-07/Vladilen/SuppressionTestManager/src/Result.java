import java.util.Arrays;

public class Result {
    private String author;
    private String methodName;
    private double timeInMs = Double.MAX_VALUE;
    private boolean isNormal = true;

    public Result(String author, String methodName, double timeInMs) {
        this.author = author;
        this.methodName = methodName;
        this.timeInMs = timeInMs;
    }

    public Result(String author, String methodName, boolean isNormal) {
        this.author = author;
        this.methodName = methodName;
        this.isNormal = isNormal;
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

    String[] getResult(){
        String[] result;
        if (isNormal)
        {
            result = new String[5];
            result[0] = "Результаты тестирования:";
            result[1] = "Имя метода: " + methodName;
            result[2] = "Автор: " + author;
            result[3] = "Статус: без ошибок";
            result[4] = "Среднее время работы: " + timeInMs/1000.0 + "s (" + timeInMs + "ms)";
        } else {
            result = new String[4];
            result[0] = "Результаты тестирования:";
            result[1] = "Имя метода: " + methodName;
            result[2] = "Автор: " + author;
            result[3] = "Статус: завершен с ошибкой!";
        }
        return result;
    }

    public void print(){
        Arrays.stream(this.getResult()).forEach(System.out::println);
        System.out.println(); //empty line
    }
}
