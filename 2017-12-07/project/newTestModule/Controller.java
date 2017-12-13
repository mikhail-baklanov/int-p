package interfaces;

public interface Controller
{
	void start(String suppressionFilename,String dir,List<SuppressionChecker> listOfChekers);


    /*
		старт запускает методы из suppressionChecker для объектов и отображает статистику в угодном вам виде
		(время работы, результат работы)
     */
    
}
