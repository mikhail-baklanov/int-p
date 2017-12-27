package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

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

    private OnExitGameCallback exitListener;

    List<String> players = new ArrayList<>();

    public PreGameView(OnExitGameCallback exitListener) {
        this.exitListener = exitListener;
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("exitGame")
    public void onClick(ClickEvent e) {
        exitListener.onExit();
    }

    /**
     * Вывод информации об оставшемся времени до начала игры
     * @param time время до начала игры, представленное в виде строки
     */
    public void setPreGameTimer(String time){
        preGameTimer.setInnerHTML(time);
    }

    public void fillPlayerTable(){
        HTMLPanel widget = new HTMLPanel(" <div class=\"{style.game-started_players_item}\">\n" +
                "                        <div>Номер</div><div>Имя игрока</div>\n" +
                "                    </div>");
        playersContainer.add(widget);
    }

    public void setPlayers (List<String> players) {
        this.players.clear();
        this.players.addAll(players);
        fillPlayerTable();
    }

}
