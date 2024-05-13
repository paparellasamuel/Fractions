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
    void testInvalidFractionZeroDenominator(
            @ForAll @IntRange(min = Integer.MIN_VALUE) int numerator,
             @ForAll @IntRange(max = 0) int denominator)
    {
        // If the denominator is zero throw new arithmetic exception
        assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(numerator, denominator));

        //Statistics:
        try {
            Fraction.getReducedFraction(numerator, denominator);
            Statistics.collect("Exception Cases", "No Exception");
        } catch (ArithmeticException e) {
            Statistics.collect("Exception Cases", "ArithmeticException");
        }
    }

    // PBT2
    @Property
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void reducedFractionMustNotHaveZeroDenominatorOrNumerator(
            @ForAll @IntRange(min = Integer.MIN_VALUE + 1) int numerator,
             @ForAll @IntRange(min = Integer.MIN_VALUE + 1) int denominator)
    {
        // First condition: the denominator must not be zero
        // Second condition: the numerator must not be zero
        // A fraction that has a zero numerator doesn't have any mathematical sense
        Assume.that(denominator != 0 && numerator != 0);

        assertDoesNotThrow(() -> Fraction.getReducedFraction(numerator, denominator));


        // Statistics:
        Fraction reducedFraction = Fraction.getReducedFraction(numerator, denominator);

        // Collect statistics on the distribution of numerators and denominators
        Statistics.collect("Numerator", reducedFraction.getNumerator());
        Statistics.collect("Denominator", reducedFraction.getDenominator());

        // Collect statistics on the GCD of the input fractions
        int gcd = Fraction.greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
        Statistics.collect("GCD", gcd);

        /*
            This block of code checks if the original fraction (numerator/denominator) is equal to
            the reduced fraction.

             The isEqual variable is set to true if both the numerator and denominator of the original
             fraction are equal to those of the reduced fraction.
         */
        boolean isEqual = numerator == reducedFraction.getNumerator() && denominator == reducedFraction.getDenominator();

        /*
            This line collects the result of the equality check.

             If isEqual is true, it means that the original fraction and the reduced fraction are the
             same, otherwise it means that the original fraction and the reduced fraction are different.
         */
        Statistics.collect("Fraction Equality", isEqual);
    }

    //PBT3
    @Property()
    @Report(Reporting.GENERATED)
    void testMIN_VALUEOverflowOddNumeratorMIN_VALUEDenominator(
            @ForAll @IntRange(min = Integer.MIN_VALUE + 1) int numerator,
             @ForAll @IntRange(min = Integer.MIN_VALUE, max = Integer.MIN_VALUE) int denominator)
    {
        /*
            If the denominator is negative:
             1. it can be Integer.MIN_VALUE;

              They can't both be Integer.MIN_VALUE because of this condition:
              if (denominator == Integer.MIN_VALUE && (numerator & 1) == 0)
              {
                   numerator /= 2;
                   denominator /= 2;
              }

               In this way we make sure that both are not odd numbers
         */
        Assume.that ((numerator & 1) != 0);

        assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(numerator, denominator));

        //Statistics:
        try {
            Fraction.getReducedFraction(numerator, denominator);
            Statistics.collect("Exception Cases", "No Exception");
        } catch (ArithmeticException e) {
            Statistics.collect("Exception Cases", "ArithmeticException");
        }
    }

    // PBT4
    @Property
    @Report(Reporting.GENERATED)
    void testMIN_VALUEOverflowMIN_VALUENumeratorOddDenominator(
            @ForAll @IntRange(min = Integer.MIN_VALUE, max = Integer.MIN_VALUE) int numerator,
             @ForAll @IntRange(min = Integer.MIN_VALUE + 1, max = -1) int denominator)
    {
        /*
            If the denominator is negative:
             2. the numerator can be Integer.MIN_VALUE;

              They can't both be Integer.MIN_VALUE because of this condition:
              if (denominator == Integer.MIN_VALUE && (numerator & 1) == 0)
              {
                   numerator /= 2;
                   denominator /= 2;
              }

               In this way we make sure that both are not odd numbers
         */
        Assume.that((denominator & 1 ) != 0);

        assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(numerator, denominator));

        //Statistics:
        try {
            Fraction.getReducedFraction(numerator, denominator);
            Statistics.collect("Exception Cases", "No Exception");
        } catch (ArithmeticException e) {
            Statistics.collect("Exception Cases", "ArithmeticException");
        }
    }

    // PBT5
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
        */
        Assume.that(denominator < 0 && // Denominator is negative
                // Combine remaining denominator and numerator checks
                (denominator != Integer.MIN_VALUE && numerator != Integer.MIN_VALUE) ||
                (denominator == Integer.MIN_VALUE && (numerator != 0 && numerator % 2 == 0)));

        assertDoesNotThrow(() -> Fraction.getReducedFraction(numerator, denominator));
    }
}