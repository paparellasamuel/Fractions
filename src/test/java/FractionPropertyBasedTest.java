import it.uniba.itss2324.SAFR.homeworks.Fraction;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.statistics.Histogram;
import net.jqwik.api.statistics.Statistics;
import net.jqwik.api.statistics.StatisticsReport;

import static org.junit.jupiter.api.Assertions.*;

public class FractionPropertyBasedTest 
{
    // PBT1
    @Property
    @Report(Reporting.GENERATED)
    void testInvalidFractionZeroDenominator(@ForAll int numerator,
                                             @ForAll @IntRange(max = 0) int denominator)
    {
        // If the denominator is zero throw new arithmetic exception
        assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(numerator, denominator));
    }

    // PBT2
    @Property
    @Report(Reporting.GENERATED)
    void reducedFractionMustNotHaveZeroDenominatorOrNumerator(
            @ForAll @IntRange(min = Integer.MIN_VALUE + 1) int numerator,
             @ForAll @IntRange(min = Integer.MIN_VALUE + 1) int denominator)
    {
        // First condition: the denominator must not be zero
        // Second condition: the numerator must not be zero
        // A fraction that has a zero numerator doesn't have any mathematical sense
        Assume.that(denominator != 0 && numerator != 0);

        assertDoesNotThrow(() -> Fraction.getReducedFraction(numerator, denominator));
    }

    //PBT3
    @Property()
    @Report(Reporting.GENERATED)
    void test3(@ForAll @IntRange(min = Integer.MIN_VALUE) int numerator,
               @ForAll @IntRange(min = Integer.MIN_VALUE, max = -1) int denominator)
    {
        /*
            If the denominator is negative:
             1. it can be Integer.MIN_VALUE;
              2. the numerator can be Integer.MIN_VALUE;
               3. both can be Integer.MIN_VALUE.
         */
        Assume.that(numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE);

        assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(numerator, denominator));
    }

    // PBT4
    @Property
    @Report(Reporting.GENERATED)
    void testValidNegativeReducedFraction(
            @ForAll @IntRange(min = Integer.MIN_VALUE) int numerator,
             @ForAll @IntRange(min = Integer.MIN_VALUE, max = -1) int denominator)
    {
        /*
            Third condition: if the denominator is negative:
               1.1 it must not be Integer.MIN_VALUE;
                1.2 the numerator must not be Integer.MIN_VALUE;
                 1.3 both do not have to be equal to Integer.MIN_VALUE.
        */
        Assume.that(denominator < 0 && // Denominator is negative
                // Combine remaining denominator and numerator checks
                (denominator != Integer.MIN_VALUE && numerator != Integer.MIN_VALUE) ||
                (denominator == Integer.MIN_VALUE && (numerator != 0 && numerator % 2 == 0)));

        assertDoesNotThrow(() -> Fraction.getReducedFraction(numerator, denominator));
    }
}