package ru.relex.intertrust.set.client;

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
import ru.relex.intertrust.set.shared.GameState;

import java.util.List;

public class ResultView extends Composite {
    public void setGameState(GameState gameState) {
        setResultGameTime(gameState.getTime());
        setResultSets(gameState.getCountSets());
        setResultGamePlayers(gameState.getPlayers(), gameState.getScore());
    }

    interface ResultViewUiBinder extends UiBinder<Widget, ru.relex.intertrust.set.client.ResultView> {
    }

    private static ru.relex.intertrust.set.client.ResultView.ResultViewUiBinder uiBinder = GWT.create(ru.relex.intertrust.set.client.ResultView.ResultViewUiBinder.class);

    @UiField
    SpanElement resultGameTime;

    @UiField
    SpanElement resultSets;

    @UiField
    HTMLPanel resultGamePlayers;

    @UiField
    Button exitGame;

    private OnExitGameCallback exitListener;

    public ResultView(OnExitGameCallback exitListener) {
        this.exitListener = exitListener;
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("exitGame")
    public void onClick(ClickEvent e) {
        exitListener.onExit();
    }

    public void setResultGameTime(Long time) {
        this.resultGameTime.setInnerHTML(Utils.formatTime(time));
    }

    public void setResultSets(int sets) {
        this.resultSets.setInnerHTML("" + sets);
    }

    public void setResultGamePlayers(List<String> players, List<Integer> score) {
        resultGamePlayers.clear();
        for (int i = 0; i < players.size(); i++) {
            HTMLPanel widget = new HTMLPanel("<div class=\"game-started_players_item\">\n" +
                    "                        <div>" + players.get(i) + "</div><div>" + score.get(i) + "</div>\n" +
                    "                    </div>");
            resultGamePlayers.add(widget);
        }
    }
}
