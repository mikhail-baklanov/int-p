package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.server.GameState;


public class Set implements EntryPoint {

	public void onModuleLoad() {
        RequestBuilder request = new RequestBuilder(RequestBuilder.GET, "http://localhost:8888/Set.html");
        try {
            request.sendRequest(null, new RequestCallback(){
                @Override
                public void onResponseReceived(Request request, Response response) {

                    //GWT.log(response.getText());
                    // You get the response as a String so more processing required to convert/extract data
                }

                @Override
                public void onError(Request request, Throwable exception) {

                }
            });

        } catch (RequestException e) {
            e.printStackTrace();
        }
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
