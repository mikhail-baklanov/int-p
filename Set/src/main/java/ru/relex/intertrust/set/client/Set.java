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

    private static SetServiceAsync serviceAsync = GWT.create(SetService.class);

    /**
     * Обработчик для успешной регистрации пользователя
     */
    private OnLoginSuccessCallback loginCallback = new OnLoginSuccessCallback() {
        @Override
        public void onLogin(String name) {

        }
    };

    public void onModuleLoad() {
       LoginView loginBlock = new LoginView();
       RootPanel.get("gwt-wrapper").add(loginBlock);

       Timer timer = new Timer() {
           @Override
           public void run() {
               serviceAsync.getGameState(new AsyncCallback<GameState>() {
                   @Override
                   public void onFailure(Throwable caught) {
                       consoleLog(caught.getMessage());
                   }

                   @Override
                   public void onSuccess(GameState gameState) {
                       currentGameState = gameState;
                       // Добавление нужного экрана для текущего состояния игры
                       if (gameState.getTime() < 0) {
                           //TODO Add login view using container
                       } else if (gameState.isStart()){
                           // TODO Add game screen
                       }
                   }
               });
           }
       };
       timer.schedule(REQUEST_PERIOD);
    }

    /**
     * Функция печати сообщения в консоль браузера.
     * @param message сообщение, которое будет напечатано
     */
    native void consoleLog(String message) /*-{
        console.log(message);
    }-*/;
}