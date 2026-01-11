package basic;

import java.util.*;

public class oop3 {

    /* ===============================
       GENERICS
       =============================== */
    public class Box<T> {
        private T value;
        public Box(T value) { this.value = value; }
        public T get() { return value; }
        public void set(T value) { this.value = value; }
        @Override
        public String toString() { return "Box{" + String.valueOf(value) + "}"; }
    }

    // Bounded wildcard example: works with any Number subtype
    public void sumNumbers(List<? extends Number> nums) {
        double sum = 0.0;
        for (Number n : nums) sum += n.doubleValue();
        System.out.println("Sum of numbers: " + sum);
    }

    /* ===============================
       EXCEPTION HANDLING
       =============================== */
    public static class ValidationException extends Exception {
        public ValidationException(String message) { super(message); }
    }

    public void validateAge(int age) throws ValidationException {
        if (age < 0) throw new ValidationException("Age cannot be negative");
        if (age > 150) throw new ValidationException("Age is unrealistically high");
        System.out.println("Age " + age + " is valid");
    }

    /* ===============================
       IMMUTABILITY
       =============================== */
    public final class ImmutableUser {
        private final String name;
        private final int age;
        private final List<String> roles;

        public ImmutableUser(String name, int age, List<String> roles) {
            this.name = name;
            this.age = age;
            // Defensive copy + unmodifiable view ensures immutability
            this.roles = List.copyOf(roles);
        }
        public String getName() { return name; }
        public int getAge() { return age; }
        public List<String> getRoles() { return roles; }
        @Override
        public String toString() {
            return "ImmutableUser{name='" + name + "', age=" + age + ", roles=" + roles + "}";
        }
    }

    /* ===============================
       OBJECT LIFECYCLE
       =============================== */
    // Demonstrates resource lifecycle via AutoCloseable and try-with-resources
    public class TempResource implements AutoCloseable {
        private final String name;
        public TempResource(String name) {
            this.name = name;
            System.out.println("Resource '" + name + "' initialized");
        }
        public void use() {
            System.out.println("Resource '" + name + "' in use");
        }
        @Override
        public void close() {
            System.out.println("Resource '" + name + "' closed");
        }
    }

    /* ===============================
       DEMO METHOD
       =============================== */
    public void demonstrateAll() {
        System.out.println("\n=== GENERICS DEMO ===");
        Box<String> bs = new Box<>("Hello");
        Box<Integer> bi = new Box<>(42);
        System.out.println(bs);
        System.out.println(bi);
        bs.set("World");
        System.out.println("Updated " + bs);
        sumNumbers(Arrays.asList(1, 2, 3, 4.5));

        System.out.println("\n=== EXCEPTION HANDLING DEMO ===");
        try {
            validateAge(30);
            validateAge(-1); // triggers ValidationException
        } catch (ValidationException e) {
            System.out.println("Validation failed: " + e.getMessage());
        } finally {
            System.out.println("Validation attempt complete (finally block)");
        }

        System.out.println("\n=== IMMUTABILITY DEMO ===");
        List<String> roles = new ArrayList<>(List.of("USER", "ADMIN"));
        ImmutableUser u = new ImmutableUser("Alex", 28, roles);
        System.out.println(u);
        System.out.println("Attempting to modify roles (should fail)...");
        try {
            u.getRoles().add("SUPERADMIN");
        } catch (UnsupportedOperationException ex) {
            System.out.println("Cannot modify immutable roles: " + ex);
        }
        roles.add("AUDITOR"); // original list changes, immutable copy unaffected
        System.out.println("Original list changed, immutable user still: " + u);

        System.out.println("\n=== OBJECT LIFECYCLE DEMO ===");
        try (TempResource r = new TempResource("demo")) {
            r.use();
        } // close() called automatically here
        System.out.println("After try-with-resources block - resource cleaned up");
    }
}
