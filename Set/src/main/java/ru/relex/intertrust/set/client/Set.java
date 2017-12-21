package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;


public class Set implements EntryPoint {


	public void onModuleLoad() {
		LoginView loginBlock = new LoginView();
		RootPanel.get("gwt-wrapper").add(loginBlock);
	}

}
