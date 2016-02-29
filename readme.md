# Implementation of arbitary large number and its arithmetics

##Summary
___

##Input Output Specification
___

##Class Description
___
**LargeInteger.java	** Class of large number and implementing all the arithmetic operation.

**AssignmentCmd.java** 

**Cmd.java**

**ConditionCmd.java**

**LargeIntegerCalculator.java**

**PrintlistCmd.java	**

**RetriveCmd.java**

##Algorithm
___
#### 1.	Arithmetic Implementation:	
#####Representation of Number
We are using a LinkedList object to represent the large number, each node in the LinkedList object actually is a base 2^31 number. As a result, this representation of large number significantly speed up all the operations due to the shorter length. Each node stores a long number
#####Addition and Subtraction
We actully use one addition method to handle both add and subtract operation, and this method can take a flag to determine we are adding or subtracting. And the wrapper method only take care of the sign of the result and some corner case like summation equals to 0 and adding or subtracting 0. 
#####Multiplication
We do this using the naive algorithm whose running time is O(n^2). It just simply iterate all the nodes in both two large numbers and update the current node by the multiplication of two nodes and current carry, then update carry and move forward to next node from least bit to most bit.
#####Division, Mod, Power and SquareRoot
All of these methods are implemented by binary search method. Division and Mod algorithom takes O((logn)^2) (in this case the n is the value of the large number, so this takes more time when numerator is too large and denominator is too small), each time the current numerator will be decreased by double of current denominator.  Power and SquareRoot are using naive binary search algorithm.
#### 2.	Shunting Yard Implementation:	