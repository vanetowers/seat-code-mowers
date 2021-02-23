package com.seatcode.mower.domain;

import java.util.Arrays;

/**
 * Class for the cardinal points for the grid navigation
 */
public enum CardinalPoints {
    N, S, E, W;

    public static boolean contains(String testedValue) {
        return Arrays.stream(values()).map(Enum::name).anyMatch(point -> point.equals(testedValue));
    }

}
