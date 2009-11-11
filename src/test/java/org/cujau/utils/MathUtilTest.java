package org.cujau.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class MathUtilTest {

    public static final double DDELTA = 0.0001;
    public static final float FDELTA = 0.0001f;

    @Test
    public void testEqWithDelta() {
        assertTrue( MathUtil.eqWithDelta( 0.0, 0.0, DDELTA ) );
        assertTrue( MathUtil.eqWithDelta( 0.0f, 0.0f, FDELTA ) );

        assertTrue( MathUtil.eqWithDelta( 0.99, 0.99, DDELTA ) );
        assertTrue( MathUtil.eqWithDelta( 0.99f, 0.99f, FDELTA ) );

        assertTrue( MathUtil.eqWithDelta( 0.0001, 0.0, DDELTA ) );
        assertTrue( MathUtil.eqWithDelta( 0.0001f, 0.0f, FDELTA ) );

        assertTrue( MathUtil.eqWithDelta( 0.0, 0.0001, DDELTA ) );
        assertTrue( MathUtil.eqWithDelta( 0.0f, 0.0001f, FDELTA ) );

        assertFalse( MathUtil.eqWithDelta( 0.0, 0.00011, DDELTA ) );
        assertFalse( MathUtil.eqWithDelta( 0.0f, 0.00011f, FDELTA ) );

        assertFalse( MathUtil.eqWithDelta( 0.00011, 0.0, DDELTA ) );
        assertFalse( MathUtil.eqWithDelta( 0.00011f, 0.0f, FDELTA ) );

        assertFalse( MathUtil.eqWithDelta( 0.0001000001, 0.0, DDELTA ) );
        assertFalse( MathUtil.eqWithDelta( 0.0001000001f, 0.0f, FDELTA ) );

        assertFalse( MathUtil.eqWithDelta( 0.999, 1.0, DDELTA ) );
        assertFalse( MathUtil.eqWithDelta( 0.999f, 1.0f, FDELTA ) );

    }

    @Test
    public void testLtEqWithDelta() {
        assertTrue( MathUtil.ltEqWithDelta( 0.0, 0.0, DDELTA ) );
        assertTrue( MathUtil.ltEqWithDelta( 0.0f, 0.0f, FDELTA ) );

        assertTrue( MathUtil.ltEqWithDelta( 0.99, 0.99, DDELTA ) );
        assertTrue( MathUtil.ltEqWithDelta( 0.99f, 0.99f, FDELTA ) );

        assertTrue( MathUtil.ltEqWithDelta( 0.0001, 0.0, DDELTA ) );
        assertTrue( MathUtil.ltEqWithDelta( 0.0001f, 0.0f, FDELTA ) );

        assertTrue( MathUtil.ltEqWithDelta( 0.0, 0.0001, DDELTA ) );
        assertTrue( MathUtil.ltEqWithDelta( 0.0f, 0.0001f, FDELTA ) );

        assertTrue( MathUtil.ltEqWithDelta( -1.0, 0.00011, DDELTA ) );
        assertTrue( MathUtil.ltEqWithDelta( -1.0f, 0.00011f, FDELTA ) );

        assertTrue( MathUtil.ltEqWithDelta( -1.0003, 0.0001, DDELTA ) );
        assertTrue( MathUtil.ltEqWithDelta( -1.0003f, 0.0001f, FDELTA ) );

        assertTrue( MathUtil.ltEqWithDelta( 10.3456 - 10.3455997, 0.0, DDELTA ) );
        assertTrue( MathUtil.ltEqWithDelta( 10.3456f - 10.3455997f, 0.0f, FDELTA ) );

        assertTrue( MathUtil.ltEqWithDelta( 10.3456, 10.3455997, DDELTA ) );
        assertTrue( MathUtil.ltEqWithDelta( 10.3456f, 10.3455997f, FDELTA ) );

    }

    @Test
    public void testGtEqWithDelta() {
        assertTrue( MathUtil.gtEqWithDelta( 0.0, 0.0, DDELTA ) );
        assertTrue( MathUtil.gtEqWithDelta( 0.0f, 0.0f, FDELTA ) );

        assertTrue( MathUtil.gtEqWithDelta( 0.99, 0.99, DDELTA ) );
        assertTrue( MathUtil.gtEqWithDelta( 0.99f, 0.99f, FDELTA ) );

        assertTrue( MathUtil.gtEqWithDelta( 0.0001, 0.0, DDELTA ) );
        assertTrue( MathUtil.gtEqWithDelta( 0.0001f, 0.0f, FDELTA ) );

        assertTrue( MathUtil.gtEqWithDelta( 0.0001, 0.0, DDELTA ) );
        assertTrue( MathUtil.gtEqWithDelta( 0.0001f, 0.0f, FDELTA ) );

        assertTrue( MathUtil.gtEqWithDelta( 0.00011, -1.0, DDELTA ) );
        assertTrue( MathUtil.gtEqWithDelta( 0.00011f, -1.0f, FDELTA ) );

        assertTrue( MathUtil.gtEqWithDelta( 0.0001, -1.0003, DDELTA ) );
        assertTrue( MathUtil.gtEqWithDelta( 0.0001f, -1.0003f, FDELTA ) );

        assertTrue( MathUtil.gtEqWithDelta( 0.0, 10.3456 - 10.3455997, DDELTA ) );
        assertTrue( MathUtil.gtEqWithDelta( 0.0f, 10.3456f - 10.3455997f, FDELTA ) );

        assertTrue( MathUtil.gtEqWithDelta( 10.3456, 10.3455997, DDELTA ) );
        assertTrue( MathUtil.gtEqWithDelta( 10.3456f, 10.3455997f, FDELTA ) );

    }

}
