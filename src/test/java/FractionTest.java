import it.uniba.itss2324.SAFR.homeworks.Fraction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FractionTest
{
    @Nested
    class FractionConstructorTests
    {
        @Test
        void shouldReturnAValidFraction() // T1
        {
            Fraction f1 = new Fraction(1, 2);
            Fraction f2 = new Fraction(1, -2);
            Fraction f3 = new Fraction(-1, -2);

            assertAll(
                () -> assertEquals(1, f1.getNumerator()), // T1.1
                () -> assertEquals(2, f1.getDenominator()), // T1.1

                () -> assertEquals(-1, f2.getNumerator()), // T1.2
                () -> assertEquals(2, f2.getDenominator()), // T1.2

                () -> assertEquals(1, f3.getNumerator()), // T1.3
                () -> assertEquals(2, f3.getDenominator()) // T1.3
            );
        }

        @Test
        void zeroDenominatorShouldThrowArithmeticException() // T2
        {
            assertThrows(ArithmeticException.class, () -> new Fraction(1, 0));
        }

        @Test
        void negativeDenominatorShouldThrowArithmeticException() // T3
        {
            // First case: the numerator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> new Fraction(Integer.MIN_VALUE, -1));

            // Second case: the denominator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> new Fraction(1, Integer.MIN_VALUE));
        }

        // The range values of the numerator is [Integer.MIN_VALUE, Integer.MAX_VALUE],
        // while the range values of the denominator is [Integer.MIN_VALUE, -1] U [1, Integer.MAX_VALUE]
        @ParameterizedTest
        @MethodSource("validFractionsWithinRangeProvider")
        void validFractionsWithinRange(int numerator, int denominator) // T4*
        {
            assertDoesNotThrow(() -> new Fraction(numerator, denominator));
        }

        static Stream<Arguments> validFractionsWithinRangeProvider() 
        {
            return Stream.of(
                    // First case: numerator = Integer.MIN_VALUE
                    Arguments.of(Integer.MIN_VALUE, 1), // T4.1
                    Arguments.of(Integer.MIN_VALUE, Integer.MAX_VALUE), // T4.2

                    // Second case: numerator = Integer.MAX_VALUE
                    Arguments.of(Integer.MAX_VALUE, -1), // T4.3
                    Arguments.of(Integer.MAX_VALUE, 1), // T4.4
                    Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE) // T4.5
            );
        }
    }

    @Nested
    class GetReducedFractionMethodTests 
    {
        @Test
        void shouldReturnAValidReducedFraction() // T5
        {
            assertAll(
                // T5.1, 2/4 should return 1/2
                () -> assertEquals(new Fraction(1, 2), Fraction.getReducedFraction(2, 4)),
                // T5.2, 2/-6 should return -1/3
                () -> assertEquals(new Fraction(-1, 3), Fraction.getReducedFraction(2, -6)),
                // T5.3, -4/6 should return -2/3
                () -> assertEquals(new Fraction(-2, 3), Fraction.getReducedFraction(-4, 6)),
                // T5.4, -6/-4 should return 3/2
                () -> assertEquals(new Fraction(3, 2), Fraction.getReducedFraction(-6, -4)),
                // T5.5, 2/2 should return 1/1 or 1
                () -> assertEquals(new Fraction (1, 1), Fraction.getReducedFraction(2, 2))
            );
        }

        @Test
        void zeroDenominatorShouldThrowArithmeticException() // T6
        {
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(1, 0));
        }

        @Test
        public void zeroNumeratorShouldReturnZEROConstant() // T7
        {
            assertEquals(Fraction.ZERO, Fraction.getReducedFraction(0, 1));
        }

        @ParameterizedTest
        @MethodSource("validEvenFractionProvider")
        void validEvenNumeratorMIN_VALUE_DenominatorFraction(int numerator, int denominator) // T8
        {
            assertEquals(new Fraction(numerator / 2, denominator / 2), Fraction.getReducedFraction(numerator, denominator));
        }

        static Stream<Arguments> validEvenFractionProvider()
        {
            return Stream.of(
                    Arguments.of(2, Integer.MIN_VALUE), // T8.1
                    Arguments.of(-2, Integer.MIN_VALUE) // T8.2
            );
        }

        // This test also verifies the method behavior with prime numbers.
        // 1 and 3 are indeed the first two prime numbers
        @ParameterizedTest
        @MethodSource("validOddFractionProvider")
        void validOddNumeratorOddDenominatorFraction(int numerator, int denominator) // T9
        {
            assertEquals(new Fraction(numerator, denominator), Fraction.getReducedFraction(numerator, denominator));
        }

        static Stream<Arguments> validOddFractionProvider()
        {
            return Stream.of(
                    Arguments.of(1, 3), // T9.1
                    Arguments.of(1, -3), // T9.2
                    Arguments.of(-1, 3), // T9.3
                    Arguments.of(-1, -3) // T9.4
            );
        }

        @Test
        void negativeDenominatorShouldThrowArithmeticException() // T10, same test as T3
        {
            // First case: the numerator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(Integer.MIN_VALUE, -1));

            // Second case: the denominator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(1, Integer.MIN_VALUE));
        }

        // The range values of the numerator is [Integer.MIN_VALUE, Integer.MAX_VALUE],
        // while the range values of the denominator is [Integer.MIN_VALUE, -1] U [1, Integer.MAX_VALUE]
        @ParameterizedTest
        @MethodSource("validReducedFractionsWithinRangeProvider")
        void validReducedFractionsWithinRange(int numerator, int denominator) // T11*
        {
            assertDoesNotThrow(() -> Fraction.getReducedFraction(numerator, denominator));
        }

        static Stream<Arguments> validReducedFractionsWithinRangeProvider() 
        {
            return Stream.of(
                    // First case: numerator = Integer.MIN_VALUE
                    Arguments.of(Integer.MIN_VALUE, 1), // T11.1
                    Arguments.of(Integer.MIN_VALUE, Integer.MAX_VALUE), // T11.2

                    // Second case: numerator = Integer.MAX_VALUE
                    Arguments.of(Integer.MAX_VALUE, -1), // T11.3
                    Arguments.of(Integer.MAX_VALUE, 1), // T11.4
                    Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE) // T11.5
            );
        }
    }

    @Nested
    class GreatestCommonDivisorTests
    {
        @Test
        void gcdBetweenZeroAndIntNumber() // T12
        {
            assertAll(
                () -> assertEquals(2, Fraction.greatestCommonDivisor(0, 2)), // T12.1
                () -> assertEquals(2, Fraction.greatestCommonDivisor(0, -2)), // T12.2
                () -> assertEquals(2, Fraction.greatestCommonDivisor(2, 0)), // T12.3
                () -> assertEquals(2, Fraction.greatestCommonDivisor(-2, 0)) // T12.4
            );
        }

        @Test
        void testGCDOverflow() // T13
        {
            // First case: u is 0 and v is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> {
                Fraction.greatestCommonDivisor(0, Integer.MIN_VALUE);
            });

            // Second case: u is Integer.MIN_VALUE and v is 0
            assertThrows(ArithmeticException.class, () -> {
                Fraction.greatestCommonDivisor(Integer.MIN_VALUE, 0);
            });
        }

        @Test
        void gdcBetweenOneAndIntNumber() // T14
        {
            assertAll(
                () -> assertEquals(1, Fraction.greatestCommonDivisor(1, 2)), // T14.1
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-1, 2)), // T14.2
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-1, -2)), // T14.3
                () -> assertEquals(1, Fraction.greatestCommonDivisor(2, 1)), // T14.4
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-2, 1)), // T14.5
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-2, -1)) // T14.6
            );
        }
    }
}