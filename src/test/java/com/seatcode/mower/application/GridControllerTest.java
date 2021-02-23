package com.seatcode.mower.application;

import static org.mockito.Mockito.mock;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.seatcode.mower.domain.Mower;

public class GridControllerTest {

    private static final String VALID_FILE_PATH = "src/test/resources/validInputFile.txt";

    private static final String INVALID_FILE_PATH = "src/test/resources/movement_to_same_final_coordinate.txt";

    private GridController gridController;

    private ByteArrayOutputStream testOut;

    @Before
    public void setUp() {
        MowerService mowerService = mock(MowerService.class);
        GridService gridService = mock(GridService.class);
    }

    @Test
    public void whenMowerInstructionsAreCorrect_thenMowersShouldMove() {
        gridController = new GridController(getMockInstructions(VALID_FILE_PATH));
        Assert.assertNotNull(gridController);
        gridController.moveMowers();
        setUpOutput();
        gridController.printMowersFinalPosition();

        StringBuilder finalPositionPrinted = new StringBuilder();
        finalPositionPrinted.append("2 3 N").append(System.lineSeparator());
        finalPositionPrinted.append("5 1 E").append(System.lineSeparator());

        Assert.assertEquals(finalPositionPrinted.toString(), getOutput());
    }

    /**
     * It was assumed the movement won't be done if the next position is already busy (A Mower is already there).
     */
    @Test
    public void whenMowerShouldMoveToANotEmptyPosition_thenMowerWillSkipTheMovement() {
        setUpOutput();

        StringBuilder expectedFinalPositions = new StringBuilder();
        expectedFinalPositions.append("2 3 N").append(System.lineSeparator());
        expectedFinalPositions.append("5 1 E").append(System.lineSeparator());

        GridController gridControllerWhenMowersCanCompleteAllMovements = new GridController(getMockInstructions(VALID_FILE_PATH));
        Assert.assertNotNull(gridControllerWhenMowersCanCompleteAllMovements);
        gridControllerWhenMowersCanCompleteAllMovements.moveMowers();
        Assert.assertEquals(expectedFinalPositions.toString(), getFinalPositionsString(gridControllerWhenMowersCanCompleteAllMovements.getGridMowerFinalPositions()));

        expectedFinalPositions = new StringBuilder();
        expectedFinalPositions.append("2 2 N").append(System.lineSeparator());
        expectedFinalPositions.append("5 1 E").append(System.lineSeparator());
        expectedFinalPositions.append("1 5 N").append(System.lineSeparator());

        gridController = new GridController(getMockInstructions(INVALID_FILE_PATH));
        Assert.assertNotNull(gridController);
        gridController.moveMowers();
        Assert.assertEquals(expectedFinalPositions.toString(), getFinalPositionsString(gridController.getGridMowerFinalPositions()));
    }

    @Test
    public void verifyPrintMowers() {
        StringBuilder expectedFinalPositionPrinted = new StringBuilder();
        expectedFinalPositionPrinted.append("2 3 N").append(System.lineSeparator());
        expectedFinalPositionPrinted.append("5 1 E").append(System.lineSeparator());

        setUpOutput();

        gridController = new GridController(getMockInstructions(VALID_FILE_PATH));
        Assert.assertNotNull(gridController);
        gridController.moveMowers();
        gridController.printMowersFinalPosition();

        Assert.assertEquals(expectedFinalPositionPrinted.toString(), getOutput());
    }

    private List<String> getMockInstructions(String filePath) {
        List<String> instructions = new ArrayList<>();
        try {
            instructions = Files.lines(Paths.get(filePath)).collect(Collectors.toList());
        } catch (IOException ioException) {
            System.err.println("Exception while processing file - Detail: " + ioException.getMessage());
        }
        return instructions;
    }

    private String getFinalPositionsString(Map<Integer, Mower> gridMowerFinalPositions){
        AtomicReference<String> finalPositions = new AtomicReference<>("");
        gridMowerFinalPositions.forEach(
                (key, mower) -> {
                    Point position = mower.getPosition();
                    StringBuilder mowerFinalPosition = new StringBuilder()
                            .append(position.x)
                            .append(" ")
                            .append(position.y)
                            .append(" ")
                            .append(mower.getCardinalPoint());
                    finalPositions.set(finalPositions + mowerFinalPosition.toString() + System.lineSeparator());
                }
        );
        return finalPositions.get();
    }

    private void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private String getOutput() {
        return testOut.toString();
    }
}