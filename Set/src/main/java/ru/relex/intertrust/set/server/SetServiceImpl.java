package ru.relex.intertrust.set.server;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.relex.intertrust.set.client.service.SetService;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.CardsDeck;
import ru.relex.intertrust.set.shared.GameState;

import javax.servlet.ServletException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс, содержащий серверную логику игры Set
 */
public class SetServiceImpl extends RemoteServiceServlet implements SetService {

    private TimerTask t = new StartTimer();
    private Timer timer = new Timer();
    private static final String GAME_STATE = "gameState";
    private static final String USER_NAME = "userName";
    private static final long PERIOD_MS = 500;
    private static final int INITIAL_NUMBER_OF_CARDS = 12;
    private static final int MAX_NUMBER_OF_CARDS = 21;
    private static final int FINE = 5; //штраф
    private static final int REWARD = 3; //награда

    /**
     * Первоначальная инициализация
     * Выполняется при первом запуске сервера
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        super.init();
        initGame();
        timer.schedule(t, 0, PERIOD_MS);
    }

    /**
     * Инициализация игры
     */
    public void initGame() {
        getServletContext().setAttribute(GAME_STATE, new GameState());
    }

    /**
     * Регистрация игрока в игре:
     * имя игрока проверяется на уникальность,
     * проверяется уникальность сессии и состояние игры
     * регистрация игрока и постановка его в режим ожидания игры,
     * если введенные данные корректны, игрок уже не зарегистрирован и игра еще не идет
     * @param name имя игрока
     * @return true, если регистрация прошла успешно
     */
    @Override
    public boolean login(String name) {

        if (name.trim().isEmpty())
            return false;
        GameState gameState =(GameState) getServletContext().getAttribute(GAME_STATE);//getGameState();?
        boolean success;

        synchronized (gameState) {


            success = !gameState.hasPlayer(name) && !gameState.isStart() &&
                    getThreadLocalRequest().getSession().getAttribute(USER_NAME) == null;
            if (success) {
                if (gameState.getActivePlayers()==0)
                {
                    gameState.prepareTime();
                }
                gameState.addPlayer(name);
                gameState.setActivePlayers(gameState.getActivePlayers()+1);
                getThreadLocalRequest().getSession().setAttribute(USER_NAME, name);

            }
        }

        return success;
    }

    /**
     * Добавление нескольких карт на стол
     * удаляет добавленные карты из колоды
     * @param amountOfCards количество добавляемых карт
     */
    public void addCards (int amountOfCards) {
        GameState gameState = getGameState();
        for (int i=0;i<amountOfCards;i++) {
            Card CardInDeck=gameState.getDeck().get(gameState.getDeck().size()-1);
            gameState.getCardsOnDesk().add(CardInDeck);
            gameState.getDeck().remove(CardInDeck);
        }
    }

    /**
     * Метод, инициализирующий начало игрового процесса
     * меняет флаг isStart на true, т.е. показывает, что игра уже идет
     * генерирует колоду карт (cardsDeck)
     * добавляет в карты, которые должны отображаться на экране двенадцать карт (в cardsOnDesk)
     * удаляет из колоды cardsDeck карты из cardsOnDesk
     */
    public void startGame() {
        GameState gameState = getGameState();
        gameState.setStart(true);
        List<Card> cardsDeck = new CardsDeck().startCardsDeck();
        gameState.setDeck(cardsDeck);
        addCards(INITIAL_NUMBER_OF_CARDS);
    }

    /**
     * Выход из игры:
     * удаление игрока из списка игроков.
     * Осуществление проверки признаков конца игры:
     * если игроков нет, создается новая игра
     */
    @Override
    public void exit() {
        GameState gameState = getGameState();
        synchronized (gameState) {
            int playerNumber = getPlayerNumber((String) getThreadLocalRequest().getSession().getAttribute(USER_NAME));
            getThreadLocalRequest().getSession().removeAttribute(USER_NAME);
            gameState.setActivePlayers(gameState.getActivePlayers() - 1);
            if (gameState.getActivePlayers() == 0) {
                initGame();
            }
            if (!gameState.isStart()) {
                gameState.getPlayers().remove(playerNumber);
                gameState.getScore().remove(playerNumber);
            }
        }
    }

    /**
     * Метод, реализующий ПАС:
     * добавление игрока в список спасовавших игроков
     * получение списка карт в колоде клиента для проверки состояния игрока
     * осуществление проверки признаков конца игры
     * @param cardsInDeck кол-во карт в колоде
     */
    @Override
    public void pass(int cardsInDeck) {

        GameState gameState=getGameState();
        synchronized (gameState) {

            if (cardsInDeck == gameState.getDeck().size() && !isPassed()) //если пас пришел вовремя, то добавляем имя паснувшнего в список
            {

                gameState.AddNotAbleToPlay((String) getThreadLocalRequest().getSession().getAttribute(USER_NAME));
            }

            if (gameState.getNotAbleToPlay().size() == (gameState.getPlayers().size() / 2) + 1)//если список спасовавших больше половины игроков, то
            {                                                                        //добавляем 3карты на стол и обнуляем список пасовавших
                gameState.clearNotAbleToPlay();
                if (gameState.getDeck().size() == 0) {
                    gameState.setStart(false);
                }//если все нажали на пас, а карт в деке нет, то заканчиваем игру
                else if(gameState.getCardsOnDesk().size()<MAX_NUMBER_OF_CARDS)
                    addCards(3);
            }

        }
    }

    @Override
    /**
     * Геттер
     * @return возвращает описание состояния игры
     */
    public GameState getGameState()
    {
        GameState gameState = (GameState) getServletContext().getAttribute(GAME_STATE);
        synchronized (gameState)
        {
            return gameState;
        }
    }


    /**
     *
     * метод checkSet проверяет, являются ли полученные в параметре set карты сетом
     *            если нет, - у клиента вычитаются очки
     *            если являются, - идет проверка на то, есть ли в текущей игре на столе данные карты
     *              если есть, - клиенту добавляются очки, а со стола удаляются данные карты и запускается проверка, остались ли карты в колоде
     *                  если остались - на стол добавляются 3 новые карты, и из колоды они соответственно удаляются
     *                  если в колоде не осталось карт, проверяется, есть ли карты на столе, если нет - игра заканчивается (isStart становится false).
     * @param set принимает 3 карты от клиента
     * @return gameState после прохождения метода
     */
    @Override
    public void checkSet(Card[] set) {
        GameState gameState = getGameState();
        synchronized (gameState) {
            if (set[0] != set[1]) {
                int playerNumber = getPlayerNumber((String) getThreadLocalRequest().getSession().getAttribute(USER_NAME));
                int oldScore = gameState.getScore().get(playerNumber);
                int[] summ = {0, 0, 0, 0};
                for (int i = 0; i <= 2; i++) {
                    summ[0] += set[i].getColor();
                    summ[1] += set[i].getShapeCount();
                    summ[2] += set[i].getFill();
                    summ[3] += set[i].getShape();
                }
                for (int i = 0; i <= 3; i++) {
                    if (!(summ[i] == 3 || summ[i] == 6 || summ[i] == 9)) {
                        gameState.getScore().set(playerNumber, oldScore - FINE);
                        return;
                    }
                }
                if (!isPassed()) {
                    int existSet = 0;
                    List<Card> cardsOnDesk = gameState.getCardsOnDesk();
                    for (Card c : set) {
                        if (cardsOnDesk.contains(c))
                            existSet++;
                    }
                    if (existSet == 3) {
                        gameState.getScore().set(playerNumber, oldScore + REWARD);
                        gameState.setCountSets(gameState.getCountSets() + 1);
                        for (Card c : set)
                            gameState.getCardsOnDesk().remove(c);

                        if (gameState.getDeck().size() > 0 && gameState.getCardsOnDesk().size() <= INITIAL_NUMBER_OF_CARDS) {
                            addCards(3);
                        } else {
                            if (gameState.getCardsOnDesk().size() == 0)
                                gameState.setStart(false);
                        }
                    }
                }
            }
        }
    }

    /**
     * Возвращает номер, по которому можно получить информацию об игроке в листах
     * @param nickname имя игрока
     * @return номер игрока
     */
    public int getPlayerNumber(String nickname) {
        GameState gameState=getGameState();
        int i=0;
        while(nickname!=gameState.getPlayers().get(i))
            i++;
        return i;
    }

    /**
     * Поиск игрока в списке спасовавших игроков
     * проверка выполняется перед каждым действием со столом
     * @return true, если игрок пасовал
     */
    public boolean isPassed() {
        for(String str : getGameState().getNotAbleToPlay())
            if(str.equals((String) getThreadLocalRequest().getSession().getAttribute(USER_NAME)))return true;
        return false;

    }

    /**
     * Просто таймер тактирующий нашу переменную time каждые 0,5секунды
     * если через 60секунд после начала есть активные игроки, то игра начинается
     * как только все игроки вышли таймер прекращает работать
     * => в Game State time лежит время прошедшее со времени 1го логина
     * время в мс
     * таймер идет всегда, но при отсутствии игрока постоянно держит переменную time=-60000
     */
    private class StartTimer extends TimerTask
    {

        @Override
        public void run()
        {
            GameState gameState = getGameState();
            synchronized (gameState) {
                if (gameState.getActivePlayers() == 0) {
                    gameState.prepareTime();
                }
                if (gameState.getTime() == 0) startGame();
                gameState.setTime(gameState.getTime() + PERIOD_MS);

            }
        }
    }


}
