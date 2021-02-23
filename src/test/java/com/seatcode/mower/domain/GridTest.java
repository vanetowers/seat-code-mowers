package com.seatcode.mower.domain;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GridTest {

    private static final int SIZE_X = 5;

    private static final int SIZE_Y = 5;

    private static final List<Movements> MOVEMENTS_LIST = Arrays
            .stream(new Movements[] { Movements.L, Movements.M, Movements.R, Movements.M, Movements.L }).collect(Collectors.toList());

    private Mower mowerStartingPosition = new Mower(new Point(1, 2), CardinalPoints.N, MOVEMENTS_LIST);

    private Mower mowerFinalPosition = new Mower(new Point(2, 3), CardinalPoints.N, MOVEMENTS_LIST);

    private Map<Integer, Mower> gridMowerPositions = new LinkedHashMap<>();

    private Map<Integer, Mower> gridFinalMowerPositions = new LinkedHashMap<>();

    private Grid grid;

    private ByteArrayOutputStream testOut;

    @Before
    public void setUp() {
        gridMowerPositions.put(1, mowerStartingPosition);
        gridFinalMowerPositions.put(1, mowerFinalPosition);
        grid = new Grid(SIZE_X, SIZE_Y, gridMowerPositions);
        grid.setGridMowerFinalPositions(gridFinalMowerPositions);
    }

    @Test
    public void testSizeForCoordinateX() {
        Assert.assertEquals(SIZE_X, grid.getSizeX());
    }

    @Test
    public void testSizeForCoordinateY() {
        Assert.assertEquals(SIZE_Y, grid.getSizeY());
    }

    @Test
    public void testGridMowerStartingPositions() {
        Assert.assertEquals(gridMowerPositions, grid.getGridMowerPositions());
    }

    @Test
    public void testGridMowerFinalPositions() {
        Assert.assertNotNull(grid.getGridMowerFinalPositions());
        Assert.assertEquals(gridFinalMowerPositions, grid.getGridMowerFinalPositions());
    }

    @Test
    public void testPrintMowerFinalPositions() {
        setUpOutput();

        StringBuilder finalPositionPrinted = new StringBuilder();
        finalPositionPrinted.append("2 3 N").append(System.lineSeparator());

        grid.printFinalPositionMowersInGrid();

        assertEquals(finalPositionPrinted.toString(), getOutput());
    }

    private void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private String getOutput() {
        return testOut.toString();
    }

}