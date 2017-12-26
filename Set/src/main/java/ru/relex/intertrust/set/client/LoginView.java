package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
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
    DivElement gameStartedLogin;

    @UiField
    DivElement newPlayerLogin;

    @UiField
    DivElement gameStartTimeLogin;

    public LoginView() {
        initWidget(uiBinder.createAndBindUi(this));
        ourInstance.getGameState(new AsyncCallback<GameState>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert(throwable.getMessage());
            }
            @Override
            public void onSuccess(GameState gameState) {
                if (gameState.isStart()) {
                    if (gameState.getTime() == -60000) {
                        gameStartedLogin.addClassName("active");
                    }
                    else if (gameState.getTime() < 0) {
                        newPlayerLogin.addClassName("active");
                        gameStartTimeLogin.addClassName("active");
                    }
                } else {
                    newPlayerLogin.addClassName("active");
                }
            }
        });
    }

    @UiHandler("submitLogin")
    public void onClick(ClickEvent e) {
        errorLogin.addClassName("active");
        ourInstance.login(nicknameLogin.getValue(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert(throwable.getMessage());
            }

            @Override
            public void onSuccess(Boolean success) {
                Window.alert("login:"+success );
            }
        });
    }

    @UiHandler("nicknameLogin")
    public void doClick(ClickEvent e) {
        errorLogin.removeClassName("active");
    }

}
