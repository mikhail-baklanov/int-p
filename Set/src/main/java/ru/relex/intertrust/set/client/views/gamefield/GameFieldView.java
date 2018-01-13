package ru.relex.intertrust.set.client.views.gamefield;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.client.UIHandlerInterfaces.ExitGameUIHandler;
import ru.relex.intertrust.set.client.UIHandlerInterfaces.GameFieldViewUIHandler;
import ru.relex.intertrust.set.client.constants.GameConstants;
import ru.relex.intertrust.set.client.service.SetService;
import ru.relex.intertrust.set.client.service.SetServiceAsync;
import ru.relex.intertrust.set.client.util.Utils;
import ru.relex.intertrust.set.client.views.card.CardView;
import ru.relex.intertrust.set.client.views.container.ContainerView;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

import java.util.ArrayList;
import java.util.List;

import static ru.relex.intertrust.set.client.util.Utils.consoleLog;

public class GameFieldView extends Composite {

    interface StartViewUiBinder extends UiBinder<Widget, GameFieldView>{
    }

    interface GameFieldStyles extends CssResource {
        @ClassName("separator")
        String separator();

        @ClassName("statistic-item")
        String statisticItem();

        @ClassName("passed")
        String passed();
    }

    private GameConstants gameConstants = GWT.create(GameConstants.class);

    private static StartViewUiBinder uiBinder = GWT.create(StartViewUiBinder.class);

    private static SetServiceAsync ourInstance = GWT.create(SetService.class);

    /**
     *  Контейнер для виджетов карт.
     */
    @UiField
    HTMLPanel cardContainer;

    /**
     *  Контейнер для отображения собранных сетов.
     */
    @UiField
    SpanElement countOfSets;

    /**
     *  Контейнер для статистики игроков.
     */
    @UiField
    FlowPanel statisticContainer;

    /**
     *  Прошедшее время с начала игры.
     */
    @UiField
    DivElement time;

    /**
     *  Левый блок игрового поля.
     */
    @UiField
    HTMLPanel leftBar;

    /**
     *  Правый блок игрового поля.
     */
    @UiField
    HTMLPanel rightBar;

    /**
     *  Оставшиеся карты.
     */
    @UiField
    SpanElement cardLeft;

    /**
     *  Кнопка для скрытия правого бара.
     */
    @UiField
    SimplePanel slideButton;

    /**
     *  Кнопка для пасса.
     */
    @UiField
    Button passButton;

    /**
     *  Кнопка для выхода из игры.
     */
    @UiField
    Button exitGame;

    /**
     *  Контейнеры для констант.
     */
    @UiField
    DivElement statistic;

    @UiField
    SpanElement players;

    @UiField
    SpanElement gamePoints;

    @UiField
    SpanElement cardLeftSpan;

    @UiField
    SpanElement countOfSetsLabel;

    /**
     *  Необходимые для использования стили.
     */
    @UiField
    static GameFieldStyles style;

    /**
     *  Текущее состояние игры.
     */
    private GameState currentGameState = new GameState();

    /**
     *  Массив выбранных игроком карт.
     */
    private List<CardView> choosedCards = new ArrayList<>();

    /**
     *  Обработчик GameFieldView.
     */
    private GameFieldViewUIHandler uiHandler;

    public GameFieldView(GameFieldViewUIHandler uiHandler) {
        this.uiHandler = uiHandler;

        initWidget(uiBinder.createAndBindUi(this));
        setGameFieldConstants();

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

    @UiHandler("exitGame")
    public void onClickExit(ClickEvent e) {
        this.cardContainer.clear();
        uiHandler.exit();
    }

    @UiHandler("passButton")
    public void doClick(ClickEvent e) {
        ourInstance.pass(currentGameState.getDeck().size(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    /**
     *  Метод, который заполняет View статичным текстом.
     */
    private void setGameFieldConstants() {
        this.statistic.setInnerHTML(gameConstants.statistic());
        this.exitGame.setHTML(gameConstants.exitGame());
        this.players.setInnerHTML(gameConstants.players());
        this.gamePoints.setInnerHTML(gameConstants.gamePoints());
        this.passButton.setHTML(gameConstants.pass());
        this.cardLeftSpan.setInnerHTML(gameConstants.cardsInDeck() + ": ");
        this.countOfSetsLabel.setInnerHTML(gameConstants.setsCollected());
    }

    /**
     *  Метод, который актуализирует прошедшее с начала игры время.
     *  @param time время
     */
    private void setTime(String time){
        this.time.setInnerHTML(time);
    }

    /**
     *  Метод, который актуализирует количество оставшихся карт.
     *  @param cardLeftCount количество оставшихся карт
     */
    private void setCardLeft(int cardLeftCount){
        if (cardLeft.getInnerHTML().equals("") || Integer.parseInt(cardLeft.getInnerHTML()) != cardLeftCount)
        this.cardLeft.setInnerHTML("" + cardLeftCount);
    }

    /**
     *  Метод, который актуализирует статистику игроков.
     *  @param nickNames имена игроков
     *  @param scores баллы игроков
     */
    private void setStatistics(List<String> nickNames, List<Integer> scores){
        if(statisticContainer.getWidgetCount() == 0) {
            for (int i = 0; i < nickNames.size(); i++) {
                HTML player = new HTML("<span>" + nickNames.get(i) + "</span><span>" + scores.get(i) + "</span>");
                player.setStyleName(style.statisticItem());
                this.statisticContainer.add(player);

                HTML separator = new HTML("");
                separator.setStyleName(style.separator());
                this.statisticContainer.add(separator);
            }
        } else {
            List<Integer> oldScores = currentGameState.getScore();
            List<HTML> players = new ArrayList<>();
            for (int i = 0; i < statisticContainer.getWidgetCount(); i += 2)
                players.add((HTML) statisticContainer.getWidget(i));

            for(int i = 0; i < players.size(); i++) {
                if (oldScores.get(i) != scores.get(i)) {
                    players.get(i).setHTML("<span>" + nickNames.get(i) + "</span><span>" + scores.get(i) + "</span>");
                }
                if (currentGameState.getNotAbleToPlay() != null && currentGameState.getNotAbleToPlay().contains(nickNames.get(i))) {
                    HTML player = (HTML) statisticContainer.getWidget(i);
                    player.addStyleName(style.passed());
                } else {
                    HTML player = (HTML) statisticContainer.getWidget(i);
                    player.removeStyleName(style.passed());
                }
            }
        }
    }

    /**
     *  Метод, который актуализирует количество найденных сетов.
     *  @param findSets количество найденных сетов
     */
    private void setHistory(int findSets){
            if (!countOfSets.getInnerHTML().equals("" + findSets))
                countOfSets.setInnerHTML("" + findSets);
    }

    /**
     *  Метод, который актуализирует карты на столе.
     *  @param newCardsOnDesk актуальные карты на столе
     */
    private void setCards(List<Card> newCardsOnDesk){
        boolean isActual;
        for (int i = 0; i < cardContainer.getWidgetCount(); i++) {
            isActual = false;
            CardView cardOnDesk = (CardView) cardContainer.getWidget(i);
            for (Card newCard: newCardsOnDesk) {
                if (cardOnDesk.getCard().equals(newCard)) {
                    isActual = true;
                    break;
                }
            }
            if (!isActual)
                removeFromDesk(cardOnDesk);
        }

        for (Card newCard: newCardsOnDesk) {
            isActual = false;
            for (int i = 0; i < cardContainer.getWidgetCount(); i++) {
                CardView cardOnDesk = (CardView) cardContainer.getWidget(i);
                if (newCard.equals(cardOnDesk.getCard())) {
                    isActual = true;
                    break;
                }
            }
            if (!isActual)
                addOnDesk(new CardView(newCard));
        }

    }

    /**
     *  Метод, который удаляет View карту со стола.
     *  @param cardOnDesk карта
     */
    private void removeFromDesk(CardView cardOnDesk) {
        Timer timer = new Timer() {
            @Override
            public void run() {
                cardContainer.remove(cardOnDesk);
            }
        };
        cardOnDesk.getElement().addClassName("not-active");
        timer.schedule(300);
        choosedCards.remove(cardOnDesk);
    }

    /**
     *  Метод, который добавляет View карту на стол.
     *  @param card карта
     */
    private void addOnDesk(CardView card) {
        ClickHandler click = new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                chooseCard(clickEvent.getSource());
            }
        };
        Timer timer = new Timer() {
            @Override
            public void run() {
                card.getElement().addClassName("visible");
            }
        };
        card.sinkEvents(Event.ONCLICK);
        card.addHandler(click, ClickEvent.getType());
        cardContainer.add(card);
        timer.schedule(300);
    }

    /**
     *  Метод, актуализирует всю информацию.
     *  @param gameState серверное состояние игры
     */
    public void setGameState(GameState gameState) {
        setCardLeft(gameState.getDeck().size());
        setCards(gameState.getCardsOnDesk());
        setHistory(gameState.getCountSets());
        setStatistics(gameState.getPlayers(), gameState.getScore());
        setTime(Utils.formatTime(gameState.getTime()));
        currentGameState = gameState;
    }

    /**
     *  Метод, который добавляет карту в массив выбранных карт,
     *  при выборе трёх карт вызывает обработчик для проверки сета и очищает массив.
     *  @param widget карта
     */
    private void chooseCard (Object widget) {
        CardView card = (CardView) widget;
        if (!choosedCards.contains(card)) {
            card.getElement().addClassName("active");
            choosedCards.add(card);
            if (choosedCards.size() == 3) {
                Card[] cards = new Card[] {choosedCards.get(0).getCard(), choosedCards.get(1).getCard(), choosedCards.get(2).getCard()};
                uiHandler.checkSet(cards);
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
