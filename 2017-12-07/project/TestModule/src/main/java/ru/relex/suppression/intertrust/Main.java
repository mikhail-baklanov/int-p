package ru.relex.suppression.intertrust;

import ru.relex.suppression.intertrust.apps.*;
import ru.relex.suppression.intertrust.controllers.*;
import ru.relex.suppression.intertrust.interfaces.Controller;

public class Main {
    public static void main(String[] args) {
        new Alexander();
        new AlexanderCtrl();
        for(Controller item : Registrator.getControllers())
            item.start(args[0],args[1],Registrator.getCheckers());
    }
}
