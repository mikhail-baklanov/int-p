package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.shared.GameState;

public class Set implements EntryPoint {

    /**
     * Период опроса сервера для получения значений таймера
     */
    private static final int REQUEST_PERIOD = 1000;

    private final ContainerView containerView = new ContainerView();
    private final AnotherGameView anotherGameView = new AnotherGameView();

    private String playerName;
    private NextState nextState = new NextState();

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
                    currentView = loginView;
                    containerView.setView(currentView);
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

    private final LoginView loginView = new LoginView(loginCallback);

    private final StartView startView = new StartView(exitGameCallback);


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
    native static void consoleLog(String message) /*-{
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
                //gameState = getTestGameState();
                processGameState(gameState);
            }
        });
    }

    static class NextState{
        int counter = 0;
        int ticValue = 5;
        GameState states[] = {
                TestGameState.getInitialGameState(),
                TestGameState.getInitialGameStateWithTimer(),
                TestGameState.getAnotherGameState(),
                TestGameState.getWaitingGameState(),
                TestGameState.getRunningGameState()};
        int length = states.length;
        boolean isCurrentPlayerRegistered;
        int index=0;

        GameState get() {
            GameState gameState;
            isCurrentPlayerRegistered = index > 1;
            if (index >= length) {
                gameState = states[length - 1];
            } else {
                gameState = states[index];
                if (counter == 0){
                    index++;
                    consoleLog("Фейковое состояние №"+index);
                    if (index < length){
                        counter = ticValue;
                    }
                } else {
                    counter--;
                }
            }
            return gameState;
        }
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

    private GameState getTestGameState() {
        GameState gameState = nextState.get();
        playerName = nextState.isCurrentPlayerRegistered
                ? TestGameState.getCurrentPlayerName()
                : null;
        return gameState;
    }
}