package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.shared.GameState;

public class Set implements EntryPoint {

	public void onModuleLoad() {
       LoginView loginBlock = new LoginView();
       RootPanel.get("gwt-wrapper").add(loginBlock);
    }

}