package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.relex.intertrust.set.shared.GameState;

@RemoteServiceRelativePath("service")
public interface SetService extends RemoteService
{
    boolean login(String name);

    /**
     * @return возвращает описание состояния игры
     */
    GameState getGameState();

    //boolean checkSet(Card[] set);
    void exit();
    boolean pass(int cardsInDeck);
}

