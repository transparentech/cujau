package org.cujau.utils;

public class MathUtil {

    public static boolean eqWithDelta( double a, double b, double delta ) {
        if ( Double.compare( a, b ) == 0 ) {
            return true;
        }
        if ( !( Math.abs( a - b ) <= delta ) ) {
            return false;
        }
        return true;
    }

    /**
     * Is "a" less than or equal to "b", using the float fudge, "delta".
     * 
     * @param a
     * @param b
     * @param delta
     * @return
     */
    public static boolean ltEqWithDelta( double a, double b, double delta ) {
        if ( ( a - b ) <= delta ) {
            return true;
        }
        return false;
    }

    /**
     * Is "a" greater than or equal to "b", using the float fudge, "delta".
     * 
     * @param a
     * @param b
     * @param delta
     * @return
     */
    public static boolean gtEqWithDelta( double a, double b, double delta ) {
        if ( ( b - a ) <= delta ) {
            return true;
        }
        return false;
    }

}
