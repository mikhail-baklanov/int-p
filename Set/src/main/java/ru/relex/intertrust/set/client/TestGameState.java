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
     * Флаг, показывающий, зарегистрирован ли текущий пользователь
     */
    private static boolean IS_PLAYER_REGISTERED = false;

    /**
     * Начальное состояние игры, когда в ней еще нет игроков
     */
    public static GameState getInitialGameState() {
        IS_PLAYER_REGISTERED = false;
        return new GameState();
    }

    /**
     * Состояние игры, когда текущий игрок еще не зарегистрирован, и идет ожидание игроков
     */
    public static GameState getWaitingGameState() {
        GameState gameState = getInitialGameState();
        addPlayer(gameState, SAMPLE_PLAYER_NAME);
        return gameState;
    }

    /**
     * Состояние игры, когда текущий игрок не зарегистрирован, но игра уже идет
     */
    public static GameState getAnotherGameState() {
        GameState gameState = getWaitingGameState();
        gameState.setStart(true);
        return gameState;
    }

    /**
     * Состояние игры, когда текущий игрок зарегистрировался; идет ожидание следующих игроков
     */
    public static GameState getWaitingWithCurrentGameState() {
        GameState gameState = new GameState();
        addPlayer(gameState, CURRENT_PLAYER_NAME);
        IS_PLAYER_REGISTERED = true;
        return gameState;
    }

    /**
     * Состояние игры, которая идет в данный момент, и текущий игрок в ней участвует
     * runningGameState
     */
    public static GameState getRunningGameState() {
        GameState gameState = getWaitingWithCurrentGameState();
        gameState.setStart(true);
        return gameState;
    }

    /**
     * Вспомогательная функция добавления игрока в игру
     */
    private static void addPlayer(GameState gameState, String playerName) {
        gameState.addPlayer(playerName);
        gameState.setActivePlayers(gameState.getActivePlayers() + 1);
    }

    public static String getCurrentPlayerName() {
        return CURRENT_PLAYER_NAME;
    }

    public static boolean isCurrentPlayerRegistered() {
        return IS_PLAYER_REGISTERED;
    }
}
