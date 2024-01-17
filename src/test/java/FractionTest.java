import it.uniba.itss2324.homework1.Fraction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FractionTest
{
    private static Fraction frazione;

    @org.junit.jupiter.api.Nested
    class FractionConstructorTests
    {
        @ParameterizedTest
        @MethodSource("validFractionProvider")
        void shouldReturnAValidFraction(int numerator, int denominator) //T1
        {
            Fraction fraction1 = new Fraction(numerator, denominator);

            int expectedNumerator = numerator;
            int expectedDenominator = denominator;

            if (denominator < 0)
            {
                expectedNumerator = -numerator;
                expectedDenominator = -denominator;
            }

            assertEquals(new Fraction(expectedNumerator, expectedDenominator), fraction1);
        }

        static Stream<Arguments> validFractionProvider()
        {
            return Stream.of(
                    Arguments.of(1, 2),
                    Arguments.of(1, -2),
                    Arguments.of(1, 3),
                    Arguments.of(-1, -3)
            );
        }

        @Test
        void denominatorZeroShouldThrowArithmeticException() //T2
        {
            assertThrows(ArithmeticException.class, () -> new Fraction(1,0));
        }

        @Test
        void negativeDenominatorShouldThrowArithmeticException() //T3
        {
            // First case: the numerator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> new Fraction(Integer.MIN_VALUE,-1));

            // Second case: the denominator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> new Fraction(1, Integer.MIN_VALUE));
        }

        // The minimum necessary condition for overflow to occur is that the numerator or denominator is equal to Math.addExact(Integer.MAX_VALUE, 1)
        @Test
        void testIntegerOverflow() //T4
        {
            assertThrows(ArithmeticException.class, () -> {
                new Fraction(1, Math.addExact(Integer.MAX_VALUE, 1));
            });
            assertThrows(ArithmeticException.class, () -> {
                new Fraction(Math.addExact(Integer.MAX_VALUE, 1), 1);
            });
            assertThrows(ArithmeticException.class, () -> {
                new Fraction(Math.addExact(Integer.MAX_VALUE, 1), Math.addExact(Integer.MAX_VALUE, 1));
            });
        }

        // The minimum necessary condition for underflow to occur is that the numerator or denominator is equal to Math.subtractExact(Integer.MIN_VALUE, 1).
        @Test
        void testIntegerUnderFlow() //T5
        {
            assertThrows(ArithmeticException.class, () -> {
                new Fraction(1, Math.subtractExact(Integer.MIN_VALUE, 1));
            });
            assertThrows(ArithmeticException.class, () -> {
                new Fraction(Math.subtractExact(Integer.MIN_VALUE, 1), 1);
            });
            assertThrows(ArithmeticException.class, () -> {
                new Fraction(Math.subtractExact(Integer.MIN_VALUE, 1), Math.subtractExact(Integer.MIN_VALUE, 1));
            });
        }

        @ParameterizedTest
        @MethodSource("validFractionsWithinRangeProvider")
        void validFractionsWithinRange(int numerator, int denominator) //T6
        {
            assertDoesNotThrow(() -> new Fraction(numerator, denominator));
        }

        static Stream<Arguments> validFractionsWithinRangeProvider()
        {
            return Stream.of(
                    // 1st case: numerator = Integer.MIN_VALUE
                    Arguments.of(Integer.MIN_VALUE, 1),
                    Arguments.of(Integer.MIN_VALUE, Integer.MAX_VALUE),

                    // 2nd case: numerator = Integer.MAX_VALUE
                    Arguments.of(Integer.MAX_VALUE, - 1),
                    Arguments.of(Integer.MAX_VALUE, 1),
                    Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE)
            );
        }
    }

    @org.junit.jupiter.api.Nested
    class GetReducedFractionMethodTests
    {
        @BeforeAll
        static void reducedFractionSetup ()
        {
            frazione = new Fraction(2,4);
        }

        @ParameterizedTest
        @MethodSource("reducedFractionProvider")
        void shouldReturnAValidReducedFraction(int numeratore, int denominatore) //T7
        {
            frazione.getReducedFraction(numeratore, denominatore);

            assertEquals(frazione.getNumerator(), frazione.getNumerator());
            assertEquals(frazione.getDenominator(), frazione.getDenominator());
        }

        public static Stream<Arguments> reducedFractionProvider() {
            return Stream.of(
                    Arguments.of(2, 4),
                    Arguments.of(4, 4),
                    Arguments.of(6, 3),
                    Arguments.of(10, 20),
                    Arguments.of(30, 60),
                    Arguments.of(10, 100),
                    Arguments.of(-12, -12)

            );
        }

        @Test
        void GetReducedFractionWithMinValueNumeratorAndNegativeDenominator() //T8
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(Integer.MIN_VALUE, -1);
            });
        }

        @Test
        void GetReducedFractionNegativeNumeratorBeyondMinValueWithMinValueDenominator() //T9
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(Integer.MIN_VALUE - 1, Integer.MIN_VALUE);
            });
        }

        @Test
        void GetReducedFractionOddNumeratorWithMinValueDenominatorAgain() //T10
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(5, Integer.MIN_VALUE);
            });
        }

        @Test
        void GetReducedFractionMaxValueNumeratorWithLargeNegativeDenominator() //T11
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(Integer.MAX_VALUE, Integer.MIN_VALUE);
            });
        }

        @Test
        void GetReducedFractionMinValueNumeratorWithLargePositiveDenominator() //T12
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(Integer.MIN_VALUE, Integer.MAX_VALUE);
            });
        }
    }
}