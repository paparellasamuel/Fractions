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

    @BeforeAll
    static void setup()
    {
        int setupNumerator = 1;
        int setupDenominator = 2;

        frazione = new Fraction(setupNumerator, setupDenominator);
    }

    @ParameterizedTest
    @MethodSource("validFractionProvider")
    void shouldReturnAValidFraction(int numeratore, int denominatore) //T1
    {
        frazione.setNumerator(numeratore);
        frazione.setDenominator(denominatore);

        assertEquals(numeratore, frazione.getNumerator());
        assertEquals(denominatore, frazione.getDenominator());
    }

    static Stream<Arguments> validFractionProvider() {
        return Stream.of(
                Arguments.of(1, 2),
                Arguments.of(1, 3),
                Arguments.of(1, 4),
                Arguments.of(3, 4),
                Arguments.of(1, 10),
                Arguments.of(1, 100),
                Arguments.of(1, 1000)
        );
    }

    @Test
    void zeroFractionShouldReturnZEROConstant () //T2
    {
        frazione.setNumerator(0);
        frazione.setDenominator(1);

        assertEquals(ZERO.getNumerator(), frazione.getNumerator());
        assertEquals(ZERO.getDenominator(), frazione.getDenominator());
    }

    @Test
    void denominatorZeroShouldThrowArithmeticException () //T3
    {
        assertThrows(ArithmeticException.class, () -> {
            frazione.setNumerator(1);
            frazione.setDenominator(0);
        });
    }

    @Test
    void negativeDenominatorShouldThrowArithmeticException () //T4
    {
        assertThrows(ArithmeticException.class, () -> {
            frazione.setNumerator(Integer.MIN_VALUE);
            frazione.setDenominator(-1);
        });
    }

    @Test
    void testIntegerOverflow () //T5
    {
        assertThrows(ArithmeticException.class, () -> {
            frazione.setNumerator(Integer.MAX_VALUE);
            frazione.setDenominator(Integer.MAX_VALUE);
        });
    }

    @Test
    void testIntegerUnderFlow () //T6
    {
        assertThrows(ArithmeticException.class, () -> {
            frazione.setNumerator(Integer.MIN_VALUE);
            frazione.setDenominator(Integer.MIN_VALUE);
        });
    }

    @ParameterizedTest
    @MethodSource("reducedFractionProvider")
    void shouldReturnAValidReducedFraction(int numeratore, int denominatore) //T1
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