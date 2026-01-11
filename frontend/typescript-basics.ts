// TypeScript Basics - Class-based approach

/* === CLASSES AND TYPES === */
class User {
    name: string;
    age: number;

    constructor(name: string, age: number) {
        this.name = name;
        this.age = age;
    }

    greet(): string {
        return `Hello, ${this.name}!`;
    }

    isAdult(): boolean {
        return this.age >= 18;
    }
}

/* === INTERFACES === */
interface Product {
    id: number;
    name: string;
    price: number;
    inStock?: boolean; // Optional property
}

class ProductService {
    products: Product[] = [];

    addProduct(product: Product): void {
        this.products.push(product);
    }

    getProduct(id: number): Product | undefined {
        return this.products.find(p => p.id === id);
    }

    getTotalValue(): number {
        return this.products.reduce((acc, p) => acc + p.price, 0);
    }
}

/* === GENERICS === */
class DataStore<T> {
    private items: T[] = [];

    add(item: T): void {
        this.items.push(item);
    }

    getAll(): T[] {
        return [...this.items];
    }

    filter(predicate: (item: T) => boolean): T[] {
        return this.items.filter(predicate);
    }
}

/* === ENUM === */
enum Status {
    Active = "ACTIVE",
    Inactive = "INACTIVE",
    Pending = "PENDING"
}

class Task {
    constructor(
        public title: string,
        public status: Status = Status.Pending
    ) {}

    complete(): void {
        this.status = Status.Active;
    }
}

/* === ASYNC CLASS METHODS === */
class ApiClient {
    private baseUrl: string = 'https://jsonplaceholder.typicode.com';

    async fetchPost(id: number): Promise<any> {
        try {
            const response = await fetch(`${this.baseUrl}/posts/${id}`);
            if (!response.ok) {
                throw new Error(`HTTP error ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error('Fetch error:', error);
            throw error;
        }
    }
}

/* === INHERITANCE AND POLYMORPHISM === */
abstract class Animal {
    constructor(public name: string) {}
    
    abstract makeSound(): string;
    
    move(): string {
        return `${this.name} is moving`;
    }
}

class Dog extends Animal {
    constructor(name: string, public breed: string) {
        super(name);
    }

    makeSound(): string {
        return 'Woof!';
    }

    fetch(): string {
        return `${this.name} is fetching the ball`;
    }
}

/* === DEMONSTRATION CLASS === */
class TypeScriptDemo {
    run(): void {
        console.log('=== TypeScript Basics Demo ===\n');

        // User class
        const user = new User('Andres', 30);
        console.log(user.greet());
        console.log(`Is adult: ${user.isAdult()}`);

        // Product service
        const productService = new ProductService();
        productService.addProduct({ id: 1, name: 'Laptop', price: 1200 });
        productService.addProduct({ id: 2, name: 'Mouse', price: 25 });
        console.log(`\nTotal value: $${productService.getTotalValue()}`);

        // Generics
        const numberStore = new DataStore<number>();
        numberStore.add(10);
        numberStore.add(20);
        numberStore.add(30);
        console.log(`\nNumbers > 15:`, numberStore.filter(n => n > 15));

        const userStore = new DataStore<User>();
        userStore.add(new User('Alice', 25));
        userStore.add(new User('Bob', 17));
        console.log(`Adults:`, userStore.filter(u => u.isAdult()).map(u => u.name));

        // Enums and Tasks
        const task = new Task('Learn TypeScript');
        console.log(`\nTask status: ${task.status}`);
        task.complete();
        console.log(`Task status after completion: ${task.status}`);

        // Inheritance
        const animals: Animal[] = [new Dog('Buddy', 'Golden Retriever')];
        animals.forEach(animal => {
            console.log(`\n${animal.name} says: ${animal.makeSound()}`);
            console.log(animal.move());
            if (animal instanceof Dog) {
                console.log(animal.fetch());
            }
        });

        // Async
        const api = new ApiClient();
        api.fetchPost(1).then(data => {
            console.log('\nFetched post:', data);
        });
    }
}

// Run the demo
const demo = new TypeScriptDemo();
demo.run();
