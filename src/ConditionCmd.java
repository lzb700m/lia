import java.util.Map;

/**
 * Concrete subclass to represent an conditional command
 * 
 * @author Peng Li
 */
public class ConditionCmd extends Cmd {

	int notzero; // line number to execute as input from console when condition
					// is true
	int zero; // line number to execute as input from console when condition is
				// false

	public ConditionCmd(String var, int base, Map<String, LargeInteger> varMap,
			int notzero, int zero) {
		super(var, base, varMap);
		this.notzero = notzero;
		this.zero = zero;
	}

	@Override
	@Deprecated
	public void execute() {

	}

	/**
	 * Check the condition, and return the command line number according to the
	 * check result
	 * 
	 * @return command line number to be executed next
	 */
	public int check() {
		if (varMap.get(var).compareTo(LargeInteger.ZERO) == 0) {
			return zero;
		} else {
			return notzero;
		}
	}

	@Override
	public String toString() {
		String s = "conditional command, var: " + var + " , notzero line: "
				+ notzero + ", zero line: " + zero;
		return s;
	}

}
