import java.util.Scanner;
import basic.JavaCore;
import basic.oop;
import basic.oop2;
import basic.oop3;
import basic.lambda8;
import basic.streams8;
import basic.OptionalExample;

public class App {
    public static void main(String[] args) {
        // User Input Handling
        Scanner sc = new Scanner(System.in);
        System.out.println("Hey Andres How are you? ");
        System.out.println("Write a number to start: ");
        int number = Integer.parseInt(sc.nextLine()); // Read input as string and parse to integer
        System.out.println("You wrote: " + number);
        sc.close();

        // basic Java Concepts
        JavaCore javaCore = new JavaCore();
        javaCore.executeValues();
        
        // Object-Oriented Programming - All 4 Pillars
        oop oopDemo = new oop();
        oopDemo.demonstratePolymorphism();
        oopDemo.demonstrateEqualsHashCode();

        // Individual animal creation example
        oop.Animal myDog = oopDemo.new Dog("Max", 5, "Labrador");
        System.out.println("\n=== Individual Animal Example ===");
        myDog.makeSound();
        myDog.move();

        // Interfaces and Composition (other file)
        oop2 extra = new oop2();
        extra.demonstrateInterfaceAndComposition();

        // Generics, Exceptions, Immutability, Lifecycle (other file)
        oop3 more = new oop3();
        more.demonstrateAll();

        // Lambda Expressions (Java 8+)
        System.out.println("\n=== Lambda Expressions Demo ===");
        lambda8 lambdaDemo = new lambda8();
        lambdaDemo.demostrateLambda();

        // Streams API (Java 8+)
        System.out.println("\n=== Streams API Demo ===");
        streams8 streamsDemo = new streams8();
        streamsDemo.demostrateStreams();

        // Optional & Time APIs (Java 8+)
        System.out.println("\n=== Optional & Time APIs Demo ===");
        OptionalExample optionalDemo = new OptionalExample();
        optionalDemo.demonstrateOptional();
        optionalDemo.demonstrateTimeAPIs();
    }
}
