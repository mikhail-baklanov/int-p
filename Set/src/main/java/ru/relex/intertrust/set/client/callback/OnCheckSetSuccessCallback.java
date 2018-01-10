package ru.relex.intertrust.set.client.callback;

import ru.relex.intertrust.set.shared.Card;

import java.util.List;

public interface OnCheckSetSuccessCallback {
    void onCheckSet(Card[] cards);
}
