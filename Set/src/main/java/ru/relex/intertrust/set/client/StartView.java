package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

import java.util.ArrayList;
import java.util.List;

public class StartView extends Composite {

    GameState gs = null;

    private List<Card> cards = new ArrayList<>();

    interface StartViewUiBinder extends UiBinder<Widget, StartView>{
    }


    private static StartViewUiBinder uiBinder = GWT.create(StartViewUiBinder.class);

    private static SetServiceAsync ourInstance = GWT.create(SetService.class);

    public StartView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    HTMLPanel start;

    @UiField
    HTMLPanel cardContainer;

    @UiField
    FlowPanel historyContainer;

    @UiField
    FlowPanel statisticContainer;

    @UiField
    DivElement time;

    @UiField
    DivElement cardLeft;

    @UiField
    Button passButton;

    public void setTime(String time){
        this.time.setInnerHTML("<div>"+time+"</div>");
    }

    public void setStatistics(List<String> nickNames, List<Integer> scores){
        this.statisticContainer.clear();
        for (int i = 0; i < nickNames.size()-1; i++) {
            //todo Один или несколько стилей игнорируются компилятором - (конкретно не отображаются палочки между ником и его результатом)
            HTML player = new HTML("<div class=\"statistic-item\" style=\"margin: 10px 0;\"><span>"+nickNames.get(i)+"</span><span>"+scores.get(i)+"</span>\n</div>");
            this.statisticContainer.add(player);
            HTML separator = new HTML("<div class=\"seporator\"></div>");
            this.statisticContainer.add(separator);
        }
        HTML player = new HTML("<div class=\"statistic-item\">\n<span>"+nickNames.get(nickNames.size()-1)+"</span><span>"+scores.get(scores.size()-1)+"</span>\n</div>");
        this.statisticContainer.add(player);
    }

    public void setHistory(List<String> logs){
        this.historyContainer.clear();
        for (int i = 0; i < logs.size()-1; i++) {
            HTML player = new HTML("<div class=\"history-item\">"+logs.get(i)+"</div>");
            this.historyContainer.add(player);
            HTML separator = new HTML("<div class=\"seporator\"></div>");
            this.historyContainer.add(separator);
        }
        HTML player = new HTML("<div class=\"history-item\">"+logs.get(logs.size()-1)+"</div>");
        this.historyContainer.add(player);
    }

    public void setCardLeft(int cardLeftCount){
        this.cardLeft.setInnerHTML("<div>Карт в колоде: "+cardLeftCount+"</div>");
    }

    public void setCards(List<Card> cardsOnDesk){
        cardContainer.clear();
        for (int i = 0; i < cardsOnDesk.size(); i++) {
            CardView cardView = new CardView(cardsOnDesk.get(i));
            cardContainer.add(cardView);
        }
    }

    @UiHandler("passButton")
    public void onClick(ClickEvent e) {
        ourInstance.pass(gs.getDeck().size(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void setGameState(GameState gameState) {
        setStatistics(gameState.getPlayers(), gameState.getScore());
        setCardLeft(gameState.getDeck().size());
        if (!cards.containsAll(gameState.getCardsOnDesk()))
        {
            cards = gameState.getCardsOnDesk();
            setCards(gameState.getCardsOnDesk());
        }
        setTime(gameState.getTime()/60000+":"+(gameState.getTime()%60000)/1000);
    }
}
