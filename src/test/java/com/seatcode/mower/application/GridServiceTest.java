package com.seatcode.mower.application;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.seatcode.mower.domain.CardinalPoints;
import com.seatcode.mower.domain.Grid;
import com.seatcode.mower.domain.Movements;
import com.seatcode.mower.domain.Mower;

public class GridServiceTest {

    private static final String VALID_FILE_PATH = "src/test/resources/validInputFile.txt";
    private static final String INVALID_FILE_MISSEDMOWERSINFO_PATH = "src/test/resources/invalidFileStructure.txt";
    private static final String INVALID_FILE_MISSEDGRIDINFO_PATH = "src/test/resources/invalidFileStructureMissingGridInfo.txt";
    private static final String ERROR_MESSAGE_GRID = "It was not possible to get the information for grid correctly.";
    private static final String ERROR_MESSAGE_MOWERS = "It was not possible to get the information for mowers correctly.";
    private static final List<Movements> MOVEMENTS = Arrays
            .stream(new Movements[] { Movements.M, Movements.M, Movements.R, Movements.M, Movements.M, Movements.R, Movements.M,
                    Movements.R, Movements.R, Movements.M }).collect(Collectors.toList());
    private static final String MOWER_CLASS = "com.seatcode.mower.domain.Mower";

    private GridService gridService;

    private MowerService mowerService;

    private ByteArrayOutputStream testOut = null;

    @Before
    public void setUp() {
        mowerService = mock(MowerService.class);
        gridService = new GridService();
    }

    @Test
    public void whenInstructionsInFileAreValid_thenGridShouldBeCreated() {
        Assert.assertNotNull(gridService);
        when(mowerService.createMower(any(), any(), any())).thenReturn(getMower(new Point(1, 2), CardinalPoints.N));
        Grid grid = gridService.createGridWithCustomSizeWithMowersPositioned(mowerService, getInstructions(VALID_FILE_PATH));
        Assert.assertNotNull(grid);
        Assert.assertEquals(5, grid.getSizeX());
        Assert.assertEquals(5, grid.getSizeY());

        Assert.assertNotNull(grid.getGridMowerPositions());
        Assert.assertEquals(2, grid.getGridMowerPositions().size());

        grid.getGridMowerPositions().forEach((key, value) -> {
            Assert.assertEquals(MOWER_CLASS, value.getClass().getName());
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenInfoForGridIsNotCorrect_thenGridShouldNotBeCreatedAndExceptionShouldBeThrown() {
        try {
            Assert.assertNotNull(gridService);
            Grid grid = gridService
                    .createGridWithCustomSizeWithMowersPositioned(mowerService, getInstructions(INVALID_FILE_MISSEDGRIDINFO_PATH));
        } catch (IllegalArgumentException illegalArgumentException) {
            assertEquals(ERROR_MESSAGE_GRID, illegalArgumentException.getMessage());
            throw illegalArgumentException;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenInfoForMowersIsNotCorrect_thenGridShouldNotBeCreatedAndExceptionShouldBeThrown() {
        try {
            Assert.assertNotNull(gridService);
            setUpOutput();
            Grid grid = gridService
                    .createGridWithCustomSizeWithMowersPositioned(mowerService, getInstructions(INVALID_FILE_MISSEDMOWERSINFO_PATH));
        } catch (IllegalArgumentException illegalArgumentException) {
            assertEquals(ERROR_MESSAGE_MOWERS, illegalArgumentException.getMessage());
            throw illegalArgumentException;
        }
    }

    @Test
    public void whenIsAnInvalidCoordinate_thenFalseBooleanIsReturned() {
        Assert.assertNotNull(gridService);
        when(mowerService.createMower(any(), any(), any())).thenReturn(getMower(new Point(1, 2), CardinalPoints.N));
        Grid grid = mock(Grid.class);

        Point futurePointNotValid = new Point(-1, -1);
        Assert.assertFalse(gridService.isValidCoordinate(grid, new HashMap<>(), futurePointNotValid));

        Point futurePointBiggerThanGridSize = new Point(5, 5);
        when(grid.getSizeX()).thenReturn(2);
        when(grid.getSizeY()).thenReturn(2);
        Assert.assertFalse(gridService.isValidCoordinate(grid, new HashMap<>(), futurePointBiggerThanGridSize));

        Map<Integer, Mower> mowersInGridMap = getMowersInGridMap();
        when(grid.getSizeX()).thenReturn(5);
        when(grid.getSizeY()).thenReturn(5);
        Assert.assertFalse(gridService.isValidCoordinate(grid, mowersInGridMap, new Point(2, 3)));

        when(grid.getGridMowerPositions()).thenReturn(mowersInGridMap);
        Assert.assertFalse(gridService.isValidCoordinate(grid, mowersInGridMap, new Point(1, 5)));
    }

    @Test
    public void whenIsAValidCoordinate_thenTrueBooleanIsReturned() {
        Assert.assertNotNull(gridService);
        when(mowerService.createMower(any(), any(), any())).thenReturn(getMower(new Point(1, 2), CardinalPoints.N));
        Grid grid = mock(Grid.class);

        Point futurePoint = new Point(1, 1);
        when(grid.getSizeX()).thenReturn(5);
        when(grid.getSizeY()).thenReturn(5);
        Assert.assertTrue(gridService.isValidCoordinate(grid, new HashMap<>(), futurePoint));

        Map<Integer, Mower> mowersInGridMap = getMowersInGridMap();
        when(grid.getSizeX()).thenReturn(5);
        when(grid.getSizeY()).thenReturn(5);
        Assert.assertTrue(gridService.isValidCoordinate(grid, mowersInGridMap, futurePoint));

        when(grid.getGridMowerPositions()).thenReturn(mowersInGridMap);
        Assert.assertTrue(gridService.isValidCoordinate(grid, mowersInGridMap, futurePoint));

    }

    @Test
    public void checkGridMowersFinalPosition() {
        Assert.assertNotNull(gridService);
        when(mowerService.createMower(any(), any(), any())).thenReturn(getMower(new Point(1, 2), CardinalPoints.N));
        Map<Integer, Mower> mowersInGridMap = getMowersInGridMap();
        Grid grid = mock(Grid.class);
        when(grid.getGridMowerFinalPositions()).thenReturn(mowersInGridMap);
        Assert.assertEquals(mowersInGridMap, gridService.getGridMowerFinalPositions(grid));

    }

    private List<String> getInstructions(String filePath) {
        List<String> instructions = new ArrayList<>();
        try {
            instructions = Files.lines(Paths.get(filePath)).collect(Collectors.toList());
        } catch (IOException ioException) {
            System.err.println("Exception while processing file - Detail: " + ioException.getMessage());
        }
        return instructions;
    }

    private Optional<Mower> getMower(Point point, CardinalPoints cardinalPoint) {
        return Optional.of(new Mower(point, cardinalPoint, MOVEMENTS));
    }

    private Map<Integer, Mower> getMowersInGridMap() {
        Map<Integer, Mower> mowersInGridMap = new LinkedHashMap<>();
        Point point1 = new Point(2, 3);
        mowersInGridMap.put(point1.hashCode(), getMower(point1, CardinalPoints.N).get());
        Point point2 = new Point(1, 5);
        mowersInGridMap.put(point2.hashCode(), getMower(point2, CardinalPoints.E).get());
        return mowersInGridMap;
    }

    private void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

}