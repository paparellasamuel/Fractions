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
    @StatisticsReport(format = Histogram.class)
    void invalidReducedFraction(@ForAll @IntRange() int numerator,
                                 @ForAll @IntRange() int denominator)
    {

    }

    // PBT2
    @Property
    @Report(Reporting.GENERATED)
    void validReducedFraction(@ForAll @IntRange(min = Integer.MIN_VALUE) int numerator,
                               @ForAll @IntRange(min = Integer.MIN_VALUE) int denominator)
    {
        // First condition: the denominator must not be zero
        Assume.that(denominator != 0);

        // Second condition: the numerator must not be zero
        // A fraction that has a zero numerator doesn't have any mathematical sense
        Assume.that(numerator != 0);

        /*
            Third condition: if the denominator is negative:
               1.1 it must not be Integer.MIN_VALUE;
                1.2 the numerator must not be Integer.MIN_VALUE;
                 1.3 both do not have to be equal to Integer.MIN_VALUE;

        Assume.that(denominator < 0 &&
                // Combine numerator and denominator checks using logical AND
                (denominator != Integer.MIN_VALUE && numerator != Integer.MIN_VALUE) ||
                // Allow one to be Integer.MIN_VALUE but not both
                (denominator == Integer.MIN_VALUE && (numerator % 2 == 0)));

            Probably this condition here must be in an another PBT for equal value distribution
         */

        Fraction reducedFraction = Fraction.getReducedFraction(numerator, denominator);

        Statistics.collect(denominator == -1 ? "-1" : "other values");
        Statistics.collect(denominator == 1 ? "1" : "other values");
    }
}