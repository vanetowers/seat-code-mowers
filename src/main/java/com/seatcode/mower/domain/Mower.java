package com.seatcode.mower.domain;

import java.awt.Point;
import java.util.List;
import java.util.UUID;

/**
 * Class that represents a Mower
 */
public class Mower {

    private String id;
    private Point position;
    private CardinalPoints cardinalPoint;
    private List<Movements> movements;

    public Mower(Point mowerInitialPosition, CardinalPoints mowerInitialCardinalPoint, List<Movements> movements) {
        this.id = UUID.randomUUID().toString();
        this.position = mowerInitialPosition;
        this.cardinalPoint = mowerInitialCardinalPoint;
        this.movements = movements;
    }

    public String getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public CardinalPoints getCardinalPoint() {
        return cardinalPoint;
    }

    public List<Movements> getMovements() {
        return movements;
    }

    public void setFinalPosition(Point finalPosition) {
        this.position = finalPosition;
    }

    public void setFinalCardinalPoint(CardinalPoints finalCardinalPoint) {
        this.cardinalPoint = finalCardinalPoint;
    }

}
