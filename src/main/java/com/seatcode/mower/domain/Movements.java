package com.seatcode.mower.domain;

import java.util.Arrays;

/**
 * Class for the mowers possible instructions
 */
public enum Movements {
    L, R, M;

    public static boolean contains(String testedValue) {
        return Arrays.stream(values()).map(Enum::name).anyMatch(code -> code.equals(testedValue));
    }
}
