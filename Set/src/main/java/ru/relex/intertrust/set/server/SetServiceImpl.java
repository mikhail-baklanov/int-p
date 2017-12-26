package ru.relex.intertrust.set.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.relex.intertrust.set.client.SetService;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.CardsDeck;
import ru.relex.intertrust.set.shared.GameState;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;

public class SetServiceImpl extends RemoteServiceServlet implements SetService
{

    private static final String GAME_STATE = "gameState";
    private static final String USER_NAME = "userName";

    @Override
    public void init() throws ServletException {
        super.init();
        getServletContext().setAttribute(GAME_STATE, new GameState());
    }

    @Override
    public boolean login(String name)
    {
        if (name == null)
            return false;
        GameState gameState = getGameState();//(GameState) getServletContext().getAttribute(GAME_STATE);
        boolean success;

        synchronized (gameState) {
            success = !gameState.hasPlayer(name) && !gameState.isStart() &&
                    getThreadLocalRequest().getSession().getAttribute(USER_NAME) == null;
            if (success) {
                gameState.addPlayer(name);
                getThreadLocalRequest().getSession().setAttribute(USER_NAME, name);
            }
        }
        return success;
    }

    /**
     * Метод, который вызывают при начале игры
     * меняет флаг isStart на true, т.е. показывает, что игра уже идет
     * генерирует колоду карт (cardsDeck)
     * добавляет в карты, которые должны отображаться на экране двенадцать карт (в cardsOnDesk)
     * удаляет из колоды cardsDeck карты из cardsOnDesk
     */
    public void StartGame() {
        GameState gameState = getGameState();
        gameState.setStart(true);
        List<Card> cardsDeck = new CardsDeck().startCardsDeck();
        gameState.setDeck(cardsDeck);
        List<Card> cardsOnDesk = new ArrayList<>();
        for (int i=0;i<12;i++) {
            Card CardInDeck=gameState.getDeck().get(gameState.getDeck().size()-1);
            cardsOnDesk.add(CardInDeck);
            gameState.getDeck().remove(CardInDeck);
        }
        gameState.setCardsOnDesk(cardsOnDesk);
    }

    @Override
    public void exit()
    {
        getThreadLocalRequest().getSession().removeAttribute(USER_NAME);
        // TODO проверить, началась ли игра. Если не началась, удалить игрока из списка и остановить игру, если список пуст
    }


    @Override
    public void pass(int cardsInDeck)
    {
        //TODO добавить изменение состояния в ableToPlay
        //Как получить имя(номер) игрока нажавшнего пас?
        GameState gameState=getGameState();
        if(cardsInDeck==gameState.getDeck().size())
        {
            String nickname= (String) getThreadLocalRequest().getSession().getAttribute(USER_NAME);



            //gameState.getAbleToPlay()

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
            return gameState;
        }
    }


    /**
     * @param set принимает 3 карты от клиента
     * метод checkSet проверяет, являются ли полученные в параметре set карты сетом
     *            если нет, - у клиента вычитаются очки
     *            если являются, - идет проверка на то, есть в текущей игре на столе данные карты
     *              если есть, - клиенту добавляются очки, а со стола удаляются данные карты
     * @return gameState после прохождения метода
     */
    @Override
    public GameState checkSet(Card[] set) {
        GameState gameState = getGameState();
        int playerNumber=getPlayerNumber((String) getThreadLocalRequest().getSession().getAttribute(USER_NAME));
        int oldScore=gameState.getScore().get(playerNumber);
        int[] summ = {0, 0, 0, 0};
        for (int i = 0; i <= 2; i++) {
            summ[0] += set[i].getColor();
            summ[1] += set[i].getShapeCount();
            summ[2] += set[i].getFill();
            summ[3] += set[i].getShape();
        }
        for (int i = 0; i <= 3; i++) {
            if (summ[i] != 3 || summ[i] != 6 || summ[i] != 9) {
                gameState.getScore().set(oldScore,oldScore-5);
                return gameState;
            }
        }
        int existSet=0;
        List<Card> cardsOnDesk=gameState.getCardsOnDesk();
        for (int j=0;j<=2;j++) {
            for (int i = 0; i < cardsOnDesk.size(); i++) {
                if (set[j]==cardsOnDesk.get(i))
                    existSet++;
            }
        }
        if (existSet==3) {
            gameState.getScore().set(oldScore,oldScore+3);
            for (int i = 0; i <= 3; i++) {
                gameState.getCardsOnDesk().remove(set[i]);
            }
            return gameState;
        }
        return gameState;
    }


    public int getPlayerNumber(String nickname)
    {
        GameState gameState=getGameState();
        int i=0;
        while(nickname!=gameState.getPlayers().get(i))
            i++;
        return i;
    }


}
