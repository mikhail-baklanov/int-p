package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.shared.GameState;

import java.util.ArrayList;
import java.util.List;

public class Set implements EntryPoint {

    /**
     * Период опроса сервера для получения значений таймера
     */
    private static final int REQUEST_PERIOD = 2000;

    /**
     * Текущее состояние игры
     */
    private GameState currentGameState;

    /**
     * Имя текущего игрока
     */
    private String playerName;

    /**
     * Обработчик для успешной регистрации пользователя.
     * Необходимо сохранить имя текущего пользователя
     */

    private static SetServiceAsync serviceAsync = GWT.create(SetService.class);

    public void onModuleLoad() {
        StartView startView = new StartView();
        RootPanel.get("gwt-wrapper").add(startView);
        List<String> list = new ArrayList<>();
        list.add("Surton присоединился");
        list.add("Sei4 присоединился");
        list.add("Damien присоединился");
        list.add("Aniki присоединился");
        list.add("Eneto присоединился");
        list.add("Zurbon присоединился");
        startView.setHistory(list);

        List<String> players = new ArrayList<>();
        players.add("Surton");
        players.add("Sei4");
        players.add("Damien");
        players.add("Boboka");
        players.add("Aniki");
        players.add("Eneto");
        players.add("Zurbon");
        List<Integer> scores = new ArrayList<>();
        scores.add(1300);
        scores.add(1250);
        scores.add(1200);
        scores.add(1151);
        scores.add(1101);
        scores.add(1052);
        scores.add(1002);
        startView.setStatistics(players, scores);

        startView.setTime("4:20");

        startView.setCardLeft(25);
    }

    public void onModuleLoad1() {
        final ContainerView containerView = new ContainerView();
        //gameView = new GameView();
        Timer timer = new Timer() {
            @Override
            public void run() {
                serviceAsync.getGameState(new AsyncCallback<GameState>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        consoleLog(caught.getMessage());
                    }

                    // Добавление нужного экрана для текущего состояния игры
                    @Override
                    public void onSuccess(GameState gameState) {
                        //TODO Сравнить gameState и currentGameState?
                        //Если состояние игры изменилось, то переключаем экран
                        currentGameState = gameState;
                        if (gameState.isStart()) {
                            if (gameState.getTime() > 0) {
                                containerView.setView(new AnotherGameView());
                                RootPanel.get("gwt-wrapper").add(containerView);
                            } else
                           /*
                            Если игра не началась, проверяем, зарегистрирован ли текущий пользователь.
                            Если пользователь не зарегистрирован, отображаем экран регистрации,
                            иначе отображаем экран ожидания других игроков с оставшимся временем до начала игры.
                            */
                                if (gameState.hasPlayer(playerName)) {
                                    containerView.setView(new PreGameView());
                                    RootPanel.get("gwt-wrapper").add(containerView);
                                }
                           /*
                            Игра идет. Если текущий игрок в ней зарегистрирован,
                            то будет отображен экран основной игры, в которой он может принять участие,
                            иначе будет отображен экран с информацией о начатой ранее игре.
                            */
                            //containerView.setView(gameState.hasPlayer(playerName) ? gameView : anotherGameView);
                        } else {
                            containerView.setView(new StartView()); //todo new LoginView()
                            RootPanel.get("gwt-wrapper").add(containerView);
                        }
                    }
                });
            }
        };
        timer.schedule(REQUEST_PERIOD);
        timer.run();
    }

    /**
     * Функция печати сообщения в консоль браузера.
     *
     * @param message сообщение, которое будет напечатано
     */
    native void consoleLog(String message) /*-{
        console.log(message);
    }-*/;
}