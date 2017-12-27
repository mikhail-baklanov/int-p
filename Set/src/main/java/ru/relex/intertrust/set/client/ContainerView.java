package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;

public class ContainerView extends Composite {

    interface ContainerViewUiBinder extends UiBinder<Widget, ContainerView> {
    }

    private static ContainerView.ContainerViewUiBinder uiBinder = GWT.create(ContainerView.ContainerViewUiBinder.class);

    @UiField
    HTMLPanel containerPanel;

    public ContainerView () {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setView (Widget widget) {
        Timer timer = new Timer() {
            @Override
            public void run() {
                containerPanel.clear();
                containerPanel.add(widget);
                containerPanel.getElement().getStyle().setOpacity(1);
            }
        };

        if (containerPanel.getWidgetCount() != 0) {
            containerPanel.getElement().getStyle().setOpacity(0);
            timer.schedule(400);
        } else
            timer.run();
    }
}
