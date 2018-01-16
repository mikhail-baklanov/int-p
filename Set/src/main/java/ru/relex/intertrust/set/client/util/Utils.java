package ru.relex.intertrust.set.client.util;

/**
 * Класс для вспомогательных методов.
 */
public class Utils {

    private Utils() {}


    /**
     * Метод форматирует вывод таймера.
     *
     * @param timeMs сообщение, которое будет напечатано
     */
    public static String formatTime(long timeMs) {
        timeMs /= 1000;
        if (timeMs<0) {
            return (-timeMs/60 < 10 ? "0" + -timeMs/60 : -timeMs/60) + ":" + (-timeMs%60 <10 ? "0" + -timeMs%60 : -timeMs%60);
        } else {
            return (timeMs/60 < 10 ? "0" + timeMs/60 : timeMs/60) + ":" + (timeMs%60 < 10 ? "0" + timeMs%60 : timeMs%60);
        }
    }

    /**
     * Метод выводит сообщения в консоль браузера.
     *
     * @param message сообщение, которое будет напечатано
     */
    public native static void consoleLog(String message) /*-{
        console.log(message);
    }-*/;

    /**
     * Метод выводит переданные объекты в консоль браузера.
     *
     * @param objects сообщение, которое будет напечатано
     */
    public native static void consoleLog(Object... objects) /*-{
        console.log(objects);
    }-*/;
}
