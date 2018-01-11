package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import ru.relex.intertrust.set.client.callback.ExitGameUIHandler;
import ru.relex.intertrust.set.client.callback.GameFieldViewUIHandler;
import ru.relex.intertrust.set.client.callback.LoginViewUIHandler;
import ru.relex.intertrust.set.client.service.SetService;
import ru.relex.intertrust.set.client.service.SetServiceAsync;
import ru.relex.intertrust.set.client.views.anothergame.AnotherGameView;
import ru.relex.intertrust.set.client.views.container.ContainerView;
import ru.relex.intertrust.set.client.views.gamefield.GameFieldView;
import ru.relex.intertrust.set.client.views.login.LoginView;
import ru.relex.intertrust.set.client.views.pregame.PreGameView;
import ru.relex.intertrust.set.client.views.result.ResultView;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

import static ru.relex.intertrust.set.client.util.Utils.consoleLog;

public class SetPresenter implements ExitGameUIHandler, LoginViewUIHandler, GameFieldViewUIHandler {

    /**
     * Период опроса сервера для получения значений таймера
     */
    private static final int REQUEST_PERIOD = 1000;

    private final ContainerView containerView;
    private final AnotherGameView anotherGameView = new AnotherGameView();

    private String playerName;
    private LoginView loginView;
    private GameFieldView startView;
    private ResultView resultView;
    private PreGameView preGameView;


    /**
     * Текущий экран
     */
    private Widget currentView;

    private static SetServiceAsync serviceAsync = GWT.create(SetService.class);

    @Override
    public void exit() {
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


    public SetPresenter(ContainerView containerView) {
        this.containerView = containerView;

        loginView = new LoginView(this);
        startView = new GameFieldView(this);
        resultView = new ResultView(this);
        preGameView = new PreGameView(this);

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
                    preGameView.setPreGameTimer(gameStateTime);
                    preGameView.setPlayers(gameState.getPlayers());
                    newView = preGameView;
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

    @Override
    public void login(String name) {
        serviceAsync.login(name, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
                loginView.showLoginError();
            }

            @Override
            public void onSuccess(Boolean result) {
                playerName = name;
                currentView = preGameView;
                containerView.setView(currentView);
                requestServer();
            }
        });
    }

    @Override
    public void checkSet(Card[] cards) {
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
    }
}