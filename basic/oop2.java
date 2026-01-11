package basic;

public class oop2 {

    /* ===============================
       INTERFACE DEMONSTRATION
       =============================== */
    public interface Pet {
        void play();
        void feed();
    }

    public class Hamster implements Pet {
        private String name;
        public Hamster(String name) { this.name = name; }
        @Override
        public void play() { System.out.println(name + " runs on the wheel!"); }
        @Override
        public void feed() { System.out.println(name + " eats sunflower seeds."); }
        @Override
        public String toString() { return "Hamster{name='" + name + "'}"; }
    }

    public class Parrot implements Pet {
        private String name;
        public Parrot(String name) { this.name = name; }
        @Override
        public void play() { System.out.println(name + " mimics sounds and talks!"); }
        @Override
        public void feed() { System.out.println(name + " eats fruit."); }
        public void fly() { System.out.println(name + " flies around the room."); }
        @Override
        public String toString() { return "Parrot{name='" + name + "'}"; }
    }

    /* ===============================
       COMPOSITION DEMONSTRATION
       =============================== */
    public class Engine {
        private int horsepower;
        public Engine(int horsepower) { this.horsepower = horsepower; }
        public void start() { System.out.println("Engine started with " + horsepower + " HP"); }
        public int getHorsepower() { return horsepower; }
    }

    public class Wheel {
        private int size;
        public Wheel(int size) { this.size = size; }
        public int getSize() { return size; }
    }

    // Car is composed of Engine and Wheels (HAS-A relationship)
    public class Car implements Drivable {
        private Engine engine;
        private Wheel[] wheels;
        private String model;
        
        public Car(String model, Engine engine, Wheel[] wheels) {
            this.model = model;
            this.engine = engine;      // Composition: Car HAS an Engine
            this.wheels = wheels;      // Composition: Car HAS Wheels
        }
        
        public void describe() {
            System.out.println("Car " + model + " with engine " + engine.getHorsepower() + " HP and " + wheels.length + " wheels");
        }
        
        @Override
        public void drive() {
            engine.start();
            System.out.println("Car " + model + " is driving on " + wheels[0].getSize() + " inch wheels");
        }
    }

    /* ===============================
       INTERFACE + COMPOSITION TOGETHER
       =============================== */
    public interface Drivable { void drive(); }

    public class Driver {
        private String name;
        private Drivable vehicle;  // Composition: Driver HAS a vehicle
        
        public Driver(String name, Drivable vehicle) {
            this.name = name;
            this.vehicle = vehicle;
        }
        
        public void go() {
            System.out.println(name + " is ready to go!");
            vehicle.drive();  // Interface call, concrete behavior provided by composed object
        }
    }

    /* ===============================
       DEMO METHOD
       =============================== */
    public void demonstrateInterfaceAndComposition() {
        System.out.println("\n=== INTERFACE DEMO ===");
        Pet h = new Hamster("Nibbles");
        Pet p = new Parrot("Polly");
        h.play(); h.feed();
        p.play(); p.feed();
        ((Parrot) p).fly(); // Downcast to access Parrot-specific method
        
        System.out.println("\n=== COMPOSITION DEMO ===");
        Engine engine = new Engine(150);
        Wheel[] wheels = { new Wheel(16), new Wheel(16), new Wheel(16), new Wheel(16) };
        Car car = new Car("Sedan", engine, wheels);
        car.describe();
        
        System.out.println("\n=== INTERFACE + COMPOSITION DEMO ===");
        Driver d = new Driver("Alex", car);
        d.go();
    }

/*         | Concept        | Meaning        | Example                 |
    | -------------- | -------------- | ----------------------- |
    | Inheritance    | IS-A           | Dog extends Animal      |
    | Interface      | CAN-DO         | Dog implements Runnable |
    | Composition    | HAS-A          | Car has an Engine       |
    | Loose coupling | Easy to change | Engine interchangeable  |
 */
}
