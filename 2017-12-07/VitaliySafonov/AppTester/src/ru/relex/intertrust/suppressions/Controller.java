package ru.relex.intertrust.suppressions;

import java.util.List;

public interface Controller
{
    void start(String suppressionFilename,String dir,List<SuppressionChecker> listOfChekers);
}
