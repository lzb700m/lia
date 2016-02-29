import java.util.Map;

/**
 * Concrete subclass to represent an print list command
 * 
 * @author Peng Li
 */
public class PrintlistCmd extends Cmd {

	public PrintlistCmd(String var, int base, Map<String, LargeInteger> varMap) {
		super(var, base, varMap);
	}

	@Override
	void execute() {
		varMap.get(var).printList();
	}

	@Override
	public String toString() {
		String s = "printlist command, var: " + var;
		return s;
	}

}
