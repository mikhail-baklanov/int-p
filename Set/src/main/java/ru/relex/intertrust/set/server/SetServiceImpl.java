package ru.relex.intertrust.set.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.relex.intertrust.set.client.SetService;

public class SetServiceImpl extends RemoteServiceServlet implements SetService
{

    @Override
    public void login(String name)
    {

    }

    @Override
    public void exit()
    {

    }

    @Override
    public boolean pass(int cardsInDeck)
    {
        return false;
    }


    @Override
    public GameState getGameState()
    {
        return null;
    }
/*
    @Override
    public boolean checkSet(Card[] set)
    {
        return false;
    }
*/

}
