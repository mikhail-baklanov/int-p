package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import ru.relex.intertrust.set.client.UIHandlerInterfaces.ChangeModeUIHandler;
import ru.relex.intertrust.set.client.UIHandlerInterfaces.ExitGameUIHandler;
import ru.relex.intertrust.set.client.UIHandlerInterfaces.GameFieldViewUIHandler;
import ru.relex.intertrust.set.client.UIHandlerInterfaces.LoginViewUIHandler;
import ru.relex.intertrust.set.client.service.SetService;
import ru.relex.intertrust.set.client.service.SetServiceAsync;
import ru.relex.intertrust.set.client.views.GameStateComposite;
import ru.relex.intertrust.set.client.views.anothergame.AnotherGameView;
import ru.relex.intertrust.set.client.views.container.ContainerView;
import ru.relex.intertrust.set.client.views.gamefield.GameFieldView;
import ru.relex.intertrust.set.client.views.login.LoginView;
import ru.relex.intertrust.set.client.views.pregame.PreGameView;
import ru.relex.intertrust.set.client.views.result.ResultView;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

import static ru.relex.intertrust.set.client.util.Utils.consoleLog;

public class SetPresenter implements ExitGameUIHandler, LoginViewUIHandler, GameFieldViewUIHandler, ChangeModeUIHandler {

    /**
     * Период опроса сервера для получения значений таймера
     */
    private static final int REQUEST_PERIOD = 1000;

    private final ContainerView containerView;
    private boolean isAnotherGameView = true;
    private GameStateComposite anotherGameView = new AnotherGameView(this);

    private String playerName;
    private LoginView loginView;
    private GameFieldView startView;
    private ResultView resultView;
    private PreGameView preGameView;


    /**
     * Текущий экран
     */
    private Widget currentView;
    /**
     * 	Создание экземпляра класса взаимодействия с сервисом
     */
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


    SetPresenter(ContainerView containerView) {
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

    /**
     * Запрос состояния игры с сервера
     */
    private void requestServer () {
        serviceAsync.getGameState(new AsyncCallback<GameState>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(GameState gameState) {
                processGameState(gameState);
            }
        });
    }


    /**
     * Начальный выбор view и настройка отображающейся на ней информации.
     * Если игроков нет, то отображается loginView
     * Если игроки есть, то список игроков и время до начала игры
     *
     * @param gameState информация о состоянии игры
     */
    private void processGameState(GameState gameState) {
        long gameStateTime = gameState.getTime();
        Widget newView;
        if (gameState.isStart()) {
            if (gameStateTime >= 0) {
                if (hasCurrentPlayer(gameState)) {
                    startView.setGameState(gameState);
                    newView = startView;
                    if (gameState.getDeck().size() == 0 && !gameState.isStart()) {
                        newView = resultView;
                    }
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

    /**
     * Проверяется можно ли использовать данное имя игроку
     * @param gameState информация о состоянии игры
     * @return true,если можно использовать имя
     *         false,если имя null или уже есть на сервере
     */
    private boolean hasCurrentPlayer(GameState gameState) {
        return playerName != null && gameState.hasPlayer(playerName);
    }

    @Override
    public void login(String name) {
        serviceAsync.login(name, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(Boolean result) {
                if(!result)
                    loginView.showLoginError();
                else {
                    playerName = name;
                    currentView = preGameView;
                    containerView.setView(currentView);
                    requestServer();
                }
            }
        });
    }

    @Override
    public boolean checkSet(Card[] cards) {
        final boolean[] isSet = new boolean[1];
        serviceAsync.checkSet(cards, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(Boolean result) {
                requestServer();
                isSet[0] = result;

            }
        });
        return isSet[0];
    }

    @Override
    public void pass(int count) {
        serviceAsync.pass(count, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(Void aVoid) { requestServer(); }
        });
    }

    @Override
    public void changeMode() {
        if (isAnotherGameView){
            anotherGameView = new GameFieldView(this);
        } else
            anotherGameView = new AnotherGameView(this);
        isAnotherGameView = !isAnotherGameView;
    }

    @Override
    public boolean canChange(GameState gameState) {
        //Если текущего игрока нет на сервере, то можно менять режимы отображения
        return !hasCurrentPlayer(gameState);
    }
}