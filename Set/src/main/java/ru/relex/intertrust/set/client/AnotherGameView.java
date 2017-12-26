package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class AnotherGameView extends Composite {
    interface AnotherGameViewUiBinder extends UiBinder<Widget, AnotherGameView> {
    }

    private static AnotherGameView.AnotherGameViewUiBinder uiBinder = GWT.create(AnotherGameView.AnotherGameViewUiBinder.class);

    @UiField
    Button exitGame;

    @UiField
    SpanElement anotherGameTime;

    @UiField
    SpanElement anotherGameCards;

    @UiField
    HTMLPanel anotherGamePlayers;

    public AnotherGameView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setAnotherGameTime(String time) {
        this.anotherGameTime.setInnerHTML(time);
    }

    public void setAnotherGameCards(String cards) {
        this.anotherGameCards.setInnerHTML(cards);
    }

    public void setAnotherGamePlayers(Widget players) {
        this.anotherGamePlayers.add(players);
    }
}

