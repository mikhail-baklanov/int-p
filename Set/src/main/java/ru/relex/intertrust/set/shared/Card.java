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

    public int getColor() {
        return color;
    }

    public int getShapeCount() {
        return shapeCount;
    }

    public int getFill() {
        return fill;
    }

    public int getShape() {
        return shape;
    }

    public Card() { }

    public boolean equals (Card inHand) {
        boolean isEqual=false;
        if (inHand.getColor()==color && inHand.getFill()==fill && inHand.getShape()==shape && inHand.getShapeCount()==shapeCount)
            isEqual=true;
        return isEqual;
    }
}