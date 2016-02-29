import java.util.Map;

/**
 * Abstract class to represent command from console
 * 
 * @author Peng Li
 */
public abstract class Cmd {

	String var; // returning variable of the command
	int base; // base used by large integer in this calculator
	Map<String, LargeInteger> varMap; // all involved variable

	public Cmd(String var, int base, Map<String, LargeInteger> varMap) {
		this.base = base;
		this.var = var;
		this.varMap = varMap;
	}

	// abstract method to be implemented by sub-class
	abstract void execute();
}
