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
        }

        @Test
        void zeroNumeratorShouldReturnZero() // T3
        {
            Fraction zero1 = new Fraction(0,1);
            Fraction zero2 = new Fraction(0, -2);

            // First case: the denominator is one
            assertEquals(0, zero1.getNumerator());
            assertEquals(1, zero1.getDenominator());

            // Second case: the denominator is minus two
            assertEquals(0, zero2.getNumerator());
            assertEquals(1, zero2.getDenominator());
        }

        @Test
        void negativeDenominatorShouldThrowArithmeticException() // T4
        {
            // First case: the numerator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> new Fraction(Integer.MIN_VALUE, -1));

            // Second case: the denominator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> new Fraction(1, Integer.MIN_VALUE));

            //Other boundaries
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
    class GetReducedFractionMethodTests
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
                    Arguments.of(new Fraction(1, 1), -2, -2)
            );
        }

        @Test
        void zeroDenominatorShouldThrowArithmeticException() // T7
        {
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(1, 0));
        }

        @Test
        void zeroNumeratorShouldReturnZEROConstant() // T8
        {
            assertEquals(Fraction.ZERO, Fraction.getReducedFraction(0, 1));
        }

        @ParameterizedTest
        @MethodSource("validEvenFractionProvider")
        void validEvenNumeratorMIN_VALUE_DenominatorFraction(int numerator, int denominator) // T9
        {
            assertEquals(new Fraction(numerator / 2, denominator / 2), Fraction.getReducedFraction(numerator, denominator));
        }

        static Stream<Arguments> validEvenFractionProvider()
        {
            return Stream.of(
                    Arguments.of(2, Integer.MIN_VALUE), // T9.1
                    Arguments.of(-2, Integer.MIN_VALUE) // T9.2
            );
        }

        // This test also verifies the method behavior with prime numbers.
        // 1 and 3 are indeed the first two prime numbers
        @ParameterizedTest
        @MethodSource("validOddFractionProvider")
        void validOddNumeratorOddDenominatorFraction(int numerator, int denominator) // T10
        {
            assertEquals(new Fraction(numerator, denominator), Fraction.getReducedFraction(numerator, denominator));
        }

        static Stream<Arguments> validOddFractionProvider()
        {
            return Stream.of(
                    Arguments.of(1, 3), // T10.1
                    Arguments.of(1, -3), // T10.2
                    Arguments.of(-1, 3), // T10.3
                    Arguments.of(-1, -3) // T10.4
            );
        }

        @Test
        void negativeDenominatorShouldThrowArithmeticException() // T11, same test as T4
        {
            // First case: the numerator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(Integer.MIN_VALUE, -1));

            // Second case: the denominator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(1, Integer.MIN_VALUE));

            //Other boundaries
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(Integer.MAX_VALUE, Integer.MIN_VALUE));
        }

        // The range values of the numerator is [Integer.MIN_VALUE, Integer.MAX_VALUE],
        // while the range values of the denominator is [Integer.MIN_VALUE, -1] U [1, Integer.MAX_VALUE]
        @Test
        void reducedFractionBoundaryValues() // T12
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
        void shouldReturnCorrectGCD() // T13
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
        void zeroOperandsShouldThrowArithmeticException() // T14
        {
            assertThrows(ArithmeticException.class, () -> Fraction.greatestCommonDivisor(0, 0));
        }

        @Test
        void gcdBetweenZeroAndIntNumber() // T15
        {
            assertAll(
                // First case: u is zero and v is an int number
                () -> assertEquals(2, Fraction.greatestCommonDivisor(0, 2)), // T14.1
                () -> assertEquals(2, Fraction.greatestCommonDivisor(0, -2)), // T14.2

                // Second case: u is an int number and v is zero
                () -> assertEquals(2, Fraction.greatestCommonDivisor(2, 0)), // T14.3
                () -> assertEquals(2, Fraction.greatestCommonDivisor(-2, 0)) // T14.4
            );
        }

        @Test
        void testGCDOverflow() // T16
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
        void gdcBetweenOneAndIntNumber() // T17
        {
            assertAll(
                // First case: u is one and v is an int number
                () -> assertEquals(1, Fraction.greatestCommonDivisor(1, 2)), // T17.1
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-1, 2)), // T17.2
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-1, -2)), // T17.3

                // Second case: u is an int number and v is one
                () -> assertEquals(1, Fraction.greatestCommonDivisor(2, 1)), // T17.4
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-2, 1)), // T17.5
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-2, -1)), // T17.6

                // Third case: both u and v are one
                () -> assertEquals(1, Fraction.greatestCommonDivisor(1, 1)), // T17.7
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-1, 1)), // T17.8
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-1, -1)) // T17.9
            );
        }

        @ParameterizedTest
        @MethodSource("gcdBoundaryValuesProvider")
        void testGCDBoundaryValues(int expected, int u, int v) // T18
        {
            assertEquals(expected, Fraction.greatestCommonDivisor(u, v));
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