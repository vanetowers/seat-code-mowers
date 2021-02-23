package com.seatcode.mower.infrastructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileController {

    /**
     * Pattern that matches spaces repeated consecutively
     */
    private static final Pattern CONSECUTIVE_SPACES_PATTERN = Pattern.compile("[ ]+");

    /**
     * Pattern to validate the grid size information from file. Example 5 5
     */
    private static final String REGEX_GRIDSIZE = "(\\d)+(\\s){1}(\\d)+";

    /**
     * Pattern to validate the mower position information from file. Example: 3 3 E
     */
    private static final String REGEX_MOWER_POSITION = "(\\d)+(\\s){1}(\\d)+(\\s){1}[NSEW]{1}";

    /**
     * Pattern that match with a sequence of the letters: L, R and M, without spaces.
     */
    private static final String REGEX_MOWER_MOVEMENT = "[LRM]*";

    private static final String SPACE = " ";

    private List<String> listInstructions;
    private String specificErrorMessage;
    private List<String> positions;

    public FileController(String[] args) {
        readInputFileAndExtractInfo(args);
        if (!listInstructions.isEmpty()) {
            validateFileInfo();
        } else {
            throw new IllegalArgumentException("Empty file is not valid for this test. Please ensure the input.txt has valid lines.");
        }
    }

    /**
     * This method returns the list with all the valid instructions for mowers.
     *
     * @return the list
     */
    public List<String> getListInstructions() {
        return listInstructions;
    }

    /**
     * This method reads the input file and extract the information to move the mowers.
     *
     * @param args the file path
     * @throws IllegalArgumentException if the file path is not specified
     * @throws IllegalArgumentException if the file does not exists in the path specified
     * @throws IllegalArgumentException if the file is empty
     */
    private void readInputFileAndExtractInfo(String[] args) {
        if (args.length == 0 || args[0] == null) {
            throw new IllegalArgumentException("Any input file path specified.");
        }
        try (Stream<String> stringStream = Files.lines(Paths.get(args[0]))) {
            listInstructions = stringStream
                    .map(String::trim)
                    .filter(line -> line.length() > 0)
                    .map(line -> CONSECUTIVE_SPACES_PATTERN.matcher(line).replaceAll(SPACE))
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());
        } catch (NoSuchFileException noSuchFileException) {
            throw new IllegalArgumentException("File not found in the specified path.");
        } catch (IOException ioException) {
            throw new IllegalArgumentException("The file path specified do not exists.");
        }
        if (listInstructions.isEmpty()) {
            throw new IllegalArgumentException("The input file has no data to process.");
        }
    }

    private void validateFileInfo() {
        if (!hasValidGridSizeInformation()) {
            throw new IllegalArgumentException(
                    "First line in file should contain a pair of valid integers (both positive), separated by space to indicate the grid size. For instance: 5 5");
        }
        if (!hasValidPositionsAndMovements()) {
            throw new IllegalArgumentException(
                    "The input file has not the proper structure. Detailed message: " + specificErrorMessage);
        }
    }

    /**
     * Method to validate if the input file contains valid information to create the grid to navigate.
     *
     * @return the boolean
     */
    private boolean hasValidGridSizeInformation() {
        String firstLine = listInstructions.get(0);
        if (firstLine.isEmpty() || !firstLine.matches(REGEX_GRIDSIZE)) {
            return false;
        }
        String[] sizeArray = firstLine.split(SPACE);
        if (sizeArray.length != 2) {
            return false;
        }
        try {
            return isValidIntegerValue(sizeArray[0]) && isValidIntegerValue(sizeArray[1]);
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    /**
     * Method to validate if the input file contains valid information for mowers position and movements.
     *
     * @return the boolean
     */
    private boolean hasValidPositionsAndMovements() {
        int index = 1;
        positions = new ArrayList<>();
        while (index < listInstructions.size()) {
            boolean isOdd = (index % 2 != 0);
            String line = listInstructions.get(index);
            if (line.isEmpty() || (isOdd && !isValidPositionInfo(index, line)) || (!isOdd && !isValidMovementsInfo(index, line))) {
                return false;
            }
            index++;
        }
        return true;
    }

    /**
     * This method validates if the information for mower positions matches with the specific RegEx
     * Also, it will validate the mower position is not duplicated in file.
     *
     * @param index index to ensure the line contains information about mower position
     * @param line  string to validate
     * @return the boolean
     */
    private boolean isValidPositionInfo(int index, String line) {
        specificErrorMessage = "The position set in line " + (index + 1) + " has not a valid value.";
        if (!line.matches(REGEX_MOWER_POSITION)) {
            return false;
        }
        String[] position = line.split(SPACE);
        if (position.length < 3) {
            return false;
        }

        String coordinateX = position[0];
        String coordinateY = position[1];

        try {
            if (!isValidIntegerValue(coordinateX) || !isValidIntegerValue(coordinateY)) {
                return false;
            }
            //to avoid duplicates
            String positionToCheck = coordinateX + "," + coordinateY;
            if (positions.contains(positionToCheck)) {
                specificErrorMessage = "The position set in line " + (index + 1) + " is duplicated. Please verify.";
                return false;
            }
            positions.add(positionToCheck);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private boolean isValidIntegerValue(String valueToCheck) {
        int value = Integer.parseInt(valueToCheck);
        return value > 0 && value < Integer.MAX_VALUE;
    }

    /**
     * This method validates if the information for mower movements matches with the specific RegEx
     *
     * @param index index to ensure the line contains information about mower movements
     * @param line  string to validate
     * @return the boolean
     */
    private boolean isValidMovementsInfo(int index, String line) {
        if (!line.matches(REGEX_MOWER_MOVEMENT)) {
            specificErrorMessage = "The movements set in line " + (index + 1) + " have not a valid value.";
            return false;
        }
        return true;
    }

}
