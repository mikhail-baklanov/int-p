package ru.relex.intertrust.set.shared;

import java.io.Serializable;

public class Card implements Serializable {
     private int color, shapeCount, fill, shape;

    public Card(int color, int shapeCount, int fill, int shape) {
        this.color = color;
        this.shapeCount = shapeCount;
        this.fill = fill;
        this.shape = shape;
    }

    public Card() { }
}