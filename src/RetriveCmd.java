import java.util.Map;

/**
 * Concrete subclass to represent an retrieve command
 * 
 * @author Peng Li
 */
public class RetriveCmd extends Cmd {

	public RetriveCmd(String var, int base, Map<String, LargeInteger> varMap) {
		super(var, base, varMap);
	}

	@Override
	void execute() {
		System.out.println(varMap.get(var));
	}

	@Override
	public String toString() {
		String s = "retrive command, var: " + var;
		return s;
	}

}
