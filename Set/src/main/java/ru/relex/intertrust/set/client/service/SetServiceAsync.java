package ru.relex.intertrust.set.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

public interface SetServiceAsync
{

    void exit(AsyncCallback<Void> async);

    /**
     * @param async данные о состоянии игры будут переданы этому обработчику обратной связи
     */
    void getGameState(AsyncCallback<GameState> async);

    void login(String name, AsyncCallback<Boolean> async);
    void pass(int cardsInDeck, AsyncCallback<Void> async);

    /**
     * Проверяет 3 карты на наличие в них сета
     *
     * @param set
     * @return true если есть
     * false если нет
     */
    void checkSet(Card[] set, AsyncCallback<Void> async);

    void isPassed(AsyncCallback<Boolean> async);

}
