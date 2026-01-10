package src;

import java.util.*;

/**
 * Core Java – pre-OOP
 */
public class JavaCore {

    /* ===============================
       ENUM
       =============================== */
    enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    /* ===============================
       CONSTANT
       =============================== */
    public static final int MAX_USERS = 100;

    /* ===============================
       PRIMITIVES & WRAPPERS
       =============================== */
    byte myByte = 127;
    int myInt = 10;
    long myLong = 100000L;
    float myFloat = 10.99f;
    double myDouble = 9.78;
    boolean myBoolean = true;

    Integer myIntegerObject = 500;
    String myString = "Hello, Java!";

    int myInt2 = (int) myDouble;
    boolean isEqualOrGreater = (myInt >= myByte);
    boolean and = (myBoolean && isEqualOrGreater);
    String result = myInt < 0 ? "Negative" : "Non-Negative";

    /* ===============================
       ARRAYS
       =============================== */
    int[] myIntArray = {1, 2, 3, 4, 5};
    int[] myIntArray2 = {1, 2, 3};
    String[] myStringArray2 = {"Red", "Green", "Blue"};
    int[][] my2DArray = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9}
    };

    /* ===============================
       COLLECTIONS
       =============================== */
    List<String> myStringList = new ArrayList<>();
    ArrayList<Integer> myIntegerList = new ArrayList<>();
    Map<String, Integer> myMap = new HashMap<>();
    Set<String> mySet = new HashSet<>();

    /* ===============================
       SWITCH EXPRESSION
       =============================== */
    int day = 3;

    String dayName = switch (day) {
        case 1 -> "Monday";
        case 2 -> "Tuesday";
        case 3 -> "Wednesday";
        case 4 -> "Thursday";
        case 5 -> "Friday";
        case 6 -> "Saturday";
        case 7 -> "Sunday";
        default -> "Invalid day";
    };

    /* ===============================
       ENTRY METHOD
       =============================== */
    public void executeValues() {
        demonstrateArrays();
        demonstrateCollections();
        demonstrateConditionals();
        demonstrateLoops();
        demonstrateEquality();
        demonstrateExceptions();
        demonstratePassByValue();
    }

    /* ===============================
       ARRAYS
       =============================== */
    private void demonstrateArrays() {
        myIntArray2[0] = 10;
        System.out.println("Array first element: " + myIntArray2[0]);
    }

    /* ===============================
       COLLECTIONS
       =============================== */
    private void demonstrateCollections() {

        // List
        myStringList.add("Apple");
        myStringList.add("Banana");
        myStringList.remove("Banana");

        // ArrayList
        myIntegerList.add(100);
        myIntegerList.add(200);
        myIntegerList.remove(Integer.valueOf(100));
        System.out.println("ArrayList first element: " + myIntegerList.get(0));

        // Map
        myMap.put("One", 1);
        myMap.put("Two", 2);
        myMap.remove("Two");

        for (Map.Entry<String, Integer> entry : myMap.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        // Set
        mySet.add("A");
        mySet.add("B");
        mySet.remove("B");

        for (String s : mySet) {
            System.out.println("Set element: " + s);
        }
    }

    /* ===============================
       IF / ELSE + SWITCH
       =============================== */
    private void demonstrateConditionals() {

        if (myInt > myByte) {
            System.out.println("myInt is greater than myByte");
        } else {
            System.out.println("myInt is not greater than myByte");
        }

        System.out.println("Day name from switch: " + dayName);
    }

    /* ===============================
       LOOPS
       =============================== */
    private void demonstrateLoops() {

        // for-each
        for (int n : myIntArray) {
            if (n == 3) continue;
            System.out.println("for-each value: " + n);
        }

        // while
        int i = 0;
        while (i < 3) {
            System.out.println("while i = " + i);
            i++;
        }

        // do-while
        int j = 0;
        do {
            System.out.println("do-while j = " + j);
            j++;
        } while (j < 3);
    }

    /* ===============================
       == VS equals()
       =============================== */
    private void demonstrateEquality() {
        Integer x = 200;
        Integer y = 200;

        System.out.println("x == y: " + (x == y));
        System.out.println("x.equals(y): " + x.equals(y));
    }

    /* ===============================
       EXCEPTIONS
       =============================== */
    private void demonstrateExceptions() {
        try {
            int error = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("Division by zero handled");
        }
    }

    /* ===============================
       PASS-BY-VALUE
       =============================== */
    private void demonstratePassByValue() {
        int x = 5;
        changeValue(x);
        System.out.println("Value of x after changeValue: " + x);
    }

    private void changeValue(int newValue) {
        newValue = 10;
        myString = "Java is fun! " + myInt;
    }
}
