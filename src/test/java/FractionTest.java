import it.uniba.itss2324.homework1.Fraction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static it.uniba.itss2324.homework1.Fraction.greatestCommonDivisor;
import static org.junit.jupiter.api.Assertions.*;

public class FractionTest {
    @org.junit.jupiter.api.Nested
    class FractionConstructorTests {
        @ParameterizedTest
        @MethodSource("validFractionProvider")
        void shouldReturnAValidFraction(int numerator, int denominator) //T1
        {
            Fraction fraction1 = new Fraction(numerator, denominator);

            int expectedNumerator = numerator;
            int expectedDenominator = denominator;

            if (denominator < 0) {
                expectedNumerator = -numerator;
                expectedDenominator = -denominator;
            }

            assertEquals(new Fraction(expectedNumerator, expectedDenominator), fraction1);
        }

        static Stream<Arguments> validFractionProvider() {
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
            assertThrows(ArithmeticException.class, () -> new Fraction(1, 0));
        }

        @Test
        void negativeDenominatorShouldThrowArithmeticException() //T3
        {
            // First case: the numerator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> new Fraction(Integer.MIN_VALUE, -1));

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

        // The range values of the numerator is [Integer.MIN_VALUE, Integer.MAX_VALUE]
        // while the range values of the denominator is [Integer.MIN_VALUE, -1] U [1, Integer.MAX_VALUE]
        @ParameterizedTest
        @MethodSource("validFractionsWithinRangeProvider")
        void validFractionsWithinRange(int numerator, int denominator) //T6
        {
            assertDoesNotThrow(() -> new Fraction(numerator, denominator));
        }

        static Stream<Arguments> validFractionsWithinRangeProvider() {
            return Stream.of(
                    // 1st case: numerator = Integer.MIN_VALUE
                    Arguments.of(Integer.MIN_VALUE, 1),
                    Arguments.of(Integer.MIN_VALUE, Integer.MAX_VALUE),

                    // 2nd case: numerator = Integer.MAX_VALUE
                    Arguments.of(Integer.MAX_VALUE, -1),
                    Arguments.of(Integer.MAX_VALUE, 1),
                    Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE)
            );
        }
    }

    @org.junit.jupiter.api.Nested
    class GetReducedFractionMethodTests {
        @ParameterizedTest
        @MethodSource("reducedFractionProvider")
        void shouldReturnAValidReducedFraction(int numerator, int denominator) //T7
        {
            int numerator1 = numerator;
            int denominator1 = denominator;

            final int gcd = greatestCommonDivisor(numerator1, denominator1);
            numerator1 = numerator1 / gcd;
            denominator1 = denominator1 / gcd;

            assertEquals(Fraction.getReducedFraction(numerator, denominator), new Fraction(numerator1, denominator1));
        }

        public static Stream<Arguments> reducedFractionProvider() {
            return Stream.of(
                    Arguments.of(2, 4),
                    Arguments.of(2, -6),
                    Arguments.of(-4, 6),
                    Arguments.of(-6, -4)
            );
        }

        @Test
        void denominatorZeroShouldThrowArithmeticException() //T8
        {
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(1, 0));
        }

        @ParameterizedTest
        @MethodSource("reducedFractionProvider")
        void ReturnAValidReducedFraction(int numerator, int denominator) //T9
        {

            assertEquals(Fraction.getReducedFraction(numerator, denominator), Fraction.getReducedFraction(numerator/2, denominator/2));
        }

        public static Stream<Arguments> reducedFractionProvider1() {
            return Stream.of(
                    Arguments.of(2, Integer.MIN_VALUE),
                    Arguments.of(-4, Integer.MIN_VALUE),
                    Arguments.of(6, Integer.MIN_VALUE)
            );
        }

        @Test
        public void testNumeratorZero() {
            Fraction result = Fraction.getReducedFraction(0, 5);
            assertEquals(Fraction.ZERO, result, "Zero numerator should return ZERO"); //T10
        }

        @Test
        void negativeDenominatorReducedShouldThrowArithmeticException() //T11
        {
            // First case: the numerator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(Integer.MIN_VALUE, -1));

            // Second case: the denominator is Integer.MIN_VALUE
            assertThrows(ArithmeticException.class, () -> Fraction.getReducedFraction(1, Integer.MIN_VALUE));
        }
/*
        @Test
        void GetReducedFractionWithMinValueNumeratorAndNegativeDenominator() //T8
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(Integer.MIN_VALUE, -1);
            });
        }*/
/*
        @Test
        void GetReducedFractionNegativeNumeratorBeyondMinValueWithMinValueDenominator() //T9
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(Integer.MIN_VALUE - 1, Integer.MIN_VALUE);
            });
        }*/
/*
        @Test
        void GetReducedFractionOddNumeratorWithMinValueDenominatorAgain() //T10
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(5, Integer.MIN_VALUE);
            });
        }*/
/*
        @Test
        void GetReducedFractionMaxValueNumeratorWithLargeNegativeDenominator() //T11
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(Integer.MAX_VALUE, Integer.MIN_VALUE);
            });
        }*/
/*
        @Test
        void GetReducedFractionMinValueNumeratorWithLargePositiveDenominator() //T12
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(Integer.MIN_VALUE, Integer.MAX_VALUE);
            });
        }*/
        }
    }
