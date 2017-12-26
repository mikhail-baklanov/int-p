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

  //  @Override
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
    public int checkSet(Card[] set)
    {
        return 0;
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
