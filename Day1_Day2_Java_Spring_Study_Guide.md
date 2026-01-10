# IMPACT.COM INTERVIEW PREPARATION
# DAY 1 & DAY 2 - JAVA & SPRING BOOT COMPLETE GUIDE

---

# DAY 1 - FRIDAY JANUARY 9, 2026
## JAVA FUNDAMENTALS + JAVA 8 FEATURES

---

# BLOCK 1: JAVA CORE (2 hours)

## 1.1 Variables and Data Types

### Primitive Types
```java
// Integer types
byte myByte = 127;           // 8 bits, -128 to 127
short myShort = 32000;       // 16 bits
int myInt = 2147483647;      // 32 bits (most common)
long myLong = 9223372036854775807L; // 64 bits (note the L)

// Floating point
float myFloat = 3.14f;       // 32 bits (note the f)
double myDouble = 3.14159;   // 64 bits (most common for decimals)

// Other primitives
boolean myBool = true;       // true or false
char myChar = 'A';           // single character
```

### Reference Types (Objects)
```java
String myString = "Hello World";  // String is an object, not primitive
Integer myInteger = 100;          // Wrapper class for int
int[] myArray = {1, 2, 3, 4, 5}; // Array is a reference type
```

### Type Casting
```java
// Implicit casting (widening) - automatic
int myInt = 100;
double myDouble = myInt;  // int to double, no data loss

// Explicit casting (narrowing) - manual
double myDouble = 9.78;
int myInt = (int) myDouble;  // double to int, loses decimal
```

---

## 1.2 Operators

### Arithmetic Operators
```java
int a = 10, b = 3;

int sum = a + b;        // 13
int diff = a - b;       // 7
int product = a * b;    // 30
int quotient = a / b;   // 3 (integer division)
int remainder = a % b;  // 1 (modulo)

// Increment/Decrement
int x = 5;
x++;  // x is now 6
x--;  // x is now 5
++x;  // x is now 6 (prefix)
--x;  // x is now 5 (prefix)
```

### Comparison Operators
```java
int a = 10, b = 5;

boolean isEqual = (a == b);      // false
boolean isNotEqual = (a != b);   // true
boolean isGreater = (a > b);     // true
boolean isLess = (a < b);        // false
boolean isGreaterOrEqual = (a >= b); // true
boolean isLessOrEqual = (a <= b);    // false
```

### Logical Operators
```java
boolean x = true, y = false;

boolean and = x && y;   // false (both must be true)
boolean or = x || y;    // true (at least one true)
boolean not = !x;       // false (inverts)
```

---

## 1.3 Control Flow

### If-Else Statements
```java
int score = 85;

if (score >= 90) {
    System.out.println("Grade: A");
} else if (score >= 80) {
    System.out.println("Grade: B");
} else if (score >= 70) {
    System.out.println("Grade: C");
} else {
    System.out.println("Grade: F");
}

// Ternary operator (shorthand if-else)
String result = (score >= 60) ? "Pass" : "Fail";
```

### Switch Statement
```java
int day = 3;

switch (day) {
    case 1:
        System.out.println("Monday");
        break;
    case 2:
        System.out.println("Tuesday");
        break;
    case 3:
        System.out.println("Wednesday");
        break;
    default:
        System.out.println("Other day");
}

// Java 14+ Enhanced Switch
String dayName = switch (day) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    case 3 -> "Wednesday";
    default -> "Other day";
};
```

### Loops
```java
// For loop
for (int i = 0; i < 5; i++) {
    System.out.println("Iteration: " + i);
}

// Enhanced for loop (for-each)
int[] numbers = {1, 2, 3, 4, 5};
for (int num : numbers) {
    System.out.println(num);
}

// While loop
int count = 0;
while (count < 5) {
    System.out.println("Count: " + count);
    count++;
}

// Do-While loop (executes at least once)
int x = 0;
do {
    System.out.println("x = " + x);
    x++;
} while (x < 5);

// Break and Continue
for (int i = 0; i < 10; i++) {
    if (i == 3) continue;  // skip 3
    if (i == 7) break;     // stop at 7
    System.out.println(i); // prints 0,1,2,4,5,6
}
```

---

## 1.4 Arrays and Collections

### Arrays
```java
// Declaration and initialization
int[] numbers = new int[5];           // array of 5 integers (default 0)
int[] nums = {1, 2, 3, 4, 5};         // array with values
String[] names = {"Alice", "Bob"};    // string array

// Accessing elements
int first = nums[0];      // 1 (zero-indexed)
int last = nums[nums.length - 1];  // 5

// Iterating
for (int i = 0; i < nums.length; i++) {
    System.out.println(nums[i]);
}

// 2D Arrays
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};
int value = matrix[1][2];  // 6 (row 1, column 2)
```

### ArrayList (Dynamic Array)
```java
import java.util.ArrayList;
import java.util.List;

// Creating ArrayList
List<String> names = new ArrayList<>();

// Adding elements
names.add("Alice");
names.add("Bob");
names.add("Charlie");
names.add(1, "David");  // insert at index 1

// Accessing elements
String first = names.get(0);  // Alice
int size = names.size();       // 4

// Removing elements
names.remove("Bob");          // remove by value
names.remove(0);              // remove by index

// Checking
boolean hasAlice = names.contains("Alice");
int index = names.indexOf("Charlie");

// Iterating
for (String name : names) {
    System.out.println(name);
}

// Clear all
names.clear();
```

### HashMap (Key-Value Pairs)
```java
import java.util.HashMap;
import java.util.Map;

// Creating HashMap
Map<String, Integer> ages = new HashMap<>();

// Adding entries
ages.put("Alice", 25);
ages.put("Bob", 30);
ages.put("Charlie", 35);

// Accessing values
int aliceAge = ages.get("Alice");  // 25
int unknownAge = ages.getOrDefault("Unknown", 0);  // 0

// Checking
boolean hasAlice = ages.containsKey("Alice");  // true
boolean has25 = ages.containsValue(25);        // true

// Removing
ages.remove("Bob");

// Iterating
for (Map.Entry<String, Integer> entry : ages.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}

// Or iterate keys/values separately
for (String name : ages.keySet()) {
    System.out.println(name);
}
for (Integer age : ages.values()) {
    System.out.println(age);
}
```

### HashSet (Unique Values)
```java
import java.util.HashSet;
import java.util.Set;

// Creating HashSet
Set<String> uniqueNames = new HashSet<>();

// Adding elements (duplicates ignored)
uniqueNames.add("Alice");
uniqueNames.add("Bob");
uniqueNames.add("Alice");  // ignored, already exists

// Size
int size = uniqueNames.size();  // 2

// Checking
boolean hasAlice = uniqueNames.contains("Alice");  // true

// Removing
uniqueNames.remove("Bob");

// Iterating (no guaranteed order)
for (String name : uniqueNames) {
    System.out.println(name);
}
```

---

## 1.5 Object-Oriented Programming (OOP)

### Classes and Objects
```java
// Class definition
public class Car {
    // Fields (instance variables)
    private String brand;
    private String model;
    private int year;
    
    // Constructor
    public Car(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }
    
    // Default constructor
    public Car() {
        this.brand = "Unknown";
        this.model = "Unknown";
        this.year = 2000;
    }
    
    // Methods
    public void start() {
        System.out.println(brand + " " + model + " is starting...");
    }
    
    public void drive(int miles) {
        System.out.println("Driving " + miles + " miles");
    }
    
    // Getters and Setters
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    // toString method
    @Override
    public String toString() {
        return year + " " + brand + " " + model;
    }
}

// Using the class
public class Main {
    public static void main(String[] args) {
        Car myCar = new Car("Toyota", "Camry", 2022);
        myCar.start();
        myCar.drive(50);
        System.out.println(myCar);  // uses toString()
    }
}
```

### Inheritance
```java
// Parent class (superclass)
public class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    public void eat() {
        System.out.println(name + " is eating");
    }
    
    public void sleep() {
        System.out.println(name + " is sleeping");
    }
}

// Child class (subclass)
public class Dog extends Animal {
    private String breed;
    
    public Dog(String name, String breed) {
        super(name);  // call parent constructor
        this.breed = breed;
    }
    
    // New method specific to Dog
    public void bark() {
        System.out.println(name + " says: Woof!");
    }
    
    // Override parent method
    @Override
    public void eat() {
        System.out.println(name + " is eating dog food");
    }
}

// Usage
Dog myDog = new Dog("Buddy", "Labrador");
myDog.eat();    // "Buddy is eating dog food" (overridden)
myDog.sleep();  // "Buddy is sleeping" (inherited)
myDog.bark();   // "Buddy says: Woof!"
```

### Polymorphism
```java
// Parent reference can hold child object
Animal myAnimal = new Dog("Buddy", "Labrador");
myAnimal.eat();   // calls Dog's eat() method (runtime polymorphism)
// myAnimal.bark();  // ERROR - Animal doesn't have bark()

// Casting
if (myAnimal instanceof Dog) {
    Dog myDog = (Dog) myAnimal;
    myDog.bark();  // Now we can call bark()
}

// Method overloading (compile-time polymorphism)
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
    
    public double add(double a, double b) {
        return a + b;
    }
    
    public int add(int a, int b, int c) {
        return a + b + c;
    }
}
```

### Interfaces
```java
// Interface definition
public interface Driveable {
    void start();
    void stop();
    void accelerate(int speed);
    
    // Default method (Java 8+)
    default void honk() {
        System.out.println("Beep beep!");
    }
}

// Another interface
public interface Electric {
    void charge();
    int getBatteryLevel();
}

// Class implementing multiple interfaces
public class Tesla implements Driveable, Electric {
    private int batteryLevel = 100;
    
    @Override
    public void start() {
        System.out.println("Tesla starting silently...");
    }
    
    @Override
    public void stop() {
        System.out.println("Tesla stopped");
    }
    
    @Override
    public void accelerate(int speed) {
        System.out.println("Accelerating to " + speed + " mph");
    }
    
    @Override
    public void charge() {
        batteryLevel = 100;
        System.out.println("Charging complete");
    }
    
    @Override
    public int getBatteryLevel() {
        return batteryLevel;
    }
}
```

### Abstract Classes
```java
// Abstract class - cannot be instantiated
public abstract class Shape {
    protected String color;
    
    public Shape(String color) {
        this.color = color;
    }
    
    // Abstract method - must be implemented by subclasses
    public abstract double getArea();
    
    // Concrete method - inherited as-is
    public void displayColor() {
        System.out.println("Color: " + color);
    }
}

// Concrete subclass
public class Circle extends Shape {
    private double radius;
    
    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}

// Another concrete subclass
public class Rectangle extends Shape {
    private double width;
    private double height;
    
    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double getArea() {
        return width * height;
    }
}
```

### Interface vs Abstract Class
```
INTERFACE:
- Can only have abstract methods (until Java 8)
- Java 8+: can have default and static methods
- A class can implement MULTIPLE interfaces
- All fields are public static final
- Use when: defining a contract/capability

ABSTRACT CLASS:
- Can have both abstract and concrete methods
- A class can extend only ONE abstract class
- Can have instance variables with any access modifier
- Use when: sharing code among related classes
```

---

## 1.6 Exception Handling

### Try-Catch-Finally
```java
public void readFile(String filename) {
    try {
        // Code that might throw an exception
        FileReader file = new FileReader(filename);
        BufferedReader reader = new BufferedReader(file);
        String line = reader.readLine();
        System.out.println(line);
        reader.close();
    } catch (FileNotFoundException e) {
        // Handle specific exception
        System.out.println("File not found: " + filename);
    } catch (IOException e) {
        // Handle another exception
        System.out.println("Error reading file: " + e.getMessage());
    } finally {
        // Always executes (cleanup code)
        System.out.println("Operation complete");
    }
}
```

### Try-With-Resources (Java 7+)
```java
// Automatically closes resources
public void readFile(String filename) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line = reader.readLine();
        System.out.println(line);
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    }
    // reader is automatically closed here
}
```

### Throwing Exceptions
```java
public void setAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative");
    }
    this.age = age;
}

// Method that declares it might throw an exception
public void riskyMethod() throws IOException {
    // ... code that might throw IOException
}
```

### Custom Exceptions
```java
// Custom exception class
public class InsufficientFundsException extends Exception {
    private double amount;
    
    public InsufficientFundsException(double amount) {
        super("Insufficient funds. Short by: $" + amount);
        this.amount = amount;
    }
    
    public double getAmount() {
        return amount;
    }
}

// Using custom exception
public void withdraw(double amount) throws InsufficientFundsException {
    if (amount > balance) {
        throw new InsufficientFundsException(amount - balance);
    }
    balance -= amount;
}
```

---

## ✅ BLOCK 1 CHECKLIST
- [ ] I understand primitive vs reference types
- [ ] I can use all comparison and logical operators
- [ ] I can write if-else, switch, and all loop types
- [ ] I can work with Arrays, ArrayList, HashMap, HashSet
- [ ] I understand Classes, Objects, Constructors
- [ ] I understand Inheritance and can use extends
- [ ] I understand Polymorphism (overriding and overloading)
- [ ] I know the difference between Interface and Abstract Class
- [ ] I can handle exceptions with try-catch-finally

---

# BLOCK 2: JAVA 8+ FEATURES (2 hours)

## 2.1 Lambda Expressions

### What is a Lambda?
A lambda is a short way to write an anonymous function (a function without a name).

### Syntax
```java
// Traditional anonymous class
Runnable runnable1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello World");
    }
};

// Lambda expression (same thing, shorter)
Runnable runnable2 = () -> System.out.println("Hello World");

// Lambda syntax:
// (parameters) -> expression
// (parameters) -> { statements; }
```

### Lambda Examples
```java
// No parameters
Runnable r = () -> System.out.println("Hello");

// One parameter (parentheses optional)
Consumer<String> printer = s -> System.out.println(s);
Consumer<String> printer2 = (s) -> System.out.println(s);

// Two parameters
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;

// Multiple statements (need braces and return)
BiFunction<Integer, Integer, Integer> calculate = (a, b) -> {
    int sum = a + b;
    int doubled = sum * 2;
    return doubled;
};

// With explicit types
BiFunction<Integer, Integer, Integer> multiply = (Integer a, Integer b) -> a * b;
```

### Common Use Cases
```java
import java.util.*;

List<String> names = Arrays.asList("Charlie", "Alice", "Bob");

// Sorting with lambda
names.sort((a, b) -> a.compareTo(b));
// Or using method reference
names.sort(String::compareTo);

// forEach with lambda
names.forEach(name -> System.out.println(name));
// Or method reference
names.forEach(System.out::println);

// Filtering with lambda (in streams)
names.stream()
     .filter(name -> name.startsWith("A"))
     .forEach(System.out::println);
```

---

## 2.2 Functional Interfaces

A functional interface has exactly ONE abstract method. Lambdas work with functional interfaces.

### Built-in Functional Interfaces
```java
import java.util.function.*;

// Predicate<T> - takes T, returns boolean
Predicate<String> isEmpty = s -> s.isEmpty();
Predicate<Integer> isPositive = n -> n > 0;
boolean result = isEmpty.test("");  // true

// Function<T, R> - takes T, returns R
Function<String, Integer> length = s -> s.length();
Function<Integer, String> intToString = n -> "Number: " + n;
int len = length.apply("Hello");  // 5

// Consumer<T> - takes T, returns nothing (void)
Consumer<String> printer = s -> System.out.println(s);
printer.accept("Hello");  // prints "Hello"

// Supplier<T> - takes nothing, returns T
Supplier<Double> random = () -> Math.random();
Supplier<String> greeting = () -> "Hello World";
double value = random.get();  // random number

// BiFunction<T, U, R> - takes T and U, returns R
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
int sum = add.apply(5, 3);  // 8

// BiPredicate<T, U> - takes T and U, returns boolean
BiPredicate<String, Integer> checkLength = (s, len) -> s.length() > len;
boolean longer = checkLength.test("Hello", 3);  // true

// UnaryOperator<T> - takes T, returns T (same type)
UnaryOperator<Integer> square = n -> n * n;
int squared = square.apply(5);  // 25

// BinaryOperator<T> - takes two T, returns T
BinaryOperator<Integer> multiply = (a, b) -> a * b;
int product = multiply.apply(4, 5);  // 20
```

### Creating Custom Functional Interface
```java
@FunctionalInterface
public interface Calculator {
    int calculate(int a, int b);
    
    // Can have default methods
    default void printResult(int a, int b) {
        System.out.println("Result: " + calculate(a, b));
    }
}

// Using it
Calculator add = (a, b) -> a + b;
Calculator subtract = (a, b) -> a - b;
Calculator multiply = (a, b) -> a * b;

add.printResult(5, 3);       // Result: 8
subtract.printResult(5, 3);  // Result: 2
```

---

## 2.3 Stream API

Streams allow you to process collections in a functional, declarative way.

### Creating Streams
```java
import java.util.stream.*;
import java.util.*;

// From Collection
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> stream1 = list.stream();

// From Array
String[] array = {"a", "b", "c"};
Stream<String> stream2 = Arrays.stream(array);

// Using Stream.of()
Stream<String> stream3 = Stream.of("a", "b", "c");

// Infinite streams
Stream<Integer> infinite = Stream.iterate(0, n -> n + 1);  // 0, 1, 2, 3...
Stream<Double> randoms = Stream.generate(Math::random);

// Range of numbers
IntStream range = IntStream.range(1, 5);      // 1, 2, 3, 4
IntStream rangeClosed = IntStream.rangeClosed(1, 5);  // 1, 2, 3, 4, 5
```

### Intermediate Operations (return a Stream)
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Anna");

// filter - keep elements matching condition
names.stream()
     .filter(name -> name.startsWith("A"))  // Alice, Anna
     
// map - transform each element
names.stream()
     .map(String::toUpperCase)  // ALICE, BOB, CHARLIE...
     
// map to different type
names.stream()
     .map(String::length)  // 5, 3, 7, 5, 4

// flatMap - flatten nested structures
List<List<Integer>> nested = Arrays.asList(
    Arrays.asList(1, 2),
    Arrays.asList(3, 4),
    Arrays.asList(5, 6)
);
nested.stream()
      .flatMap(List::stream)  // 1, 2, 3, 4, 5, 6

// sorted - sort elements
names.stream()
     .sorted()  // natural order: Alice, Anna, Bob, Charlie, David
     
names.stream()
     .sorted(Comparator.reverseOrder())  // reverse: David, Charlie, Bob...
     
names.stream()
     .sorted(Comparator.comparingInt(String::length))  // by length: Bob, Anna...

// distinct - remove duplicates
Stream.of(1, 2, 2, 3, 3, 3)
      .distinct()  // 1, 2, 3

// limit - take first n elements
names.stream()
     .limit(3)  // Alice, Bob, Charlie

// skip - skip first n elements
names.stream()
     .skip(2)  // Charlie, David, Anna

// peek - perform action without changing stream (for debugging)
names.stream()
     .peek(System.out::println)
     .map(String::toUpperCase)
     .collect(Collectors.toList());
```

### Terminal Operations (produce result, end the stream)
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// forEach - perform action on each element
names.stream().forEach(System.out::println);

// collect - gather into collection
List<String> filtered = names.stream()
    .filter(n -> n.length() > 3)
    .collect(Collectors.toList());

Set<String> nameSet = names.stream()
    .collect(Collectors.toSet());

String joined = names.stream()
    .collect(Collectors.joining(", "));  // "Alice, Bob, Charlie, David"

// toArray - convert to array
String[] array = names.stream().toArray(String[]::new);

// count - count elements
long count = names.stream()
    .filter(n -> n.startsWith("A"))
    .count();  // 1

// reduce - combine all elements into one
int sum = numbers.stream()
    .reduce(0, (a, b) -> a + b);  // 15
    
int sum2 = numbers.stream()
    .reduce(0, Integer::sum);  // same thing

Optional<Integer> max = numbers.stream()
    .reduce(Integer::max);  // Optional[5]

// min, max
Optional<String> shortest = names.stream()
    .min(Comparator.comparingInt(String::length));  // Optional[Bob]

Optional<String> longest = names.stream()
    .max(Comparator.comparingInt(String::length));  // Optional[Charlie]

// findFirst, findAny
Optional<String> first = names.stream()
    .filter(n -> n.startsWith("C"))
    .findFirst();  // Optional[Charlie]

// anyMatch, allMatch, noneMatch
boolean anyStartsWithA = names.stream().anyMatch(n -> n.startsWith("A"));  // true
boolean allLong = names.stream().allMatch(n -> n.length() > 2);  // true
boolean noneEmpty = names.stream().noneMatch(String::isEmpty);  // true
```

### Collectors Utility Class
```java
import java.util.stream.Collectors;

List<Person> people = Arrays.asList(
    new Person("Alice", 25, "Engineering"),
    new Person("Bob", 30, "Marketing"),
    new Person("Charlie", 25, "Engineering"),
    new Person("David", 35, "Marketing")
);

// groupingBy - group by property
Map<String, List<Person>> byDepartment = people.stream()
    .collect(Collectors.groupingBy(Person::getDepartment));
// {Engineering=[Alice, Charlie], Marketing=[Bob, David]}

// groupingBy with counting
Map<String, Long> countByDept = people.stream()
    .collect(Collectors.groupingBy(
        Person::getDepartment,
        Collectors.counting()
    ));
// {Engineering=2, Marketing=2}

// partitioningBy - split into two groups (true/false)
Map<Boolean, List<Person>> partitioned = people.stream()
    .collect(Collectors.partitioningBy(p -> p.getAge() > 27));
// {false=[Alice, Charlie], true=[Bob, David]}

// mapping
Map<String, List<String>> namesByDept = people.stream()
    .collect(Collectors.groupingBy(
        Person::getDepartment,
        Collectors.mapping(Person::getName, Collectors.toList())
    ));
// {Engineering=[Alice, Charlie], Marketing=[Bob, David]}

// summarizing
IntSummaryStatistics stats = people.stream()
    .collect(Collectors.summarizingInt(Person::getAge));
// count=4, sum=115, min=25, average=28.75, max=35

// averaging
double avgAge = people.stream()
    .collect(Collectors.averagingInt(Person::getAge));  // 28.75

// toMap
Map<String, Integer> nameToAge = people.stream()
    .collect(Collectors.toMap(
        Person::getName,
        Person::getAge
    ));
// {Alice=25, Bob=30, Charlie=25, David=35}
```

### Complete Stream Examples
```java
// Example 1: Filter, transform, collect
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Anna");

List<String> result = names.stream()
    .filter(name -> name.length() > 3)      // Alice, Charlie, David, Anna
    .map(String::toUpperCase)                // ALICE, CHARLIE, DAVID, ANNA
    .sorted()                                // ALICE, ANNA, CHARLIE, DAVID
    .collect(Collectors.toList());

// Example 2: Find average age of adults
List<Person> people = getPeople();

double avgAge = people.stream()
    .filter(p -> p.getAge() >= 18)
    .mapToInt(Person::getAge)
    .average()
    .orElse(0.0);

// Example 3: Get names of top 3 highest paid employees
List<String> topEarners = employees.stream()
    .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
    .limit(3)
    .map(Employee::getName)
    .collect(Collectors.toList());

// Example 4: Sum of all order totals for a customer
double totalSpent = customer.getOrders().stream()
    .flatMap(order -> order.getItems().stream())
    .mapToDouble(item -> item.getPrice() * item.getQuantity())
    .sum();
```

---

## 2.4 Optional Class

Optional is a container that may or may not contain a value. It helps avoid NullPointerException.

### Creating Optional
```java
// Empty Optional
Optional<String> empty = Optional.empty();

// Optional with value
Optional<String> name = Optional.of("Alice");

// Optional that might be null
String nullableName = getName();  // might return null
Optional<String> maybeName = Optional.ofNullable(nullableName);
```

### Using Optional
```java
Optional<String> name = Optional.of("Alice");

// isPresent / isEmpty
if (name.isPresent()) {
    System.out.println(name.get());
}

if (name.isEmpty()) {  // Java 11+
    System.out.println("No name");
}

// ifPresent - execute if value exists
name.ifPresent(n -> System.out.println("Hello, " + n));

// ifPresentOrElse - Java 9+
name.ifPresentOrElse(
    n -> System.out.println("Hello, " + n),
    () -> System.out.println("No name provided")
);

// orElse - default value
String result = name.orElse("Unknown");

// orElseGet - lazy default (computed only if needed)
String result2 = name.orElseGet(() -> computeDefault());

// orElseThrow - throw exception if empty
String result3 = name.orElseThrow(() -> new RuntimeException("Name required"));

// map - transform value if present
Optional<Integer> length = name.map(String::length);  // Optional[5]

// flatMap - when transformation returns Optional
Optional<String> upperName = name.flatMap(n -> 
    Optional.of(n.toUpperCase())
);

// filter - keep value only if matches predicate
Optional<String> longName = name.filter(n -> n.length() > 3);  // Optional[Alice]
Optional<String> shortName = name.filter(n -> n.length() > 10);  // Optional.empty
```

### Optional Best Practices
```java
// GOOD: Return Optional from methods that might not find a result
public Optional<User> findUserById(Long id) {
    User user = database.find(id);
    return Optional.ofNullable(user);
}

// GOOD: Use orElse for default values
String name = findUserById(1L)
    .map(User::getName)
    .orElse("Guest");

// BAD: Don't use Optional for fields
public class User {
    private Optional<String> middleName;  // DON'T DO THIS
}

// GOOD: Use nullable fields instead
public class User {
    private String middleName;  // Can be null
    
    public Optional<String> getMiddleName() {
        return Optional.ofNullable(middleName);
    }
}

// BAD: Don't use Optional in parameters
public void process(Optional<String> name) { }  // DON'T DO THIS

// GOOD: Use nullable parameters or overloaded methods
public void process(String name) { }
public void process() { process(null); }
```

---

## 2.5 Method References

Method references are shorthand for lambdas that just call a method.

### Types of Method References
```java
// 1. Reference to static method
// ClassName::staticMethodName

// Lambda
Function<String, Integer> parser1 = s -> Integer.parseInt(s);
// Method reference
Function<String, Integer> parser2 = Integer::parseInt;


// 2. Reference to instance method of a particular object
// object::instanceMethodName

String prefix = "Hello, ";
// Lambda
Function<String, String> greeter1 = s -> prefix.concat(s);
// Method reference
Function<String, String> greeter2 = prefix::concat;


// 3. Reference to instance method of an arbitrary object
// ClassName::instanceMethodName

// Lambda
Function<String, Integer> length1 = s -> s.length();
// Method reference
Function<String, Integer> length2 = String::length;

// Lambda
BiPredicate<String, String> equals1 = (s1, s2) -> s1.equals(s2);
// Method reference
BiPredicate<String, String> equals2 = String::equals;


// 4. Reference to constructor
// ClassName::new

// Lambda
Supplier<ArrayList<String>> listMaker1 = () -> new ArrayList<>();
// Method reference
Supplier<ArrayList<String>> listMaker2 = ArrayList::new;

// Lambda
Function<String, Person> personMaker1 = name -> new Person(name);
// Method reference
Function<String, Person> personMaker2 = Person::new;
```

### Method Reference Examples
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// Print each name
names.forEach(System.out::println);

// Convert to uppercase
List<String> upper = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// Sort by length
names.sort(Comparator.comparingInt(String::length));

// Create new objects
List<Person> people = names.stream()
    .map(Person::new)
    .collect(Collectors.toList());

// Check conditions
boolean anyEmpty = names.stream().anyMatch(String::isEmpty);
```

---

## 2.6 New Date/Time API (java.time)

### LocalDate, LocalTime, LocalDateTime
```java
import java.time.*;
import java.time.format.DateTimeFormatter;

// Current date/time
LocalDate today = LocalDate.now();           // 2026-01-09
LocalTime now = LocalTime.now();             // 14:30:45.123
LocalDateTime dateTime = LocalDateTime.now(); // 2026-01-09T14:30:45.123

// Specific date/time
LocalDate date = LocalDate.of(2026, 1, 9);
LocalDate date2 = LocalDate.of(2026, Month.JANUARY, 9);
LocalTime time = LocalTime.of(14, 30, 45);
LocalDateTime dt = LocalDateTime.of(2026, 1, 9, 14, 30);

// Parsing strings
LocalDate parsed = LocalDate.parse("2026-01-09");
LocalDateTime parsedDT = LocalDateTime.parse("2026-01-09T14:30:45");

// Custom format
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
LocalDate customParsed = LocalDate.parse("09/01/2026", formatter);
String formatted = today.format(formatter);  // "09/01/2026"

// Getting components
int year = today.getYear();        // 2026
Month month = today.getMonth();    // JANUARY
int dayOfMonth = today.getDayOfMonth();  // 9
DayOfWeek dayOfWeek = today.getDayOfWeek();  // THURSDAY
```

### Manipulating Dates
```java
LocalDate today = LocalDate.now();

// Adding/subtracting
LocalDate nextWeek = today.plusWeeks(1);
LocalDate lastMonth = today.minusMonths(1);
LocalDate nextYear = today.plusYears(1);

// With methods (returns new instance)
LocalDate firstOfMonth = today.withDayOfMonth(1);
LocalDate inMarch = today.withMonth(3);

// Comparison
boolean isBefore = today.isBefore(nextWeek);  // true
boolean isAfter = today.isAfter(lastMonth);   // true
boolean isEqual = today.isEqual(LocalDate.now());  // true (probably)
```

### Period and Duration
```java
// Period - date-based amount (years, months, days)
LocalDate start = LocalDate.of(2025, 1, 1);
LocalDate end = LocalDate.of(2026, 6, 15);
Period period = Period.between(start, end);
// 1 year, 5 months, 14 days

int years = period.getYears();   // 1
int months = period.getMonths(); // 5
int days = period.getDays();     // 14

// Duration - time-based amount (hours, minutes, seconds)
LocalTime startTime = LocalTime.of(9, 0);
LocalTime endTime = LocalTime.of(17, 30);
Duration duration = Duration.between(startTime, endTime);
// 8 hours, 30 minutes

long hours = duration.toHours();    // 8
long minutes = duration.toMinutes(); // 510
```

---

## ✅ BLOCK 2 CHECKLIST
- [ ] I can write lambda expressions
- [ ] I understand functional interfaces (Predicate, Function, Consumer, Supplier)
- [ ] I can use Stream operations (filter, map, collect, reduce)
- [ ] I understand Collectors (toList, groupingBy, joining)
- [ ] I can use Optional to handle null safely
- [ ] I can use method references
- [ ] I understand the new Date/Time API

---

# BLOCK 3: JAVASCRIPT REVIEW (1 hour)

## ES6+ Features Quick Review

### Arrow Functions
```javascript
// Traditional function
function add(a, b) {
    return a + b;
}

// Arrow function
const add = (a, b) => a + b;

// With body
const greet = (name) => {
    const greeting = `Hello, ${name}!`;
    return greeting;
};

// Single parameter (no parentheses needed)
const double = n => n * 2;

// No parameters
const sayHi = () => console.log("Hi!");
```

### Destructuring
```javascript
// Array destructuring
const [first, second, ...rest] = [1, 2, 3, 4, 5];
// first = 1, second = 2, rest = [3, 4, 5]

// Object destructuring
const { name, age, city = "Unknown" } = { name: "Alice", age: 25 };
// name = "Alice", age = 25, city = "Unknown" (default)

// In function parameters
const greet = ({ name, age }) => `${name} is ${age}`;
```

### Spread Operator
```javascript
// Array spread
const arr1 = [1, 2, 3];
const arr2 = [...arr1, 4, 5];  // [1, 2, 3, 4, 5]

// Object spread
const obj1 = { a: 1, b: 2 };
const obj2 = { ...obj1, c: 3 };  // { a: 1, b: 2, c: 3 }

// Function arguments
const sum = (a, b, c) => a + b + c;
const numbers = [1, 2, 3];
sum(...numbers);  // 6
```

### Template Literals
```javascript
const name = "Alice";
const age = 25;

// Template literal
const message = `Hello, ${name}! You are ${age} years old.`;

// Multi-line
const html = `
    <div>
        <h1>${name}</h1>
        <p>Age: ${age}</p>
    </div>
`;
```

### Promises and Async/Await
```javascript
// Promise
const fetchData = () => {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            resolve({ data: "Hello" });
        }, 1000);
    });
};

// Using .then()
fetchData()
    .then(result => console.log(result))
    .catch(error => console.error(error));

// Async/Await (cleaner)
const getData = async () => {
    try {
        const result = await fetchData();
        console.log(result);
    } catch (error) {
        console.error(error);
    }
};
```

### Array Methods
```javascript
const numbers = [1, 2, 3, 4, 5];

// map - transform each element
const doubled = numbers.map(n => n * 2);  // [2, 4, 6, 8, 10]

// filter - keep matching elements
const evens = numbers.filter(n => n % 2 === 0);  // [2, 4]

// reduce - combine into single value
const sum = numbers.reduce((acc, n) => acc + n, 0);  // 15

// find - first matching element
const found = numbers.find(n => n > 3);  // 4

// some/every
const hasEven = numbers.some(n => n % 2 === 0);  // true
const allPositive = numbers.every(n => n > 0);   // true

// Chaining
const result = numbers
    .filter(n => n > 2)
    .map(n => n * 2)
    .reduce((acc, n) => acc + n, 0);  // 24
```

---

## ✅ BLOCK 3 CHECKLIST
- [ ] I can write arrow functions
- [ ] I understand destructuring (array and object)
- [ ] I can use spread operator
- [ ] I can use template literals
- [ ] I understand Promises and async/await
- [ ] I can use map, filter, reduce

---

# DAY 1 RESOURCES

## Videos to Watch Today

| Priority | Topic | Link | Duration |
|----------|-------|------|----------|
| 1 | Java 8 Features | https://www.youtube.com/watch?v=Q93JsQ8vcwY | 2h |
| 2 | Java Streams | https://www.youtube.com/watch?v=t1-YZ6bF-g0 | 45min |
| 3 | JavaScript ES6 | https://www.youtube.com/watch?v=NCwa_xi0Uuc | 1h |

## Practice Exercises

### Java Exercises
```java
// Exercise 1: Use streams to find the sum of even numbers
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
// Expected: 30

// Exercise 2: Convert list of names to uppercase and filter length > 4
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Eve");
// Expected: ["ALICE", "CHARLIE"]

// Exercise 3: Group people by age
List<Person> people = Arrays.asList(
    new Person("Alice", 25),
    new Person("Bob", 30),
    new Person("Charlie", 25)
);
// Expected: {25=[Alice, Charlie], 30=[Bob]}

// Exercise 4: Find the longest string using Optional
List<String> words = Arrays.asList("cat", "elephant", "dog");
// Expected: Optional["elephant"]
```

---

# DAY 2 - SATURDAY JANUARY 10, 2026
## SPRING FRAMEWORK + SPRING BOOT + VUE.JS

---

# BLOCK 1: SPRING CORE CONCEPTS (2 hours)

## What is Spring Framework?

Spring is a comprehensive framework for building Java applications. It provides:
- **Dependency Injection** - managing object creation and wiring
- **Aspect-Oriented Programming** - cross-cutting concerns
- **Data Access** - JDBC, ORM support
- **Web MVC** - building web applications
- **Security** - authentication and authorization

## Dependency Injection (DI)

### The Problem Without DI
```java
// TIGHT COUPLING - bad practice
public class OrderService {
    // Service creates its own dependencies
    private EmailService emailService = new EmailService();
    private PaymentService paymentService = new PaymentService();
    
    public void processOrder(Order order) {
        paymentService.charge(order);
        emailService.sendConfirmation(order);
    }
}

// Problems:
// 1. Hard to test (can't mock dependencies)
// 2. Hard to change implementations
// 3. OrderService knows too much about how to create dependencies
```

### The Solution With DI
```java
// LOOSE COUPLING - good practice
public class OrderService {
    // Dependencies injected from outside
    private final EmailService emailService;
    private final PaymentService paymentService;
    
    // Constructor injection (preferred)
    public OrderService(EmailService emailService, PaymentService paymentService) {
        this.emailService = emailService;
        this.paymentService = paymentService;
    }
    
    public void processOrder(Order order) {
        paymentService.charge(order);
        emailService.sendConfirmation(order);
    }
}

// Benefits:
// 1. Easy to test (inject mocks)
// 2. Easy to swap implementations
// 3. OrderService doesn't know how dependencies are created
```

## Inversion of Control (IoC)

IoC means the framework controls object creation, not your code.

```
WITHOUT IoC (You control):
Your Code → Creates → Objects

WITH IoC (Framework controls):
Spring Container → Creates → Objects → Injects into → Your Code
```

The **Spring Container** (ApplicationContext) manages all your beans (objects).

## Spring Beans

A **Bean** is an object managed by Spring.

### Defining Beans with Annotations
```java
// @Component - generic bean
@Component
public class EmailService {
    public void sendEmail(String to, String message) {
        System.out.println("Sending email to " + to);
    }
}

// @Service - business logic layer
@Service
public class UserService {
    public User findById(Long id) {
        // business logic
    }
}

// @Repository - data access layer
@Repository
public class UserRepository {
    public User findById(Long id) {
        // database access
    }
}

// @Controller - web layer (MVC)
@Controller
public class UserController {
    // handles HTTP requests
}

// @RestController - REST API (returns JSON)
@RestController
public class UserRestController {
    // handles REST API requests
}
```

### Bean Scopes
```java
@Component
@Scope("singleton")  // DEFAULT - one instance per container
public class SingletonBean { }

@Component
@Scope("prototype")  // new instance every time requested
public class PrototypeBean { }

// Web scopes (only in web apps)
@Scope("request")   // one per HTTP request
@Scope("session")   // one per HTTP session
```

## Dependency Injection Methods

### 1. Constructor Injection (RECOMMENDED)
```java
@Service
public class OrderService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    // @Autowired is optional on single constructor (Spring 4.3+)
    public OrderService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
}
```

### 2. Setter Injection
```java
@Service
public class OrderService {
    private UserRepository userRepository;
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

### 3. Field Injection (NOT RECOMMENDED)
```java
@Service
public class OrderService {
    @Autowired  // Works but makes testing harder
    private UserRepository userRepository;
}
```

### Why Constructor Injection is Best
```
1. Dependencies are REQUIRED - clear contract
2. Can make fields FINAL - immutable
3. Easy to TEST - just pass mocks in constructor
4. No reflection magic needed
5. Fails fast if dependency missing
```

## Component Scanning

Spring automatically finds beans in your packages.

```java
@SpringBootApplication  // Includes @ComponentScan
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}

// By default, scans the package where this class is located
// and all sub-packages

// Custom scanning
@ComponentScan(basePackages = {"com.myapp.services", "com.myapp.repositories"})
```

## Qualifiers

When multiple beans of same type exist:

```java
public interface PaymentService {
    void pay(double amount);
}

@Service("creditCard")
public class CreditCardPaymentService implements PaymentService {
    public void pay(double amount) { /* credit card logic */ }
}

@Service("paypal")
public class PayPalPaymentService implements PaymentService {
    public void pay(double amount) { /* PayPal logic */ }
}

// Using @Qualifier to specify which one
@Service
public class OrderService {
    private final PaymentService paymentService;
    
    public OrderService(@Qualifier("creditCard") PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

## Bean Lifecycle
```java
@Component
public class MyBean {
    
    @PostConstruct  // Called after bean is created and dependencies injected
    public void init() {
        System.out.println("Bean initialized");
    }
    
    @PreDestroy  // Called before bean is destroyed
    public void cleanup() {
        System.out.println("Cleaning up resources");
    }
}
```

---

## ✅ BLOCK 1 CHECKLIST
- [ ] I understand what Spring Framework is
- [ ] I can explain Dependency Injection
- [ ] I understand Inversion of Control
- [ ] I know the stereotype annotations (@Component, @Service, @Repository, @Controller)
- [ ] I understand constructor injection and why it's preferred
- [ ] I know how component scanning works
- [ ] I can use @Qualifier when multiple beans exist

---

# BLOCK 2: SPRING BOOT (2 hours)

## What is Spring Boot?

Spring Boot = Spring Framework + Auto-Configuration + Embedded Server + Starters

```
Spring Framework: Requires lots of XML/Java config
Spring Boot: "Convention over Configuration" - works out of the box
```

### Key Features
1. **Auto-Configuration** - configures beans automatically
2. **Starter Dependencies** - pre-packaged dependency sets
3. **Embedded Server** - no need to deploy to external Tomcat
4. **Production Ready** - health checks, metrics, externalized config

## Creating a Spring Boot Project

### Using Spring Initializr (start.spring.io)
1. Go to https://start.spring.io
2. Select:
   - Project: Maven
   - Language: Java
   - Spring Boot: 3.x
   - Packaging: Jar
   - Java: 17 or 21
3. Add dependencies:
   - Spring Web
   - Spring Data JPA (for database)
   - PostgreSQL Driver (or H2 for testing)
4. Generate and download

### Project Structure
```
my-project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/myproject/
│   │   │       ├── MyProjectApplication.java    # Main class
│   │   │       ├── controller/                   # REST controllers
│   │   │       ├── service/                      # Business logic
│   │   │       ├── repository/                   # Data access
│   │   │       ├── model/                        # Entity classes
│   │   │       └── dto/                          # Data Transfer Objects
│   │   └── resources/
│   │       ├── application.properties            # Configuration
│   │       ├── application.yml                   # Alternative config
│   │       ├── static/                           # Static files
│   │       └── templates/                        # View templates
│   └── test/
│       └── java/                                 # Test classes
├── pom.xml                                       # Maven config
└── README.md
```

## The Main Application Class

```java
package com.example.myproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // = @Configuration + @EnableAutoConfiguration + @ComponentScan
public class MyProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyProjectApplication.class, args);
    }
}
```

## Configuration Files

### application.properties
```properties
# Server configuration
server.port=8080

# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=postgres
spring.datasource.password=secret

# JPA configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Logging
logging.level.root=INFO
logging.level.com.example=DEBUG
```

### application.yml (alternative)
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: postgres
    password: secret
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

logging:
  level:
    root: INFO
    com.example: DEBUG
```

## Building a REST API

### Step 1: Create the Entity (Model)
```java
package com.example.myproject.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private Double price;
    
    // Default constructor (required by JPA)
    public Product() {}
    
    // Constructor with fields
    public Product(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
```

### Step 2: Create the Repository
```java
package com.example.myproject.repository;

import com.example.myproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository provides: save, findById, findAll, delete, etc.
    
    // Custom query methods (Spring Data generates implementation)
    List<Product> findByName(String name);
    List<Product> findByPriceLessThan(Double price);
    List<Product> findByNameContainingIgnoreCase(String keyword);
}
```

### Step 3: Create the Service
```java
package com.example.myproject.service;

import com.example.myproject.model.Product;
import com.example.myproject.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        
        return productRepository.save(product);
    }
    
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}
```

### Step 4: Create the Controller
```java
package com.example.myproject.controller;

import com.example.myproject.model.Product;
import com.example.myproject.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    // GET /api/products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    
    // GET /api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // POST /api/products
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // PUT /api/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id, 
            @RequestBody Product product) {
        try {
            Product updated = productService.updateProduct(id, product);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE /api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    
    // GET /api/products/search?keyword=phone
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }
}
```

## Request/Response Annotations Explained

```java
@RestController      // = @Controller + @ResponseBody (returns JSON)
@RequestMapping      // Base URL path for all methods in controller
@GetMapping          // Handles HTTP GET
@PostMapping         // Handles HTTP POST
@PutMapping          // Handles HTTP PUT
@DeleteMapping       // Handles HTTP DELETE
@PatchMapping        // Handles HTTP PATCH

@PathVariable        // Extracts value from URL path: /products/{id}
@RequestParam        // Extracts query parameter: /search?keyword=phone
@RequestBody         // Deserializes JSON body to object
@ResponseBody        // Serializes return value to JSON (implicit in @RestController)
```

## ResponseEntity

ResponseEntity gives you control over the HTTP response:

```java
// Return with specific status
return ResponseEntity.ok(product);                    // 200 OK
return ResponseEntity.status(HttpStatus.CREATED).body(product);  // 201 Created
return ResponseEntity.notFound().build();             // 404 Not Found
return ResponseEntity.badRequest().body("Error message");  // 400 Bad Request
return ResponseEntity.noContent().build();            // 204 No Content

// With custom headers
return ResponseEntity.ok()
    .header("Custom-Header", "value")
    .body(product);
```

## Running the Application

```bash
# Using Maven
./mvnw spring-boot:run

# Or build and run JAR
./mvnw clean package
java -jar target/myproject-0.0.1-SNAPSHOT.jar
```

Application runs at: http://localhost:8080

## Testing with cURL or Postman

```bash
# Get all products
curl http://localhost:8080/api/products

# Get product by ID
curl http://localhost:8080/api/products/1

# Create product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"iPhone","description":"Smartphone","price":999.99}'

# Update product
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"iPhone Pro","description":"Pro Smartphone","price":1199.99}'

# Delete product
curl -X DELETE http://localhost:8080/api/products/1

# Search products
curl http://localhost:8080/api/products/search?keyword=phone
```

---

## ✅ BLOCK 2 CHECKLIST
- [ ] I understand what Spring Boot provides over Spring Framework
- [ ] I can create a project with Spring Initializr
- [ ] I understand the project structure
- [ ] I can configure application.properties/yml
- [ ] I can create Entity, Repository, Service, Controller
- [ ] I understand REST annotations (@GetMapping, @PostMapping, etc.)
- [ ] I can use @PathVariable, @RequestParam, @RequestBody
- [ ] I understand ResponseEntity

---

# BLOCK 3: VUE.JS BASICS (2 hours)

## What is Vue.js?

Vue.js is a progressive JavaScript framework for building user interfaces.

- **Reactive** - UI updates automatically when data changes
- **Component-based** - build reusable UI components
- **Virtual DOM** - efficient updates

## Vue 3 Setup (using CDN for learning)

```html
<!DOCTYPE html>
<html>
<head>
    <title>Vue App</title>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
</head>
<body>
    <div id="app">
        {{ message }}
    </div>

    <script>
        const { createApp } = Vue;
        
        createApp({
            data() {
                return {
                    message: 'Hello Vue!'
                }
            }
        }).mount('#app');
    </script>
</body>
</html>
```

## Vue Instance and Data

```javascript
const app = createApp({
    // Reactive data
    data() {
        return {
            message: 'Hello',
            count: 0,
            user: {
                name: 'Alice',
                age: 25
            },
            items: ['Apple', 'Banana', 'Cherry']
        }
    },
    
    // Computed properties (cached, derived from data)
    computed: {
        reversedMessage() {
            return this.message.split('').reverse().join('');
        },
        doubleCount() {
            return this.count * 2;
        }
    },
    
    // Methods
    methods: {
        increment() {
            this.count++;
        },
        greet(name) {
            alert(`Hello, ${name}!`);
        }
    },
    
    // Watchers (react to data changes)
    watch: {
        count(newValue, oldValue) {
            console.log(`Count changed from ${oldValue} to ${newValue}`);
        }
    },
    
    // Lifecycle hooks
    mounted() {
        console.log('Component mounted');
    }
});

app.mount('#app');
```

## Template Syntax

### Text Interpolation
```html
<span>{{ message }}</span>
<span>{{ user.name }}</span>
<span>{{ count + 1 }}</span>
<span>{{ ok ? 'YES' : 'NO' }}</span>
```

### Attribute Binding (v-bind or :)
```html
<!-- Full syntax -->
<img v-bind:src="imageUrl">
<a v-bind:href="url">Link</a>

<!-- Shorthand -->
<img :src="imageUrl">
<a :href="url">Link</a>
<button :disabled="isDisabled">Submit</button>
<div :class="{ active: isActive }">...</div>
<div :style="{ color: textColor }">...</div>
```

### Event Handling (v-on or @)
```html
<!-- Full syntax -->
<button v-on:click="increment">Add</button>

<!-- Shorthand -->
<button @click="increment">Add</button>
<button @click="count++">Add (inline)</button>
<input @keyup.enter="submit">
<form @submit.prevent="handleSubmit">...</form>
```

### Conditional Rendering
```html
<!-- v-if / v-else-if / v-else -->
<div v-if="type === 'A'">Type A</div>
<div v-else-if="type === 'B'">Type B</div>
<div v-else>Other Type</div>

<!-- v-show (toggles display CSS) -->
<div v-show="isVisible">Always in DOM, just hidden</div>
```

### List Rendering
```html
<!-- v-for with array -->
<ul>
    <li v-for="item in items" :key="item.id">
        {{ item.name }}
    </li>
</ul>

<!-- v-for with index -->
<ul>
    <li v-for="(item, index) in items" :key="index">
        {{ index }}: {{ item }}
    </li>
</ul>

<!-- v-for with object -->
<ul>
    <li v-for="(value, key) in user" :key="key">
        {{ key }}: {{ value }}
    </li>
</ul>
```

### Two-Way Binding (v-model)
```html
<!-- Input -->
<input v-model="message" placeholder="Type here">
<p>Message: {{ message }}</p>

<!-- Textarea -->
<textarea v-model="description"></textarea>

<!-- Checkbox -->
<input type="checkbox" v-model="checked">

<!-- Multiple checkboxes -->
<input type="checkbox" v-model="selectedFruits" value="apple">
<input type="checkbox" v-model="selectedFruits" value="banana">

<!-- Radio -->
<input type="radio" v-model="picked" value="a">
<input type="radio" v-model="picked" value="b">

<!-- Select -->
<select v-model="selected">
    <option value="a">A</option>
    <option value="b">B</option>
</select>
```

## Vue Components

### Defining a Component
```javascript
// Component definition
const TodoItem = {
    props: ['todo'],
    template: `
        <li>
            {{ todo.text }}
            <button @click="$emit('remove')">Remove</button>
        </li>
    `
};

// Main app
const app = createApp({
    components: {
        TodoItem
    },
    data() {
        return {
            todos: [
                { id: 1, text: 'Learn Vue' },
                { id: 2, text: 'Build app' }
            ]
        }
    },
    methods: {
        removeTodo(index) {
            this.todos.splice(index, 1);
        }
    }
});
```

### Using Components
```html
<div id="app">
    <ul>
        <todo-item
            v-for="(todo, index) in todos"
            :key="todo.id"
            :todo="todo"
            @remove="removeTodo(index)"
        ></todo-item>
    </ul>
</div>
```

### Single File Components (SFC) - .vue files
```vue
<template>
    <div class="product-card">
        <h2>{{ product.name }}</h2>
        <p>{{ product.description }}</p>
        <span class="price">${{ product.price }}</span>
        <button @click="addToCart">Add to Cart</button>
    </div>
</template>

<script>
export default {
    name: 'ProductCard',
    props: {
        product: {
            type: Object,
            required: true
        }
    },
    methods: {
        addToCart() {
            this.$emit('add-to-cart', this.product);
        }
    }
}
</script>

<style scoped>
.product-card {
    border: 1px solid #ccc;
    padding: 16px;
    border-radius: 8px;
}
.price {
    font-weight: bold;
    color: green;
}
</style>
```

## Vue Lifecycle Hooks

```javascript
export default {
    // Before instance is created
    beforeCreate() { },
    
    // Instance created, data is reactive
    created() {
        // Good place for API calls
        this.fetchData();
    },
    
    // Before mounting to DOM
    beforeMount() { },
    
    // Mounted to DOM
    mounted() {
        // Good place to access DOM elements
        console.log(this.$el);
    },
    
    // Before data update
    beforeUpdate() { },
    
    // After data update
    updated() { },
    
    // Before component is destroyed
    beforeUnmount() { },  // Vue 3
    
    // Component destroyed
    unmounted() { }  // Vue 3
}
```

## Making API Calls with Fetch
```javascript
export default {
    data() {
        return {
            products: [],
            loading: false,
            error: null
        }
    },
    
    async created() {
        await this.fetchProducts();
    },
    
    methods: {
        async fetchProducts() {
            this.loading = true;
            try {
                const response = await fetch('/api/products');
                this.products = await response.json();
            } catch (err) {
                this.error = err.message;
            } finally {
                this.loading = false;
            }
        },
        
        async createProduct(product) {
            const response = await fetch('/api/products', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(product)
            });
            const newProduct = await response.json();
            this.products.push(newProduct);
        }
    }
}
```

---

## ✅ BLOCK 3 CHECKLIST
- [ ] I understand Vue instance and data()
- [ ] I can use template syntax ({{ }}, v-bind, v-on)
- [ ] I understand v-if, v-for, v-model
- [ ] I can create and use components
- [ ] I understand props and events ($emit)
- [ ] I know the lifecycle hooks
- [ ] I can make API calls with fetch

---

# BLOCK 4: SYSTEM DESIGN INTRO (1 hour)

## Key Concepts

### Scalability
- **Vertical Scaling** - bigger machine (more CPU, RAM)
- **Horizontal Scaling** - more machines (add servers)

### Availability
- System is operational and accessible
- Measured in "nines" (99.9% = 8.76 hours downtime/year)

### Latency vs Throughput
- **Latency** - time for single request (ms)
- **Throughput** - requests per second (RPS)

### CAP Theorem
You can only have 2 of 3:
- **Consistency** - all nodes see same data
- **Availability** - every request gets response
- **Partition Tolerance** - system works despite network issues

---

# DAY 2 RESOURCES

## Videos to Watch Today

| Priority | Topic | Link | Duration |
|----------|-------|------|----------|
| 1 | Spring Boot Full Course | https://www.youtube.com/watch?v=9SGDpanrc8U | 3h |
| 2 | Vue 3 Crash Course | https://www.youtube.com/watch?v=FXpIoQ_rT_c | 2h |

## Practice Project

Build a complete CRUD API for "Products":
1. Create Spring Boot project at start.spring.io
2. Add Web, JPA, H2 dependencies
3. Create Product entity
4. Create ProductRepository
5. Create ProductService
6. Create ProductController
7. Test all endpoints with cURL

---

# END OF DAY 1 & 2 GUIDE

## Summary

### Day 1 Covered:
- Java Core (OOP, Collections, Exceptions)
- Java 8 (Lambdas, Streams, Optional)
- JavaScript ES6+ Review

### Day 2 Covered:
- Spring Core (DI, IoC, Beans)
- Spring Boot (REST API with CRUD)
- Vue.js Basics

## Next: Day 3
- Spring Data JPA Deep Dive
- React Basics
- More System Design

---

**YOU GOT THIS! 💪**
