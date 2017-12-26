package ru.relex.intertrust.set.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {

    private boolean isStart = false;
    private long time = -60000;
    private List<Card> deck;
    private List<Card> cardsOnDesk;
    private List<String> players = new ArrayList<>();
    private List<Integer> score;
    private int countSets;
    /**
     * Хранит состояния игроков
     * true=>может менять стол
     * false=>не может
     * false выставляется у каждого отдельного игрока при удачном нажатии на пас
     * true выставляется у всех игроков при каждом добавлении карт на стол
     */
    private List<Boolean> ableToPlay;

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

    public List<Card> getCardsOnDeck() {
        return cardsOnDesk;
    }

    public void setCardsOnDeck(List<Card> cardsOnDeck) {
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

    public void addPlayer(String name) {
        players.add(name);
    }

    public List<Boolean> getAbleToPlay() {return ableToPlay;}

    public void addAbleToPlay(Boolean isAble) {ableToPlay.add(isAble);}

    public void setAbleToPlay(List<Boolean> ableToPlay) {this.ableToPlay=ableToPlay;}
}
