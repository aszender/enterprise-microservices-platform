import java.util.Scanner;
import src.JavaCore;
import src.oop;
import src.oop.*;
import src.oop2;
import src.oop3;
import src.oop2;

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
        Animal myDog = oopDemo.new Dog("Max", 5, "Labrador");
        System.out.println("\n=== Individual Animal Example ===");
        myDog.makeSound();
        myDog.move();

        // Interfaces and Composition (other file)
        oop2 extra = new oop2();
        extra.demonstrateInterfaceAndComposition();

        // Generics, Exceptions, Immutability, Lifecycle (other file)
        oop3 more = new oop3();
        more.demonstrateAll();
    }
}
