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

    private static final int INITIAL_NUMBER_OF_CARDS = 12;
    private static final int MAX_NUMBER_OF_CARDS = 21;
    private static final int FINE = 5; //штраф
    private static final int REWARD = 3; //награда

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

    public void AddNotAbleToPlay(String name, int cardsInDeck) {
        if (cardsInDeck == getDeck().size() && !isPassed(name)) //если пас пришел вовремя, то добавляем имя паснувшнего в список
        {
            notAbleToPlay.add(name);
        }
    }

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

    /**
     * Удаление игрока из списка игроков:
     * удаление всех данных, если игра не начата
     * удаление с сохранением ника и счета, если игра идет
     * @param nickname
     */
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

    /**
     * Поиск игрока в списке спасовавших игроков
     * проверка выполняется перед каждым действием со столом
     * @param nickname имя игрока
     * @return true, если игрок спасовал
     */
    public boolean isPassed(String nickname){
        for(String str : getNotAbleToPlay())
            if(str.equals(nickname))return true;
        return false;
    }

    /**
     * Метод, осуществляющий пас:
     * проверка количества спасовавших
     * добавление карт на стол или завершение игры, если карт в колоде нет
     * @param nickname имя игрока
     */
    public void pass(String nickname){
        //if (getNotAbleToPlay().size() == (getPlayers().size() / 2) + 1)
        if (getNotAbleToPlay().size() == (getActivePlayers() / 2) + 1)
        {
            clearNotAbleToPlay();
            if (getDeck().size() == 0) {
                setStart(false);
            }//если все нажали на пас, а карт в деке нет, то заканчиваем игру
            else if(getCardsOnDesk().size() < MAX_NUMBER_OF_CARDS)
                addCards(3);
        }
    }

    /**
     * Проверка, являются ли выбранные карты сетом
     * если нет, - у клиента вычитаются очки
     * если являются, - идет проверка на то, есть ли в текущей игре на столе данные карты
     * если есть, - клиенту добавляются очки, а со стола удаляются данные карты и запускается проверка, остались ли карты в колоде
     * если остались - на стол добавляются 3 новые карты, и из колоды они соответственно удаляются
     * если в колоде не осталось карт, проверяется, есть ли карты на столе, если нет - игра заканчивается (isStart становится false).
     * @param set набор карт, выбранных игроком
     * @param user никнейм игрока
     * @return true, если это сет
     */
    public boolean checkSet(Card[] set, String user){
        if (set[0] != set[1]) {
            int playerNumber = getPlayerNumber(user);
            int oldScore = getScore().get(playerNumber);
            int[] summ = {0, 0, 0, 0};
            for (int i = 0; i <= 2; i++) {
                summ[0] += set[i].getColor();
                summ[1] += set[i].getShapeCount();
                summ[2] += set[i].getFill();
                summ[3] += set[i].getShape();
            }
            for (int i = 0; i <= 3; i++) {
                if (!(summ[i] == 3 || summ[i] == 6 || summ[i] == 9)) {
                    getScore().set(playerNumber, oldScore - FINE);
                    return false;
                }
            }
            if (!isPassed(user)) {
                int existSet = 0;
                List<Card> cardsOnDesk = getCardsOnDesk();
                for (Card c : set) {
                    if (cardsOnDesk.contains(c))
                        existSet++;
                }
                if (existSet != 3)
                    return false;
                else {
                    getScore().set(playerNumber, oldScore + REWARD);
                    setCountSets(getCountSets() + 1);
                    for (Card c : set)
                        getCardsOnDesk().remove(c);

                    if (getDeck().size() > 0 && getCardsOnDesk().size() < INITIAL_NUMBER_OF_CARDS) {
                        addCards(3);
                    } else {
                        if (getCardsOnDesk().size() == 0)
                            setStart(false);
                    }
                }
            }
        }
        return true;
    }
}
