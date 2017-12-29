package ru.relex.intertrust.set.client;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

@RemoteServiceRelativePath("service")
public interface SetService extends RemoteService
{
    /**
     * Проверяет ищет есть ли еще игроки с таким же именем
     * @param name
     * @return true если нет игроков с таким именем
     *         false если есть
     */
    boolean login(String name);

    /**
     * @return возвращает описание состояния игры
     */
    GameState getGameState();

    /**
     * Проверяет 3 карты на наличие в них сета
     * @param set - 3 карты, отправленные клиентом
     * @return измененное состояние игры
     */
    void checkSet(Card[] set);

    /**
     *  выход игрока из игры и выход из режима ожидания игры
     *  т.к. оба эти действия приводят к одному результату
     */
    void exit();

    /**
     * Вызывается при нажатии кнопки пас
     * проверяет кол-во карт в деке игрока и на сервере
     * если одинаковое, то игрок ждет пока остальные не нажмут пас
     * или не найдут сет ,и на столе появятся новые карты
     * @param cardsInDeck
     */
    void pass(int cardsInDeck);

    /**
     * Для проверки перед каждым действием со столом
     * @return
     * Если игрок в списке спасовавших true
     * если нет false
     */
    boolean isPassed();


}

