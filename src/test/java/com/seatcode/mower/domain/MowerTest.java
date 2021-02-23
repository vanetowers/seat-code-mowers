package com.seatcode.mower.domain;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MowerTest {

    private final static Point TEST_POINT = new Point(1, 2);

    private final static CardinalPoints TEST_CARDINAL_POINT = CardinalPoints.N;

    private final static List<Movements> MOVEMENTS_LIST = Arrays
            .stream(new Movements[] { Movements.L, Movements.M, Movements.L, Movements.M, Movements.L, Movements.M, Movements.L,
                    Movements.M, Movements.M }).collect(Collectors.toList());

    private Mower mower;

    @Before
    public void setUp() {
        mower = new Mower(TEST_POINT, TEST_CARDINAL_POINT, MOVEMENTS_LIST);
    }

    @Test
    public void testMowerPosition() {
        Assert.assertEquals(TEST_POINT, mower.getPosition());
    }

    @Test
    public void testMowerCardinalPoint() {
        Assert.assertEquals(TEST_CARDINAL_POINT, mower.getCardinalPoint());
    }

    @Test
    public void testMowerMovements() {
        Assert.assertEquals(MOVEMENTS_LIST, mower.getMovements());
    }

}