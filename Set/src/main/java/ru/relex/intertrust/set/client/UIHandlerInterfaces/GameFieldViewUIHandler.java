package ru.relex.intertrust.set.client.uiHandlerInterfaces;

import ru.relex.intertrust.set.shared.Card;

public interface GameFieldViewUIHandler extends ExitGameUIHandler, ChangeModeUIHandler {
    /**
     * Интерфейс для безопасного
     * клиентского вызов метода checkSet из сервиса
     * имплементируется классом SetPresenter
     * см. описание в SetService
     */
    void checkSet(Card[] cards);

    /**
     * Интерфейс для безопасного
     * клиентского вызов метода pass из сервиса
     * имплементируется классом SetPresenter
     * см. описание в SetService
     */
    void pass(int count);
}
