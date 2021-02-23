package com.seatcode.mower.infrastructure;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FileControllerTest {

    private static final String EMPTY_FILE = "src/test/resources/emptyInputFile.txt";
    private static final String VALID_FILE = "src/test/resources/validInputFile.txt";
    private static final String INVALID_GRID_SIZE_PATH = "src/test/resources/invalidGridSize.txt";
    private static final String INVALID_COORDINATE_PATH = "src/test/resources/invalidCoordinate.txt";
    private static final String INVALID_DUPLICATED_LINE_PATH = "src/test/resources/invalidDuplicatedLine.txt";
    private static final String INVALID_CARDINAL_POSITION_PATH = "src/test/resources/invalidCardinalPosition.txt";
    private static final String INVALID_MOVEMENTS_PATH = "src/test/resources/invalidMovements.txt";

    private static final String GRID_SIZE_ERROR = "First line in file should contain a pair of valid integers (both positive), separated by space to indicate the grid size. For instance: 5 5";
    private static final String COORDINATE_ERROR = "The input file has not the proper structure. Detailed message: The position set in line 2 has not a valid value.";
    private static final String DUPLICATED_LINE_ERROR = "The input file has not the proper structure. Detailed message: The position set in line 6 is duplicated. Please verify.";
    private static final String INVALID_CARDINAL_ERROR = "The input file has not the proper structure. Detailed message: The position set in line 2 has not a valid value.";
    private static final String INVALID_MOVEMENT_ERROR = "The input file has not the proper structure. Detailed message: The movements set in line 3 have not a valid value.";

    private FileController fileController;

    private String[] args;

    @Before
    public void sepUp() {
        args = new String[1];
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenNoArgumentSpecified_Then_ThrownException() {
        Arrays.fill(args, null);
        try {
            fileController = new FileController(args);
        } catch (IllegalArgumentException illegalArgumentException) {
            assertEquals("Any input file path specified.", illegalArgumentException.getMessage());
            throw illegalArgumentException;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenFileSpecifiedInArgumentsDoesNotExist_Then_ThrownException() {
        Arrays.fill(args, "somepath/somefile.txt");
        try {
            fileController = new FileController(args);
        } catch (IllegalArgumentException illegalArgumentException) {
            assertEquals("File not found in the specified path.", illegalArgumentException.getMessage());
            throw illegalArgumentException;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenFileSpecifiedInArgumentsIsEmpty_Then_ThrownException() {
        Arrays.fill(args, EMPTY_FILE);
        try {
            fileController = new FileController(args);
        } catch (IllegalArgumentException illegalArgumentException) {
            assertEquals("The input file has no data to process.", illegalArgumentException.getMessage());
            throw illegalArgumentException;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenValueForGridSizeIsNotValid_Then_ThrownException() {
        Arrays.fill(args, INVALID_GRID_SIZE_PATH);
        try {
            fileController = new FileController(args);
        } catch (IllegalArgumentException illegalArgumentException) {
            assertEquals(GRID_SIZE_ERROR, illegalArgumentException.getMessage());
            throw illegalArgumentException;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenValueForCoordinateIsNotValid_Then_ThrownException() {
        Arrays.fill(args, INVALID_COORDINATE_PATH);
        try {
            fileController = new FileController(args);
        } catch (IllegalArgumentException illegalArgumentException) {
            assertEquals(COORDINATE_ERROR, illegalArgumentException.getMessage());
            throw illegalArgumentException;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDuplicatedMowerPosition_Then_ThrownException() {
        Arrays.fill(args, INVALID_DUPLICATED_LINE_PATH);
        try {
            fileController = new FileController(args);
        } catch (IllegalArgumentException illegalArgumentException) {
            assertEquals(DUPLICATED_LINE_ERROR, illegalArgumentException.getMessage());
            throw illegalArgumentException;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenInvalidCardinalPositionValue_Then_ThrownException() {
        Arrays.fill(args, INVALID_CARDINAL_POSITION_PATH);
        try {
            fileController = new FileController(args);
        } catch (IllegalArgumentException illegalArgumentException) {
            assertEquals(INVALID_CARDINAL_ERROR, illegalArgumentException.getMessage());
            throw illegalArgumentException;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenInvalidMovementValue_Then_ThrownException() {
        Arrays.fill(args, INVALID_MOVEMENTS_PATH);
        try {
            fileController = new FileController(args);
        } catch (IllegalArgumentException illegalArgumentException) {
            assertEquals(INVALID_MOVEMENT_ERROR, illegalArgumentException.getMessage());
            throw illegalArgumentException;
        }
    }

    @Test
    public void whenFileSpecifiedInArgumentsExistsAndIsNotEmptyAndInformationIsValid_Then_ShouldWork() {
        Arrays.fill(args, VALID_FILE);
        fileController = new FileController(args);
        Assert.assertNotNull(fileController);
    }

}