package ru.relex.intertrust.set.client.service;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

@RemoteServiceRelativePath("service")
public interface SetService extends RemoteService
{
    /**
     * Регистрация игрока в игре:
     * имя игрока проверяется на уникальность,
     * проверяется уникальность сессии и состояние игры
     * регистрация игрока и постановка его в режим ожидания игры,
     * если введенные данные корректны, игрок уже не зарегистрирован и игра еще не идет.
     * @param name имя игрока
     * @return true, если регистрация прошла успешно
     */
    boolean login(String name);

    /**
     * @return возвращает описание состояния игры
     */
    GameState getGameState();

    /**
     * Метод checkSet проверяет, являются ли полученные в параметре set карты сетом.
     * @param set принимает 3 карты от клиента
     * @return true, если это сет
     */
    boolean checkSet(Card[] set);

    /**
     * Выход из игры:
     * удаление игрока из списка игроков.
     * Осуществление проверки признаков конца игры:
     * если игроков нет, создается новая игра.
     */
    void exit();

    /**
     * Метод, реализующий ПАС:
     * добавление игрока в список спасовавших игроков
     * получение списка карт в колоде клиента для проверки состояния игрока.
     * @param cardsInDeck кол-во карт в колоде
     */
    void pass(int cardsInDeck);



}

