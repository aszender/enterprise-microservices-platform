
package basic;


interface Engine {
    void start();
}

//@Component
//class ElectricEngine implements Engine {}

//@Component
class Car {
    private final Engine engine;

    public Car(Engine engine) {
        this.engine = engine;
    }

    public Engine getEngine() {
        return engine;
    }
}
