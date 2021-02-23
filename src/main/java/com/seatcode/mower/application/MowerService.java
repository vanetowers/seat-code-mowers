package com.seatcode.mower.application;

import java.awt.Point;
import java.util.List;
import java.util.Optional;

import com.seatcode.mower.domain.CardinalPoints;
import com.seatcode.mower.domain.Movements;
import com.seatcode.mower.domain.Mower;

/**
 * Service class to handle actions for the mowers.
 */
public class MowerService {

    public Optional<Mower> createMower(Point position, CardinalPoints orientation, List<Movements> movements) {
        return Optional.ofNullable(new Mower(position, orientation, movements));
    }

    public Mower updateMowerPositionAndCardinalPoint(Mower mower, Point newPosition, CardinalPoints newCardinalPoint) {
        Mower mowerUpdated = mower;
        mowerUpdated.setFinalPosition(newPosition);
        mowerUpdated.setFinalCardinalPoint(newCardinalPoint);
        return mowerUpdated;
    }

    public Point getNextMoverPoint(CardinalPoints cardinalPoint, Point position) {
        Point newPosition = position;
        int coordinateX = position.x;
        int coordinateY = position.y;

        switch (cardinalPoint) {
        case N:
            newPosition = new Point(coordinateX, coordinateY + 1);
            break;
        case S:
            newPosition = new Point(coordinateX, coordinateY - 1);
            break;
        case E:
            newPosition = new Point(coordinateX + 1, coordinateY);
            break;
        case W:
            newPosition = new Point(coordinateX - 1, coordinateY);
            break;
        }
        return newPosition;
    }

    public CardinalPoints getNextCardinalPoint(CardinalPoints cardinalPoint, Movements movement) {
        switch (cardinalPoint) {
        case N:
            cardinalPoint = movement.equals(Movements.R) ? CardinalPoints.E : CardinalPoints.W;
            break;
        case S:
            cardinalPoint = movement.equals(Movements.R) ? CardinalPoints.W : CardinalPoints.E;
            break;
        case E:
            cardinalPoint = movement.equals(Movements.R) ? CardinalPoints.S : CardinalPoints.N;
            break;
        case W:
            cardinalPoint = movement.equals(Movements.R) ? CardinalPoints.N : CardinalPoints.S;
            break;
        }
        return cardinalPoint;
    }

}
