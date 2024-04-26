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
    void invalidReducedFraction (@ForAll @IntRange(min = Integer.MIN_VALUE + 1) int numerator,
                                 @ForAll @IntRange(min = Integer.MIN_VALUE + 1) int denominator)
    {
        Assume.that(denominator != 0);

        Fraction reducedFraction = Fraction.getReducedFraction(numerator, denominator);

        assertAll(
                () -> assertTrue(reducedFraction.getDenominator() != 0),
                () -> assertTrue(reducedFraction.getNumerator() != Integer.MIN_VALUE),
                () -> assertTrue(reducedFraction.getDenominator() != Integer.MIN_VALUE),
                () -> assertTrue(reducedFraction.getNumerator() != Integer.MIN_VALUE &&
                        reducedFraction.getDenominator() != Integer.MIN_VALUE)
        );

        Statistics.collect(denominator == -1 ? "-1" : "other values");
        Statistics.collect(denominator == 1 ? "1" : "other values");
    }
}