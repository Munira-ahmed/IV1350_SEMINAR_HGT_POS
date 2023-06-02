# IV1350_SEMINAR_HGT_POS
Additional Higher Grade Tasks

Problems to solve before starting:
1 Breaks VMC as view directly calls integration layer, for example: error message handler and log handler 

2 It's better to observe Sale, instead of the controller. Now the controller needs to know when the total amount paid has changed, ask Sale about it, and then send it to the observers. 

3 There's no reason to catch an exception just to immediately throw it again, as you did for example SaleInformation in the controller. If you don't want to change anything with the exception, it's better to remove all exception handling from that method, and just let the exception pass directly to the view. Your solution is accepted (1p). To get 2p you have to correct bullets 1-3. Submit the updated report under seminar 5 if you make these changes. Clearly indicate the changes in the updated report.


Task 1:
Use the Template Method design pattern for the observers from seminar 4. The template class
you create must follow the structure described by the pseudocode in the task.The grade
is not affected no matter how simple or advanced the view is.

Task 2:
This task relates to the section Second Reason to Prefer Composition: Inheritance Breaks
Encapsulation in chapter 9.3 in A First Course in Object-Oriented Development. Make
sure to understand that section before proceeding. The section describes how a class
can be adapted using inheritance, and how it can be adapted using composition.
The task is now to adapt any class in the java libraries from Oracle. You are free
to choose any class to adapt, except a list, and also free to choose how you adapt it.
Write one new class that adapts using inheritance, and another new class that adapts
using composition, as described in the course book section mentioned above. Also write
a main method which instantiates your new classes and executes the adaptions. The
program must include printouts illustrating how your classes work. A suggestion, if it’s
hard to find a class to adapt, is to choose java.util.Random.

Task 3:
Write unit tests for all methods that print to System.out in your Process Sale program.
This must include at least the main method, the View class and the receipt printout.
If you have additional methods printing to System.out, also those must be tested. The
tests of the View class must test all printouts in that class, but you only have to test
printout containing information. It’s not required to test printouts which exist only to
make the view more readable.
