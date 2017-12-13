package ru.relex.intertrust.suppression;

import ru.relex.intertrust.suppression.alexander.*;
import ru.relex.intertrust.suppression.interfaces.Controller;
import ru.relex.intertrust.suppression.margarita.MargaritaChecker;
import ru.relex.intertrust.suppression.margarita.MargaritaController;

public class Main {
    static {
        Registrator.register(new Alexander());
        Registrator.register(new AlexanderCtrl());

        Registrator.register(new MargaritaChecker());
        Registrator.register(new MargaritaController());
    }
    public static void main(String[] args) {
        for(Controller item : Registrator.getControllers())
            item.start(args[0],args[1],Registrator.getCheckers());
    }
}
