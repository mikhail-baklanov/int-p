package ru.relex.intertrust.set.client.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.relex.intertrust.set.shared.Card;

import java.util.List;

public interface GameFieldViewUIHandler extends ExitGameUIHandler {
    void checkSet(Card[] cards);
}
