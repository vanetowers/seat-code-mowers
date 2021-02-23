package com.seatcode.mower.application;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.seatcode.mower.domain.CardinalPoints;
import com.seatcode.mower.domain.Grid;
import com.seatcode.mower.domain.Movements;
import com.seatcode.mower.domain.Mower;

/**
 * Class to control the navigation of the mowers according to the instructions specified in the input file.
 */
public class GridController {

    private MowerService mowerService;
    private GridService gridService;
    private Grid grid;
    private Map<Integer, Mower> updatedMowersInGrid = new LinkedHashMap<>();

    public GridController(List<String> instructions) {
        if (!instructions.isEmpty()) {
            mowerService = getMowerService();
            gridService = getGridService();
            grid = gridService.createGridWithCustomSizeWithMowersPositioned(mowerService, instructions);
        } else {
            throw new IllegalArgumentException("No instructions found for mowers and pleateau.");
        }
    }

    /**
     * To move the mowers through the grid according to instructions given.
     */
    public void moveMowers() {
        gridService.getGridMowerPositions(grid).forEach((point, mower) -> updatedMowersInGrid =
                updateMowersInGridMap(updatedMowersInGrid, navigate(mower, updatedMowersInGrid)));
        gridService.updateGridMowerFinalPositions(grid, updatedMowersInGrid);
    }

    /**
     * To print the final positions of the mowers.
     */
    public void printMowersFinalPosition() {
        gridService.printGrid(grid);
    }

    /**
     * To print the final positions of the mowers.
     */
    public Map<Integer, Mower> getGridMowerFinalPositions() {
        return gridService.getGridMowerFinalPositions(grid);
    }

    /**
     * This method will update the navigation map according to the mower movement.
     *
     * @param mowersInGrid the current map
     * @param updatedMower mower in movement
     * @return the map updated
     */
    private Map<Integer, Mower> updateMowersInGridMap(Map<Integer, Mower> mowersInGrid, Mower updatedMower) {
        Map<Integer, Mower> mowersInGridMap = mowersInGrid;
        mowersInGridMap.put(updatedMower.getPosition().hashCode(), updatedMower);
        return mowersInGridMap;
    }

    /**
     * This method will apply the movements for each mower and will return the mower updated with the new position and orientation.
     * If the coordinate to move is not a valid coordinate, the mower will remain in the previous position.
     *
     * @param mower               the mower to move
     * @param updatedMowersInGrid the map with the updated information for mowers in movement
     * @return the mover updated in the new position and orientation
     */
    private Mower navigate(Mower mower, Map<Integer, Mower> updatedMowersInGrid) {
        Mower mowerUpdated = mower;
        Point position = mower.getPosition();
        CardinalPoints cardinalPoint = mower.getCardinalPoint();
        for (Movements futureMovement : mower.getMovements()) {
            if (futureMovement.equals(Movements.M)) {
                Point futurePosition = mowerService.getNextMoverPoint(cardinalPoint, position);
                if (gridService.isValidCoordinate(grid, updatedMowersInGrid, futurePosition)) {
                    mowerUpdated = mowerService.updateMowerPositionAndCardinalPoint(mower, futurePosition, cardinalPoint);
                    position = futurePosition;
                }
            } else {
                cardinalPoint = mowerService.getNextCardinalPoint(cardinalPoint, futureMovement);
            }
        }
        if (!cardinalPoint.equals(mower.getCardinalPoint())) {
            mowerUpdated = mowerService.updateMowerPositionAndCardinalPoint(mower, position, cardinalPoint);
        }
        return mowerUpdated;
    }

    private MowerService getMowerService() {
        if (mowerService == null) {
            mowerService = new MowerService();
        }
        return mowerService;
    }

    private GridService getGridService() {
        if (gridService == null) {
            gridService = new GridService();
        }
        return gridService;
    }

}
