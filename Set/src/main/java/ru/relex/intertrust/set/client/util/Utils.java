package ru.relex.intertrust.set.client.util;

import com.google.gwt.core.client.GWT;
import ru.relex.intertrust.set.client.constants.GameConstants;

public class Utils {

    public static String formatTime(long timeMs) {
        timeMs /= 1000;
        if (timeMs<0) {
            return (-timeMs/60 < 10 ? "0" + -timeMs/60 : -timeMs/60) + ":" + (-timeMs%60 <10 ? "0" + -timeMs%60 : -timeMs%60);
        } else {
            return (timeMs/60 < 10 ? "0" + timeMs/60 : timeMs/60) + ":" + (timeMs%60 < 10 ? "0" + timeMs%60 : timeMs%60);
        }
    }

    /**
     * Функция печати сообщения в консоль браузера.
     * @param message сообщение, которое будет напечатано
     */
    public native static void consoleLog(String message) /*-{
        console.log(message);
    }-*/;

    public native static void consoleLog(Object... objects) /*-{
        console.log(objects);
    }-*/;
}
