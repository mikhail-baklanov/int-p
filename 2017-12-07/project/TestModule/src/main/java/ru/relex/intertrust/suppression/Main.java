package ru.relex.intertrust.suppression;

import ru.relex.intertrust.suppression.alexander.*;
import ru.relex.intertrust.suppression.evgeny.*;
import ru.relex.intertrust.suppression.interfaces.Controller;
import ru.relex.intertrust.suppression.margarita.*;
import ru.relex.intertrust.suppression.sergei.*;
import ru.relex.intertrust.suppression.vitaliy.*;
import ru.relex.intertrust.suppression.oleg.*;
import ru.relex.intertrust.suppression.vladilen.*;

public class Main {
    static {
        Registrator.register(new Alexander());
        Registrator.register(new AlexanderCtrl());

        Registrator.register(new MargaritaChecker());
        Registrator.register(new MargaritaController());

        Registrator.register(new SuppresionSergey());
        Registrator.register(new ControllerSergey());

        Registrator.register(new FindDeletedClasses());
        Registrator.register(new EvgenyController());

        Registrator.register(new FindingFilesWithoutRegExp());
        Registrator.register(new RegStart());

        Registrator.register(new SupOleg());
        Registrator.register(new OlegController());

        Registrator.register(new DenisovSuppressionCheckerAdapter());
        Registrator.register(new MainClass());

    }
    public static void main(String[] args) {
        for(Controller item : Registrator.getControllers())
            item.start(args[0],args[1],Registrator.getCheckers());
    }
}
