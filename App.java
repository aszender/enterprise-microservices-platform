import java.util.Scanner;
import src.JavaCore;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Hey Andres How are you? ");
        System.out.println("Write a number to start: ");
        int number = Integer.parseInt(sc.nextLine()); // Read input as string and parse to integer
        System.out.println("You wrote: " + number);
        sc.close();

        JavaCore javaCore = new JavaCore();
        javaCore.executeValues();
        
    }
}
