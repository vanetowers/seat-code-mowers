package com.seatcode.mower.domain;

import java.awt.Point;
import java.util.Map;

/**
 * Class to represent the plateau as a grid.
 */
public class Grid {

    private static final String SPACE = " ";

    private int sizeX;
    private int sizeY;
    private Map<Integer, Mower> gridMowerPositions;
    private Map<Integer, Mower> gridMowerFinalPositions;

    public Grid(int sizeX, int sizeY, Map<Integer, Mower> mowersInPositions) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.gridMowerPositions = mowersInPositions;
        this.gridMowerFinalPositions = mowersInPositions;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public Map<Integer, Mower> getGridMowerPositions() {
        return gridMowerPositions;
    }

    public Map<Integer, Mower> getGridMowerFinalPositions() {
        return gridMowerFinalPositions;
    }

    public void setGridMowerFinalPositions(Map<Integer, Mower> gridMowerFinalPositions) {
        this.gridMowerFinalPositions = gridMowerFinalPositions;
    }

    public void printFinalPositionMowersInGrid() {
        gridMowerFinalPositions.forEach(
                (key, mower) -> {
                    Point position = mower.getPosition();
                    StringBuilder mowerFinalPosition = new StringBuilder()
                            .append(position.x)
                            .append(SPACE)
                            .append(position.y)
                            .append(SPACE)
                            .append(mower.getCardinalPoint());
                    System.out.println(mowerFinalPosition.toString());
                }
        );
    }

}
