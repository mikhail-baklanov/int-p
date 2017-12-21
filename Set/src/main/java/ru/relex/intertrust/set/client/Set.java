package ru.relex.intertrust.set.client;

//import ru.relex.intertrust.set.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;


public class Set implements EntryPoint 
{

	public void onModuleLoad() 
	{
		FlowPanel rootWidget = new FlowPanel();
		rootWidget.add(new Button("нажми на меня"));
		RootPanel.get("root").add(rootWidget);
	}
}
