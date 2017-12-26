package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.shared.GameState;

public class AnotherGameView extends Composite {
    interface AnotherGameViewUiBinder extends UiBinder<Widget, AnotherGameView> {
    }

    private static AnotherGameView.AnotherGameViewUiBinder uiBinder = GWT.create(AnotherGameView.AnotherGameViewUiBinder.class);

    @UiField
    Button exitGame;

    @UiField
    HTMLPanel anotherGame;

    public AnotherGameView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}

