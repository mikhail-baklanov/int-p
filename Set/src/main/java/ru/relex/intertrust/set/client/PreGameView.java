package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class PreGameView extends Composite {

    interface PreGameViewUiBinder extends UiBinder<Widget, PreGameView> {
    }

    private static PreGameViewUiBinder uiBinder = GWT.create(PreGameViewUiBinder.class);

    private static SetServiceAsync ourInstance = GWT.create(SetService.class);

    @UiField
    Button exitGame;

    @UiField
    SpanElement preGameTimer;

    @UiField
    HTMLPanel playersContainer;

    public PreGameView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Вывод информации об оставшемся времени до начала игры
     * @param time время до начала игры, представленное в виде строки
     */
    public void setPreGameTimer(String time){
        preGameTimer.setInnerHTML(time);
    }

    /**
     * Добавление списка с игроками на окно ожидания игры
     * @param widget виджет со списком игроков
     */
    public void fillPlauerTable(Widget widget){
        playersContainer.clear();
        playersContainer.add(widget);
    }
}
