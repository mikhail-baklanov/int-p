package ru.relex.intertrust.set.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {

    private boolean isStart = false; //флаг, показывающий, идет ли игра (true - игра идет, false - игра не начата)

    private long time = -TIME_TO_GAME;//серверное время
    private List<Card> deck;//колода
    private List<Card> cardsOnDesk =new ArrayList<>();//карты на столе
    private List<String> players = new ArrayList<>();//список игроков в игре
    private List<Integer> score = new ArrayList<>();//список с количеством очков каждого игрока
    private int countSets=0;//найдено сетов
    private int activePlayers=0;//активные игроки
    /**
     * Хранит имена игроков нажавших пас
     * Каждый раз когда игрок нажимает пас сюда добавляется его имя
     * При каждом изменении стола лист очищается
     */
    private List<String> notAbleToPlay = new ArrayList<>();

    private static final long TIME_TO_GAME = 10000;
    private static final long PERIOD_MS = 500;

    public int getActivePlayers() {
        return activePlayers;
    }

    public void setActivePlayers(int activePlayers) {
        this.activePlayers = activePlayers;
    }



    public boolean hasPlayer(String name) {
        for (String player: players)
            if (name.equals(player))
                return true;
        return false;
    };

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public List<Card> getCardsOnDesk() {
        return cardsOnDesk;
    }

    public void setCardsOnDesk(List<Card> cardsOnDeck) {
        this.cardsOnDesk = cardsOnDeck;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public List<Integer> getScore() {
        return score;
    }

    public void setScore(List<Integer> score) {
        this.score = score;
    }

    public int getCountSets() {
        return countSets;
    }

    public void setCountSets(int countSets) {
        this.countSets = countSets;
    }

    public void addScore(Integer scores){score.add(scores);}

    public void addPlayer(String name)
    {
        players.add(name);
        score.add(new Integer(0));
    }

    public List<String> getNotAbleToPlay() {return notAbleToPlay;}

    public void AddNotAbleToPlay(String name) {notAbleToPlay.add(name);}

    public void setAbleToPlay(List<String> notAbleToPlay) {this.notAbleToPlay=notAbleToPlay;}

    public void clearNotAbleToPlay() { notAbleToPlay.clear();}

    public void prepareTime() {
        setTime(-GameState.TIME_TO_GAME);
    }

    //убранные методы
    /**
     * Метод, который обновляет игровое время:
     * увеличивает время на PERIOD_MS миллисекунд при наличии хотя бы одного зарегистрировавшегося игрока
     * при отсутствии игрока время всегда равно -TIME_TO_GAME
     */
    public void updateOrPrepareGameTime(){
        if (getActivePlayers() == 0) {
            prepareTime();
        }
        setTime(getTime() + PERIOD_MS);
    }

    /**
     * Возвращает номер, по которому можно получить информацию об игроке в листах
     * @param nickname имя игрока
     * @return номер игрока
     */
    public int getPlayerNumber(String nickname){
        int i=0;
        while(nickname != getPlayers().get(i))
            i++;
        return i;
    }

    /**
     * Добавление нескольких карт на стол,
     * удаляет добавленные карты из колоды
     * @param amountOfCards количество добавляемых карт
     */
    public void addCards(int amountOfCards){
        for (int i = 0; i < amountOfCards; i++) {
            Card CardInDeck = getDeck().get(getDeck().size()-1);
            getCardsOnDesk().add(CardInDeck);
            getDeck().remove(CardInDeck);
        }
    }

    /**
     * Метод, добавляющий нового игрока в gameState
     * если игроков не было, инициализируется таймер
     * @param nickname имя игрока
     */
    public void createNewPlayer(String nickname){
        if (getActivePlayers()==0) { prepareTime(); }
        addPlayer(nickname);
        setActivePlayers(getActivePlayers()+1);
    }

    //поправить
    public void removePlayer(String nickname){
        int playerNumber = getPlayerNumber(nickname);
        setActivePlayers(getActivePlayers() - 1);
        if (!isStart()) {
            getPlayers().remove(playerNumber);
            getScore().remove(playerNumber);
        }
    }

    /**
     * Метод, инициализирующий начало игрового процесса
     * меняет флаг isStart на true, т.е. показывает, что игра уже идет
     * генерирует колоду карт (cardsDeck)
     * добавляет в карты, которые должны отображаться на экране двенадцать карт (в cardsOnDesk)
     * удаляет из колоды cardsDeck карты из cardsOnDesk
     * @param initialCardsNumber начальное кол-во карт на поле
     */
    public void startGame(int initialCardsNumber){
        setStart(true);
        List<Card> cardsDeck = new CardsDeck().startCardsDeck();
        setDeck(cardsDeck);
        addCards(initialCardsNumber);
    }


}
