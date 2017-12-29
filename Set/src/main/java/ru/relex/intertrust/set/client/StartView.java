package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

import java.util.ArrayList;
import java.util.List;

public class StartView extends Composite {

    GameState gs = new GameState();

    private List<Card> cards = new ArrayList<>();

    interface StartViewUiBinder extends UiBinder<Widget, StartView>{
    }

    private static HTML separator = new HTML("<div class=\"separator\"></div>");

    private static StartViewUiBinder uiBinder = GWT.create(StartViewUiBinder.class);

    private static SetServiceAsync ourInstance = GWT.create(SetService.class);

    public StartView(OnExitGameCallback exitListener) {
        this.exitListener = exitListener;
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

    @UiHandler("exitGame")
    public void onClickExit(ClickEvent e) {
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
        HTML sets = new HTML("<div>Сетов собрано: "+findSets+"</div>");
        this.historyContainer.add(sets);
    }

    public void setCardLeft(int cardLeftCount){
        this.cardLeft.setInnerHTML("<div>Карт в колоде: "+cardLeftCount+"</div>");
    }

    public void setCards(List<Card> cardsOnDesk){
        boolean issetFlag = false;
        if(gs.getCardsOnDesk().size() == 0)
            for (int i = 0; i < cardsOnDesk.size(); i++) {
                CardView cardView = new CardView(cardsOnDesk.get(i));
                cardContainer.add(cardView);
            }
        else {
            for (int i = 0; i < gs.getCardsOnDesk().size(); i++) {
                for (int j = 0; j < cardsOnDesk.size(); j++) {
                    if (gs.getCardsOnDesk().get(j).equals(cardsOnDesk.get(i))) {
                        issetFlag = true;
                        break;
                    }
                }
                if (!issetFlag)
                    cardContainer.remove(new CardView(cardsOnDesk.get(i)));
                issetFlag = false;
            }

            for (int i = 0; i < cardsOnDesk.size(); i++) {
                for (int j = 0; j < gs.getCardsOnDesk().size(); j++) {
                    if (cardsOnDesk.get(i).equals(gs.getCardsOnDesk().get(j))) {
                        issetFlag = true;
                        break;
                    }
                }
                if (!issetFlag)
                    cardContainer.add(new CardView(cardsOnDesk.get(i)));
                issetFlag = false;
            }
        }

    }

    @UiHandler("passButton")
    public void doClick(ClickEvent e) {
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
        setCards(gameState.getCardsOnDesk());
        setTime(Utils.formatTime(gameState.getTime()));
        gs = gameState;
    }
}
