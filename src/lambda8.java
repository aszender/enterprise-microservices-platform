package src;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class lambda8 {

    @FunctionalInterface
    public interface Box {
        String getValue(); // Single abstract method
        int hashCode();
    }

    @FunctionalInterface
    public interface Calculator {
        int calculate(int a, int b);
        default void printResult(int a, int b){
            System.out.println("result: " + calculate(a, b));
        }
    }

    // Custom method that takes a Function as parameter
    int process(String s, Function<String, Integer> logic) {
    return logic.apply(s);
    }

    public void demostrateLambda(){
            process("Andres", s -> s.length());
            process("Emilio", s -> s.indexOf('m'));
            // Using custom functional interface
            Box myBox = new Box() {
                @Override
                public String getValue() {
                    return "Hello from Anonymous Class!";
                }
            };
            System.out.println(myBox.getValue());
            Box myBoxLambda = () -> "Hello from Lambda!";
            System.out.println(myBoxLambda.getValue());

            Calculator adder = (a, b) -> a + b; // Lambda for addition
            Calculator multiplier = (a, b) -> a * b; // Lambda for multiplication
            adder.printResult(5, 3);
            multiplier.printResult(5, 3);

            // Using built-in functional interfaces

            Supplier<Integer> num = () -> 42; // Supplier functional interface built-in
            System.out.println("Supplier value: " + num.get());

            BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b; // BiFunction built-in
            System.out.println("BiFunction add: " + add.apply(5, 7));

            Function<String, Integer> stringLength = s -> s.length(); // Function built-in
            System.out.println("Function string length: " + stringLength.apply("Hello Lambda"));
            int len = stringLength.apply("Test");
            System.out.println("Length of 'Test': " + len);


            Predicate<Integer> isEven = n -> n % 2 == 0; // Predicate built-in
            System.out.println("Predicate is 4 even? " + isEven.test(4));

            Consumer<String> print = s -> System.out.println("Consumer print: " + s); // Consumer built-in
            print.accept("Hello Consumer!");
        }
}



