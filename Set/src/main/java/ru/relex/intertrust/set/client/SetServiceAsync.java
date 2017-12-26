package ru.relex.intertrust.set.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

public interface SetServiceAsync
{
    void checkSet(Card[] set, AsyncCallback<Integer> async);
    void exit(AsyncCallback<Void> async);

    /**
     * @param async данные о состоянии игры будут переданы этому обработчику обратной связи
     */
    void getGameState(AsyncCallback<GameState> async);

    void login(String name, AsyncCallback<Boolean> async);
    void pass(int cardsInDeck, AsyncCallback<Boolean> async);
}
