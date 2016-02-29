import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Map;

/**
 * Concrete subclass to represent an assignment command
 * 
 * @author Peng Li
 */
public class AssignmentCmd extends Cmd {

	String[] mathExp; // reverse polish notation of the math expression

	public AssignmentCmd(String var, int base,
			Map<String, LargeInteger> varMap, String[] mathExp) {
		super(var, base, varMap);
		this.mathExp = mathExp;
	}

	@Override
	/**
	 * implementation of execute() method for assignment command
	 */
	public void execute() {
		Deque<LargeInteger> operandStack = new ArrayDeque<>(); // stack for
																// operands
		for (String s : mathExp) {
			if (s.matches(Utilities.NUM)) {
				// push a constant into operand stack
				operandStack.push(new LargeInteger(s, base));

			} else if (s.matches(Utilities.VAR)) {
				// push the value of a variable into operand stack
				operandStack.push(varMap.get(s));

			} else if (s.matches(Utilities.OPS)) {
				// incase of operator, pop relevant operand from stack, perform
				// operation and push the result back into the operand stack
				// notice that the first popped value is the right operand
				LargeInteger opr1;
				LargeInteger opr2;
				switch (s) {
				case "+":
					opr1 = operandStack.pop();
					opr2 = operandStack.pop();
					operandStack.push(LargeInteger.add(opr2, opr1));
					break;

				case "-":
					opr1 = operandStack.pop();
					opr2 = operandStack.pop();
					operandStack.push(LargeInteger.subtract(opr2, opr1));
					break;

				case "*":
					opr1 = operandStack.pop();
					opr2 = operandStack.pop();
					operandStack.push(LargeInteger.product(opr2, opr1));
					break;

				case "/":
					opr1 = operandStack.pop();
					opr2 = operandStack.pop();
					operandStack.push(LargeInteger.divide(opr2, opr1));
					break;

				case "%":
					opr1 = operandStack.pop();
					opr2 = operandStack.pop();
					operandStack.push(LargeInteger.mod(opr2, opr1));
					break;

				case "^":
					opr1 = operandStack.pop();
					opr2 = operandStack.pop();
					operandStack.push(LargeInteger.power(opr2, opr1));
					break;

				case "!":
					operandStack
							.push(LargeInteger.factorial(operandStack.pop()));
					break;

				case "~":
					operandStack.push(LargeInteger.squareRoot(operandStack
							.pop()));
					break;

				default:
					break;
				}
			}
		}

		// store the value of the returning variable
		varMap.put(var, operandStack.pop());
	};

	@Override
	public String toString() {
		String s = "assignment command, var: " + var + " , RPN expression: "
				+ Arrays.toString(mathExp);
		return s;
	}
}
