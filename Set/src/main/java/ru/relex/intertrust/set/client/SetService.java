package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.relex.intertrust.set.shared.GameState;

@RemoteServiceRelativePath("SetService")
public interface SetService extends RemoteService
{
    void login(String name);

    /**
     * @return возвращает описание состояния игры
     */
    public GameState getGameState();

    //boolean checkSet(Card[] set);
    void exit();
    boolean pass(int cardsInDeck);
}

