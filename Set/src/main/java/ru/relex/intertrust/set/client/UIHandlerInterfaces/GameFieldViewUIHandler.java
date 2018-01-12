package ru.relex.intertrust.set.client.UIHandlerInterfaces;

import ru.relex.intertrust.set.shared.Card;

public interface GameFieldViewUIHandler extends ExitGameUIHandler {
    /**
     * Интерфейс для безопасного
     * клиентского вызов метода checkSet из сервиса
     * имплементируется классом SetPresenter
     * см. описание в SetService
     */
    void checkSet(Card[] cards);
}
