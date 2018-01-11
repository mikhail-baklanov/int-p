package ru.relex.intertrust.set.client.views.result;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import ru.relex.intertrust.set.client.callback.OnExitGameCallback;
import ru.relex.intertrust.set.client.util.Utils;
import ru.relex.intertrust.set.shared.GameState;

import java.util.List;

public class ResultView extends Composite {
    /**
     * Установка нового состояния игры
     * @param gameState новое состояние игры
     */
    public void setGameState(GameState gameState) {
        setResultGameTime(gameState.getTime());
        setResultSets(gameState.getCountSets());
        setResultGamePlayers(gameState.getPlayers(), gameState.getScore());
    }

    interface ResultViewUiBinder extends UiBinder<Widget, ResultView> {
    }

    private static ResultView.ResultViewUiBinder uiBinder = GWT.create(ResultView.ResultViewUiBinder.class);

    /**
     * Время игры
     */
    @UiField
    SpanElement resultGameTime;

    /**
     * Количество собранных сетов
     */
    @UiField
    SpanElement resultSets;

    /**
     * Контейнер с результатами игроков
     */
    @UiField
    HTMLPanel resultGamePlayers;

    /**
     * Слушатель нажатия на кнопку выхода из просмотра результатов
     */
    private OnExitGameCallback exitListener;

    public ResultView(OnExitGameCallback exitListener) {
        this.exitListener = exitListener;
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Метод, обеспечивающий выход из просмотра результатов
     * @param e событие нажатия мыши
     */
    @UiHandler("exitGame")
    public void onClick(ClickEvent e) {
        exitListener.onExit();
    }

    /**
     * Устанавливает время игры
     * @param time время игры в миллисекундах
     */
    private void setResultGameTime(Long time) {
        this.resultGameTime.setInnerHTML(Utils.formatTime(time));
    }

    /**
     * Устанавливает количество собранных сетов
     * @param numberOfSets количество сетов
     */
    private void setResultSets(int numberOfSets) {
        this.resultSets.setInnerHTML("" + numberOfSets);
    }

    /**
     * Устанавливает результаты
     * @param players список игроков
     * @param score список результатов для каждого из игроков
     */
    private void setResultGamePlayers(List<String> players, List<Integer> score) {
        resultGamePlayers.clear();
        for (int i = 0; i < players.size(); i++) {
            HTMLPanel widget = new HTMLPanel("<div class=\"game-started_players_item\">\n<div>" +
                    players.get(i) + "</div><div>" + score.get(i) + "</div>\n</div>");
            resultGamePlayers.add(widget);
        }
    }
}
