package it.uniba.itss2324.homework1;

/**
 * {@link Fraction} is a {@link Number} implementation that
 * stores fractions accurately.
 *
 * <p>This class is immutable, and interoperable with most methods that accept
 * a {@link Number}.</p>
 *
 * <p>Note that this class is intended for common use cases, it is <i>int</i>
 * based and thus suffers from various overflow issues. For a BigInteger based
 * equivalent, please see the Commons Math BigFraction class.</p>
 *
 * @since 2.0
 */

public final class Fraction
{
    /**
     * The numerator number part of the fraction
     */
    private int numerator;

    /**
     * The getter for the numerator part of the fraction
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * The setter for the numerator part of the fraction
     */
    public void setNumerator(int numerator)
    {
        this.numerator = numerator;
    }

    /**
     * The denominator number part of the fraction
     */
    private int denominator;

    /**
     * The getter for the denominator part of the fraction
     */
    public int getDenominator ()
    {
        return denominator;
    }

    /**
     * The setter for the denominator part of the fraction
     */
    public void setDenominator(int denominator)
    {
        if (denominator == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        }
        this.denominator = denominator;
    }

    /**
     * {@link Fraction} representation of 0.
     */
    public static final Fraction ZERO = new Fraction(0, 1);

    /**
     * Constructs a {@link Fraction} instance with the 2 parts
     * of a fraction Y/Z.
     *
     * @param numerator  the numerator, for example the three in 'three sevenths'
     * @param denominator  the denominator, for example the seven in 'three sevenths'
     */
    public Fraction(int numerator, int denominator)
    {
        if (denominator == 0)
        {
            throw new ArithmeticException("The denominator must not be zero");
        }

        if (denominator < 0)
        {
            if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE)
            {
                throw new ArithmeticException("overflow: can't negate");
            }

            numerator = -numerator;
            denominator = -denominator;
        }

        if (numerator == Integer.MAX_VALUE + 1 || denominator == Integer.MAX_VALUE + 1)
        {
            throw new ArithmeticException("Numerator or denominator too large to represent as an Integer.");
        }

        if (numerator == Integer.MIN_VALUE -1 || denominator == Integer.MIN_VALUE - 1)
        {
            throw new ArithmeticException("Numerator or denominator too small to represent as an Integer.");
        }

        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Creates a reduced {@link Fraction} instance with the 2 parts
     * of a fraction Y/Z.
     *
     * <p>For example, if the input parameters represent 2/4, then the created
     * fraction will be 1/2.</p>
     *
     * <p>Any negative signs are resolved to be on the numerator.</p>
     *
     * @param numerator  the numerator, for example the three in 'three sevenths'
     * @param denominator  the denominator, for example the seven in 'three sevenths'
     * @return a new fraction instance, with the numerator and denominator reduced
     * @throws ArithmeticException if the denominator is {@code zero}
     */
    public static Fraction getReducedFraction(int numerator, int denominator)
    {
        if (denominator == 0)
        {
            throw new ArithmeticException("The denominator must not be zero");
        }
        if (numerator == 0)
        {
            return ZERO; // normalize zero.
        }
        // allow 2^k/-2^31 as a valid fraction (where k>0)
        if (denominator == Integer.MIN_VALUE && (numerator & 1) == 0)
        {
            numerator /= 2;
            denominator /= 2;
        }
        if (denominator < 0)
        {
            if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE)
            {
                throw new ArithmeticException("overflow: can't negate");
            }
            numerator = -numerator;
            denominator = -denominator;
        }
        // simplify fraction.
        final int gcd = greatestCommonDivisor(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;
        return new Fraction(numerator, denominator);
    }

    /**
     * Gets the greatest common divisor of the absolute value of
     * two numbers, using the "binary gcd" method which avoids
     * division and modulo operations.  See Knuth 4.5.2 algorithm B.
     * This algorithm is due to Josef Stein (1961).
     *
     * @param u  a non-zero number
     * @param v  a non-zero number
     * @return the greatest common divisor, never zero
     */
    private static int greatestCommonDivisor(int u, int v)
    {
        // From Commons Math:
        if (u == 0 || v == 0)
        {
            if (u == Integer.MIN_VALUE || v == Integer.MIN_VALUE)
            {
                throw new ArithmeticException("overflow: gcd is 2^31");
            }
            return Math.abs(u) + Math.abs(v);
        }
        // if either operand is abs 1, return 1:
        if (Math.abs(u) == 1 || Math.abs(v) == 1)
        {
            return 1;
        }
        // keep u and v negative, as negative integers range down to
        // -2^31, while positive numbers can only be as large as 2^31-1
        // (i.e. we can't necessarily negate a negative number without
        // overflow)
        if (u > 0)
        {
            u = -u;
        } // make u negative
        if (v > 0)
        {
            v = -v;
        } // make v negative
        // B1. [Find power of 2]
        int k = 0;
        while ((u & 1) == 0 && (v & 1) == 0 && k < 31)
        { // while u and v are both even...
            u /= 2;
            v /= 2;
            k++; // cast out twos.
        }
        if (k == 31)
        {
            throw new ArithmeticException("overflow: gcd is 2^31");
        }
        // B2. Initialize: u and v have been divided by 2^k and at least
        // one is odd.
        int t = (u & 1) == 1 ? v : -(u / 2)/* B3 */;
        // t negative: u was odd, v may be even (t replaces v)
        // t positive: u was even, v is odd (t replaces u)
        do
        {
            /* assert u<0 && v<0; */
            // B4/B3: cast out twos from t.
            while ((t & 1) == 0)
            { // while t is even.
                t /= 2; // cast out twos
            }
            // B5 [reset max(u,v)]
            if (t > 0)
            {
                u = -t;
            } else
            {
                v = t;
            }
            // B6/B3. at this point both u and v should be odd.
            t = (v - u) / 2;
            // |u| larger: t positive (replace u)
            // |v| larger: t negative (replace v)
        } while (t != 0);
        return -u * (1 << k); // gcd is u*2^k
    }

    /**
     * Compares this fraction to another object to test if they are equal.
     *
     * <p>To be equal, both values must be equal. Thus 2/4 is not equal to 1/2.</p>
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if this object is equal
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (!(obj instanceof Fraction))
        {
            return false;
        }
        final Fraction other = (Fraction) obj;
        return getNumerator() == other.getNumerator() && getDenominator() == other.getDenominator();
    }
}