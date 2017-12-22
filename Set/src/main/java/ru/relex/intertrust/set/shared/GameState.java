package ru.relex.intertrust.set.shared;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {

    private boolean isStart = false;
    private long time = -60000;
    private List<Card> deck;
    private List<Card> cardsOnDeck;
    private List<String> players;
    private List<Integer> score;
    private int countSets;

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
        return cardsOnDeck;
    }

    public void setCardsOnDeck(List<Card> cardsOnDeck) {
        this.cardsOnDeck = cardsOnDeck;
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
}
