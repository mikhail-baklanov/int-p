package ru.relex.intertrust.set.server;

import ru.relex.intertrust.set.shared.GameState;

public class Application {
    private static GameState game = new GameState();

    public static GameState getGameState() {
        return game;
    }

    public static void setStart() {
        game.setStart(true);
    }
}
