package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.client.callback.OnCheckSetSuccessCallback;
import ru.relex.intertrust.set.client.callback.OnExitGameCallback;
import ru.relex.intertrust.set.client.callback.OnLoginSuccessCallback;
import ru.relex.intertrust.set.client.service.SetService;
import ru.relex.intertrust.set.client.service.SetServiceAsync;
import ru.relex.intertrust.set.client.views.anothergame.AnotherGameView;
import ru.relex.intertrust.set.client.views.container.ContainerView;
import ru.relex.intertrust.set.client.views.gamefield.GameFieldView;
import ru.relex.intertrust.set.client.views.login.LoginView;
import ru.relex.intertrust.set.client.views.pregame.PreGameView;
import ru.relex.intertrust.set.client.views.result.ResultView;
import ru.relex.intertrust.set.shared.GameState;

import static ru.relex.intertrust.set.client.util.Utils.consoleLog;

public class Set implements EntryPoint {

    /**
     * Период опроса сервера для получения значений таймера
     */
    private static final int REQUEST_PERIOD = 1000;

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
                    playerName = null;
                    requestServer();
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
        currentView = preGameView;
        containerView.setView(currentView);
        requestServer();
    };

    private OnCheckSetSuccessCallback onCheckSet = cards -> {
        serviceAsync.checkSet(cards, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(Void aVoid) {
                requestServer();
            }
        });
    };

    private final LoginView loginView = new LoginView(loginCallback);

    private final GameFieldView startView = new GameFieldView(exitGameCallback, onCheckSet);

    private final ResultView resultView = new ResultView(exitGameCallback);


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


    private void requestServer () {
        serviceAsync.getGameState(new AsyncCallback<GameState>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            // Добавление нужного экрана для текущего состояния игры
            @Override
            public void onSuccess(GameState gameState) {
                //gameState = getTestGameState();
                processGameState(gameState);
            }
        });
    }



    private void processGameState(GameState gameState) {
        long gameStateTime = gameState.getTime();
        Widget newView;
        if (gameState.isStart()) {
            if (gameStateTime >= 0) {
                if (hasCurrentPlayer(gameState)) {
                    startView.setGameState(gameState);
                    newView = startView;
                } else {
                    anotherGameView.setGameState(gameState);
                    newView = anotherGameView;
                }
            }
            else {
                newView = loginView;
            }
        } else {
            if (hasCurrentPlayer(gameState)) {
//                if (gameState.getDeck().size() == 0 && gameState.getTime() > 0)
//                    newView = resultView;
//                else {
                    preGameView.setPreGameTimer(gameStateTime);
                    preGameView.setPlayers(gameState.getPlayers());
                    newView = preGameView;
//                }
            }
            else {
                if (gameStateTime < 0 && gameState.getActivePlayers() != 0)
                    loginView.setLoginTimer(gameStateTime);
                else
                    loginView.removeLoginTimer();
                newView = loginView;
            }
        }
        if (!newView.equals(currentView)) {
            currentView = newView;
            containerView.setView(currentView);
        }
    }

    private boolean hasCurrentPlayer(GameState gameState) {
        return playerName != null && gameState.hasPlayer(playerName);
    }
}