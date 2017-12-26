package ru.relex.intertrust.set.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.relex.intertrust.set.client.SetService;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

import javax.servlet.ServletException;

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
        GameState gameState = (GameState) getServletContext().getAttribute(GAME_STATE);
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

    @Override
    public void exit()
    {
        getThreadLocalRequest().getSession().removeAttribute(USER_NAME);
        // TODO проверить, началась ли игра. Если не началась, удалить игрока из списка и остановить игру, если список пуст
    }


    @Override
    public boolean pass(int cardsInDeck)
    {
        //TODO добавить изменение состояния в ableToPlay
        //Как получить имя(номер) игрока нажавшнего пас?
        GameState gameState=getGameState();
        if(cardsInDeck==gameState.getDeck().size())
        {
            //gameState.getAbleToPlay()
            return true;
        }

        return false;
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


    @Override
    public void checkSet(Card[] set) {
        GameState gameState=getGameState();
        int[] summ= {0,0,0,0};
        for (int i=0;i<=2;i++) {
            summ[0]+=set[i].getColor();
            summ[1]+=set[i].getShapeCount();
            summ[2]+=set[i].getFill();
            summ[3]+=set[i].getShape();
        }
        for (int i=0;i<=3;i++) {
            if (summ[i]!=3 || summ[i]!=6 || summ[i]!=9) {
                gameState.
            }
        }

    }


}
