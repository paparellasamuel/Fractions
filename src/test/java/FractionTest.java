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
        @ParameterizedTest
        @MethodSource("validFractionProvider")
        void shouldReturnAValidFraction(Fraction fraction, int expectedNumerator, int expectedDenominator) // T1
        {
            assertAll(
                    () -> assertEquals(expectedNumerator, fraction.getNumerator()),
                    () -> assertEquals(expectedDenominator, fraction.getDenominator())
            );
        }

        private static Stream<Arguments> validFractionProvider() {
            return Stream.of(
                    Arguments.of(new Fraction(1, 2), 1, 2), // T1.1
                    Arguments.of(new Fraction(1, -2), -1, 2), // T1.2
                    Arguments.of(new Fraction(-1, 2), -1, 2), // T1.3
                    Arguments.of(new Fraction(-1, -2), 1, 2) // T1.4
            );
        }

        @Test
        void zeroDenominatorShouldThrowArithmeticException() // T2
        {
            assertThrows(ArithmeticException.class, () -> new Fraction(1, 0));

            // Other cases:
            // first case: numerator is Integer.MAX_VALUE
            assertThrows(ArithmeticException.class, () -> new Fraction(Integer.MAX_VALUE, 0));

            // second case: numerator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> new Fraction(Integer.MIN_VALUE, 0));
        }

        @Test
        void zeroNumeratorShouldReturnZero() // T3
        {
            // First case: the denominator is one
            assertEquals(Fraction.ZERO, new Fraction(0, 1));

            // Second case: the denominator is minus two
            assertEquals(Fraction.ZERO, new Fraction(0, -2));

            // Other cases:
            // first case: denominator is Integer.MAX_VALUE
            assertEquals(Fraction.ZERO, new Fraction(0, Integer.MAX_VALUE));

            // second case: denominator is Integer.MIN_VALUE
            assertEquals(Fraction.ZERO, new Fraction(0, Integer.MIN_VALUE));
        }

        @Test
        void negativeDenominatorShouldThrowArithmeticException() // T4
        {
            // First case: the numerator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> new Fraction(Integer.MIN_VALUE, -1));

            // Second case: the denominator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> new Fraction(1, Integer.MIN_VALUE));

            // Other cases: first both numerator and denominator are Integer.MIN_VALUE;
            // second: the numerator is Integer.MAX_VALUE and the denominator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> new Fraction(Integer.MIN_VALUE, Integer.MIN_VALUE));
            assertThrows(ArithmeticException.class, () -> new Fraction(Integer.MAX_VALUE, Integer.MIN_VALUE));
        }

        // The range values of the numerator is [Integer.MIN_VALUE, Integer.MAX_VALUE],
        // while the range values of the denominator is [Integer.MIN_VALUE, -1] U [1, Integer.MAX_VALUE]
        @ParameterizedTest
        @MethodSource("fractionBoundaryValuesProvider")
        void fractionBoundaryValues(Fraction fraction, int expectedNumerator, int expectedDenominator) // T5
        {
            assertAll(
                    () -> assertEquals(expectedNumerator, fraction.getNumerator()),
                    () -> assertEquals(expectedDenominator, fraction.getDenominator())
            );
        }

        private static Stream<Arguments> fractionBoundaryValuesProvider()
        {
            return Stream.of(
                    // First case: the numerator is Integer.MIN_VALUE
                    Arguments.of(new Fraction(Integer.MIN_VALUE, 1), Integer.MIN_VALUE, 1),
                    Arguments.of(new Fraction(Integer.MIN_VALUE, Integer.MAX_VALUE), Integer.MIN_VALUE, Integer.MAX_VALUE),

                    // Second case: the numerator is Integer.MAX_VALUE
                    Arguments.of(new Fraction(Integer.MAX_VALUE, -1), -Integer.MAX_VALUE, 1),
                    Arguments.of(new Fraction(Integer.MAX_VALUE, 1), Integer.MAX_VALUE, 1),
                    Arguments.of(new Fraction(Integer.MAX_VALUE, Integer.MAX_VALUE), Integer.MAX_VALUE, Integer.MAX_VALUE)
            );
        }
    }

    @Nested
    class GetReducedFractionTests
    {
        @ParameterizedTest
        @MethodSource("reducedFractionProvider")
        void shouldReturnAValidReducedFraction(Fraction expectedFraction, int numerator, int denominator) // T6
        {
            assertEquals(expectedFraction, Fraction.getReducedFraction(numerator, denominator));
        }

        private static Stream<Arguments> reducedFractionProvider()
        {
            return Stream.of(
                    // T6.1, 2/4 should return 1/2
                    Arguments.of(new Fraction(1, 2), 2, 4),

                    // T6.2, 2/-6 should return -1/3
                    Arguments.of(new Fraction(-1, 3), 2, -6),

                    // T6.3, -4/6 should return -2/3
                    Arguments.of(new Fraction(-2, 3), -4, 6),

                    // T6.4, -6/-4 should return 3/2
                    Arguments.of(new Fraction(3, 2), -6, -4),

                    // T6.5, 2/2 should return 1/1 or 1
                    Arguments.of(new Fraction(1, 1), 2, 2),
                    Arguments.of(new Fraction(-1, 1), -2, 2),
                    Arguments.of(new Fraction(-1, 1), 2, -2),
                    Arguments.of(new Fraction(1, 1), -2, -2),

                    // T6.6, odd numerator and odd denominator
                    // This test also verifies the method behavior with prime numbers.
                    // 1 and 3 are indeed the first two prime numbers
                    Arguments.of(new Fraction(1, 3), 1, 3),
                    Arguments.of(new Fraction(-1, 3), 1, -3),
                    Arguments.of(new Fraction(-1, 3), -1, 3),
                    Arguments.of(new Fraction(1, 3), -1, -3),

                    // T6.7, odd numerator and even denominator
                    Arguments.of(new Fraction(3, 4), 3, 4),
                    Arguments.of(new Fraction(-3, 4), 3, -4),
                    Arguments.of(new Fraction(-3, 4), -3, 4),
                    Arguments.of(new Fraction(3, 4), -3, -4),

                    // T6.8, even numerator and odd denominator
                    Arguments.of(new Fraction(2, 3), 2, 3),
                    Arguments.of(new Fraction(-2, 3), 2, -3),
                    Arguments.of(new Fraction(-2, 3), -2, 3),
                    Arguments.of(new Fraction(2, 3), -2, -3)

            );
        }

        @Test
        void zeroDenominatorShouldThrowArithmeticException() // T7
        {
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(1, 0));

            // Other cases:
            // first case: numerator is Integer.MAX_VALUE
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(Integer.MAX_VALUE, 0));

            // second case: numerator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(Integer.MIN_VALUE, 0));
        }

        @Test
        void zeroNumeratorShouldReturnZEROConstant() // T8
        {
            assertEquals(Fraction.ZERO, Fraction.getReducedFraction(0, 1));

            // Other cases:
            // first case: denominator is Integer.MAX_VALUE
            assertEquals(Fraction.ZERO, Fraction.getReducedFraction(0, Integer.MAX_VALUE));

            // second case: denominator is Integer.MIN_VALUE
            assertEquals(Fraction.ZERO, Fraction.getReducedFraction(0, Integer.MIN_VALUE));
        }

        @Test
        void negativeDenominatorShouldThrowArithmeticException() // T9, same test as T4
        {
            // First case: the numerator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(Integer.MIN_VALUE, -1));

            // Second case: the denominator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(1, Integer.MIN_VALUE));

            // Other case
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(Integer.MAX_VALUE, Integer.MIN_VALUE));
        }

        // The range values of the numerator is [Integer.MIN_VALUE, Integer.MAX_VALUE],
        // while the range values of the denominator is [Integer.MIN_VALUE, -1] U [1, Integer.MAX_VALUE]
        @Test
        void reducedFractionBoundaryValues() // T10
        {
            assertAll(
            // First case: the numerator is Integer.MIN_VALUE
            () -> assertEquals(new Fraction(Integer.MIN_VALUE, 1), Fraction.getReducedFraction(Integer.MIN_VALUE, 1)),
            () -> assertEquals(new Fraction(Integer.MIN_VALUE, Integer.MAX_VALUE), Fraction.getReducedFraction(Integer.MIN_VALUE, Integer.MAX_VALUE)),

            // Second case: the numerator is Integer.MAX_VALUE
            () -> assertEquals(new Fraction(-Integer.MAX_VALUE, 1), Fraction.getReducedFraction(Integer.MAX_VALUE, -1)),
            () -> assertEquals(new Fraction(Integer.MAX_VALUE, 1), Fraction.getReducedFraction(Integer.MAX_VALUE, 1)),
            () -> assertEquals(new Fraction(1, 1), Fraction.getReducedFraction(Integer.MAX_VALUE, Integer.MAX_VALUE))
            );
        }
    }

    @Nested
    class GreatestCommonDivisorTests
    {
        @Test
        void shouldReturnCorrectGCD() // T11
        {
            // Classic GCD calculation
            assertEquals(4, Fraction.greatestCommonDivisor(4, 8));
            assertEquals(4, Fraction.greatestCommonDivisor(-4, 8));
            assertEquals(4, Fraction.greatestCommonDivisor(8, -4));
            assertEquals(4, Fraction.greatestCommonDivisor(-8, -4));

            // The GCD of two prime numbers that are not the same is 1
            assertEquals(1, Fraction.greatestCommonDivisor(3, 5));
            assertEquals(1, Fraction.greatestCommonDivisor(-3, 5));
            assertEquals(1, Fraction.greatestCommonDivisor(3, -5));
            assertEquals(1, Fraction.greatestCommonDivisor(-3, -5));

            // GCD calculation between an even number and an odd number
            // First case: u is even and v is odd
            assertEquals(1, Fraction.greatestCommonDivisor(2, 3));
            assertEquals(1, Fraction.greatestCommonDivisor(-2, 3));
            assertEquals(1, Fraction.greatestCommonDivisor(2, -3));
            assertEquals(1, Fraction.greatestCommonDivisor(-2, -3));

            // Second case: u is odd and v is even
            assertEquals(1, Fraction.greatestCommonDivisor(3, 4));
            assertEquals(1, Fraction.greatestCommonDivisor(-3, 4));
            assertEquals(1, Fraction.greatestCommonDivisor(3, -4));
            assertEquals(1, Fraction.greatestCommonDivisor(-3, -4));
        }

        @Test
        void zeroOperandsShouldThrowArithmeticException() // T12
        {
            assertThrows(ArithmeticException.class, () -> Fraction.greatestCommonDivisor(0, 0));
        }

        @Test
        void gcdBetweenZeroAndIntNumber() // T13
        {
            assertAll(
                // First case: u is zero and v is an int number
                () -> assertEquals(2, Fraction.greatestCommonDivisor(0, 2)), // T13.1
                () -> assertEquals(2, Fraction.greatestCommonDivisor(0, -2)), // T13.2

                // Second case: u is an int number and v is zero
                () -> assertEquals(2, Fraction.greatestCommonDivisor(2, 0)), // T13.3
                () -> assertEquals(2, Fraction.greatestCommonDivisor(-2, 0)) // T13.4
            );
        }

        @Test
        void testGCDOverflow() // T14
        {
            // First case: u is zero and v is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> {
                Fraction.greatestCommonDivisor(0, Integer.MIN_VALUE);
            });

            // Second case: u is Integer.MIN_VALUE and v is zero
            assertThrows(ArithmeticException.class, () -> {
                Fraction.greatestCommonDivisor(Integer.MIN_VALUE, 0);
            });

            // Third case: both u and v are Integer.MIN_VALUE.
            // This triggers the condition at line 171
            assertThrows(ArithmeticException.class, () -> {
                Fraction.greatestCommonDivisor(Integer.MIN_VALUE, Integer.MIN_VALUE);
            });
        }

        @Test
        void gdcBetweenOneAndIntNumber() // T15
        {
            assertAll(
                // First case: u is one and v is an int number
                () -> assertEquals(1, Fraction.greatestCommonDivisor(1, 2)), // T15.1
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-1, 2)), // T15.2
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-1, -2)), // T15.3

                // Second case: u is an int number and v is one
                () -> assertEquals(1, Fraction.greatestCommonDivisor(2, 1)), // T15.4
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-2, 1)), // T15.5
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-2, -1)), // T15.6

                // Third case: both u and v are one
                () -> assertEquals(1, Fraction.greatestCommonDivisor(1, 1)), // T15.7
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-1, 1)), // T15.8
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-1, -1)) // T15.9
            );
        }

        @ParameterizedTest
        @MethodSource("gcdBoundaryValuesProvider")
        void testGCDBoundaryValues(int gcdExpected, int u, int v) // T16
        {
            assertEquals(gcdExpected, Fraction.greatestCommonDivisor(u, v));
        }

        static Stream<Arguments> gcdBoundaryValuesProvider()
        {
            return Stream.of(
                    // GCD calculation between one and Integer.MIN_VALUE
                    Arguments.of(1, 1, Integer.MIN_VALUE),
                    Arguments.of(1, Integer.MIN_VALUE, 1),
                    Arguments.of(1, -1, Integer.MIN_VALUE),
                    Arguments.of(1, Integer.MIN_VALUE, -1),

                    // GCD calculation between one and Integer.MAX_VALUE
                    Arguments.of(1, 1, Integer.MAX_VALUE),
                    Arguments.of(1, Integer.MAX_VALUE, 1),
                    Arguments.of(1, -1, Integer.MAX_VALUE),
                    Arguments.of(1, Integer.MAX_VALUE, -1),

                    // GCD calculation between Integer.MIN_VALUE and Integer.MAX_VALUE
                    Arguments.of(1, Integer.MIN_VALUE, Integer.MAX_VALUE),
                    Arguments.of(1, Integer.MAX_VALUE, Integer.MIN_VALUE),
                    Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE),

                    // Test GCD with an even number and Integer.MIN_VALUE
                    Arguments.of(2, 2, Integer.MIN_VALUE),
                    Arguments.of(2, Integer.MIN_VALUE, 2),
                    Arguments.of(2, -2, Integer.MIN_VALUE),
                    Arguments.of(2, Integer.MIN_VALUE, -2),

                    // Test GCD with an even number and Integer.MAX_VALUE
                    Arguments.of(1, 2, Integer.MAX_VALUE),
                    Arguments.of(1, Integer.MAX_VALUE, 2),
                    Arguments.of(1, -2, Integer.MAX_VALUE),
                    Arguments.of(1, Integer.MAX_VALUE, -2),

                    // Test GCD with an odd number and Integer.MIN_VALUE
                    Arguments.of(1, 3, Integer.MIN_VALUE),
                    Arguments.of(1, Integer.MIN_VALUE, 3),
                    Arguments.of(1, -3, Integer.MIN_VALUE),
                    Arguments.of(1, Integer.MIN_VALUE, -3),

                    // Test GCD with an odd number and Integer.MAX_VALUE
                    Arguments.of(1, 3, Integer.MAX_VALUE),
                    Arguments.of(1, Integer.MAX_VALUE, 3),
                    Arguments.of(1, -3, Integer.MAX_VALUE),
                    Arguments.of(1, Integer.MAX_VALUE, -3)
            );
        }
    }
}