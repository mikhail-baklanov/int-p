package ru.relex.intertrust.set.shared;

import ru.relex.intertrust.set.shared.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CardsDeck - класс, содержащий информацию о колоде карт.
 */
public class CardsDeck {
    public static List<Card> getCardsDeck() {
        return cardsDeck;
    }

    private static List<Card> cardsDeck=new ArrayList<>();

    /**
     * Метод startCardDeck() собирает колоду из 81 неповторяющейся карты из класса Card
     * Collections.shuffle(cardsDeck) - перемешивает собранную колоду
     */

    public static void startCardsDeck() {
        for (int cardNumber=0;cardNumber<=80;cardNumber++) {
            Card card=new Card((int)Math.floor(cardNumber/27)+1,
                    (int)Math.floor((cardNumber%9)/3)+1,
                    (int)Math.floor((cardNumber%27)/9)+1,
                    cardNumber%3+1);
            cardsDeck.add(card);
        }
        Collections.shuffle(cardsDeck);
    }
}
