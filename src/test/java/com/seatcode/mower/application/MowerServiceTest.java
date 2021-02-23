package com.seatcode.mower.application;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.seatcode.mower.domain.CardinalPoints;
import com.seatcode.mower.domain.Movements;
import com.seatcode.mower.domain.Mower;

public class MowerServiceTest {

    private static final List<Movements> MOVEMENTS = Arrays
            .stream(new Movements[] { Movements.M, Movements.M, Movements.R, Movements.M, Movements.M, Movements.R, Movements.M,
                    Movements.R, Movements.R, Movements.M }).collect(Collectors.toList());

    MowerService mowerService;

    @Before
    public void setUp() {
        mowerService = new MowerService();
    }

    @Test
    public void createMower() {
        Assert.assertNotNull(mowerService);
        Point point = new Point(1, 2);
        Optional<Mower> mowerEvaluated = mowerService.createMower(point, CardinalPoints.N, MOVEMENTS);
        Optional<Mower> expectedMower = Optional.of(getMower(point, CardinalPoints.N));
        Assert.assertTrue(mowerEvaluated.isPresent());
        Assert.assertTrue(expectedMower.isPresent());

        Assert.assertEquals(expectedMower.get().getPosition(), mowerEvaluated.get().getPosition());
        Assert.assertEquals(expectedMower.get().getCardinalPoint(), mowerEvaluated.get().getCardinalPoint());
    }

    @Test
    public void updateMowerPositionAndCardinalPoint() {
        Assert.assertNotNull(mowerService);
        Point point = new Point(1, 2);
        Point nextPoint = new Point(1, 2);
        Mower mowerEvaluated = mowerService
                .updateMowerPositionAndCardinalPoint(getMower(point, CardinalPoints.N), nextPoint, CardinalPoints.S);
        Mower expectedMowerUpdated = getMower(nextPoint, CardinalPoints.S);
        Assert.assertEquals(expectedMowerUpdated.getPosition(), mowerEvaluated.getPosition());
        Assert.assertEquals(expectedMowerUpdated.getCardinalPoint(), mowerEvaluated.getCardinalPoint());
    }

    @Test
    public void getNextMoverPoint() {
        Assert.assertNotNull(mowerService);
        Point point = new Point(1, 2);
        Assert.assertEquals(new Point(1, 3), mowerService.getNextMoverPoint(CardinalPoints.N, point));
        Assert.assertEquals(new Point(1, 1), mowerService.getNextMoverPoint(CardinalPoints.S, point));
        Assert.assertEquals(new Point(2, 2), mowerService.getNextMoverPoint(CardinalPoints.E, point));
        Assert.assertEquals(new Point(0, 2), mowerService.getNextMoverPoint(CardinalPoints.W, point));
    }

    @Test
    public void checkNextCardinalPoint() {
        Assert.assertNotNull(mowerService);
        Assert.assertEquals(CardinalPoints.E, mowerService.getNextCardinalPoint(CardinalPoints.N, Movements.R));
        Assert.assertEquals(CardinalPoints.W, mowerService.getNextCardinalPoint(CardinalPoints.N, Movements.L));
        Assert.assertEquals(CardinalPoints.W, mowerService.getNextCardinalPoint(CardinalPoints.S, Movements.R));
        Assert.assertEquals(CardinalPoints.E, mowerService.getNextCardinalPoint(CardinalPoints.S, Movements.L));
        Assert.assertEquals(CardinalPoints.S, mowerService.getNextCardinalPoint(CardinalPoints.E, Movements.R));
        Assert.assertEquals(CardinalPoints.N, mowerService.getNextCardinalPoint(CardinalPoints.E, Movements.L));
        Assert.assertEquals(CardinalPoints.N, mowerService.getNextCardinalPoint(CardinalPoints.W, Movements.R));
        Assert.assertEquals(CardinalPoints.S, mowerService.getNextCardinalPoint(CardinalPoints.W, Movements.L));
    }

    private Mower getMower(Point point, CardinalPoints cardinalPoint) {
        return new Mower(point, cardinalPoint, MOVEMENTS);
    }

}