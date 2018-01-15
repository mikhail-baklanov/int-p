package ru.relex.intertrust.set.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

public interface SetServiceAsync
{
    void exit(AsyncCallback<Void> async);

    void getGameState(AsyncCallback<GameState> async);

    void login(String name, AsyncCallback<Boolean> async);

    void pass(int cardsInDeck, AsyncCallback<Void> async);

    void checkSet(Card[] set, AsyncCallback<Boolean> async);
}
