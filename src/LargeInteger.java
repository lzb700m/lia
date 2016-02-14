import java.util.Iterator;
import java.util.LinkedList;

/**
 * Immutable class LargeInteger represent arbitrary large integer numbers
 *
 * @author LiP
 */
public class LargeInteger implements Comparable<LargeInteger> {

    /*
     * Default base of the LargeInteger object if not indicated by application
     */
    private static final int DEFAULT_BASE = 10;
    /*
     * Base of the LargeInteger
     */
    private int base;
    /*
     * List representation of the LargeInteger, with least significant digits at
     * index 0. Each node is a Long value that is between 0 and (base - 1)
     */
    private LinkedList<Long> digits = new LinkedList<Long>();
    /*
     * -1 for negative LargeInteger, 1 for positive LargeInteger, 0 for zero
     * value
     */
    private int sign;
    /*
     * Actual effective length of the LargeInteger as represented in digits
     */
    private long length;

    /*
     * TODO Constructor taking String as argument. Constructor for XYZ class;
     * takes a string s as parameter, that stores a number in decimal, and
     * creates the XYZ object representing that number. The string can have
     * arbitrary length. In Level 1, the string contains only the numerals 0-9,
     * with no leading zeroes.
     */
    public LargeInteger(String str, int b) {
        this(b);
        if (str.length() == 0) {
            return;
        }
        this.sign = 1;
        if (str.charAt(0) == '+') {
            this.sign = 1;
            str.substring(1);
        } else if (str.charAt(0) == '-') {
            this.sign = -1;
            str.substring(1);
        }
        for (int i = 0; i < str.length(); i++) {
            // invalid string
            if (!Character.isDigit(str.charAt(0))) {
                this.sign = 1;
                this.digits.clear();
                return;
            }
            this.digits.add(0, Long.valueOf(str.charAt(i) - '0'));
        }
        this.convert(b);
    }

    public LargeInteger(String str) {
        this(str, DEFAULT_BASE);
    }

    /* TODO Constructor taking long as argument. Constructor for XYZ class. */
    public LargeInteger(Long num, int b) {
        this.base = 10;
        this.sign = num == 0 ? 0 : (num > 0 ? 1 : -1);
        while (num > 0) {
            int r = (int) (num % 10);
            this.digits.add(Long.valueOf(r));
            num /= 10;
        }
        this.convert(b);
    }

    public LargeInteger(Long num) {
        this(num, DEFAULT_BASE);
    }

    /*
     * TODO implement compareTo() function
     */
    @Override
    public int compareTo(LargeInteger o) {
        int result = compareWithoutSign(o);
        if (this.sign == o.sign) {
            return this.sign > 0 ? result : -result;
        } else {
            return this.sign > 0 ? 1 : -1;
        }
    }

    public int compareWithoutSign(LargeInteger o) {
        if (this.digits.size() != o.digits.size()) return this.digits.size() - o.digits.size();
        Iterator<Long> iter1 = this.digits.descendingIterator();
        Iterator<Long> iter2 = o.digits.descendingIterator();
        while (iter1.hasNext()) {
            Long n1 = iter1.next(), n2 = iter2.next();
            if (n1 > n2) return 1;
            if (n1 < n2) return -1;
        }
        return 0;
    }

    /*
     * TODO convert the XYZ class object into its equivalent string (in
     * decimal). There should be no leading zeroes in the string.
     */
    @Override
    public String toString() {
        LargeInteger num = this;
        if (num.sign == 0) return "0";
        if (this.base != 10) {
            num = convert(this, 10);
        }

        StringBuilder sb = new StringBuilder(num.digits.size());
        if (num.sign < 0) sb.append("-");
        Iterator<Long> iter = num.digits.descendingIterator();
        Long first = getNext(iter);
        while (first == 0L) {
            iter.remove();
            first = getNext(iter);
        }
        sb.append(first);
        while (iter.hasNext())
            sb.append(iter.next());
        return sb.toString();
    }

    /*
     * TODO Print the base + ":" + elements of the list, separated by spaces.
     */
    public void printList() {

    }



    private static Long getNext(Iterator<Long> iter) {
        return iter.hasNext() ? iter.next() : null;
    }

    /**
     *
     * Basic operation of addition or substraction; if it is minus, ths abs(num1) should
     * always larger than abs(num2)
     *
     * @param num1 Summand or minuend  (number to be added or substracted)
     * @param num2 Addend or reduction
     * @param plus operation is plus or minus
     * @return LargeInteger new LargeInteger instance without sign
     */
    private static LargeInteger addition(LargeInteger num1, LargeInteger num2, boolean plus) {

        Iterator<Long> iter_1 = num1.digits.iterator();
        Iterator<Long> iter_2 = num2.digits.iterator();
        LargeInteger result = new LargeInteger("");
        int sign = plus ? 1 : -1;
        Long carry = 0L;
        Long n1 = getNext(iter_1), n2 = getNext(iter_2);
        while (n1 != null || n2 != null) {
            Long temp = 0L;
            if (n1 == null) {
                temp = sign * n2;
                n2 = getNext(iter_2);
            } else if (n2 == null) {
                temp = n1;
                n1 = getNext(iter_1);
            } else {
                temp = n1 + sign * n2;
                n1 = getNext(iter_1);
                n2 = getNext(iter_2);
            }
            temp += carry;
            if (temp >= num1.base) {
                carry = 1L;
                temp -= num1.base;
            } else if (temp < 0) {
                carry = -1L;
                temp += num1.base;
            } else {
                carry = 0L;
            }
            result.digits.add(temp);
        }
        if (carry != 0) {
            result.digits.add(carry);
        }
        Iterator<Long> iter = result.digits.descendingIterator();
        while (iter.hasNext() && iter.next() == 0) iter.remove();
        return result;
    }

    private static LargeInteger add(LargeInteger num1, LargeInteger num2, boolean flag) {
        if ((num1.sign == num2.sign) == flag) {
            LargeInteger result = addition(num1, num2, true);
            result.sign = num1.sign;
            return result;
        }
        else {
            int cmp = num1.compareWithoutSign(num2);
            LargeInteger result;
            if (cmp > 0) {
                result = addition(num1, num2, false);
                result.sign = num1.sign;
                return result;
            }
            else if (cmp < 0){
                result = addition(num2, num1, false);
                result.sign = num2.sign;
                return result;
            }
            else {
                return new LargeInteger(num1.base);
            }
        }
    }


    /*
     * TODO sum of two numbers stored as XYZ.
     */
    public static LargeInteger add(LargeInteger num1, LargeInteger num2) {
        return add(num1, num2, true);
    }

    /*
     * TODO given two XYZ a and b as parameters, representing the numbers n1 and
     * n2 repectively, returns the XYZ corresponding to n1-n2. If you have
     * implemented negative numbers, return the actual answer. If not, then if
     * the result is negative, it returns the XYZ for number 0.
     */
    public static LargeInteger subtract(LargeInteger num1, LargeInteger num2) {
        return add(num1, num2, false);
    }



    /**
     * get remainder for each iteration
     * remainder is no larger than base
     *
     * @param num        current quotient
     * @param targetBase
     * @return remainder for current num divided by target base
     */
    public static Long getRemainder(LargeInteger num, int targetBase) {
        Long temp = 0L;
        int curBase = num.base;
        LargeInteger tempNum = new LargeInteger(num);
        Iterator<Long> iter = tempNum.digits.descendingIterator();
        tempNum.digits.clear();
        while (iter.hasNext()) {
            Long cur = iter.next();
            temp = temp * curBase + cur;
            Long val = temp / targetBase;
            temp = temp % targetBase;
            tempNum.digits.add(0, val);
        }
        iter = num.digits.descendingIterator();
        while (iter.hasNext()) {
            Long val = iter.next();
            if (val != 0) break;
            iter.remove();
        }
        return temp;
    }

    /**
     * Converting current largeInteger instance into a new LargeInteger instance with given targetBase,
     *
     * @param targetBase
     */
    private void convert(int targetBase) {
        if (this.base == targetBase) return;
        LargeInteger temp = new LargeInteger(this);
        this.base = targetBase;
        this.digits.clear();
        while (!temp.digits.isEmpty()) {
            this.digits.add(getRemainder(temp, targetBase));
        }
    }

    /**
     * Converting input LargeInteger into a new LargeInteger with given base.
     *
     * @param LargeInteger
     * @param targetBase
     * @return a new LargeInteger instance with given LargeInteger and base
     */
    public static LargeInteger convert(LargeInteger LargeInteger, int targetBase) {
        LargeInteger temp = new LargeInteger(LargeInteger);
        temp.convert(targetBase);
        return temp;
    }

    /**
     * Initialize a zero LargeInteger instance with given base.
     * @param base
     */
    public LargeInteger(int base) {
        this.base = base;
        this.sign = 0;
    }

    /**
     * Initialize current LargeInteger instance as given LargeInteger
     *
     * @param num input large integer
     */
    public LargeInteger(LargeInteger num) {
        this.base = num.base;
        this.sign = num.sign;
        for (int i = 0; i < num.digits.size(); i++) {
            this.digits.add(num.digits.get(i));
        }
    }
    
    /*
     * TODO product of two numbers.
     */
    public static LargeInteger product(LargeInteger a, LargeInteger b) {
        return null;
    }

    /*
     * TODO Divide a by b result. Fractional part is discarded (take just the
     * quotient). Both a and b may be positive or negative. If b is 0, raise an
     * exception.
     */
    public static LargeInteger divide(LargeInteger a, LargeInteger b) {
        return null;
    }

    /*
     * TODO remainder you get when a is divided by b (a%b). Assume that a is
     * non-negative, and b > 0.
     */
    public static LargeInteger mod(LargeInteger a, LargeInteger b) {
        return null;
    }

    /*
     * TODO given an XYZ a, representing the number x and n, returns the BigNum
     * corresponding to x^n (x to the power n). Assume that n is a nonnegative
     * number. Use divide-and-conquer to implement power using O(log n) calls to
     * product and add.
     */
    public static LargeInteger power(LargeInteger a, long n) {
        return null;
    }

    /*
     * TODO (L2) Return a^n, where a and n are both XYZ. Here a may be negative,
     * but assume that n is non-negative.
     */
    public static LargeInteger power(LargeInteger a, LargeInteger n) {
        return null;
    }

    /*
     * TODO return the square root of a (truncated). Use binary search. Assume
     * that a is non-negative.
     */
    public static LargeInteger squareRoot(LargeInteger a) {
        return null;
    }

}
