import java.math.BigInteger;

public class JavaBigIntegerTester {

	public static void main(String[] args) {
		BigInteger a = new BigInteger("992027944069944027992001");
		System.out.println(a.multiply(new BigInteger("2")));

		LargeInteger l = new LargeInteger("992027944069944027992001");
		System.out.println(LargeInteger.add(l, l));
	}

}
