import java.util.LinkedList;

/**
 * Immutable class LargeInteger represent arbitrary large integer numbers
 * 
 * @author LiP
 * 
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
	private LinkedList<Long> digits;
	/*
	 * -1 for negative LargeInteger, 1 for positive LargeInteger, 0 for zero
	 * value
	 */
	private int sign;
	/*
	 * Actual effective length of the LargeInteger as represented in digits
	 */
	private int length;

	/*
	 * TODO Constructor taking String as argument. Constructor for XYZ class;
	 * takes a string s as parameter, that stores a number in decimal, and
	 * creates the XYZ object representing that number. The string can have
	 * arbitrary length. In Level 1, the string contains only the numerals 0-9,
	 * with no leading zeroes.
	 */
	public LargeInteger(String str, int b) {
	}

	public LargeInteger(String str) {
		this(str, DEFAULT_BASE);
	}

	/* TODO Constructor taking long as argument. Constructor for XYZ class. */
	public LargeInteger(Long num, int b) {
	}

	public LargeInteger(Long num) {
		this(num, DEFAULT_BASE);
	}

	/*
	 * TODO implement compareTo() function
	 */
	@Override
	public int compareTo(LargeInteger other) {
		return 0;
	}

	/*
	 * TODO convert the XYZ class object into its equivalent string (in
	 * decimal). There should be no leading zeroes in the string.
	 */
	public String toString() {
		return null;
	}

	/*
	 * TODO Print the base + ":" + elements of the list, separated by spaces.
	 */
	public void printList() {

	}

	/*
	 * TODO sum of two numbers stored as XYZ.
	 */
	public static LargeInteger add(LargeInteger a, LargeInteger b) {
		return null;
	}

	/*
	 * TODO given two XYZ a and b as parameters, representing the numbers n1 and
	 * n2 repectively, returns the XYZ corresponding to n1-n2. If you have
	 * implemented negative numbers, return the actual answer. If not, then if
	 * the result is negative, it returns the XYZ for number 0.
	 */
	public static LargeInteger subtract(LargeInteger a, LargeInteger b) {
		return null;
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
