package interfaces;

public interface Controller
{
	void start(String suppressionFilename,String dir,List<SuppressionChecker> listOfChekers);
	void print(List<Integer> listOfTime,List<SuppressionChecker> listOfChekers);

    /*
		старт запускает методы из suppressionChecker для объектов и сохраняет их время в List 
		в конце start вызывается метод print, принимающий list со временем и отображающий статистику в угодном вам виде
		listOfChekers в print для возможности отображения статистики
     */
    
}
