#Integer arithmetic with arbitrarily large numbers

##Summary
All requirements from Level 1, Level 2 and Level 3 are fulfilled.

A LargeInteger class that can represent an arbitrary large integer is implemented, it also implements static methods for LargeInteger arithmetics. Including: addition, subtraction, product, integer division, modulo, power, factorial and square root. A public printList() method is also provided the internal representation of the LargeInteger object.

A calculator program LargeIntegerCalculator implements the required input / output specification for user to perform complicated LargeInteger arithmetics. It supports not only simple arithmetic operators, but also compound arithmetic expressions, including parenthesis. It also supports condition based goto statements that can be used to construct loops.


##Input Output Specification

The LargeIntegerCalculator program takes an optional command line argument as the base of LargeInteger used in the calculation. If the base is not specified, a default base value of 10 is used. To launch to program, run:

```
javac LargeIntegerCalculator.java
java LargeIntegerCalculator [base]
```

Then the program takes input from stdin (console). The input is a sequence of lines. Each line has the line number as its first element. Lines are numbered but not necessarily sequentially. The input uses names of variables, which are always single letters (a,b,...). Each line has one of the following formats (after the line number).[Comments starting with # are not part of the specification):

```
# Operations of the form var=something calculate the expression in
# something and assign it to the variable on the left


lineno var=NumberInDecimal	# sets x to be that number
lineno var=var+var		# sum of two numbers
lineno var=var*var		# product of two numbers
lineno var=var-var		# first number minus second number
lineno var=var/var		# first number divided by second number
lineno var=var%var		# remainder of first number divided by second number
lineno var=var^var		# power
lineno var=var!			# factorial of number
lineno var=var~			# square root of number
lineno var=math expressions
						# compound math expression of operators above, parenthesis allowed 
lineno var				# print the value of the variable to stdout (console)
lineno var?notzero:zero	# if var value is not 0, then go to Line number notzero, :zero is optional, if present, go to line zero, if var value is equal to 0
lineno var)				# call printList() for the XYZ of the variable
```

When the input is done (signalled by typing ctrl-d in Unix/Mac or ctrl-z in windows), the program starts to evaluate and execute the command read from console. For example:

```
Sample input:
1 x=999
2 y=8
3 z=x+y
4 z
5 a=x^y
6 a
7 z)

Sample output:
Its output is shown below, assuming that B = 100.  Only lines 4, 6, and 7 produce output.
1007
992027944069944027992001
100:7 10
```

##Class Description

**LargeInteger.java**: Class of artitrary large number and the implementation of all the arithmetic operations.

**LargeIntegerCalculator.java**: LargeInteger calculator program.

**Cmd.java**: Base class for all command object, this is an abstract class.

**AssignmentCmd.java**: Concrete class for assignment command.

**ConditionCmd.java**: Concrete class for conditional command.

**PrintlistCmd.java**: Concrete class for printList command.

**RetriveCmd.java**: Concrete class for retrieve command.

**Utilities.java**: Definition of common constants and regular expressions, and implementation of shunting yard algorithm.

##Algorithm and Design

###1. Arithmetic Operation Implementation:	
####Representation of Number
We are using a LinkedList object to represent the large number, each node in the LinkedList object actually is a base 2^31 number. As a result, this representation of large number significantly speed up all the operations due to the shorter length. Each node stores a long number

####Addition and Subtraction
We actully use one addition method to handle both add and subtract operation, and this method can take a flag to determine we are adding or subtracting. And the wrapper method only take care of the sign of the result and some corner case like summation equals to 0 and adding or subtracting 0.

####Multiplication
We do this using the naive algorithm whose running time is O(n^2). It just simply iterate all the nodes in both two large numbers and update the current node by the multiplication of two nodes and current carry, then update carry and move forward to next node from least bit to most bit.

####Division, Mod, Power and SquareRoot
All of these methods are implemented by binary search method. Division and Mod algorithom takes O((logn)^2) (in this case the n is the value of the large number, so this takes more time when numerator is too large and denominator is too small), each time the current numerator will be decreased by double of current denominator.  Power and SquareRoot are using naive binary search algorithm.

####Base conversion
All the operations implemented in the LargeInteger class are using base 10. Only when you call printList method, we will construct a string by the give base. When we convert the number to other base, we divide the current number by new base, and update the current number by the quotient, the remainder is the digit in that bit from lower bit to higher bit.

### 2.Calculator Program
####Math expression parsing
The input expression from console is in infix notation, in order to effeciently process the command, we need to parse it into reverse polish notation. The shunting yard algorithm ([wikipedia shunting_yard]) is implemented to perform this task.

####Cmd Class design
We categorized the console commands into 4 categories (assignment, retrive, condition based goto and printList command). And they all inherit the base class Cmd which is defined as an abstract class, and they all implement the execute() abstract method. This allows us to keep all command objects into a single arrayList, and by using polymorphism, allows us to simplify our code significantly.

####Command execution pointer
The commands are executed sequentially except when we find a conditional statement, in which case we need to go to a particular line of command according to the condition. Instead of using recursion (which could cause stack overflow if the command is really complicated), we implement an iterative algorithm for managing the execution pointer. Whenever a conditional statement is found, we first evaluate the condition and calculate the next-to-be executed command, terminate current iteration and start a new one.


[wikipedia shunting_yard]: https://en.wikipedia.org/wiki/Shunting-yard_algorithm