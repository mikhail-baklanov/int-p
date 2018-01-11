package ru.relex.intertrust.set.client.views.gamefield;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.client.callback.OnCheckSetSuccessCallback;
import ru.relex.intertrust.set.client.callback.OnExitGameCallback;
import ru.relex.intertrust.set.client.constants.GameConstants;
import ru.relex.intertrust.set.client.service.SetService;
import ru.relex.intertrust.set.client.service.SetServiceAsync;
import ru.relex.intertrust.set.client.util.Utils;
import ru.relex.intertrust.set.client.views.card.CardView;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

import java.util.ArrayList;
import java.util.List;

import static ru.relex.intertrust.set.client.util.Utils.consoleLog;

public class GameFieldView extends Composite {

    private GameState gs = new GameState();

    private GameConstants gameConstants = GWT.create(GameConstants.class);

    private List<CardView> choosedCards = new ArrayList<>();

    interface StartViewUiBinder extends UiBinder<Widget, GameFieldView>{
    }

    private static HTML separator = new HTML("<div class=\"separator\"></div>");

    private static StartViewUiBinder uiBinder = GWT.create(StartViewUiBinder.class);

    private static SetServiceAsync ourInstance = GWT.create(SetService.class);

    public GameFieldView(OnExitGameCallback exitListener, OnCheckSetSuccessCallback checkSetSuccessCallback) {
        this.exitListener = exitListener;
        this.checkListener = checkSetSuccessCallback;
        initWidget(uiBinder.createAndBindUi(this));
        slideButton.sinkEvents(Event.ONCLICK);
        slideButton.addHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!leftBar.getElement().hasClassName("active")) {
                    leftBar.getElement().addClassName("active");
                    rightBar.getElement().addClassName("active");
                } else {
                    leftBar.getElement().removeClassName("active");
                    rightBar.getElement().removeClassName("active");
                }
            }
        }, ClickEvent.getType());
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
    HTMLPanel leftBar;

    @UiField
    DivElement cardLeft;

    @UiField
    SimplePanel slideButton;

    @UiField
    Button passButton;

    @UiField
    Button exitGame;

    @UiField
    HTMLPanel rightBar;

    private OnExitGameCallback exitListener;
    private OnCheckSetSuccessCallback checkListener;

    @UiHandler("exitGame")
    public void onClickExit(ClickEvent e) {
        this.cardContainer.clear();
        exitListener.onExit();
    }

    public void setTime(String time){
        this.time.setInnerHTML("<div>"+time+"</div>");
    }

    public void setStatistics(List<String> nickNames, List<Integer> scores){
        this.statisticContainer.clear();
        for (int i = 0; i < nickNames.size(); i++) {
            String s = "";
            if (gs.getNotAbleToPlay()!=null && gs.getNotAbleToPlay().contains(nickNames.get(i)))
                s = "background-color: red;";
            //todo Один или несколько стилей игнорируются компилятором - (конкретно не отображаются палочки между ником и его результатом)
            HTML player = new HTML("<div class=\"statistic-item\" style=\"margin: 10px 0;"+s+"\"><span>"+nickNames.get(i)+"</span><span>"+scores.get(i)+"</span>\n</div>");
            this.statisticContainer.add(player);
            this.statisticContainer.add(separator);
        }
//        String s = "";
//        HTML player = new HTML("<div class=\"statistic-item\" style=\"margin: 10px 0; \"><span>"+nickNames.get(nickNames.size()-1)+"</span><span>"+scores.get(scores.size()-1)+"</span>\n</div>");
//        this.statisticContainer.add(player);
    }


    public void setHistory(int findSets){
        this.historyContainer.clear();
//        for (int i = 0; i < logs.size()-1; i++) {
//            HTML player = new HTML("<div class=\"history-item\">"+logs.get(i)+"</div>");
//            this.historyContainer.add(player);
//            HTML separator = new HTML("<div class=\"seporator\"></div>");
//            this.historyContainer.add(separator);
//        }
//        HTML player = new HTML("<div class=\"history-item\">"+logs.get(logs.size()-1)+"</div>");
//        this.historyContainer.add(player);
        HTML sets = new HTML("<div class=\"history-item\">"+gameConstants.setsCollected()+": "+findSets+"</div>");
        this.historyContainer.add(sets);
        this.historyContainer.add(separator);
    }

    public void setCardLeft(int cardLeftCount){
        this.cardLeft.setInnerHTML("<div>Карт в колоде: "+cardLeftCount+"</div>");
    }

    public void setCards(List<Card> cardsOnDesk){
        ClickHandler click = new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                chooseCard(clickEvent.getSource());
            }
        };
        if(gs.getCardsOnDesk().size() == 0)
            for (int i = 0; i < cardsOnDesk.size(); i++) {
                CardView card = new CardView(cardsOnDesk.get(i));
                card.sinkEvents(Event.ONCLICK);
                card.addHandler(click, ClickEvent.getType());
                cardContainer.add(card);
            }
        else {
            boolean issetFlag = false;
            for (int i = 0; i < cardContainer.getWidgetCount(); i++) {
                for (int j = 0; j < cardsOnDesk.size(); j++) {
                    CardView cardOnTable = (CardView) cardContainer.getWidget(i);
                    if (cardOnTable.getCard().equals(cardsOnDesk.get(j))) {
                        issetFlag = true;
                        break;
                    }
                }
                if (!issetFlag) {
                    cardContainer.remove(cardContainer.getWidget(i));
                    choosedCards.remove(cardContainer.getWidget(i));
                }
                issetFlag = false;
            }

            for (int i = 0; i < cardsOnDesk.size(); i++) {
                for (int j = 0; j < gs.getCardsOnDesk().size(); j++) {
                    if (cardsOnDesk.get(i).equals(gs.getCardsOnDesk().get(j))) {
                        issetFlag = true;
                        break;
                    }
                }
                if (!issetFlag) {
                    CardView card = new CardView(cardsOnDesk.get(i));
                    card.sinkEvents(Event.ONCLICK);
                    card.addHandler(click, ClickEvent.getType());
                    cardContainer.add(card);
                }
                issetFlag = false;
            }
        }

    }

    @UiHandler("passButton")
    public void doClick(ClickEvent e) {
        ourInstance.pass(gs.getDeck().size(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void setGameState(GameState gameState) {
        setStatistics(gameState.getPlayers(), gameState.getScore());
        setCardLeft(gameState.getDeck().size());
        setCards(gameState.getCardsOnDesk());
        setHistory(gameState.getCountSets());
        setTime(Utils.formatTime(gameState.getTime()));
        gs = gameState;
    }

    private void chooseCard (Object widget) {
        CardView card = (CardView) widget;
        if (!choosedCards.contains(card)) {
            card.getElement().addClassName("active");
            choosedCards.add(card);
            if (choosedCards.size() == 3) {
                Card[] cards = new Card[] {choosedCards.get(0).getCard(), choosedCards.get(1).getCard(), choosedCards.get(2).getCard()};
                checkListener.onCheckSet(cards);
                for (CardView item: choosedCards)
                    item.getElement().removeClassName("active");
                choosedCards.clear();
            }
        } else {
            card.getElement().removeClassName("active");
            choosedCards.remove(card);
        }
    }
}
