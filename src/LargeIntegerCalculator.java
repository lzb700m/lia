import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Simple calculator for arbitrary large integers. Operation supported: addition
 * (+), subtraction(-), product(*), integer division(/), modulo(%), power(^),
 * factorial(!), square root(~), retrieve value(variable), print internal
 * representation of large integer(variable)) and conditional
 * statement(variable?line number 1:line number 2).
 * 
 * @author Peng Li
 */

public class LargeIntegerCalculator {

	/*
	 * Constant Definition: Regex for categorizing input command, this is
	 * according to project specification
	 */
	// assignment command "variable=expression"
	private static final String ASSIGNMENT = "[a-z]+=.+";
	// retrieve command "variable"
	private static final String RETRIEVE = "[a-z]+";
	// conditional command (if statement) "variable?line1:line2" (":line2" is
	// optional)
	private static final String CONDITION = "[a-z]+\\?(0|[1-9][0-9]*)(:(0|[1-9][0-9]*))?";
	// print list command "variable)"
	private static final String PRINTLIST = "[a-z]+\\)";

	int base; // base used by large integer in this calculator
	private List<Cmd> cmdList; // list of command from console
	private Map<Integer, Integer> cmdSequence; // correspondence of actual
												// command order and line number
	private Map<String, LargeInteger> varTable; // table of all variables

	/**
	 * Constructor
	 * 
	 * @param base
	 */
	public LargeIntegerCalculator(int base) {
		this.base = base;
		this.cmdList = new ArrayList<>();
		this.cmdSequence = new HashMap<>();
		this.varTable = new HashMap<>();
	}

	/**
	 * Add a command into the calculator
	 * 
	 * @param index
	 *            : actual sequence of the command
	 * @param lineNum
	 *            : line number of command as input from console
	 * @param cmd
	 *            : actual command as input from console
	 */
	private void addCmd(int index, int lineNum, String cmd) {
		// tokenize the command string
		String[] tokens = cmd.split(Utilities.CMD_TOKENIZER);
		String var; // return variable name of the command
		Cmd newCmd; // command object

		if (cmd.matches(ASSIGNMENT)) {
			// for assignment command, convert it into reverse polish notation
			// before storing it in the command list
			String[] mathExp = Utilities.parseInfix(Arrays.copyOfRange(tokens,
					2, tokens.length));
			var = tokens[0];
			newCmd = new AssignmentCmd(var, base, varTable, mathExp);

		} else if (cmd.matches(RETRIEVE)) {
			var = tokens[0];
			newCmd = new RetriveCmd(var, base, varTable);

		} else if (cmd.matches(CONDITION)) {
			var = tokens[0];
			int notzero = Integer.parseInt(tokens[2]);
			// the else part of a conditional statement is optional, use -1 to
			// indicate that the else part is not there
			int zero = (tokens.length > 3) ? Integer.parseInt(tokens[4]) : -1;
			newCmd = new ConditionCmd(var, base, varTable, notzero, zero);

		} else if (cmd.matches(PRINTLIST)) {
			var = tokens[0];
			newCmd = new PrintlistCmd(var, base, varTable);

		} else {
			System.out.println("Not a valid command.");
			return;
		}

		if (!cmdSequence.containsKey(lineNum)) {
			cmdSequence.put(lineNum, index);
			cmdList.add(newCmd);
		} else {
			System.out.println("Line number already exists.");
			return;
		}
	}

	/**
	 * Diver method for executing the calculator instance
	 * 
	 */
	private void execute() {
		// start from the first command(0), nextIteration indicates start
		// command index of next iteration. -1 indicates end of program.
		int nextIteration = execute(0);
		while (nextIteration != -1) {
			nextIteration = execute(nextIteration);
		}
	}

	/**
	 * Execute command from command list sequentially starting from index. In
	 * case of conditional statement, break the loop and return the command
	 * index given by the condition.
	 * 
	 * @param index
	 *            : index of start command
	 * @return start command index of next iteration, or -1 if all commands are
	 *         executed to the end
	 */
	private int execute(int index) {
		int switchTo = -1;
		for (int i = index; i < cmdList.size(); i++) {
			if (!(cmdList.get(i) instanceof ConditionCmd)) {
				cmdList.get(i).execute();
			} else {
				ConditionCmd cCmd = (ConditionCmd) cmdList.get(i);
				if (cCmd.check() != -1) {
					switchTo = cmdSequence.get(cCmd.check());
					break;
				}
			}
		}

		return switchTo;
	}

	/**
	 * Driver program for command line arbitrary large integer calculator
	 * 
	 * @param args
	 *            : base of the large integer for internal representation
	 */
	public static void main(String[] args) {
		int base;
		if (args.length > 0) {
			base = Integer.parseInt(args[0]);
		} else {
			base = 10;
		}

		// create a calculator instance
		LargeIntegerCalculator calc = new LargeIntegerCalculator(base);

		int index = 0; // actual order of the command line input
		int lineNum; // user input line number of command line input
		String cmd; // actual command content (exclude the line number part)

		Scanner sc = new Scanner(System.in);
		Scanner innerSc = null; // inner scanner : scan over a line
		String line = null;

		while (!(line = sc.nextLine()).isEmpty()) {
			innerSc = new Scanner(line);
			lineNum = innerSc.nextInt();
			cmd = innerSc.next();
			// add command into calculator instance
			calc.addCmd(index, lineNum, cmd);
			index++;
		}

		innerSc.close();
		sc.close();
		calc.execute();
	}
}
