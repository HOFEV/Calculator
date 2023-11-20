import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Main
 */
public class Main {
   public static void main(String[] args) throws Exception {
         System.out.println("Input");
         Calculator.inputValue();
         System.out.println();
         Calculator.astimate();
         System.out.println("Output:");
         System.out.println(Calculator.output()); 

   }
}

/**
 *Calculator
 */
final class Calculator   {
   private static String firstValue;
   private static String secondValue;
   private static String sign;
   private static StringBuilder textResult = new StringBuilder();

   private Calculator() {}
 
   public static void inputValue() throws Exception{

      String[] text;
      
      try (Scanner scanner = new Scanner(System.in)) {
         text = (scanner.nextLine()).split(" ");
         firstValue = text[0];
         sign = text[1];
         secondValue = text[2];

         try {
            String nextString = text[3];
         } catch (Exception ex) {
            return;
         } 

      } catch (Exception ex) {
         //ex.printStackTrace(); // -_-
         //System.out.println("Error of input"); 
      } 

      throw new Exception(); // По требованию задания
   }

   public static void astimate() throws Exception {
      int result;
      int rimFirstValue;
      int rimSecondValue;

      if (RimNumeric.isRimNumber(firstValue) && RimNumeric.isRimNumber(secondValue)) {

         rimFirstValue = RimNumeric.convertRimtoArabicNumber(firstValue);
         rimSecondValue = RimNumeric.convertRimtoArabicNumber(secondValue);

         if (checkIntCount(rimFirstValue) && checkIntCount(rimSecondValue)) {
            result = operation(rimFirstValue, rimSecondValue);

            if (result < 1) {
               throw new Exception();
            } else {
               textResult.append(RimNumeric.convertArabictoRimNumber(result));
            }
         } else {
            throw new Exception();
         }
         
      } else if (checkIntCount(Integer.parseInt(firstValue)) && checkIntCount(Integer.parseInt(secondValue))) {
         textResult.append(String.valueOf(operation(Integer.parseInt(firstValue), Integer.parseInt(secondValue))));
      } else {
         throw new Exception(); // В случае если числа не будут совпадать по типу
      }
   }

   public static StringBuilder output() {
      return textResult;
   }

   private static int operation(int firstNumber, int secondNumber) throws Exception {
      int result = 0;
      switch (sign) {
         case "+":
            result = firstNumber + secondNumber;
            break;
         case "-":
            result = firstNumber - secondNumber;
            break;
         case "/":
            result = firstNumber / secondNumber;
            break;
         case "*":
            result = firstNumber * secondNumber;
            break;
         default:
            throw new Exception(); // В случае если не будет такого знака
         }

      return result;
   }

   private static boolean checkIntCount(int count) {
      return (count <= 10 && count >= 0);
   }

}





final class RimNumeric {
   private static Map<Character, Integer> hashMap = new HashMap<>();
   private static Map<Character, Integer> secondHashMap = new HashMap<>();
   private static Map<Integer, String> reversListOfNumber = new HashMap<>();

   private RimNumeric() {}

   static {
      hashMap.put('I', 1); 
      secondHashMap.put('V', 5); 
      hashMap.put('X', 10); 
      secondHashMap.put('L', 50); 
      hashMap.put('C', 100); 
      secondHashMap.put('D', 500); 
      hashMap.put('M', 1000); 
   
      reversListOfNumber.put(1000, "M");
      reversListOfNumber.put(500, "D");
      reversListOfNumber.put(100, "C");
      reversListOfNumber.put(50, "L");
      reversListOfNumber.put(10, "X");
      reversListOfNumber.put(5, "V");
      reversListOfNumber.put(1, "I"); 
   } 

   private static Integer getNumber(char c) {
      return hashMap.get(c) != null ? hashMap.get(c) : secondHashMap.get(c); 
   }

   public static boolean isRimNumber(String text) {
      char[] symbol = text.toCharArray(); // Разбиваем строку на массив символов
      Integer number = 0;
      int lastNumber = 0;
      int count = 0;
      boolean isSecond = false;

      // Проверяем каждый элемент входного параметра
      for (char c : symbol) {
         number = getNumber(c);
         isSecond = hashMap.containsValue(number) ? false : true;

         // Проверяем, что все символы являются римскими
         if (number == null) return false; 

         // Проверяем правильную последовательность
         if (lastNumber != 0 && lastNumber < number) {
            if (isSecond) {
               if (lastNumber != (number / 5)) return false;
            } else {
               if (lastNumber != (number / 10)) return false;
            }
         }

         // Проверяем есть ли больше 3 подряд повторяющихся римских чисел
         if (lastNumber != number) {
            lastNumber = number;
            count = 0;
         } else if (count > 1) {
            return false; // если встретим 3 подряд одинаковых римских числа 
         } else {
            count++;
         }

         isSecond = false;
      }

      return true;
   }

   public static int convertRimtoArabicNumber(String text) {
      char[] symbol = text.toCharArray();
      int number = 0;
      int lastNumber = 0;
      int sum = 0;

      for (char c : symbol) {
         number = getNumber(c);

         if (number > lastNumber) {
            if (sum == 0) {
               sum = number - lastNumber;
            } else {
               sum += number - 2*lastNumber;
            }
         } else {
            sum += number;
         }
         lastNumber = number;
      }
      return sum;
   }  

   
   public static StringBuilder convertArabictoRimNumber(int number) {
      StringBuilder finalCount = new StringBuilder();
      int[] listOfNumbers = new int[4]; // больше 4-х значного числа получить не получится

      //Разбиваем входной параметр на массив чисел
      for (int i = 0; i < listOfNumbers.length; i++) {
         
         listOfNumbers[i] = number % 10;
         number = number / 10;
      }

      for (int i = listOfNumbers.length - 1; i >= 0; i--) {
         if (listOfNumbers[i] != 0) {
            finalCount.append(helper(listOfNumbers[i], i));
         }
      }

      return finalCount;
   }
   
   private static StringBuilder helper(int count, int pos) {
      StringBuilder text = new StringBuilder();
      int multiplier = 1;

      for (int i = 0; i < pos; i++) {
         multiplier = multiplier*10;
      }

      switch (count) {
         case 1:
         case 2:
         case 3:
            for (int i = 0; i < count; i++) {
               text.append(reversListOfNumber.get(multiplier));  
            }
            break;
         case 4:
            text.append(reversListOfNumber.get(multiplier));
         case 5:
            text.append(reversListOfNumber.get(multiplier*5));
            break;
         case 6:
            text.append(reversListOfNumber.get(multiplier*5));
            text.append(reversListOfNumber.get(multiplier));
            break;
         case 7:
            text.append(reversListOfNumber.get(multiplier*5));
            text.append(reversListOfNumber.get(multiplier)).append(reversListOfNumber.get(multiplier));
            break;
         case 8:
            text.append(reversListOfNumber.get(multiplier*5));
            System.out.println(multiplier*5);
            text.append(reversListOfNumber.get(multiplier)).append(reversListOfNumber.get(multiplier)).append(reversListOfNumber.get(multiplier));
            break;
         case 9:
            text.append(reversListOfNumber.get(multiplier));
            text.append(reversListOfNumber.get(multiplier*10));
      }

      return text;
   }
}