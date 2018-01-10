package ru.relex.intertrust.set.client.views.card;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.shared.Card;

public class CardView extends Composite {
    interface CardViewUiBinder extends UiBinder<Widget, CardView> {
    }

    private static CardView.CardViewUiBinder uiBinder = GWT.create(CardView.CardViewUiBinder.class);

    @UiField
    HTMLPanel cardContainer;

    private static String FILL[] = {"H", "O", "S"};
    private static String COLOR[] = {"g", "p", "r"};
    private static String SHAPE[] = {"D", "P", "S"};

    private Card card;

    public CardView(Card card) {
        this.card = card;
        initWidget(uiBinder.createAndBindUi(this));
        for (int i=1;i<=card.getShapeCount();i++) {
            cardContainer.add(new HTML(new SafeHtml() {
                @Override
                public String asString() {
                    return "<div class=\"shape\" style=\"background-image: url('images/icons/1"+
                            FILL[card.getFill()-1]+
                            COLOR[card.getColor()-1]+
                            SHAPE[card.getShape()-1]+".svg');\"></div>";
                }
            }));
        }
    }

    public Card getCard () {
        return card;
    }
}

