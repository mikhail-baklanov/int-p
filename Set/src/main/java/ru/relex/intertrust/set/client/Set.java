package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

public class Set implements EntryPoint {

    /**
     * Период опроса сервера для получения значений таймера
     */
    private static final int REQUEST_PERIOD = 2000;

    private final ContainerView containerView = new ContainerView();
    private final AnotherGameView anotherGameView = new AnotherGameView();

    private String playerName;

    /**
     * Текущий экран
     */
    private Widget currentView;

    private static SetServiceAsync serviceAsync = GWT.create(SetService.class);

    private OnExitGameCallback exitGameCallback = new OnExitGameCallback() {
        @Override
        public void onExit() {
            serviceAsync.exit(new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable throwable) {
                    consoleLog(throwable.getMessage());
                }

                @Override
                public void onSuccess(Void aVoid) {
                    containerView.setView(loginView);
                    currentView = loginView;
                }
            });
        }
    };


    private final PreGameView preGameView = new PreGameView(exitGameCallback);

    /**
     * Обработчик для успешной регистрации пользователя.
     * Необходимо сохранить имя текущего пользователя
     */
    private OnLoginSuccessCallback loginCallback = name -> {
        playerName = name;
        containerView.setView(preGameView);
        currentView = preGameView;
    };

    private final LoginView loginView = new LoginView(loginCallback);


    public void onModuleLoad() {
        RootPanel.get("gwt-wrapper").add(containerView);
        requestServer();

        Timer timer = new Timer() {
           @Override
           public void run() {
               requestServer();
           }
       };
        timer.scheduleRepeating(REQUEST_PERIOD);
    }

    /**
     * Функция печати сообщения в консоль браузера.
     * @param message сообщение, которое будет напечатано
     */
    native void consoleLog(String message) /*-{
        console.log(message);
    }-*/;

    private void requestServer () {
        serviceAsync.getGameState(new AsyncCallback<GameState>() {
            @Override
            public void onFailure(Throwable caught) {
                consoleLog(caught.getMessage());
            }

            // Добавление нужного экрана для текущего состояния игры
            @Override
            public void onSuccess(GameState gameState) {
                Widget newView;
                if (gameState.isStart()) {
                    if (gameState.getTimer() > 0) {
                        newView = anotherGameView;
                    } else {
                        newView = loginView;
                    }
                } else {
                    if (playerName != null && gameState.hasPlayer(playerName)) {
                        preGameView.setPlayers(gameState.getPlayers());
                        newView = preGameView;
                    }
                    else
                        newView = loginView;
                }
                if (!newView.equals(currentView)) {
                    currentView = newView;
                    containerView.setView(currentView);
                }
            }
        });
    }
}