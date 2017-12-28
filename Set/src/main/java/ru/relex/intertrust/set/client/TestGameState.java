package ru.relex.intertrust.set.client;

import ru.relex.intertrust.set.shared.GameState;

public class TestGameState {

    /**
     * Имя текущего игрока
     */
    private static final String CURRENT_PLAYER_NAME = "Ivan";

    /**
     * Имя другого игрока
     */
    private static final String SAMPLE_PLAYER_NAME = "Someone";

    /**
     * Начальное состояние игры, когда в ней еще нет игроков
     */
    public static GameState getGameState1() {
        return new GameState();
    }

    /**
     * Состояние игры, когда кто-то уже зарегистрировался; идет ожидание следующих игроков
     */
    public static GameState getGameState2() {
        GameState gameState = new GameState();
        addPlayer(gameState, SAMPLE_PLAYER_NAME);
        return gameState;
    }

    /**
     * Состояние игры, которая идет в данный момент, но текущий игрок в ней не участвует
     */
    public static GameState getGameState3() {
        GameState gameState = getGameState2();
        gameState.setStart(true);
        return gameState;
    }

    /**
     * Состояние игры, которая идет в данный момент, и текущий игрок в ней участвует
     */
    public static GameState getGameState4() {
        GameState gameState = getGameState3();
        gameState.addPlayer(CURRENT_PLAYER_NAME);
        return gameState;
    }

    /**
     * Вспомогательная функция добавления игрока в игру
     */
    private static void addPlayer(GameState gameState, String playerName) {
        gameState.addPlayer(playerName);
        gameState.setActivePlayers(gameState.getActivePlayers() + 1);
    }
}
