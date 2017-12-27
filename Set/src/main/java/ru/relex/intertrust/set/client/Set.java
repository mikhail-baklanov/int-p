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
                               containerView.setView(new LoginView());
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
     * @param message сообщение, которое будет напечатано
     */
    native void consoleLog(String message) /*-{
        console.log(message);
    }-*/;
}