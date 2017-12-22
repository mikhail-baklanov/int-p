package ru.relex.intertrust.set.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.relex.intertrust.set.server.Card;
import ru.relex.intertrust.set.server.GameState;

@RemoteServiceRelativePath("SetService")
public interface SetService extends RemoteService
{
    void login(String name);

    GameState getGameState();

    //boolean checkSet(Card[] set);
    void exit();
    boolean pass(int cardsInDeck);
}

