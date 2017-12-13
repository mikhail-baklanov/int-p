package ru.relex.inttrust.suppresion;

public class Main {

    public static void main(String[] args) {
        new Suppresion();
        new Controller();
        String sdir = "C:\\Projects\\int-p\\2017-11-30\\Sergei\\suppressionChecker\\test data\\checkstyle-suppressions.xml";
        String ddir = "C:\\Projects\\int-p\\2017-11-30\\Sergei\\suppressionChecker\\test data\\a.txt";

        for (ru.relex.inttrust.suppresion.interfaces.Controller ctrl: Registrator.getControllers()){
            ctrl.start(sdir, ddir, Registrator.getCheckers());
        }

    }
}
