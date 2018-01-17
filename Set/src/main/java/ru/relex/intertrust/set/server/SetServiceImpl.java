package ru.relex.intertrust.set.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.relex.intertrust.set.client.service.SetService;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

import javax.servlet.ServletException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс, содержащий серверную логику игры Set.
 */
public class SetServiceImpl extends RemoteServiceServlet implements SetService {

    private TimerTask              t                         =   new StartTimer();
    private Timer                  timer                     =   new Timer();
    private static final String    GAME_STATE                =   "gameState";
    private static final String    USER_NAME                 =   "userName";
    private static final long      PERIOD_MS                 =   1000;
    private static final int       INITIAL_NUMBER_OF_CARDS   =   12;

    @Override
    public GameState getGameState()
    {
        GameState gameState = (GameState) getServletContext().getAttribute(GAME_STATE);
        synchronized (gameState)
        {
            return gameState;
        }
    }

    /**
     * Первоначальная инициализация.
     * Выполняется при первом запуске сервера.
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        super.init();
        initGame();
    }

    /**
     * Инициализация игры.
     */
    public void initGame() {
        getServletContext().setAttribute(GAME_STATE, new GameState());
    }

    @Override
    public boolean login(String name) {
        if (name.trim().isEmpty())
            return false;
        GameState gameState =(GameState) getServletContext().getAttribute(GAME_STATE);

        boolean success;
        synchronized (gameState) {
            success = !gameState.hasPlayer(name) && !gameState.isStart() &&
                    getThreadLocalRequest().getSession().getAttribute(USER_NAME) == null;
            if (success) {
                if (gameState.getPlayers().size() == 0)
                    timer.schedule(t, 0, PERIOD_MS);

                gameState.createNewPlayer(name);
                getThreadLocalRequest().getSession().setAttribute(USER_NAME, name);
            }
        }
        return success;
    }

    /**
     * Метод, вызывающий инициализацию игрового процесса.
     */
    public void startGame() {
        GameState gameState = getGameState();
        gameState.startGame(INITIAL_NUMBER_OF_CARDS);
    }

    @Override
    public void exit() {
        GameState gameState = getGameState();
        synchronized (gameState) {
            gameState.removePlayer((String) getThreadLocalRequest().getSession().getAttribute(USER_NAME));
            getThreadLocalRequest().getSession().removeAttribute(USER_NAME);
            if (gameState.getActivePlayers() == 0) {
                reloadTimer();
                initGame();
            }
        }
    }

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

    @Override
    public Card[] searchSet(Card[] cardArray) {
        GameState gameState = getGameState();
        synchronized (gameState) {
            Card[] set = new Card[3];
            for (int i = 0; i < cardArray.length; i++) {
                set[0] = cardArray[i];
                for (int j = i+1; j < cardArray.length; j++) {
                    set[1] = cardArray[j];
                    for (int k = j+1; k < cardArray.length; k++)
                        set[2] = cardArray[k];
                }
            }
            if (gameState.checkSet(set))
                return set;
        }
        return null; //или Exception?
    }

    @Override
    public boolean checkSet(Card[] set) {
        GameState gameState = getGameState();
        boolean isSet;
        synchronized (gameState) {
            String player = (String) getThreadLocalRequest().getSession().getAttribute(USER_NAME);
            isSet = gameState.checkSet(set, player);
        }
        return isSet;
    }

    public boolean isPassed() {
        GameState gameState = getGameState();
        return gameState.isPassed((String) getThreadLocalRequest().getSession().getAttribute(USER_NAME));
    }

    /**
     * Метод обнуляет таймер.
     */
    private void reloadTimer() {
        timer.cancel();
        timer = new Timer();
        t = new StartTimer();
    }

    /**
     * Класс, переодически обновляющий игровое время каждые PERIOD_MS миллисекунд.
     * Если время равняется 0 миллисекунд, начинает игру.
     */
    private class StartTimer extends TimerTask
    {
        /**
         * Метод, осуществляющий обновление времени.
         */
        @Override
        public void run()
        {
            GameState gameState = getGameState();
            synchronized (gameState) {
                gameState.updateGameTime(PERIOD_MS);
                if (gameState.getTime() == 0) startGame();
            }
        }
    }
}
