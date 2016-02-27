import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

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

        // empty string considered as 0
        if (str.length() == 0) {
            return;
        }

        // "0" string
        if (str.length() == 1 && str.charAt(0) - '0' == 0) {
            return;
        }
        this.sign = 1;
        if (str.charAt(0) == '+') {
            this.sign = 1;
            str = str.substring(1);
        } else if (str.charAt(0) == '-') {
            this.sign = -1;
            str = str.substring(1);
        }
        for (int i = 0; i < str.length(); i++) {
            // invalid string
            if (!Character.isDigit(str.charAt(0))) {
                this.sign = 0;
                this.digits = new LinkedList<Long>();
                return;
            }
            this.digits.add(0, Long.valueOf(str.charAt(i) - '0'));
        }
        this.convert(MASK + 1);
    }

    public LargeInteger(String str) {
        this(str, DEFAULT_BASE);
    }

    private static int MASK = 0x7fffffff;

    /* TODO Constructor taking long as argument. Constructor for XYZ class. */
    public LargeInteger(Long num, int b) {
        this.base = b;
        this.sign = num == 0 ? 0 : (num > 0 ? 1 : -1);
        while (num > 0) {
            this.digits.add(Long.valueOf(num & MASK));
            num = num >>> 31;
        }
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
        if (this.sign == 0) return "0";
        String res = stringWithBase(10, "").reverse().toString();
        if (this.sign < 0) return "-" + res;
        return res;
    }

    /*
 * TODO Print the base + ":" + elements of the list, separated by spaces.
 */
    public void printList() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.base);
        sb.append(":");
        sb.append(stringWithBase(this.base, " ").toString());
        System.out.println(sb.toString());
    }


    private static Long getNext(Iterator<Long> iter) {
        return iter.hasNext() ? iter.next() : null;
    }

    /**
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
            Long temp;
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
            carry = temp >> 31;

            result.digits.add(temp & MASK);
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
            // same sign addition or different sign substract
            // just use add
            LargeInteger result = addition(num1, num2, true);
            result.sign = num1.sign;
            return result;
        } else {
            // substract
            int cmp = num1.compareWithoutSign(num2);
            LargeInteger result;
            if (cmp > 0) {
                result = addition(num1, num2, false);
                result.sign = num1.sign;
                return result;
            } else if (cmp < 0) {
                // exchange num2 and num1 then sign should be reversed
                result = addition(num2, num1, false);
                result.sign = -num2.sign;
                return result;
            } else {
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
        LargeInteger tempNum = new LargeInteger(num);
        long curBase = num.base;
        Iterator<Long> iter = tempNum.digits.descendingIterator();
        num.digits.clear();
        while (iter.hasNext()) {
            Long cur = iter.next();
            if (curBase == MASK + 1) {
                temp = (temp << 31) + cur;
            } else {
                temp = temp * curBase + cur;
            }
            Long val;
            if (targetBase == MASK + 1) {
                val = temp >>> 31;
                temp = temp & MASK;
            } else {
                val = temp / targetBase;
                temp = temp % targetBase;
            }
            num.digits.add(0, val);
        }
        iter = num.digits.descendingIterator();
        while (iter.hasNext()) {
            Long val = iter.next();
            if (val != 0) break;
            iter.remove();
        }
        return temp;
    }

    private StringBuilder stringWithBase(int targetBase, String seperator) {
        LargeInteger temp = new LargeInteger(this);
        temp.base = MASK + 1;
        StringBuilder sb = new StringBuilder();
        while (!temp.digits.isEmpty()) {
            sb.append(seperator);
            sb.append(getRemainder(temp, targetBase));
        }
        return sb;
    }

    /**
     * Converting current largeInteger instance into a new LargeInteger instance with given targetBase,
     *
     * @param targetBase
     */
    private void convert(int targetBase) {
        LargeInteger temp = new LargeInteger(this);
        temp.base = DEFAULT_BASE;
        this.digits.clear();
        while (!temp.digits.isEmpty()) {
            Long n = getRemainder(temp, targetBase);
            this.digits.add(n);
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
        temp.base = targetBase;
        return temp;
    }

    /**
     * Initialize a zero LargeInteger instance with given base.
     *
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
        if (a.base != b.base) return null;
        if (a.sign == 0 || b.sign == 0) {
            return new LargeInteger(a.base);
        }
        LargeInteger result = multi(a, b);
        result.sign = a.sign != b.sign ? -1 : 1;
        return result;
    }

    private void shiftToBigger(int n) {
        for (int i = 0; i < n; i++)
            this.digits.addFirst(0L);
    }

    private void shiftToSmaller(int n) {
        for (int i = 0; i < n; i++)
            this.digits.removeFirst();
    }

    private static LargeInteger multi(LargeInteger a, LargeInteger b) {
        LargeInteger result = new LargeInteger(a.base);
        ListIterator<Long> iter_a = a.digits.listIterator();
        Long carry = 0L;
        while (iter_a.hasNext()) {
            ListIterator<Long> iter_b = b.digits.listIterator();
            Long cur_a = iter_a.next();
            int idx = iter_a.nextIndex() - 1;
            ListIterator<Long> res_iter = result.digits.listIterator(idx);
            while (iter_b.hasNext()) {
                Long cur = iter_b.next();
                if (res_iter.hasNext()) {
                    cur = cur * cur_a + res_iter.next() + carry;
                    res_iter.set(cur & MASK);
                } else {
                    cur = cur * cur_a + carry;
                    res_iter.add(cur & MASK);
                }
                carry = cur >>> 31;
            }
            while (carry > 0) {
                result.digits.add(carry & MASK);
                carry = carry >>> 31;
            }
        }
        return result;
    }

    public int length() {
        return this.digits.size();
    }

    private static LargeInteger[] split(LargeInteger a, int index) {
        LargeInteger a1 = new LargeInteger(a.base);
        a1.digits.addAll(a.digits.subList(0, index)); //lower part
        a1.sign = a.sign;
        LargeInteger a2 = new LargeInteger(a.base);
        a2.digits.addAll(a.digits.subList(index, a.digits.size())); // higher part
        a2.sign = a.sign;
        return new LargeInteger[]{a1, a2};
    }

//    private static LargeInteger multi(LargeInteger a, LargeInteger b) {
//        if (a.length() == 0 || b.length() == 0) return new LargeInteger(a.base);
//        if (a.length() == 1 && b.length() == 1) {
//            Long r = a.digits.iterator().next() * b.digits.iterator().next();
//            LargeInteger result = new LargeInteger(a.base);
//            result.digits.add(r % a.base);
//            if (r / a.base > 0) result.digits.add(r / a.base);
//            return result;
//        }
//        int idx = (Math.max(a.length(), b.length()) + 1) / 2;
//        LargeInteger A[] = split(a, idx);
//        LargeInteger B[] = split(b, idx);
//        // least significant is part placed in index 0, hight part is placed in index 1
//        LargeInteger a1b1 = multi(A[1], B[1]); // higher part multiplication
//        LargeInteger a2b2 = multi(A[0], B[0]); // lower part multiplication
//        LargeInteger a1b1_add = add(A[1], A[0]);
//        LargeInteger a2b2_add = add(B[1], B[0]);
//        LargeInteger ab = multi(a1b1_add, a2b2_add);
//        LargeInteger result = subtract(subtract(ab, a1b1), a2b2);
//        a1b1.shift(2 * idx);
//        result.shift(idx);
//        return add(add(result, a1b1), a2b2);
//    }

    public static final LargeInteger ZERO = new LargeInteger(10);

    public static final LargeInteger ONE = new LargeInteger(1L, 10);

    public static final LargeInteger TWO = new LargeInteger(2L, 10);
    /*
     * TODO Divide a by b result. Fractional part is discarded (take just the
     * quotient). Both a and b may be positive or negative. If b is 0, raise an
     * exception.
     */
    public static LargeInteger[] div(LargeInteger a, LargeInteger b) {
        LargeInteger quotient = new LargeInteger(a.base);
        quotient.sign = 1;
        LargeInteger temp_a = a;
        while (temp_a.compareTo(b) >= 0) {
            LargeInteger tempQuotient = ONE;
            LargeInteger temp_b = b;
            while (temp_a.compareWithoutSign(temp_b) >= 0) {
                temp_a = add(temp_a, temp_b, temp_a.sign != temp_b.sign);
                quotient = add(quotient, tempQuotient, quotient.sign == tempQuotient.sign);
                tempQuotient = add(tempQuotient, tempQuotient);
                temp_b = add(temp_b, temp_b);
            }
        }
        return new LargeInteger[] {quotient, temp_a};
    }

    private LargeInteger getHighPart(int digits) {
        return getPart(0, digits);
    }

    /**
     *
     * @param fromHight  high is most significant digits, parameter from 0...length
     * @param toLow     low is least significant digits, from 0...length, should be higher than fromHight
     * @return new large integer contains the number from high to low.
     */
    private LargeInteger getPart(int fromHight, int toLow) {
        LargeInteger r = new LargeInteger(this.base);
        r.sign = this.sign;
        r.digits.addAll(this.digits.subList(this.length() - toLow - 1 , this.length() - fromHight));
        return r;
    }

    public static LargeInteger divide(LargeInteger a, LargeInteger b) {
        if (b.sign == 0) throw new ArithmeticException("Division by zero");
        if (a.sign == 0) return ZERO;
        if (b.compareWithoutSign(ONE) == 0) {
            LargeInteger r = new LargeInteger(a);
            r.sign = a.sign == b.sign ? 1 : -1;
            return r;
        }
        LargeInteger [] res = div(a, b);
        res[0].base = a.base;
        return res[0];
//        int i = b.length();
//        LargeInteger quotient = new LargeInteger(a.base);
//        LargeInteger part = a.getHighPart(b.length());
//        int curQuo = 0, curRem = 0;
//        for (; i < a.length(); i += 1) {
//            int cmp = part.compareWithoutSign(b);
//            if (cmp == 0) {
//                curRem = 0;
//                curQuo *= 10;
//            }
//            else if (cmp < 0) {
//                i += 1;
//                Long nextNum = a.digits.get(a.length() - i - b.length());
//                part.digits.add(0, nextNum);
//            }
//            else {
//                LargeInteger [] division = div(part, b);
//                quotient = add(quotient, division[0]);
//            }
//        }
    }

    /*
     * TODO remainder you get when a is divided by b (a%b). Assume that a is
     * non-negative, and b > 0.
     */
    public static LargeInteger mod(LargeInteger a, LargeInteger b) {
        if (b.sign == 0) throw new ArithmeticException("Division by zero");
        if (a.sign == 0) return ZERO;
        if (b.compareWithoutSign(ONE) == 0) {
            LargeInteger r = new LargeInteger(a);
            r.sign = a.sign == b.sign ? 1 : -1;
            return r;
        }
        LargeInteger [] res = div(a, b);
        res[1].base = a.base;
        return res[1];
    }

    /*
     * TODO given an XYZ a, representing the number x and n, returns the BigNum
     * corresponding to x^n (x to the power n). Assume that n is a nonnegative
     * number. Use divide-and-conquer to implement power using O(log n) calls to
     * product and add.
     */
    public static LargeInteger power(LargeInteger a, long n) {
        if (n == 0) {
            if (a.compareTo(ZERO) == 0) throw new ArithmeticException("Division by zero");
            return new LargeInteger("1", a.base);
        }
        if (n == 1) {
            return a;
        }
        if (n%2 == 0) {
            return product(power(a, n/2), power(a, n/2));
        }
        else {
            return product(product(power(a, n/2), power(a, n/2)), a);
        }
    }

    /*
     * TODO (L2) Return a^n, where a and n are both XYZ. Here a may be negative,
     * but assume that n is non-negative.
     */
    private static LargeInteger pow(LargeInteger x, LargeInteger n) {
        if (n.length() == 1) {
            return power(x, n.digits.get(0));
        }
        else {
            long a0 = n.digits.get(0);
            n.shiftToSmaller(1);
            LargeInteger xToS = power(x, n);
            long B = (long)MASK + 1;
            return product(power(xToS, B), power(x, a0));
        }
    }

    public static LargeInteger power(LargeInteger x, LargeInteger n) {
        if (n.compareTo(ZERO) == 0) {
            if (x.compareTo(ZERO) == 0) throw new ArithmeticException("Division by zero");
            LargeInteger res = new LargeInteger("1", x.base);
            return res;
        }
        int sign = x.sign;
        LargeInteger temp = new LargeInteger(n);
        long firstNum = n.digits.get(0);
        LargeInteger res = pow(x, temp);
        if (firstNum%2 == 0) {
            res.sign = 1;
        }
        else {
            res.sign = sign;
        }
        return res;
    }

    /*
     * TODO return the square root of a (truncated). Use binary search. Assume
     * that a is non-negative.
     */
    public static LargeInteger squareRoot(LargeInteger a) {
        if (a.sign < 0) throw new ArithmeticException("Negative Number");
        LargeInteger low = new LargeInteger(ONE);
        LargeInteger high = new LargeInteger(a);
        while (add(low, ONE).compareTo(high) < 0) {
            LargeInteger mid = divide(add(low, high), new LargeInteger("2"));
            LargeInteger p = product(mid, mid);
            if (a.compareWithoutSign(p) < 0) {
                high = mid;
            }
            else if (a.compareWithoutSign(p) > 0) {
                low = mid;
            }
            else return mid;
        }
        return low;
    }

    public static LargeInteger fatorial(LargeInteger a) {
        LargeInteger res;
        if (a.compareWithoutSign(ZERO) == 0 || a.compareWithoutSign(ONE) == 0) {
            res = new LargeInteger(ONE);
            res.base = a.base;
            return res;
        }
        LargeInteger i = ONE;
        res = ONE;
        while (i.compareWithoutSign(a)<=0) {
            res = product(i, res);
            i = add(i, ONE);
        }
        res.base = a.base;
        return res;
    }


}
