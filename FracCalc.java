// Caroline Sun
// Period 6
// AP CSA
// Fraction Calculator Project

import java.util.*;

// This program is an arthmetic calculator that can do simple mathematic operations (addition, subtraction, multiplication, division).
// Fraction and mixed number operations can also be done with the right format. 
public class FracCalc {

   // It is best if we have only one console object for input
   public static Scanner console = new Scanner(System.in);
   
   // This main method will loop through user input and then call the correct method to execute the user's request 
   // for help, test, or the mathematical operation on fractions. or, quit.
   public static void main(String[] args) {
   
      // initialize to false so that we start our loop
      boolean done = false;
      
      // When the user types in "quit", we are done.
      while (!done) {
         // prompt the user for input
         String input = getInput();
         
         // special case the "quit" command
         if (input.equalsIgnoreCase("quit")) {
            done = true;
         } else if (!UnitTestRunner.processCommand(input, FracCalc::processCommand)) {
        	   // We allowed the UnitTestRunner to handle the command first.
            // If the UnitTestRunner didn't handled the command, process normally.
            String result = processCommand(input);
            
            // print the result of processing the command
            System.out.println(result);
         }
      }
      
      System.out.println("Goodbye!");
      console.close();
   }

   // Prompt the user with a simple, "Enter: " and get the line of input.
   // Return the full line that the user typed in.
   public static String getInput() {
      System.out.print("Enter: ");
      String input = console.nextLine();
      if (input.equals("quit")) {
         return "quit";
      } else {
         return input;
      }
   }
   // processCommand will process every user command except for "quit".
   // It will return the String that should be printed to the console.
   // This method won't print anything.
   public static String processCommand(String input) {
      if (input.equalsIgnoreCase("help")) {
         return provideHelp();
      }
      // if the command is not "help", it should be an expression.
      // Of course, this is only if the user is being nice.
      return processExpression(input);
   }

   // The processExpression method calls separate helper methods and solves the problem in the input

   // Because most of the actual calculations are done in helper methods, this method's main job is to create the 
   // parameters for the helper methods by coverting parts of the String into integers
   public static String processExpression(String input) {
      // parses the given input into three objects: the first number, the operation symbol, and the second number
      Scanner parser = new Scanner(input);
      String obj1 = parser.next();
      String obj2 = parser.next();
      String obj3 = parser.next();
      // closes parser because no more objects are needed if the input was formatted correctly
      parser.close();
      // turns obj2 into a char because the symbol should be a singular character
      char sign = obj2.charAt(0);
         
      // The inproperFraction method is called to turn the two numbers into inproper fractions
      // This will help later because it won't matter if the numbers are whole, fractions, or mixed because they're all being converted,
      // so the calculations will be the same way; through fraction math
      String firstNum = inproperFraction(obj1); 
      String secondNum =  inproperFraction(obj3);

      // If either number receives the word "invalid" from the inproperFraction method, then an alert is returned that tells the user to enter something else
      if ((firstNum.equals("invalid")) || (secondNum.equals("invalid"))) {
         return "0 can't be a denominator, please enter something else";
      }

      // Separates the number Strings into two numbers each, the numerator and denominator
      int num1 = Integer.parseInt(firstNum.substring(0, firstNum.indexOf("/")));
      int den1 = Integer.parseInt(firstNum.substring(firstNum.indexOf("/") + 1, firstNum.length()));
      int num2 = Integer.parseInt(secondNum.substring(0, secondNum.indexOf("/")));
      int den2 = Integer.parseInt(secondNum.substring(secondNum.indexOf("/") + 1, secondNum.length()));
         
      // The actual result value calls the calculation method that has the numerators, denominators, and operator sign as parameters that will be used
      String result = calculation(num1, den1, num2, den2, sign);

      // If the calculation method returns the world "invalid" then an alert is returned that tells the user to enter something else
      if (result.equals("invalid")) {
         return "0 can't be a denominator, please enter something else";
      }
      // The final result String is again separated into two numbers so the whole fraction can be reduced
      int finalNum = Integer.parseInt(result.substring(0, result.indexOf("/")));
      int finalDen = Integer.parseInt(result.substring(result.indexOf("/") + 1, result.length()));
      // The final number that will be returned through input is a reduced version of the result that will be given through the reduce method
      input = reduce(finalNum, finalDen);    
      return input;    
   }

   // The inproperFraction method takes a String that's assumed to be a type of number (whole, fraction, mixed), and turns it into an inproper fraction
   // It's assumed that the user has typed in the numbers in the correct format (whole = 1, fraction = 1/2, mixed = 1_1/2)
   // The only situation where the input might be incorrect is where the denominator for a fraction is zero, which is undefined
   public static String inproperFraction(String value) {
      // objects are declared to be later used
      // den is 1 instead of 0 because fractions are undefined with a denominator equal to 0
      int whole = 0;
      int num = 0;
      int den = 1;
      // If the number is a whole number, whole would be what the given number is
      // This looks for the presence of a slash, which is used in both fractions and mixed numbers
      
      if (value.indexOf("/") == -1 ) {
            whole = Integer.parseInt(value);
         // If the number is a fraction, the String is split from where the beginning to the slash (numerator), then from the slash to the end (denominator)
         // This looks for the presence of an underscore, which is used only in mixed numbers
         } else {
            if (value.indexOf("_") == -1) {
            num = Integer.parseInt(value.substring(0, value.indexOf("/")));
            den = Integer.parseInt(value.substring(value.indexOf("/") + 1, value.length()));
            // If the number is a mixed number, the String is split from the beginning to the underscore (whole), then from the underscore to the slash (numerator),
            // then from the slash to the end (denominator)
            // If the past two conditions were false, then a slash and an underscore would've been present, which is only true for mixed numbers
            } else {
               // if the number is a mixed number
               whole = Integer.parseInt(value.substring(0, value.indexOf("_")));
               num = Integer.parseInt(value.substring(value.indexOf("_") + 1, value.indexOf("/")));
               den = Integer.parseInt(value.substring(value.indexOf("/") + 1, value.length()));
            }
         }
         
         // If the denominator is 0, then the fraction is undefined and Java will send an error, so if the denominator is equal to zero, the word "invalid" is 
         // directly returned to signify that the number is wrong
         if (den == 0) {
            return "invalid";
         }
         // If both numerator and denominator are negative, they're turned positive 
         // If only the denominator is negative, the sign is swapped to the numerator for aesthetics
         // In both situations, the two values are turned into the opposite sign
         if ((num < 0 && den < 0) || (den < 0)) {
            num = -num;
            den = -den;
         } 
         // Mixed numbers are written with a negative sign at the beginning (Ex: -2_3/4), but that still means the rest of the number, aka the fraction, is also negative
         // If the whole number is negative, the numerator is also converted to a negative number so they won't conflict
         if (whole < 0) {
            num = -num;
         }
         // This is the actual inproper fraction making using the values found above
         // The numerator is changed to include the whole number, which is done by multiplying the whole number by the denominator
         num = whole * den + num;

         // The final objects are made back into a string with a fraction and then returned
         String inproper = num + "/" + den;
         return inproper;
   }

   // The calculation method is where the actual calculations happen by calling all the numbers
   // and the symbol for the specific operation through parameters, then based on what the sign is,
   // it will do fraction math with the numerators and denominators
   public static String calculation(int num1, int den1, int num2, int den2, char sign) {
      // Declaring the final numerator and denominator results that will be returned as a string
      int numResult = 0;
      int denResult = 0;
      // Fraction addition is done here
      if (sign == '+') {
         // If the two denominators are equal, then only the numerators need to be added
         if (den1 == den2) {
            numResult = num1 + num2;
            denResult = den1;
         // If the two denominators aren't equal, conversions of the numerators and denominators will
         // have to be done using multiplication
         } else {
            numResult = num1 * den2 + num2 * den1;
            denResult = den1 * den2;
         }
      // Fraction subtraction is done here
      } else if (sign == '-') {
         // If the two denominators are equal, then only the numerators need to be subtracted
         if (den1 == den2) {
            numResult = num1 - num2;
            denResult = den1;
         // If the two denominators aren't equal, conversions of the numerators and denominators will
         // have to be done using multiplication
         } else {
            numResult = num1 * den2 - num2 * den1;
            denResult = den1 * den2;
         }
      // Fraction multiplication is done here
      // (whether the denominators are equal or not doesn't matter here because they'll be multiplied anyway)
      } else if (sign == '*') {
         numResult = num1 * num2;
         denResult = den1 * den2;
      // Fraction division is done here
      } else if (sign == '/') {
         if (den1 == 0 || den2 == 0) {
            return "invalid";
         } else {
         numResult = num1 * den2;
         denResult = den1 * num2;
         }
      }
      // Positive and negative conversions are done lastly

      // If both numerator and denominator are negative, they're turned positive 
      // If only the denominator is negative, the sign is swapped to the numerator for aesthetics
      // In both situations, the two values are turned into the opposite sign
      if ((numResult < 0 && denResult < 0) || (denResult < 0)) {
         numResult = -numResult;
         denResult = -denResult;
      }
      // Uses the final results of the numerator and denominator to make the final String result and returns it
      String result = numResult + "/" + denResult;
      return result;
   }

   // This method is to reduce the final result to a simplified version if needed, such as inproper or unsimplified fractions
   public static String reduce (int num, int den) {
      String result;

      // Below finds the greatest common factor of the numerator and the denominator to reduce the fraction later
      // 
      int dividend = Math.abs(num);
      int divisor = Math.abs(den);
      while (divisor != 0) {
         int remainder = dividend % divisor;
         dividend = divisor;
         divisor = remainder;
      }
      int gcf = dividend;

      // By dividng both values by the gcf, you will find the lowest possible fraction value
      // Ex: 12/16, gcf = 4, 12/4 = 3, 16/4 = 4, 12/16 = 3/4
      num /= gcf;
      den /= gcf;

      // If there's no remainder after actually dividing the numerator and denominator, the result is a whole number
      // Ex: 15 % 5 = 0, 15/5 = 3, result = 3
      if (num % den == 0) {
         result = "" + (num / den);
      // In integer division, a lower number divided by a higher number will equal 0, so if the numerator is already 
      // less than the denominator, then the result will be the normal fraction
      // Ex: 5/8 = 0, result = 5/8
      } else if (num / den == 0) {
         result = num + "/" + den;
      // If none of the following cases were true, then the result would be a mixed number, so the whole number is found
      // through integer division, then the numerator is the remainder of the integer division, and then den would stay the same
      // (The numerator is turned positive because the sign would only be displayed on the whole number)
      // Ex: 29/5, 29/5 = 5, 24 % 5 = 4, result = 5 4/5
      } else {
         result = (num / den) + " " + Math.abs(num % den) + "/" + den;
      }
      // Returns the final reduced result 
      return result;
   }
   // When called for, this method will return a string that should help the user
   // understand how to use the program. 
   public static String provideHelp() {
      String help = "Type in a simple mathematical equation into the calculator (+, -, *, /)";
      help += "\nYour input should be in the following format: [first number] [space] [operator sign] [space] [second number]";
      help += "\nFor fractions, use a slash (Ex: 1 + 1/2) \nFor mixed numbers, add an underscore between the whole number and the fraction (Ex: 1 + 1_2/3)";
      help += "\nMake sure your input is formatted correctly if there's issues; 0 isn't a valid denominator"; 
      help += "\ndon't write in more advanced calculations, this calculator is only for simple operations\n";
      return help;
   }
}