import it.uniba.itss2324.SAFR.homeworks.Fraction;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Assume;
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
            Assume.that(fraction.getDenominator() != 0 && fraction.getDenominator() != Integer.MIN_VALUE && fraction.getNumerator() != Integer.MIN_VALUE);

            Fraction f1 = new Fraction(fraction.getNumerator(), fraction.getDenominator());
            assertEquals(f1, fraction);
        }

        @Provide
        private Arbitrary<Fraction> validFractionRange()
        {
            Arbitrary<Integer> numerator = Arbitraries.integers().between(Integer.MIN_VALUE, Integer.MAX_VALUE);
            Arbitrary<Integer> denominator = Arbitraries.integers().between(Integer.MIN_VALUE, Integer.MAX_VALUE);
            return Combinators.combine(numerator, denominator).as(Fraction::new);
        }
}