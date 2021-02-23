package com.seatcode.mower.infrastructure;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainMowerControllerTest {

    private static final String VALID_INPUT_FILE_TXT = "src/test/resources/validInputFile.txt";

    private MainMowerController mainMowerController;

    private File validFile;

    private String[] args;

    private ByteArrayOutputStream testOut = null;

    @Before
    public void setUp() {
        args = new String[1];
        validFile = new File(VALID_INPUT_FILE_TXT);
    }

    @After
    public void restoreSystemInput() {
        System.setOut(System.out);
    }

    @Test
    public void whenFileSpecifiedIsValid_Then_MainShouldBeExecutedSuccessfully() {
        Arrays.fill(args, validFile.getAbsolutePath());

        mainMowerController = new MainMowerController();
        mainMowerController.setArguments(args);
        setUpOutput();

        mainMowerController.main(args);
    }

    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

}