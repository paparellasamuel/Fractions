import it.uniba.itss2324.homework1.Fraction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static it.uniba.itss2324.homework1.Fraction.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FractionTest
{
    private static Fraction frazione;

    @org.junit.jupiter.api.Nested
    class FractionConstructorTests
    {
        @ParameterizedTest
        @MethodSource("validFractionProvider")
        void shouldReturnAValidFraction(int numeratore, int denominatore) //T1
        {
            Fraction frazione1 = new Fraction(numeratore, denominatore);

            int expectedNumerator = numeratore;
            int expectedDenominator = denominatore;

            if (denominatore < 0)
            {
                expectedNumerator = -numeratore;
                expectedDenominator = -denominatore;
            }

            assertEquals(expectedNumerator, frazione1.getNumerator());
            assertEquals(expectedDenominator, frazione1.getDenominator());
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
        void denominatorZeroShouldThrowArithmeticException () //T2
        {
            assertThrows(ArithmeticException.class, () -> {
                new Fraction(1,0);
            });
        }

        @Test
        void negativeDenominatorShouldThrowArithmeticException () //T3
        {
            assertThrows(ArithmeticException.class, () -> {
                new Fraction(Integer.MIN_VALUE,-1);
            });
        }

        @Test
        void testIntegerOverflow () //T4
        {
            assertThrows(ArithmeticException.class, () -> {
                new Fraction(Integer.MAX_VALUE,Integer.MAX_VALUE);
            });
        }

        @Test
        void testIntegerUnderFlow () //T5
        {
            assertThrows(ArithmeticException.class, () -> {
                new Fraction(Integer.MIN_VALUE,Integer.MIN_VALUE);
            });
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
        void shouldReturnAValidReducedFraction(int numeratore, int denominatore) //T6
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
        void GetReducedFractionWithMinValueNumeratorAndNegativeDenominator() //T7
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(Integer.MIN_VALUE, -1);
            });
        }

        @Test
        void GetReducedFractionNegativeNumeratorBeyondMinValueWithMinValueDenominator() //T8
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(Integer.MIN_VALUE - 1, Integer.MIN_VALUE);
            });
        }

        @Test
        void GetReducedFractionOddNumeratorWithMinValueDenominatorAgain() //T9
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(5, Integer.MIN_VALUE);
            });
        }

        @Test
        void GetReducedFractionMaxValueNumeratorWithLargeNegativeDenominator() //T10
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(Integer.MAX_VALUE, Integer.MIN_VALUE);
            });
        }

        @Test
        void GetReducedFractionMinValueNumeratorWithLargePositiveDenominator() //T11
        {
            assertThrows(ArithmeticException.class, () -> {
                frazione.getReducedFraction(Integer.MIN_VALUE, Integer.MAX_VALUE);
            });
        }
    }
}