package ru.relex.intertrust.set.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.relex.intertrust.set.server.Card;

public interface SetServiceAsync
{
    //void checkSet(Card[] set, AsyncCallback<Boolean> async);
    void exit(AsyncCallback<Void> async);
    //void getGameState(AsyncCallback<GameState> async);
    void login(String name, AsyncCallback<Void> async);
    void pass(int cardsInDeck, AsyncCallback<Boolean> async);
}
