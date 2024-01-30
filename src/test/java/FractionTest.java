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

            //Other boundaries
            assertThrows(ArithmeticException.class, () -> new Fraction(Integer.MIN_VALUE, Integer.MIN_VALUE));
            assertThrows(ArithmeticException.class, () -> new Fraction(Integer.MAX_VALUE, Integer.MIN_VALUE));
        }

        // The range values of the numerator is [Integer.MIN_VALUE, Integer.MAX_VALUE],
        // while the range values of the denominator is [Integer.MIN_VALUE, -1] U [1, Integer.MAX_VALUE]
        @Test
        void validFractionsWithinRange() // T4
        {
            Fraction f1 = new Fraction(Integer.MIN_VALUE, 1);
            Fraction f2 = new Fraction(Integer.MIN_VALUE, Integer.MAX_VALUE);
            Fraction f3 = new Fraction(Integer.MAX_VALUE, -1);
            Fraction f4 = new Fraction(Integer.MAX_VALUE, 1);
            Fraction f5 = new Fraction(Integer.MAX_VALUE, Integer.MAX_VALUE);

            assertAll(
                () -> assertEquals(Integer.MIN_VALUE, f1.getNumerator()), // T4.1
                () -> assertEquals(1, f1.getDenominator()), // T4.1

                () -> assertEquals(Integer.MIN_VALUE, f2.getNumerator()), // T4.2
                () -> assertEquals(Integer.MAX_VALUE, f2.getDenominator()), // T4.2

                () -> assertEquals(-Integer.MAX_VALUE, f3.getNumerator()), // T4.3
                () -> assertEquals(1, f3.getDenominator()), // T4.3

                () -> assertEquals(Integer.MAX_VALUE, f4.getNumerator()), // T4.4
                () -> assertEquals(1, f4.getDenominator()), // T4.4

                () -> assertEquals(Integer.MAX_VALUE, f5.getNumerator()), // T4.5
                () -> assertEquals(Integer.MAX_VALUE, f5.getDenominator()) // T4.5
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

            //Other boundary
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(Integer.MAX_VALUE, Integer.MIN_VALUE));
        }

        // The range values of the numerator is [Integer.MIN_VALUE, Integer.MAX_VALUE],
        // while the range values of the denominator is [Integer.MIN_VALUE, -1] U [1, Integer.MAX_VALUE]
        @Test
        void validReducedFractionsWithinRange() // T11
        {
            assertAll(
            // First case: numerator = Integer.MIN_VALUE
            () -> assertEquals(new Fraction(Integer.MIN_VALUE, 1), Fraction.getReducedFraction(Integer.MIN_VALUE, 1)),    
            () -> assertEquals(new Fraction(Integer.MIN_VALUE, Integer.MAX_VALUE), Fraction.getReducedFraction(Integer.MIN_VALUE, Integer.MAX_VALUE)),
            
            // Second case: numerator = Integer.MAX_VALUE
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
        void shouldReturnCorrectGCD () // T12
        {
            // Classic GCD calculation
            assertEquals(2, Fraction.greatestCommonDivisor(2, 4));
            assertEquals(2, Fraction.greatestCommonDivisor(-2, 4));
            assertEquals(2, Fraction.greatestCommonDivisor(2, -4));
            assertEquals(2, Fraction.greatestCommonDivisor(-2, -4));

            // The GCD of two prime numbers that are not the same is 1
            assertEquals(1, Fraction.greatestCommonDivisor(3, 5));
            assertEquals(1, Fraction.greatestCommonDivisor(-3, 5));
            assertEquals(1, Fraction.greatestCommonDivisor(3, -5));
            assertEquals(1, Fraction.greatestCommonDivisor(-3, -5));
        }
        
        @Test
        void gcdBetweenZeroAndIntNumber() // T13
        {
            assertAll(
                () -> assertEquals(2, Fraction.greatestCommonDivisor(0, 2)), // T13.1
                () -> assertEquals(2, Fraction.greatestCommonDivisor(0, -2)), // T13.2

                () -> assertEquals(2, Fraction.greatestCommonDivisor(2, 0)), // T13.3
                () -> assertEquals(2, Fraction.greatestCommonDivisor(-2, 0)) // T13.4
            );
        }

        @Test
        void testGCDOverflow() // T14
        {
            // First case: u is 0 and v is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> {
                Fraction.greatestCommonDivisor(0, Integer.MIN_VALUE);
            });

            // Second case: u is Integer.MIN_VALUE and v is 0
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
                () -> assertEquals(1, Fraction.greatestCommonDivisor(1, 2)), // T15.1
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-1, 2)), // T15.2
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-1, -2)), // T15.3

                () -> assertEquals(1, Fraction.greatestCommonDivisor(2, 1)), // T15.4
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-2, 1)), // T15.5
                () -> assertEquals(1, Fraction.greatestCommonDivisor(-2, -1)) // T15.6
            );
        }

        @Test
        void testGCDBoundaryValues() // T16
        {
            assertAll(
                () -> assertEquals(1, Fraction.greatestCommonDivisor(1, Integer.MAX_VALUE)), // T16.1
                () -> assertEquals(1, Fraction.greatestCommonDivisor(Integer.MAX_VALUE, 1)), // T16.2

                () -> assertEquals(1, Fraction.greatestCommonDivisor(1, Integer.MIN_VALUE)), // T16.3
                () -> assertEquals(1, Fraction.greatestCommonDivisor(Integer.MIN_VALUE, 1)), // T16.4

                () -> assertEquals(1, Fraction.greatestCommonDivisor(Integer.MIN_VALUE, Integer.MAX_VALUE)), // T16.5
                () -> assertEquals(1, Fraction.greatestCommonDivisor(Integer.MAX_VALUE, Integer.MIN_VALUE)), // T16.6                
                () -> assertEquals(Integer.MAX_VALUE, Fraction.greatestCommonDivisor(Integer.MAX_VALUE, Integer.MAX_VALUE)), // T16.7

                // Test GCD with an even number and Integer.MIN_VALUE and Integer.MAX_VALUE
                () -> assertEquals(2, Fraction.greatestCommonDivisor(2, Integer.MIN_VALUE)), // T16.8
                () -> assertEquals(2, Fraction.greatestCommonDivisor(Integer.MIN_VALUE, 2)), // T16.9
                () -> assertEquals(1, Fraction.greatestCommonDivisor(2, Integer.MAX_VALUE)), // T16.10
                () -> assertEquals(1, Fraction.greatestCommonDivisor(Integer.MAX_VALUE, 2)), // T16.11

                // Test GCD with an odd number and Integer.MIN_VALUE and Integer.MAX_VALUE
                () -> assertEquals(1, Fraction.greatestCommonDivisor(3, Integer.MIN_VALUE)), // T16.12
                () -> assertEquals(1, Fraction.greatestCommonDivisor(Integer.MIN_VALUE, 3)), // T16.13
                () -> assertEquals(1, Fraction.greatestCommonDivisor(3, Integer.MAX_VALUE)), // T16.14
                () -> assertEquals(1, Fraction.greatestCommonDivisor(Integer.MAX_VALUE, 3)) // T16.15
            );
        }
    }
}