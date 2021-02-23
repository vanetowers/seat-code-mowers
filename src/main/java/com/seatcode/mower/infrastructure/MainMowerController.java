package com.seatcode.mower.infrastructure;

import com.seatcode.mower.application.GridController;

/**
 * Main entry class for the Movers Navigation App
 * The app will control the navigation of the mowers in a rectangular green grass plateau in order to cut the grass
 * and send to the SEAT Maintenance Office a complete view of the surrounding terrain.
 */
public class MainMowerController {

    private static FileController fileController;

    private static GridController gridController;

    private static String[] arguments;

    public static void main(String[] args) {
        try {
            setArguments(args);
            getFileController();
            getGridController().moveMowers();
            getGridController().printMowersFinalPosition();
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println(illegalArgumentException.getMessage());
        }
    }

    public static FileController getFileController() {
        if (fileController == null) {
            fileController = new FileController(getArguments());
        }
        return fileController;
    }

    public static GridController getGridController() {
        if (gridController == null) {
            gridController = new GridController(getFileController().getListInstructions());
        }
        return gridController;
    }

    private static String[] getArguments() {
        return arguments;
    }

    public static void setArguments(String[] args) {
        arguments = args;
    }

}
