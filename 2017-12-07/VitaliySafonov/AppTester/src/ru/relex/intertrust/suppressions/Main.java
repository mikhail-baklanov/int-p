package ru.relex.intertrust.suppressions;

public class Main
{
    public static void main(String[] args)
    {
        FindingFilesWithoutRegExp a = new FindingFilesWithoutRegExp();



//общий	 вызов и проверка работаспособности+отображение статистики
        for(Controller item : Registrator.getControllers())
            //item.start(args[0],args[1],Registrator.getCheckers());
            item.start("checkstyle-suppressions.xml","files.txt",Registrator.getCheckers());

    }
}
