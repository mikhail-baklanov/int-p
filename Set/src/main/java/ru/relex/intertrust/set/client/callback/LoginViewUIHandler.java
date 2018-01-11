package ru.relex.intertrust.set.client.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginViewUIHandler {
    /**
     * Обработчик для регистрации пользователя.
     */
    void login(String name);
}
