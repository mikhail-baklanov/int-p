package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.shared.GameState;

public class LoginView extends Composite {

    interface LoginViewUiBinder extends UiBinder<Widget, LoginView> {
    }

    private static LoginViewUiBinder uiBinder = GWT.create(LoginViewUiBinder.class);

    private static SetServiceAsync ourInstance = GWT.create(SetService.class);

    @UiField
    Button submitLogin;

    @UiField
    SpanElement errorLogin;

    @UiField
    TextBox nicknameLogin;

    @UiField
    SpanElement loginTimer;

    @UiField
    DivElement timeBlockLogin;

    /**
     * Обработчик события регистрации пользователя
     */
    private OnLoginSuccessCallback loginListener;

    public LoginView(OnLoginSuccessCallback loginListener) {
        initWidget(uiBinder.createAndBindUi(this));
        nicknameLogin.getElement().setAttribute("required", "true");
        this.loginListener = loginListener;

        KeyDownHandler returnKeyHandler = new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    submitLogin.click();
                }
            }
        };

        nicknameLogin.addKeyDownHandler(returnKeyHandler);
    }

    @UiHandler("submitLogin")
    public void onClick(ClickEvent e) {
        String name = nicknameLogin.getValue();
        ourInstance.login(name, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert(throwable.getMessage());
            }

            @Override
            public void onSuccess(Boolean success) {
                if (success)
                    loginListener.onLogin(name);
                else
                    errorLogin.addClassName("active");
            }
        });
    }

    @UiHandler("nicknameLogin")
    public void doClick(ClickEvent e) {
        errorLogin.removeClassName("active");
    }

    public void setLoginTimer(long time){
        timeBlockLogin.addClassName("active");
        loginTimer.setInnerHTML(Utils.formatTime(time));
    }

    public void removeLoginTimer(){
        timeBlockLogin.removeClassName("active");
    }
}
