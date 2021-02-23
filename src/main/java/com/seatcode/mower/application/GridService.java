package com.seatcode.mower.application;

import java.awt.Point;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.seatcode.mower.domain.CardinalPoints;
import com.seatcode.mower.domain.Grid;
import com.seatcode.mower.domain.Movements;
import com.seatcode.mower.domain.Mower;

/**
 * Service class to handle actions for the navigation grid.
 */
public class GridService {

    private static final String SPACE = "[\\s]+";
    private static final int INDEX_GRID_CONFIGURATION_LINE = 0;
    private static final int INDEX_MOWERS_STARTING_LINE = 1;

    /**
     * This method returns the navigation grid created according the specific size, and all the mowers placed in the position specified.
     *
     * @param mowerService instance of the MowerService class
     * @param instructions list of instructions to move the mowers
     * @return the navigation grid with mowers positioned
     */
    public Grid createGridWithCustomSizeWithMowersPositioned(MowerService mowerService, List<String> instructions) {
        Optional<String[]> optionalLine = Optional.ofNullable(instructions.get(INDEX_GRID_CONFIGURATION_LINE)).map(v -> v.split(SPACE));
        if (optionalLine.isPresent()) {
            String[] size = optionalLine.get();
            if (size.length == 2) {
                List<String> mowersInstructions = instructions.subList(INDEX_MOWERS_STARTING_LINE, instructions.size());
                Map<Integer, Mower> mowersInPositions = getMowersInPositions(mowersInstructions, mowerService);
                if (mowersInPositions.size() > 0) {
                    return new Grid(Integer.parseInt(size[0]), Integer.parseInt(size[1]), mowersInPositions);
                } else {
                    throw new IllegalArgumentException("It was not possible to get the information for mowers correctly.");
                }
            } else {
                throw new IllegalArgumentException("It was not possible to get the information for grid correctly.");
            }
        } else {
            throw new IllegalArgumentException("It was not possible to create the grid correctly. Please verify the file structure.");
        }
    }

    /**
     * This method validates if the coordinate to move the mower is a valid position in the navigation grid.
     *
     * @param grid            the initial navigation grid
     * @param mowersInGridMap the current map
     * @param futurePosition  the future position to move
     * @return
     */
    public boolean isValidCoordinate(Grid grid, Map<Integer, Mower> mowersInGridMap, Point futurePosition) {
        int coordinateX = futurePosition.x;
        int coordinateY = futurePosition.y;
        return coordinateX > 0 && coordinateY > 0 && coordinateX <= grid.getSizeX() && coordinateY <= grid.getSizeY()
                && !grid.getGridMowerPositions().containsKey(futurePosition.hashCode()) && !mowersInGridMap
                .containsKey(futurePosition.hashCode());
    }

    /**
     * Method to print the final location of the mowers in the navigation grid.
     *
     * @param grid the navigation grid
     */
    public void printGrid(Grid grid) {
        grid.printFinalPositionMowersInGrid();
    }

    /**
     * Method to retrieve the initial location for mowers in the navigation grid.
     *
     * @param grid the navigation grid
     * @return the map with the starting position for mowers
     */
    public Map<Integer, Mower> getGridMowerPositions(Grid grid) {
        return grid.getGridMowerPositions();
    }

    /**
     * Method to retrieve the final location for the mowers in the navigation grid.
     *
     * @param grid the navigation grid
     * @return the map with the final positions for mowers
     */
    public Map<Integer, Mower> getGridMowerFinalPositions(Grid grid) {
        return grid.getGridMowerFinalPositions();
    }

    /**
     * Method to set the final location of the mowers in the navigation grid after movements.
     *
     * @param grid           the navigation grid
     * @param finalPositions the map with the final position for mowers
     */
    public void updateGridMowerFinalPositions(Grid grid, Map<Integer, Mower> finalPositions) {
        grid.setGridMowerFinalPositions(finalPositions);
    }

    /**
     * This method will place the mowers in their corresponding starting position.
     *
     * @param instructions the instructions with the positions and movements for the mowers
     * @param mowerService the instance of the MowerService class
     * @return the map with mowers placed in their starting positions.
     */
    private Map<Integer, Mower> getMowersInPositions(List<String> instructions, MowerService mowerService) {
        Point position;
        Map<Integer, Mower> mowersInPositions = new LinkedHashMap<>();
        Iterator<String> mowerIterator = instructions.iterator();
        while (mowerIterator.hasNext()) {
            String[] positionArray = getPositionInfo(mowerIterator.next());
            List<Movements> movements = getMovements(mowerIterator.next());
            if (positionArray.length > 0 && !movements.isEmpty()) {
                position = getPosition(Integer.parseInt(positionArray[0]), Integer.parseInt(positionArray[1]));
                Optional<Mower> mower = mowerService.createMower(position, getOrientation(positionArray[2]), movements);
                if (mower.isPresent() && !mowersInPositions.containsKey(position.hashCode())) {
                    mowersInPositions.put(position.hashCode(), mower.get());
                } else {
                    throw new IllegalArgumentException("It was not possible to create the mower with the information given in file.");
                }
            } else {
                throw new IllegalArgumentException("It was not possible to place mowers in grid due wrong information given in file.");
            }
        }
        return mowersInPositions;
    }

    private String[] getPositionInfo(String line) {
        Optional<String[]> positionInfo = Optional.ofNullable(line).map(v -> v.split(SPACE));
        if (positionInfo.isPresent()) {
            String[] position = positionInfo.get();
            if (position.length == 3) {
                return position;
            }
        }
        return new String[] {};
    }

    private Point getPosition(int coordinateX, int coordinateY) {
        return new Point(coordinateX, coordinateY);
    }

    private CardinalPoints getOrientation(String cardinalPoint) {
        if (!CardinalPoints.contains(cardinalPoint)) {
            throw new IllegalArgumentException("Invalid value for cardinal point: " + cardinalPoint);
        }
        return CardinalPoints.valueOf(cardinalPoint);
    }

    private List<Movements> getMovements(String line) {
        List<Movements> listMovements = new ArrayList<>();
        CharacterIterator characterIterator = new StringCharacterIterator(line);
        while (characterIterator.current() != CharacterIterator.DONE) {
            String movementValue = String.valueOf(characterIterator.current());
            if (!Movements.contains(movementValue)) {
                throw new IllegalArgumentException("Invalid value for movement: " + movementValue);
            }
            listMovements.add(Movements.valueOf(movementValue));
            characterIterator.next();
        }
        return listMovements;
    }

}
