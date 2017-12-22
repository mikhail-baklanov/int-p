package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.server.GameState;


public class Set implements EntryPoint {

	public void onModuleLoad() {
	    if (!GameState.getInstance().isStart()) {
	        if (GameState.getInstance().getTime()==-60000) {
                LoginView loginBlock = new LoginView();
                RootPanel.get("gwt-wrapper").add(loginBlock);
            }
            else {//TODO добавить к LoginView обратный отсчет
            }
        }
        else {
	        //TODO подключить экран 3;
        }
	}


}
