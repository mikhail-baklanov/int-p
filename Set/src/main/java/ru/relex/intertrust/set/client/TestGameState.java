package ru.relex.intertrust.set.client;

import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.CardsDeck;
import ru.relex.intertrust.set.shared.GameState;

import java.util.List;

class TestGameState {

    /**
     * Имя текущего игрока
     */
    private static final String CURRENT_PLAYER_NAME = "Ivan";

    /**
     * Имя другого игрока
     */
    private static final String SAMPLE_PLAYER_NAME = "Someone";

    //region Game states

    /**
     * Начальное состояние игры, когда в ней еще нет игроков
     */
    static GameState getInitialGameState() {
        return new GameState();
    }

    /**
     * Начальное состояние игры, когда текущий игрок еще не зарегистрирован,
     * но другой игрок зарегистрирован, и на экране появляется таймер до начала игры
     */
    static GameState getInitialGameStateWithTimer() {
        return getGameStateWithPlayer(SAMPLE_PLAYER_NAME);
    }
    /**
     * Состояние игры, когда текущий игрок не зарегистрирован, но игра уже идет
     */
    static GameState getAnotherGameState() {
        GameState gameState = getGameStateWithPlayer(SAMPLE_PLAYER_NAME);
        initRunningGameState(gameState);
        return gameState;
    }

    /**
     * Состояние игры, когда текущий игрок зарегистрирован,
     * но идет ожидание других игроков
     */
    static GameState getWaitingGameState() {
        return getGameStateWithPlayer(CURRENT_PLAYER_NAME);
    }

    /**
     * Состояние игры, когда текущий игрок зарегистрирован, и игра уже идет
     */
    static GameState getRunningGameState() {
        GameState gameState = getWaitingGameState();
        initRunningGameState(gameState);
        return gameState;
    }
    //endregion

    /**
     * Функция, возвращающая имя текущего игрока
     * @return Имя текущего игрока
     */
    static String getCurrentPlayerName() {
        return CURRENT_PLAYER_NAME;
    }

    /**
     * Вспомогательная функция, возвращающая новое состояние игры
     * с добавленным игроком
     * @param playerName Имя игрока
     * @return Состояние игры с одним добавленным игроком
     */
    private static GameState getGameStateWithPlayer(String playerName) {
        GameState gameState = new GameState();
        gameState.addPlayer(playerName);
        gameState.setActivePlayers(gameState.getActivePlayers() + 1);
        return gameState;
    }

    /**
     * Вспомогательная функция инициализации запущенной игры
     * @param gameState Состояние запущенной игры
     */
    private static void initRunningGameState(GameState gameState) {
        List<Card> cardsDeck = new CardsDeck().startCardsDeck();
        gameState.setDeck(cardsDeck);
        gameState.setStart(true);
        gameState.setTime(1);
    }
}
