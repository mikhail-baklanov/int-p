package ru.relex.intertrust.set.client.UIHandlerInterfaces;

public interface LoginViewUIHandler {
    /**
     * Интерфейс для безопасного
     * клиентского вызов метода login из сервиса
     * имплементируется классом SetPresenter
     * см. описание в SetService
     * */
    void login(String name);
}