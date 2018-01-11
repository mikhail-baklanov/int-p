package ru.relex.intertrust.set.client.views.anothergame;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.client.util.Utils;
import ru.relex.intertrust.set.shared.GameState;

import java.util.List;

public class AnotherGameView extends Composite {
    /**
     * Установка нового состояния игры
     * @param gameState новое состояние игры
     */
    public void setGameState(GameState gameState) {
        setAnotherGameTime(gameState.getTime());
        setAnotherGameCards(gameState.getDeck().size());
        setAnotherGamePlayers(gameState.getPlayers(), gameState.getScore());
    }

    interface AnotherGameViewUiBinder extends UiBinder<Widget, AnotherGameView> {
    }

    private static AnotherGameView.AnotherGameViewUiBinder uiBinder = GWT.create(AnotherGameView.AnotherGameViewUiBinder.class);
    /**
     * Время, прошедшее с начала игры
     */
    @UiField
    SpanElement anotherGameTime;

    /**
     * Количество оставшихся в колоде карт
     */
    @UiField
    SpanElement anotherGameCards;

    /**
     * Статистика каждого игрока
     */
    @UiField
    HTMLPanel anotherGamePlayers;

    public AnotherGameView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Устанавливает время, прошеднее с начала игры
     * @param time время
     */
    private void setAnotherGameTime(Long time) {
        this.anotherGameTime.setInnerHTML(Utils.formatTime(time));
    }

    /**
     * Устанавливает количество оставшихся в колоде карт
     * @param cardsLeft количество карт
     */
    private void setAnotherGameCards(int cardsLeft) {
        this.anotherGameCards.setInnerHTML("" + cardsLeft);
    }

    /**
     * Устанавливает статистику для каждого игрока
     * @param players список игроков
     * @param score список результатов
     */
    private void setAnotherGamePlayers(List<String> players, List<Integer> score) {
        anotherGamePlayers.clear();
        for (int i = 0; i < players.size(); i++) {
            HTMLPanel widget = new HTMLPanel("<div class=\"game-started_players_item\">\n<div>" +
                    players.get(i) + "</div><div>" + score.get(i) + "</div>\n</div>");
            anotherGamePlayers.add(widget);
        }
    }
}

