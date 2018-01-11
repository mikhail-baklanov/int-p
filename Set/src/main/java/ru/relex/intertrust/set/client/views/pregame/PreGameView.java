package ru.relex.intertrust.set.client.views.pregame;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import ru.relex.intertrust.set.client.callback.ExitGameUIHandler;
import ru.relex.intertrust.set.client.constants.GameConstants;
import ru.relex.intertrust.set.client.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class PreGameView extends Composite {

    interface PreGameViewUiBinder extends UiBinder<Widget, PreGameView> {
    }
    private GameConstants gameConstants = GWT.create(GameConstants.class);
    private static PreGameViewUiBinder uiBinder = GWT.create(PreGameViewUiBinder.class);

    /**
     * Время, оставшееся до начала игры
     */
    @UiField
    SpanElement preGameTimer;

    /**
     * Контейнер для игроков, ожидающих начала игры
     */
    @UiField
    HTMLPanel playersContainer;


    @UiField
    SpanElement beforeGame;

    @UiField
    DivElement number;

    @UiField
    DivElement namePlayer;

    @UiField
    Button exitGame;
    private ExitGameUIHandler exitListener;
    /**
     * Список игроков, ожидающих начало игры
     */
    private List<String> players = new ArrayList<>();

    public PreGameView(ExitGameUIHandler exitListener) {
        this.exitListener = exitListener;
        initWidget(uiBinder.createAndBindUi(this));
        beforeGame.setInnerHTML(gameConstants.beforeGame());
        number.setInnerHTML(gameConstants.number());
        namePlayer.setInnerHTML(gameConstants.playerName());
        exitGame.setHTML(gameConstants.exitGame());
    }

    /**
     * Метод, обеспечивающий выход из ожидания начала игры
     * @param e событие нажатия мыши
     */
    @UiHandler("exitGame")
    public void onClick(ClickEvent e) {
        exitListener.exit();
    }

    /**
     * Вывод информации об оставшемся времени до начала игры
     * @param time время до начала игры в миллисекундах
     */
    public void setPreGameTimer(long time){
        preGameTimer.setInnerHTML(Utils.formatTime(time));
    }

    /**
     * Устанавливает список игроков, ожидающих начало игры
     * @param players список игроков
     */
    public void setPlayers (List<String> players) {
        if (!this.players.containsAll(players) || !players.containsAll(this.players)) {
            this.players = players;
            playersContainer.clear();
            for (int i = 0; i < players.size(); i++) {
                HTMLPanel widget = new HTMLPanel("<div class=\"game-started_players_item\">\n<div>" +
                        (i + 1) + "</div><div>" + players.get(i) + "</div>\n</div>");
                playersContainer.add(widget);
            }
        }
    }
}