package src;
import java.util.Objects;

public class oop {
    
    /* ===============================
       ENCAPSULATION
       Private fields with public getters/setters
       =============================== */
    public abstract class Animal {
        private String name;  // Private field - encapsulation
        private int age;      // Private field - encapsulation
        private String species;

        public Animal(String name, int age, String species) {
            this.name = name;
            this.age = age;
            this.species = species;
        }

        // Getters and setters - controlled access to private fields
        public void setName(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public int getAge() {
            return age;
        }
        
        public void setAge(int age) {
            if (age > 0) {  // Validation - encapsulation benefit
                this.age = age;
            }
        }
        
        public String getSpecies() {
            return species;
        }

        /* ===============================
           ABSTRACTION
           Abstract method forces subclasses to implement
           =============================== */
           public abstract void makeSound();  // Abstract method 
        
        public abstract void move();       // Another abstract method
        
        // Concrete method available to all subclasses
        public void sleep() {
            System.out.println(name + " is sleeping...");
        }

        @Override
        public String toString() {
            return species + "{name='" + name + "', age=" + age + "}";
        }
        
        // Equality & hashing based on concrete class and core fields
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Animal animal = (Animal) o;
            return age == animal.age &&
                   Objects.equals(name, animal.name) &&
                   Objects.equals(species, animal.species);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age, species);
        }
        
    }
    
    /* ===============================
       INHERITANCE & POLYMORPHISM
       Dog extends Animal
       =============================== */
    public class Dog extends Animal {
        private String breed;  // Dog-specific field
        
        public Dog(String name, int age, String breed) {
            super(name, age, "Dog");  // Call parent constructor
            this.breed = breed;
        }

        // Implementing abstract methods (abstraction requirement)
        @Override
        public void makeSound() {
            System.out.println(getName() + " says: Woof! Woof!");
        }
        
        @Override
        public void move() {
            System.out.println(getName() + " runs on four legs");
        }
        
        // Dog-specific method
        public void fetch() {
            System.out.println(getName() + " is fetching the ball!");
        }
        
        public String getBreed() {
            return breed;
        }

        // Include subclass field in equality & hashing
        @Override
        public boolean equals(Object o) {
            if (!super.equals(o)) return false;
            Dog dog = (Dog) o;
            return Objects.equals(breed, dog.breed);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), breed);
        }
    }
    
    /* ===============================
       INHERITANCE & POLYMORPHISM
       Cat extends Animal
       =============================== */
    public class Cat extends Animal {
        private boolean isIndoor;
        
        public Cat(String name, int age, boolean isIndoor) {
            super(name, age, "Cat");
            this.isIndoor = isIndoor;
        }

        @Override
        public void makeSound() {
            System.out.println(getName() + " says: Meow!");
        }
        
        @Override
        public void move() {
            System.out.println(getName() + " walks gracefully");
        }
        
        // Cat-specific method
        public void scratch() {
            System.out.println(getName() + " is scratching the furniture!");
        }

        // Include subclass field in equality & hashing
        @Override
        public boolean equals(Object o) {
            if (!super.equals(o)) return false;
            Cat cat = (Cat) o;
            return isIndoor == cat.isIndoor;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), isIndoor);
        }
    }

    /* ===============================
       EQUALS/HASHCODE DEMONSTRATION
       =============================== */
    public void demonstrateEqualsHashCode() {
        System.out.println("\n=== EQUALS & HASHCODE DEMO ===");
        Dog d1 = new Dog("Buddy", 3, "Golden Retriever");
        Dog d2 = new Dog("Buddy", 3, "Golden Retriever");
        Dog d3 = new Dog("Buddy", 3, "Labrador");

        System.out.println("d1.equals(d2): " + d1.equals(d2)); // true
        System.out.println("d1.hashCode() == d2.hashCode(): " + (d1.hashCode() == d2.hashCode())); // true
        System.out.println("d1.equals(d3): " + d1.equals(d3)); // false (different breed)
    }
    
    /* ===============================
       POLYMORPHISM DEMONSTRATION
       =============================== */
    public void demonstratePolymorphism() {
        System.out.println("\n=== POLYMORPHISM DEMONSTRATION ===");
        //1. inheritage 2. method overriding 3. Parent reference pointer to child object
        
        // Polymorphism - same reference type (Animal), different objects
        Animal dog = new Dog("Buddy", 3, "Golden Retriever");
        Animal cat = new Cat("Whiskers", 2, true);

        dog.makeSound();
        cat.makeSound();

        //is not a best practice to downcast like this, just for demonstration
        if (dog instanceof Dog){ // Checking actual object type
            Dog myDog = (Dog) dog; // Downcasting
            System.out.println("\n=== Dog Specific Actions ===");
            myDog.makeSound();
            myDog.fetch();
        }
        
        // Array of Animals - polymorphism in action
        Animal[] animals = {dog, cat};
        
        // Same method call, different behavior based on actual object type
        for (Animal animal : animals) {
            animal.makeSound();  // Polymorphic call
            animal.move();       // Polymorphic call
            animal.sleep();      // Inherited method
            System.out.println(animal.toString());
            System.out.println();
        }
        
        // Downcasting to access specific methods
        if (dog instanceof Dog) {
            ((Dog) dog).fetch();
        }
        
        if (cat instanceof Cat) {
            ((Cat) cat).scratch();
        }
    }

}