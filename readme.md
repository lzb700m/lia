# Implementation of arbitary large number and its arithmetics

##Summary
In this project, we develop a program that implements arithmetic with large integers, of arbitrary size. In our implementation we use LargeInteger class to store the arbitrary size integer number, and support some arithmetic operation, e.g: Add, Subtract, Product, Divide, Mod, Power, SquareRoot etc. And at the same time we support arbitrary base from 2 to 10000, we can convert to any other base representation.


##Input Output Specification


##Class Description

**LargeInteger.java**	 Class of large number and implementing all the arithmetic operation.

**AssignmentCmd.java** 

**Cmd.java**

**ConditionCmd.java**

**LargeIntegerCalculator.java**

**PrintlistCmd.java**

**RetriveCmd.java**

##Algorithm

#### 1.	Arithmetic Operation Implementation:	
#####Representation of Number
We are using a LinkedList object to represent the large number, each node in the LinkedList object actually is a base 2^31 number. As a result, this representation of large number significantly speed up all the operations due to the shorter length. Each node stores a long number
#####Addition and Subtraction
We actully use one addition method to handle both add and subtract operation, and this method can take a flag to determine we are adding or subtracting. And the wrapper method only take care of the sign of the result and some corner case like summation equals to 0 and adding or subtracting 0. 
#####Multiplication
We do this using the naive algorithm whose running time is O(n^2). It just simply iterate all the nodes in both two large numbers and update the current node by the multiplication of two nodes and current carry, then update carry and move forward to next node from least bit to most bit.
#####Division, Mod, Power and SquareRoot
All of these methods are implemented by binary search method. Division and Mod algorithom takes O((logn)^2) (in this case the n is the value of the large number, so this takes more time when numerator is too large and denominator is too small), each time the current numerator will be decreased by double of current denominator.  Power and SquareRoot are using naive binary search algorithm.
#####Base conversion
All the operations implemented in the LargeInteger class are using base 10. Only when you call printList method, we will construct a string by the give base. When we convert the number to other base, we divide the current number by new base, and update the current number by the quotient, the remainder is the digit in that bit from lower bit to higher bit.

#### 2.	Shunting Yard Implementation:

##Implementation Challenge
For the Arithmetic Operation part, the challenge thing is the optimize the running time of multiplication and division. After I read the source code of the implementation of Java BigInteger file, I got the idea of using a large base to store the number to reduce the length of list. The other thing is implementation of Product method, I've tried both the O(n^(log3)) and O(n^2) methods, it proved that the former has large coefficient, so the running time is even larger than the later one. The base conversion is also a big challenge, 
