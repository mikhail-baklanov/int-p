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
     * initialGameState
     */
    public static GameState getGameState1() {
        IS_PLAYER_REGISTERED = false;
        return new GameState();
    }

    /**
     * Состояние игры, когда текущий игрок зарегистрировался; идет ожидание следующих игроков
     * waitingGameState
     */
    public static GameState getGameState2() {
        GameState gameState = new GameState();
        addPlayer(gameState, CURRENT_PLAYER_NAME);
        IS_PLAYER_REGISTERED = true;
        return gameState;
    }

    /**
     * Состояние игры, которая идет в данный момент, и текущий игрок в ней участвует
     * runningGameState
     */
    public static GameState getGameState3() {
        GameState gameState = getGameState2();
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
