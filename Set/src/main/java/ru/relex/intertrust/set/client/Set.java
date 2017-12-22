package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.shared.GameState;


public class Set implements EntryPoint {

    private static SetServiceAsync ss = (SetServiceAsync) GWT.create(SetService.class);

	public void onModuleLoad() {
        ss.getGameState(new AsyncCallback<GameState>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(GameState gameState) {
                if (!gameState.isStart()) {
                    if (gameState.getTime() == -60000) {
                        LoginView loginBlock = new LoginView();
                        RootPanel.get("gwt-wrapper").add(loginBlock);
                    } else {//TODO добавить к LoginView обратный отсчет
                    }
                } else {
                    //TODO подключить экран 3;
                }
            }
        });
    }
}
