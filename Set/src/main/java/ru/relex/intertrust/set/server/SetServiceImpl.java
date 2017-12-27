package ru.relex.intertrust.set.server;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.relex.intertrust.set.client.SetService;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.CardsDeck;
import ru.relex.intertrust.set.shared.GameState;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SetServiceImpl extends RemoteServiceServlet implements SetService {

    private static final String GAME_STATE = "gameState";
    private static final String USER_NAME = "userName";

    @Override
    public void init() throws ServletException {
        super.init();
        initGame();
    }

    public void initGame() {
        getServletContext().setAttribute(GAME_STATE, new GameState());
    }

    @Override
    public boolean login(String name) {
        if (name == null)
            return false;
        GameState gameState =(GameState) getServletContext().getAttribute(GAME_STATE);//getGameState();?
        boolean success;

        synchronized (gameState) {
            success = !gameState.hasPlayer(name) && !gameState.isStart() &&
                    getThreadLocalRequest().getSession().getAttribute(USER_NAME) == null;
            if (success) {
                if (gameState.getActivePlayers()==0) {
                    gameState.setTime(System.currentTimeMillis());
                    Timer timer=new Timer();
                    StartTimer startTimer=new StartTimer();
                    timer.schedule(startTimer,60000);
                }
                gameState.addPlayer(name);
                gameState.setActivePlayers(gameState.getActivePlayers()+1);
                getThreadLocalRequest().getSession().setAttribute(USER_NAME, name);

            }
        }

        return success;
    }

    public void addCards (int amountOfCards) {
        GameState gameState = getGameState();
        for (int i=0;i<amountOfCards;i++) {
            Card CardInDeck=gameState.getDeck().get(gameState.getDeck().size()-1);
            gameState.getCardsOnDesk().add(CardInDeck);
            gameState.getDeck().remove(CardInDeck);
        }
    }

    /**
     * Метод, который необходимо вызвать при начале игры
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
        addCards(12);
    }

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
     * добавялет в список спасовавших игроков
     *
     * @param cardsInDeck
     */
    @Override
    public void pass(int cardsInDeck) {

        GameState gameState=getGameState();
        synchronized (gameState) {

        if (cardsInDeck == gameState.getDeck().size())//если пас пришел вовремя, то добавляем имя паснувшнего в список
                gameState.AddNotAbleToPlay((String) getThreadLocalRequest().getSession().getAttribute(USER_NAME));


        if (gameState.getNotAbleToPlay().size() == (gameState.getPlayers().size() / 2) + 1)//если список спасовавших больше половины игроков, то
            {                                                                        //добавляем 3карты на стол и обнуляем список пасовавших
                gameState.clearNotAbleToPlay();
            if (gameState.getDeck().size() == 0) {
                gameState.setStart(false);
            }//если все нажали на пас, а карт в деке нет, то заканчиваем игру
                else addCards(3);
            }

        }
    }

    @Override
    /**
     * @return возвращает описание состояния игры
     */
    public GameState getGameState()
    {
        GameState gameState = (GameState) getServletContext().getAttribute(GAME_STATE);
        synchronized (gameState)
    {
            long time=gameState.getTime();
            gameState.setTime(System.currentTimeMillis());
            gameState.setTimer(time - gameState.getTime());
        return gameState;
    }
    }


    /**
     * @param set принимает 3 карты от клиента
     * метод checkSet проверяет, являются ли полученные в параметре set карты сетом
     *            если нет, - у клиента вычитаются очки
     *            если являются, - идет проверка на то, есть ли в текущей игре на столе данные карты
     *              если есть, - клиенту добавляются очки, а со стола удаляются данные карты и запускается проверка, остались ли карты в колоде
     *                  если остались - на стол добавляются 3 новые карты, и из колоды они соответственно удаляются
     *                  если в колоде не осталось карт, проверяется, есть ли карты на столе, если нет - игра заканчивается (isStart становится false).
     * @return gameState после прохождения метода
     */
    @Override
    public void checkSet(Card[] set) {
        GameState gameState = getGameState();
        synchronized (gameState) {
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
                if (summ[i] != 3 || summ[i] != 6 || summ[i] != 9) {
                gameState.getScore().set(oldScore, oldScore - 5);
                    return;
                }
            }
        int existSet = 0;
        List<Card> cardsOnDesk = gameState.getCardsOnDesk();
        for (int j = 0; j <= 2; j++) {
                for (int i = 0; i < cardsOnDesk.size(); i++) {
                if (set[j] == cardsOnDesk.get(i))
                        existSet++;
                }
            }
        if (existSet == 3) {
            gameState.getScore().set(oldScore, oldScore + 3);
            gameState.setCountSets(gameState.getCountSets() + 1);
                for (int i = 0; i <= 3; i++) {
                    gameState.getCardsOnDesk().remove(set[i]);
                }
            if (gameState.getDeck().size() > 0) {
                    addCards(3);
            } else {
                if (gameState.getCardsOnDesk().size() == 0)
                        gameState.setStart(false);
                }
            }
        }
    }


    public int getPlayerNumber(String nickname) {
        GameState gameState=getGameState();
        int i=0;
        while(nickname!=gameState.getPlayers().get(i))
            i++;
        return i;
    }

    /**
     * Если игрок в списке спасовавших true
     * если нет false
     * Для проверки перед каждым действием со столом
     */
    public boolean isPassed() {
        for(String str : getGameState().getNotAbleToPlay())
            if(str.equals((String) getThreadLocalRequest().getSession().getAttribute(USER_NAME)))return true;
        return false;

    }

    class StartTimer extends TimerTask
    {
            @Override
            public void run()
            {
                GameState gameState = getGameState();
                gameState.setTime(System.currentTimeMillis());
                if(gameState.getActivePlayers()==0) return;
                startGame();
            }


    }





}
