package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class ContainerView extends Composite {

    interface ContainerViewUiBinder extends UiBinder<Widget, ContainerView> {
    }

    private static ContainerView.ContainerViewUiBinder uiBinder = GWT.create(ContainerView.ContainerViewUiBinder.class);

    @UiField
    SimplePanel simplePanel;

    public ContainerView () {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setView (Widget widget) {
        simplePanel.clear();
        simplePanel.add(widget);
    }
}
