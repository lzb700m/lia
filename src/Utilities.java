import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Utility class to define command regular expression used by the project, and
 * implementation of shunting yard algorithm
 * 
 * @author Peng Li
 */

public class Utilities {
	static final String DELIMETER = "[=+\\-*/%\\^!~\\?:()]";
	static final String CMD_TOKENIZER = "(?<=op)|(?=op)".replace("op",
			DELIMETER); // regex to tokenizer user command string

	static final String LN = "[a-z]+|(0|[1-9][0-9]*)"; // LargeNumber
	static final String VAR = "[a-z]+"; // variable
	static final String NUM = "(0|[1-9][0-9]*)"; // LargeNumber constant
	static final String OPS = "[+\\-*/%\\^!~]"; // operator
	static final String LP = "[(]"; // left parenthesis
	static final String RP = "[)]"; // right parenthesis
	static final String LTR_OPS = "[+\\-*/%]"; // left to right
												// associative
	static final String RTL_OPS = "[\\^]"; // right to left associative
	static final String U_OPS = "[!~]"; // unary operator

	// operator precedence map
	static final Map<String, Integer> PRECEDENCE;
	static {
		PRECEDENCE = new HashMap<>();
		PRECEDENCE.put("+", 0);
		PRECEDENCE.put("-", 0);
		PRECEDENCE.put("*", 5);
		PRECEDENCE.put("/", 5);
		PRECEDENCE.put("%", 5);
		PRECEDENCE.put("^", 10);
	}

	/**
	 * shunting yard algorithm
	 * 
	 * @param tokens
	 *            : infix notation of an expression
	 * @return reverse polish notation
	 */
	static String[] parseInfix(String[] tokens) {

		Queue<String> output = new LinkedList<>();
		Deque<String> opStack = new ArrayDeque<>(); // operator stack

		for (String token : tokens) {
			if (token.matches(LN)) {
				// token is a number or variable, add to output queue
				output.add(token);

			} else if (token.matches(OPS)) {
				// token is an operator
				while (!opStack.isEmpty()) {
					String top = opStack.peek();

					if (token.matches(LTR_OPS) && PRECEDENCE.containsKey(top)
							&& PRECEDENCE.get(token) <= PRECEDENCE.get(top)) {
						output.add(opStack.pop());
						continue;
					}

					if (token.matches(RTL_OPS) && PRECEDENCE.containsKey(top)
							&& PRECEDENCE.get(token) < PRECEDENCE.get(top)) {
						output.add(opStack.pop());
						continue;
					}

					break;
				}
				opStack.push(token);

			} else if (token.matches(LP)) {
				// token is left parenthesis
				opStack.push(token);

			} else if (token.matches(RP)) {
				// token is right parenthesis
				boolean foundLeft = false;
				while (!opStack.isEmpty()) {
					String top = opStack.peek();
					if (!top.matches(LP)) {
						output.add(opStack.pop());
					} else {
						foundLeft = true;
						opStack.pop();
						break;
					}
				}

				if (opStack.isEmpty() && !foundLeft) {
					// no paring left parenthesis found
					throw new IllegalArgumentException();
				}

			} else {
				// token does not match anything
				throw new IllegalArgumentException();
			}
		}

		while (!opStack.isEmpty()) {
			if (opStack.peek().matches(LP)) {
				// still left parenthesis left and no matching right parenthesis
				throw new IllegalArgumentException();
			} else {
				output.add(opStack.pop());
			}
		}

		String[] ret = output.toArray(new String[output.size()]);

		return ret;
	}
}