package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.server.GameState;

public class Set implements EntryPoint {

    private static SetServiceAsync ourInstance = GWT.create(SetService.class);

    public void onModuleLoad() {
        LoginView loginBlock = new LoginView();
        RootPanel.get("gwt-wrapper").add(loginBlock);
        ourInstance.getGameState(new AsyncCallback<GameState>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(GameState gameState) {
                if (gameState.getTime() ==-60000) {
                    loginBlock.gameStartedLogin.addClassName("active");
                }
            }
        });
    }

}

