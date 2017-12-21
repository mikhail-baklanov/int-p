package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class LoginView extends Composite {

    interface LoginViewUiBinder extends UiBinder<Widget, LoginView> {
    }

    private static LoginViewUiBinder uiBinder = GWT.create(LoginViewUiBinder.class);

    LoginView() {
        initWidget(uiBinder.createAndBindUi(this));
    }


}
