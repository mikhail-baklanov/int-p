package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

public class LoginView extends Composite {

    interface LoginViewUiBinder extends UiBinder<Widget, LoginView> {
    }

    private static LoginViewUiBinder uiBinder = GWT.create(LoginViewUiBinder.class);

    @UiField
    Button submitLogin;

    @UiField
    SpanElement errorLogin;

    @UiField
    TextBox nicknameLogin;

    @UiField
    DivElement gameStartedLogin;

    public LoginView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("submitLogin")
    public void onClick(ClickEvent e) {
        errorLogin.addClassName("active");
    }

    @UiHandler("nicknameLogin")
    public void doClick(ClickEvent e) {
        errorLogin.removeClassName("active");
    }

}
