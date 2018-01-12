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

    @Override
    /**
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
                gameState.createNewPlayer(name);
                getThreadLocalRequest().getSession().setAttribute(USER_NAME, name);
            }
        }
        return success;
    }

    /**
     * Добавление нескольких карт на стол
     * @param amountOfCards количество добавляемых карт
     */
    public void addCards (int amountOfCards) {
        GameState gameState = getGameState();
        gameState.addCards(amountOfCards);
    }

    /**
     * Метод, вызывающий инициализацию игрового процесса
     */
    public void startGame() {
        GameState gameState = getGameState();
        gameState.startGame(INITIAL_NUMBER_OF_CARDS);
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
            gameState.removePlayer((String) getThreadLocalRequest().getSession().getAttribute(USER_NAME));
            getThreadLocalRequest().getSession().removeAttribute(USER_NAME);
            if (gameState.getActivePlayers() == 0) {
                initGame();
            }
        }
    }

    /**
     * Метод, реализующий ПАС:
     * добавление игрока в список спасовавших игроков
     * получение списка карт в колоде клиента для проверки состояния игрока
     * @param cardsInDeck кол-во карт в колоде
     */
    @Override
    public void pass(int cardsInDeck) {
        //TODO fix pass
        GameState gameState=getGameState();
        synchronized (gameState) {
            String user = (String) getThreadLocalRequest().getSession().getAttribute(USER_NAME);
            gameState.AddNotAbleToPlay(user, cardsInDeck);
            gameState.pass(user);
        }
    }

    /**
     * метод checkSet проверяет, являются ли полученные в параметре set карты сетом
     * @param set принимает 3 карты от клиента
     * @return gameState после прохождения метода
     */
    @Override
    public void checkSet(Card[] set) {
        GameState gameState = getGameState();
        synchronized (gameState) {
            String player = (String) getThreadLocalRequest().getSession().getAttribute(USER_NAME);
            gameState.checkSet(set, player);
        }
    }

    /**
     * Поиск игрока в списке спасовавших игроков
     * проверка выполняется перед каждым действием со столом
     * @return true, если игрок пасовал
     */
    public boolean isPassed() {
        GameState gameState = getGameState();
        return gameState.isPassed((String) getThreadLocalRequest().getSession().getAttribute(USER_NAME));
    }

    /**
     * Класс, переодически обновляющий игровое время каждые PERIOD_MS миллисекунд,
     * если время равняется 0 миллисекунд и есть игроки, начинает игру
     */
    private class StartTimer extends TimerTask
    {
        /**
         * Метод, осуществляющий обновление времени
         */
        @Override
        public void run()
        {
            GameState gameState = getGameState();
            synchronized (gameState) {
                gameState.updateOrPrepareGameTime();
                if (gameState.getTime() == 0) startGame();
            }
        }
    }
}
