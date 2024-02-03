import it.uniba.itss2324.SAFR.homeworks.Fraction;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

import static org.junit.jupiter.api.Assertions.*;

public class FractionPropertyBasedTest 
{
        @Property
        void validFraction(@ForAll ("validFractionRange") Fraction fraction)
        {
            Fraction f1 = new Fraction(fraction.getNumerator(), fraction.getDenominator());
            assertEquals(f1, fraction);
        }

        @Provide
        private Arbitrary<Fraction> validFractionRange()
        {
            Arbitrary<Integer> numerator = Arbitraries.integers().between(Integer.MIN_VALUE, Integer.MAX_VALUE);
            Arbitrary<Integer> denominator = Arbitraries.oneOf(
                    Arbitraries.integers().between(Integer.MIN_VALUE + 1, -1),
                    Arbitraries.integers().between(1, Integer.MAX_VALUE)
            );

            return Combinators.combine(numerator, denominator).as(Fraction::new);
        }
}