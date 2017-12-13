package ru.relex.intertrust.suppression;

import ru.relex.intertrust.suppression.alexander.*;
import ru.relex.intertrust.suppression.interfaces.Controller;
import ru.relex.intertrust.suppression.vladilen.DenisovSuppressionCheckerAdapter;
import ru.relex.intertrust.suppression.vladilen.MainClass;

public class Main {
    static {
        Registrator.register(new Alexander());
        Registrator.register(new AlexanderCtrl());
        //Registrator.register(new DenisovSuppressionCheckerAdapter());
        //Registrator.register(new MainClass());
    }
    public static void main(String[] args) {
        for(Controller item : Registrator.getControllers())
            item.start(args[0],args[1],Registrator.getCheckers());
    }
}