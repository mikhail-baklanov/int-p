package ru.relex.intertrust.set.client.views.pregame;

import com.google.gwt.core.client.GWT;
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
import ru.relex.intertrust.set.client.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class PreGameView extends Composite {

    interface PreGameViewUiBinder extends UiBinder<Widget, PreGameView> {
    }

    private static PreGameViewUiBinder uiBinder = GWT.create(PreGameViewUiBinder.class);

    @UiField
    Button exitGame;

    @UiField
    SpanElement preGameTimer;

    @UiField
    HTMLPanel playersContainer;

    private ExitGameUIHandler exitListener;

    private List<String> players = new ArrayList<>();

    public PreGameView(ExitGameUIHandler exitListener) {
        this.exitListener = exitListener;
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("exitGame")
    public void onClick(ClickEvent e) {
        exitListener.exit();
    }

    /**
     * Вывод информации об оставшемся времени до начала игры
     * @param time время до начала игры, представленное в виде строки
     */
    public void setPreGameTimer(long time){
        preGameTimer.setInnerHTML(Utils.formatTime(time));
    }

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
