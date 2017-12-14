package ru.relex.intertrust.suppressions;

public class Main {

    public static void main(String[] args) {
        Registrator.register(new SuppresionSergey());
        Registrator.register(new ControllerSergey());

        for (Controller ctrl: Registrator.getControllers()){
            ctrl.start(args[0], args[1], Registrator.getCheckers());
        }

    }
}
