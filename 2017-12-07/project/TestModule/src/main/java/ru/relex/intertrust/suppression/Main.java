package ru.relex.intertrust.suppression;

import ru.relex.intertrust.suppression.alexander.*;
import ru.relex.intertrust.suppression.evgeny.EvgenyController;
import ru.relex.intertrust.suppression.evgeny.FindDeletedClasses;
import ru.relex.intertrust.suppression.interfaces.Controller;
import ru.relex.intertrust.suppression.margarita.MargaritaChecker;
import ru.relex.intertrust.suppression.margarita.MargaritaController;
import ru.relex.intertrust.suppression.sergei.ControllerSergey;
import ru.relex.intertrust.suppression.sergei.SuppresionSergey;
import ru.relex.intertrust.suppression.vitaliy.RegStart;

public class Main {
    static {
        //Registrator.register(new Alexander());
        //Registrator.register(new AlexanderCtrl());

        Registrator.register(new MargaritaChecker());
        Registrator.register(new MargaritaController());

        Registrator.register(new SuppresionSergey());
        Registrator.register(new ControllerSergey());

        Registrator.register(new FindDeletedClasses());
        Registrator.register(new EvgenyController());

        //Registrator.register(new FindDeletedClasses());
        //Registrator.register(new RegStart());


    }
    public static void main(String[] args) {
        for(Controller item : Registrator.getControllers())
            item.start("checkstyle-suppressions.xml","files.txt",Registrator.getCheckers());
    }
}
