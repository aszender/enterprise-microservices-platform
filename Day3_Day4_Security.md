# IMPACT.COM INTERVIEW PREPARATION
# DAY 3 & DAY 4 - REST APIs, JPA, REACT, SPRING SECURITY, JWT & TESTING

---

# DAY 3 - SUNDAY JANUARY 11, 2026
## REST APIs + SPRING DATA JPA + REACT BASICS

---

# BLOCK 1: REST APIs WITH SPRING BOOT (2 hours)

## What is REST?

REST (Representational State Transfer) is an architectural style for designing networked applications.

### REST Principles
1. **Stateless** - Each request contains all information needed
2. **Client-Server** - Separation of concerns
3. **Uniform Interface** - Consistent way to interact with resources
4. **Resource-Based** - Everything is a resource with a URI

### HTTP Methods
```
GET     - Read/Retrieve a resource
POST    - Create a new resource
PUT     - Update/Replace a resource completely
PATCH   - Update/Modify a resource partially
DELETE  - Remove a resource
```

### HTTP Status Codes
```
2xx - Success
  200 OK              - Request succeeded
  201 Created         - Resource created successfully
  204 No Content      - Success but no content to return

3xx - Redirection
  301 Moved Permanently
  304 Not Modified

4xx - Client Errors
  400 Bad Request     - Invalid request syntax
  401 Unauthorized    - Authentication required
  403 Forbidden       - Authenticated but not authorized
  404 Not Found       - Resource doesn't exist
  409 Conflict        - Conflict with current state
  422 Unprocessable   - Validation errors

5xx - Server Errors
  500 Internal Server Error
  502 Bad Gateway
  503 Service Unavailable
```

---

## Spring Boot REST Annotations

### @RestController
Combines @Controller and @ResponseBody - returns JSON directly.

```java
@RestController
@RequestMapping("/api/v1")
public class ProductController {
    // All methods return JSON
}
```

### Request Mapping Annotations
```java
@GetMapping("/products")           // GET /api/v1/products
@GetMapping("/products/{id}")      // GET /api/v1/products/123
@PostMapping("/products")          // POST /api/v1/products
@PutMapping("/products/{id}")      // PUT /api/v1/products/123
@PatchMapping("/products/{id}")    // PATCH /api/v1/products/123
@DeleteMapping("/products/{id}")   // DELETE /api/v1/products/123
```

### @PathVariable - Extract from URL path
```java
// URL: /api/products/123
@GetMapping("/products/{id}")
public Product getProduct(@PathVariable Long id) {
    return productService.findById(id);
}

// Multiple path variables
// URL: /api/users/5/orders/10
@GetMapping("/users/{userId}/orders/{orderId}")
public Order getOrder(
    @PathVariable Long userId,
    @PathVariable Long orderId
) {
    return orderService.findByUserAndId(userId, orderId);
}
```

### @RequestParam - Extract query parameters
```java
// URL: /api/products?category=electronics&minPrice=100
@GetMapping("/products")
public List<Product> searchProducts(
    @RequestParam String category,
    @RequestParam(required = false, defaultValue = "0") Double minPrice,
    @RequestParam(required = false) Double maxPrice
) {
    return productService.search(category, minPrice, maxPrice);
}
```

### @RequestBody - Deserialize JSON body
```java
@PostMapping("/products")
public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    Product saved = productService.save(product);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}

// With validation
@PostMapping("/products")
public ResponseEntity<Product> createProduct(
    @Valid @RequestBody ProductDTO productDTO
) {
    // @Valid triggers validation annotations on ProductDTO
    Product saved = productService.save(productDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}
```

### @RequestHeader - Extract headers
```java
@GetMapping("/profile")
public User getProfile(
    @RequestHeader("Authorization") String authHeader,
    @RequestHeader(value = "X-Custom-Header", required = false) String custom
) {
    // Extract token from "Bearer <token>"
    String token = authHeader.replace("Bearer ", "");
    return userService.getFromToken(token);
}
```

---

## ResponseEntity - Full Control Over Response

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);  // 200 OK
    }

    // GET single product
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return productService.findById(id)
            .map(ResponseEntity::ok)  // 200 OK if found
            .orElse(ResponseEntity.notFound().build());  // 404 if not found
    }

    // POST - Create product
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product saved = productService.save(product);
        
        // Return 201 Created with Location header
        URI location = URI.create("/api/products/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    // PUT - Full update
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody Product product
    ) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();  // 404
        }
        product.setId(id);
        Product updated = productService.save(product);
        return ResponseEntity.ok(updated);  // 200 OK
    }

    // PATCH - Partial update
    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(
        @PathVariable Long id,
        @RequestBody Map<String, Object> updates
    ) {
        return productService.findById(id)
            .map(product -> {
                // Apply partial updates
                if (updates.containsKey("name")) {
                    product.setName((String) updates.get("name"));
                }
                if (updates.containsKey("price")) {
                    product.setPrice((Double) updates.get("price"));
                }
                return ResponseEntity.ok(productService.save(product));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();  // 404
        }
        productService.deleteById(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
```

---

## DTOs (Data Transfer Objects)

DTOs separate your API contract from your internal entity structure.

### Why Use DTOs?
1. **Security** - Don't expose internal fields (like passwords)
2. **Flexibility** - API can differ from database schema
3. **Validation** - Validate input separately
4. **Versioning** - Change DTOs without changing entities

### Example
```java
// Entity - internal representation
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;  // DON'T expose this!
    private LocalDateTime createdAt;
    private boolean active;
    // getters, setters
}

// Request DTO - for creating/updating
public class UserCreateDTO {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    // getters, setters
}

// Response DTO - for returning data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    // NO password field!
    
    // Constructor from Entity
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
    }
    
    // getters
}

// Controller using DTOs
@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
        @Valid @RequestBody UserCreateDTO dto
    ) {
        User user = userService.create(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new UserResponseDTO(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return userService.findById(id)
            .map(UserResponseDTO::new)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
```

---

## Validation

### Add Dependency
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### Validation Annotations
```java
public class ProductDTO {
    
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be 2-100 characters")
    private String name;
    
    @Size(max = 1000, message = "Description too long")
    private String description;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @DecimalMin(value = "0.01", message = "Minimum price is 0.01")
    private BigDecimal price;
    
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
    
    @Email(message = "Invalid email")
    private String contactEmail;
    
    @Pattern(regexp = "^[A-Z]{2}\\d{4}$", message = "Invalid SKU format")
    private String sku;
    
    // getters, setters
}
```

### Global Exception Handler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Validation failed");
        response.put("errors", errors);
        
        return ResponseEntity.badRequest().body(response);
    }

    // Handle resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(
        ResourceNotFoundException ex
    ) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "An unexpected error occurred");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

// Custom exception
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

---

## REST API Best Practices

### 1. Use Nouns for Resources, Not Verbs
```
GOOD:
GET    /api/products
POST   /api/products
GET    /api/products/{id}

BAD:
GET    /api/getProducts
POST   /api/createProduct
GET    /api/getProductById
```

### 2. Use Plural Nouns
```
GOOD: /api/products, /api/users, /api/orders
BAD:  /api/product, /api/user, /api/order
```

### 3. Use HTTP Methods Correctly
```
GET    - Read only, no side effects
POST   - Create new resource
PUT    - Replace entire resource
PATCH  - Partial update
DELETE - Remove resource
```

### 4. Version Your API
```
/api/v1/products
/api/v2/products
```

### 5. Use Query Parameters for Filtering/Sorting
```
GET /api/products?category=electronics&sort=price&order=desc&page=1&size=20
```

### 6. Return Appropriate Status Codes
```
Success: 200, 201, 204
Client Error: 400, 401, 403, 404, 422
Server Error: 500
```

### 7. Provide Meaningful Error Responses
```json
{
    "status": "error",
    "code": "VALIDATION_ERROR",
    "message": "Validation failed",
    "errors": {
        "email": "Invalid email format",
        "price": "Price must be positive"
    },
    "timestamp": "2026-01-11T10:30:00Z"
}
```

---

## ✅ BLOCK 1 CHECKLIST
- [ ] I understand REST principles and HTTP methods
- [ ] I know common HTTP status codes and when to use them
- [ ] I can use @PathVariable, @RequestParam, @RequestBody
- [ ] I understand ResponseEntity and how to return different statuses
- [ ] I know why and how to use DTOs
- [ ] I can implement validation with @Valid
- [ ] I can create a global exception handler

---

# BLOCK 2: SPRING DATA JPA (2 hours)

## What is JPA?

**JPA** (Java Persistence API) is a specification for ORM (Object-Relational Mapping) in Java.
**Hibernate** is the most popular implementation of JPA.
**Spring Data JPA** makes it even easier by auto-generating repository implementations.

---

## Entity Annotations

### Basic Entity
```java
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")  // Optional: specify table name
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ProductStatus status;

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Product() {}

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    // Getters and Setters
    // ...
}

public enum ProductStatus {
    ACTIVE, INACTIVE, DISCONTINUED
}
```

### @GeneratedValue Strategies
```java
// AUTO - Hibernate picks the strategy
@GeneratedValue(strategy = GenerationType.AUTO)

// IDENTITY - Database auto-increment (MySQL, PostgreSQL SERIAL)
@GeneratedValue(strategy = GenerationType.IDENTITY)

// SEQUENCE - Database sequence (PostgreSQL, Oracle)
@GeneratedValue(strategy = GenerationType.SEQUENCE)

// TABLE - Uses a table to generate IDs (portable but slower)
@GeneratedValue(strategy = GenerationType.TABLE)
```

---

## Relationships

### @OneToMany and @ManyToOne
```java
// One Category has Many Products
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
    
    // Helper method to maintain bidirectional relationship
    public void addProduct(Product product) {
        products.add(product);
        product.setCategory(this);
    }
    
    public void removeProduct(Product product) {
        products.remove(product);
        product.setCategory(null);
    }
}

// Many Products belong to One Category
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
```

### @OneToOne
```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile profile;
}

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String bio;
    private String avatarUrl;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
```

### @ManyToMany
```java
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
}
```

---

## Fetch Types

### LAZY vs EAGER
```java
// LAZY - Load only when accessed (RECOMMENDED for collections)
@OneToMany(fetch = FetchType.LAZY)
private List<Product> products;

// EAGER - Load immediately with parent
@ManyToOne(fetch = FetchType.EAGER)
private Category category;
```

**Default Fetch Types:**
- @OneToMany: LAZY
- @ManyToMany: LAZY
- @ManyToOne: EAGER
- @OneToOne: EAGER

**Best Practice:** Use LAZY for everything, then use JOIN FETCH when needed.

---

## Repository Interface

### Basic Repository
```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository provides:
    // - save(entity)
    // - saveAll(entities)
    // - findById(id)
    // - findAll()
    // - findAllById(ids)
    // - count()
    // - deleteById(id)
    // - delete(entity)
    // - deleteAll()
    // - existsById(id)
}
```

### Query Methods (Spring Data generates implementation!)
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find by single field
    List<Product> findByName(String name);
    Optional<Product> findByNameIgnoreCase(String name);
    
    // Find by multiple fields
    List<Product> findByNameAndCategory(String name, Category category);
    List<Product> findByNameOrDescription(String name, String description);
    
    // Comparisons
    List<Product> findByPriceGreaterThan(BigDecimal price);
    List<Product> findByPriceLessThanEqual(BigDecimal price);
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
    
    // String matching
    List<Product> findByNameContaining(String keyword);
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByNameStartingWith(String prefix);
    List<Product> findByNameEndingWith(String suffix);
    
    // Null checks
    List<Product> findByDescriptionIsNull();
    List<Product> findByDescriptionIsNotNull();
    
    // Boolean
    List<Product> findByActiveTrue();
    List<Product> findByActiveFalse();
    
    // Ordering
    List<Product> findByNameOrderByPriceAsc(String name);
    List<Product> findByNameOrderByPriceDesc(String name);
    List<Product> findAllByOrderByCreatedAtDesc();
    
    // Limiting results
    List<Product> findTop5ByOrderByPriceDesc();
    Product findFirstByOrderByCreatedAtDesc();
    
    // Count
    long countByCategory(Category category);
    long countByPriceGreaterThan(BigDecimal price);
    
    // Exists
    boolean existsByName(String name);
    
    // Delete
    void deleteByName(String name);
    long deleteByCategory(Category category);
}
```

### Custom Queries with @Query
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // JPQL (Java Persistence Query Language)
    @Query("SELECT p FROM Product p WHERE p.price > :minPrice")
    List<Product> findExpensiveProducts(@Param("minPrice") BigDecimal minPrice);
    
    // JPQL with JOIN
    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id = :id")
    Optional<Product> findByIdWithCategory(@Param("id") Long id);
    
    // JPQL with multiple conditions
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND p.price BETWEEN :min AND :max")
    List<Product> searchProducts(
        @Param("keyword") String keyword,
        @Param("min") BigDecimal min,
        @Param("max") BigDecimal max
    );
    
    // Native SQL query
    @Query(value = "SELECT * FROM products WHERE stock_quantity < :threshold", nativeQuery = true)
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);
    
    // Update query
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.price = p.price * :multiplier WHERE p.category.id = :categoryId")
    int updatePricesByCategory(
        @Param("categoryId") Long categoryId,
        @Param("multiplier") BigDecimal multiplier
    );
    
    // Delete query
    @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.status = :status")
    int deleteByStatus(@Param("status") ProductStatus status);
}
```

---

## Pagination and Sorting

```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

// Repository
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(Category category, Pageable pageable);
    Page<Product> findByNameContaining(String keyword, Pageable pageable);
}

// Service
@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public Page<Product> getProducts(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
            
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(pageable);
    }
    
    public Page<Product> searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return productRepository.findByNameContaining(keyword, pageable);
    }
}

// Controller
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<Product> products = productService.getProducts(page, size, sortBy, direction);
        return ResponseEntity.ok(products);
    }
}
```

### Page Response Structure
```json
{
    "content": [
        { "id": 1, "name": "Product 1" },
        { "id": 2, "name": "Product 2" }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": { "sorted": true, "orderBy": "name" }
    },
    "totalElements": 100,
    "totalPages": 10,
    "first": true,
    "last": false,
    "numberOfElements": 10
}
```

---

## N+1 Problem and Solutions

### The Problem
```java
// This causes N+1 queries!
List<Product> products = productRepository.findAll();  // 1 query
for (Product p : products) {
    System.out.println(p.getCategory().getName());  // N additional queries!
}
```

### Solution 1: JOIN FETCH
```java
@Query("SELECT p FROM Product p JOIN FETCH p.category")
List<Product> findAllWithCategory();
```

### Solution 2: @EntityGraph
```java
@EntityGraph(attributePaths = {"category"})
List<Product> findAll();

// Or define named entity graph on entity
@Entity
@NamedEntityGraph(name = "Product.withCategory", attributeNodes = @NamedAttributeNode("category"))
public class Product { }

// Then use it
@EntityGraph(value = "Product.withCategory")
List<Product> findByPriceGreaterThan(BigDecimal price);
```

### Solution 3: Batch Fetching
```java
@OneToMany(mappedBy = "category")
@BatchSize(size = 20)  // Fetch in batches of 20
private List<Product> products;
```

---

## ✅ BLOCK 2 CHECKLIST
- [ ] I understand JPA entities and annotations (@Entity, @Id, @Column)
- [ ] I know relationship mappings (@OneToMany, @ManyToOne, @ManyToMany)
- [ ] I understand LAZY vs EAGER fetching
- [ ] I can create repositories with query methods
- [ ] I can write custom queries with @Query
- [ ] I understand pagination with Pageable and Page
- [ ] I know about N+1 problem and how to solve it

---

# BLOCK 3: REACT BASICS (2 hours)

## What is React?

React is a JavaScript library for building user interfaces.
- **Component-Based** - Build encapsulated components
- **Declarative** - Describe what UI should look like
- **Virtual DOM** - Efficient updates

---

## JSX (JavaScript XML)

JSX lets you write HTML-like syntax in JavaScript.

```jsx
// JSX
const element = <h1>Hello, World!</h1>;

// Gets compiled to:
const element = React.createElement('h1', null, 'Hello, World!');

// JSX with expressions
const name = "Alice";
const element = <h1>Hello, {name}!</h1>;

// JSX with attributes
const element = <img src={user.avatarUrl} alt="Avatar" />;

// JSX with className (not class!)
const element = <div className="container">Content</div>;

// JSX with style (object, not string!)
const element = <div style={{ color: 'red', fontSize: '16px' }}>Styled</div>;

// Conditional rendering
const element = <div>{isLoggedIn ? <LogoutButton /> : <LoginButton />}</div>;

// Rendering lists
const items = ['Apple', 'Banana', 'Cherry'];
const listItems = items.map((item, index) => 
    <li key={index}>{item}</li>
);
```

---

## Functional Components

```jsx
// Simple component
function Welcome(props) {
    return <h1>Hello, {props.name}!</h1>;
}

// Arrow function component
const Welcome = (props) => {
    return <h1>Hello, {props.name}!</h1>;
};

// With destructuring
const Welcome = ({ name, age }) => {
    return (
        <div>
            <h1>Hello, {name}!</h1>
            <p>You are {age} years old.</p>
        </div>
    );
};

// Using components
function App() {
    return (
        <div>
            <Welcome name="Alice" age={25} />
            <Welcome name="Bob" age={30} />
        </div>
    );
}
```

---

## useState Hook - State Management

```jsx
import { useState } from 'react';

function Counter() {
    // Declare state variable 'count' with initial value 0
    // setCount is the function to update it
    const [count, setCount] = useState(0);

    return (
        <div>
            <p>Count: {count}</p>
            <button onClick={() => setCount(count + 1)}>Increment</button>
            <button onClick={() => setCount(count - 1)}>Decrement</button>
            <button onClick={() => setCount(0)}>Reset</button>
        </div>
    );
}

// Multiple state variables
function Form() {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [age, setAge] = useState(0);

    return (
        <form>
            <input 
                value={name} 
                onChange={(e) => setName(e.target.value)} 
                placeholder="Name"
            />
            <input 
                value={email} 
                onChange={(e) => setEmail(e.target.value)} 
                placeholder="Email"
            />
            <input 
                type="number"
                value={age} 
                onChange={(e) => setAge(Number(e.target.value))} 
                placeholder="Age"
            />
        </form>
    );
}

// State with objects
function UserForm() {
    const [user, setUser] = useState({
        name: '',
        email: '',
        age: 0
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUser(prevUser => ({
            ...prevUser,  // Spread previous state
            [name]: value // Update specific field
        }));
    };

    return (
        <form>
            <input name="name" value={user.name} onChange={handleChange} />
            <input name="email" value={user.email} onChange={handleChange} />
            <input name="age" type="number" value={user.age} onChange={handleChange} />
        </form>
    );
}

// State with arrays
function TodoList() {
    const [todos, setTodos] = useState([]);
    const [inputValue, setInputValue] = useState('');

    const addTodo = () => {
        if (inputValue.trim()) {
            setTodos([...todos, { id: Date.now(), text: inputValue, completed: false }]);
            setInputValue('');
        }
    };

    const toggleTodo = (id) => {
        setTodos(todos.map(todo =>
            todo.id === id ? { ...todo, completed: !todo.completed } : todo
        ));
    };

    const deleteTodo = (id) => {
        setTodos(todos.filter(todo => todo.id !== id));
    };

    return (
        <div>
            <input 
                value={inputValue} 
                onChange={(e) => setInputValue(e.target.value)}
                onKeyPress={(e) => e.key === 'Enter' && addTodo()}
            />
            <button onClick={addTodo}>Add</button>
            <ul>
                {todos.map(todo => (
                    <li key={todo.id}>
                        <span 
                            style={{ textDecoration: todo.completed ? 'line-through' : 'none' }}
                            onClick={() => toggleTodo(todo.id)}
                        >
                            {todo.text}
                        </span>
                        <button onClick={() => deleteTodo(todo.id)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}
```

---

## useEffect Hook - Side Effects

useEffect runs after render. Use it for: API calls, subscriptions, DOM manipulation.

```jsx
import { useState, useEffect } from 'react';

// Basic useEffect - runs after every render
function Example() {
    const [count, setCount] = useState(0);

    useEffect(() => {
        document.title = `Count: ${count}`;
    });

    return <button onClick={() => setCount(count + 1)}>Click me</button>;
}

// Run only once (on mount) - empty dependency array
function DataFetcher() {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch('https://api.example.com/data')
            .then(response => response.json())
            .then(data => {
                setData(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error);
                setLoading(false);
            });
    }, []);  // Empty array = run only once

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;
    return <pre>{JSON.stringify(data, null, 2)}</pre>;
}

// Run when specific dependencies change
function UserProfile({ userId }) {
    const [user, setUser] = useState(null);

    useEffect(() => {
        fetch(`https://api.example.com/users/${userId}`)
            .then(response => response.json())
            .then(data => setUser(data));
    }, [userId]);  // Re-run when userId changes

    return user ? <h1>{user.name}</h1> : <p>Loading...</p>;
}

// Cleanup function - for subscriptions, timers, etc.
function Timer() {
    const [seconds, setSeconds] = useState(0);

    useEffect(() => {
        const interval = setInterval(() => {
            setSeconds(s => s + 1);
        }, 1000);

        // Cleanup function - runs before next effect and on unmount
        return () => clearInterval(interval);
    }, []);

    return <p>Seconds: {seconds}</p>;
}

// Multiple useEffects for different concerns
function UserDashboard({ userId }) {
    const [user, setUser] = useState(null);
    const [posts, setPosts] = useState([]);

    // Effect 1: Fetch user data
    useEffect(() => {
        fetchUser(userId).then(setUser);
    }, [userId]);

    // Effect 2: Fetch user's posts
    useEffect(() => {
        fetchPosts(userId).then(setPosts);
    }, [userId]);

    // Effect 3: Update document title
    useEffect(() => {
        if (user) {
            document.title = `${user.name}'s Dashboard`;
        }
    }, [user]);

    return (/* ... */);
}
```

---

## Async/Await with useEffect

```jsx
function ProductList() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Define async function inside useEffect
        const fetchProducts = async () => {
            try {
                setLoading(true);
                const response = await fetch('/api/products');
                
                if (!response.ok) {
                    throw new Error('Failed to fetch products');
                }
                
                const data = await response.json();
                setProducts(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchProducts();
    }, []);

    if (loading) return <div className="spinner">Loading...</div>;
    if (error) return <div className="error">Error: {error}</div>;
    
    return (
        <ul>
            {products.map(product => (
                <li key={product.id}>{product.name} - ${product.price}</li>
            ))}
        </ul>
    );
}
```

---

## Event Handling

```jsx
function EventExamples() {
    // Click event
    const handleClick = () => {
        console.log('Button clicked!');
    };

    // Click with event object
    const handleClickWithEvent = (e) => {
        console.log('Event:', e);
        e.preventDefault();  // Prevent default behavior
    };

    // Click with custom parameter
    const handleItemClick = (id) => {
        console.log('Item clicked:', id);
    };

    // Form submit
    const handleSubmit = (e) => {
        e.preventDefault();
        // Process form data
    };

    // Input change
    const [inputValue, setInputValue] = useState('');
    const handleInputChange = (e) => {
        setInputValue(e.target.value);
    };

    return (
        <div>
            <button onClick={handleClick}>Click Me</button>
            
            <button onClick={(e) => handleItemClick(123)}>
                Click Item 123
            </button>
            
            <form onSubmit={handleSubmit}>
                <input 
                    value={inputValue}
                    onChange={handleInputChange}
                    onFocus={() => console.log('Focused')}
                    onBlur={() => console.log('Blurred')}
                    onKeyDown={(e) => e.key === 'Enter' && handleSubmit(e)}
                />
                <button type="submit">Submit</button>
            </form>
        </div>
    );
}
```

---

## Conditional Rendering

```jsx
function ConditionalExamples({ isLoggedIn, user, items }) {
    return (
        <div>
            {/* If-else with ternary */}
            {isLoggedIn ? (
                <WelcomeMessage user={user} />
            ) : (
                <LoginPrompt />
            )}

            {/* Render only if true (&&) */}
            {isLoggedIn && <LogoutButton />}
            
            {/* Render only if false */}
            {!isLoggedIn && <LoginButton />}

            {/* Multiple conditions */}
            {user?.role === 'admin' && <AdminPanel />}
            {user?.role === 'user' && <UserDashboard />}

            {/* Null check */}
            {user && <p>Welcome, {user.name}!</p>}

            {/* Empty array check */}
            {items.length > 0 ? (
                <ItemList items={items} />
            ) : (
                <p>No items found</p>
            )}
        </div>
    );
}

// Using early return
function UserGreeting({ user }) {
    if (!user) {
        return <p>Please log in</p>;
    }

    if (user.role === 'admin') {
        return <AdminGreeting name={user.name} />;
    }

    return <UserGreeting name={user.name} />;
}
```

---

## Lists and Keys

```jsx
function ProductList({ products }) {
    return (
        <ul>
            {products.map(product => (
                // Key must be unique and stable
                <li key={product.id}>
                    <h3>{product.name}</h3>
                    <p>${product.price}</p>
                </li>
            ))}
        </ul>
    );
}

// Extracting to a component
function ProductItem({ product, onDelete }) {
    return (
        <li>
            <h3>{product.name}</h3>
            <p>${product.price}</p>
            <button onClick={() => onDelete(product.id)}>Delete</button>
        </li>
    );
}

function ProductList({ products, onDeleteProduct }) {
    return (
        <ul>
            {products.map(product => (
                <ProductItem 
                    key={product.id} 
                    product={product} 
                    onDelete={onDeleteProduct}
                />
            ))}
        </ul>
    );
}
```

---

## Props and Children

```jsx
// Basic props
function Greeting({ name, age }) {
    return <p>Hello, {name}! You are {age} years old.</p>;
}

// Default props
function Button({ label = "Click me", onClick, disabled = false }) {
    return (
        <button onClick={onClick} disabled={disabled}>
            {label}
        </button>
    );
}

// Children prop
function Card({ title, children }) {
    return (
        <div className="card">
            <h2>{title}</h2>
            <div className="card-content">
                {children}
            </div>
        </div>
    );
}

// Using Card with children
function App() {
    return (
        <Card title="User Profile">
            <p>Name: Alice</p>
            <p>Email: alice@example.com</p>
            <button>Edit Profile</button>
        </Card>
    );
}

// Passing functions as props
function Parent() {
    const handleSave = (data) => {
        console.log('Saving:', data);
    };

    return <ChildForm onSave={handleSave} />;
}

function ChildForm({ onSave }) {
    const [value, setValue] = useState('');
    
    const handleSubmit = () => {
        onSave(value);  // Call parent's function
    };

    return (
        <div>
            <input value={value} onChange={(e) => setValue(e.target.value)} />
            <button onClick={handleSubmit}>Save</button>
        </div>
    );
}
```

---

## Complete Example: Product CRUD

```jsx
import { useState, useEffect } from 'react';

const API_URL = 'http://localhost:8080/api/products';

function ProductApp() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editingProduct, setEditingProduct] = useState(null);

    // Fetch products on mount
    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        try {
            setLoading(true);
            const response = await fetch(API_URL);
            const data = await response.json();
            setProducts(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const createProduct = async (product) => {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(product)
        });
        const newProduct = await response.json();
        setProducts([...products, newProduct]);
    };

    const updateProduct = async (id, product) => {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(product)
        });
        const updatedProduct = await response.json();
        setProducts(products.map(p => p.id === id ? updatedProduct : p));
        setEditingProduct(null);
    };

    const deleteProduct = async (id) => {
        await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        setProducts(products.filter(p => p.id !== id));
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div>
            <h1>Products</h1>
            <ProductForm 
                onSubmit={editingProduct ? 
                    (p) => updateProduct(editingProduct.id, p) : 
                    createProduct
                }
                initialData={editingProduct}
                onCancel={() => setEditingProduct(null)}
            />
            <ProductList 
                products={products}
                onEdit={setEditingProduct}
                onDelete={deleteProduct}
            />
        </div>
    );
}

function ProductForm({ onSubmit, initialData, onCancel }) {
    const [name, setName] = useState(initialData?.name || '');
    const [price, setPrice] = useState(initialData?.price || '');

    useEffect(() => {
        if (initialData) {
            setName(initialData.name);
            setPrice(initialData.price);
        }
    }, [initialData]);

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit({ name, price: parseFloat(price) });
        setName('');
        setPrice('');
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                placeholder="Name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
            />
            <input
                type="number"
                placeholder="Price"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
                required
            />
            <button type="submit">{initialData ? 'Update' : 'Create'}</button>
            {initialData && <button type="button" onClick={onCancel}>Cancel</button>}
        </form>
    );
}

function ProductList({ products, onEdit, onDelete }) {
    return (
        <ul>
            {products.map(product => (
                <li key={product.id}>
                    <span>{product.name} - ${product.price}</span>
                    <button onClick={() => onEdit(product)}>Edit</button>
                    <button onClick={() => onDelete(product.id)}>Delete</button>
                </li>
            ))}
        </ul>
    );
}

export default ProductApp;
```
# Redux Complete Guide 🎯

> State Management Made Simple — Explained Like You're 5

---

## Table of Contents

1. [What Problem Does Redux Solve?](#what-problem-does-redux-solve)
2. [The Three Core Concepts](#the-three-core-concepts)
3. [The Redux Data Flow](#the-redux-data-flow)
4. [What is `action.payload`?](#what-is-actionpayload)
5. [What is a Slice?](#what-is-a-slice)
6. [useSelector and useDispatch](#useselector-and-usedispatch)
7. [How to Send Actions to the Store](#how-to-send-actions-to-the-store)
8. [Complete Working Example](#complete-working-example)
9. [Redux vs React Context](#redux-vs-react-context)
10. [Quick Reference Table](#quick-reference-table)
11. [When to Use Redux](#when-to-use-redux)

---

## What Problem Does Redux Solve?

Imagine you have a **toy box** (your app's data). In a small house with one room, finding toys is easy. But in a **big house with 20 rooms**, if toys are scattered everywhere, it's chaos!

Redux is like having **ONE special toy box in the living room** that everyone in the house knows about and can access.

---

## The Three Core Concepts

### 1. Store — The Single Toy Box

```
┌─────────────────────────────┐
│         STORE               │
│   (One source of truth)     │
│                             │
│   { count: 0, user: null }  │
└─────────────────────────────┘
```

- There's only **ONE** store in your whole app
- It holds **ALL** your app's state (data)

---

### 2. Actions — Notes That Say What Happened

Actions are like little **notes** you pass to the toy box manager saying "Hey, this happened!"

```javascript
// An action is just a plain object with a "type"
{ type: 'INCREMENT' }
{ type: 'ADD_TODO', payload: 'Buy milk' }
{ type: 'LOGIN', payload: { name: 'Andres' } }
```

**Rule:** Actions only DESCRIBE what happened. They don't change anything themselves.

---

### 3. Reducers — The Rule Book

A reducer is like a **manager with a rule book** that says:

> "When I receive THIS note, I change the toy box THIS way"

```javascript
// reducer = (currentState, action) => newState

function counterReducer(state = 0, action) {
  switch (action.type) {
    case 'INCREMENT':
      return state + 1;      // Rule: INCREMENT means add 1
    case 'DECREMENT':
      return state - 1;      // Rule: DECREMENT means subtract 1
    default:
      return state;          // Unknown note? Change nothing
  }
}
```

**Golden Rule:** Reducers are PURE functions — they never modify the old state, they return a NEW state.

---

## The Redux Data Flow

```
   User clicks button
          │
          ▼
   ┌─────────────┐
   │   ACTION    │  ← "Hey, INCREMENT happened!"
   │ {type: ...} │
   └──────┬──────┘
          │
          ▼
   ┌─────────────┐
   │   REDUCER   │  ← Checks rule book, creates NEW state
   │  (state,    │
   │   action)   │
   └──────┬──────┘
          │
          ▼
   ┌─────────────┐
   │    STORE    │  ← Updates with new state
   │  { count }  │
   └──────┬──────┘
          │
          ▼
   UI Re-renders with new data
```

---

## What is `action.payload`?

It's just a **convention** (a naming agreement), not a requirement.

```javascript
// The action object has two parts:
{
  type: 'ADD_TODO',      // WHAT happened (required)
  payload: 'Buy milk'    // THE DATA you're sending (optional, any name works)
}
```

You could call it anything:

```javascript
{ type: 'ADD_TODO', data: 'Buy milk' }      // works
{ type: 'ADD_TODO', value: 'Buy milk' }     // works
{ type: 'ADD_TODO', banana: 'Buy milk' }    // works (but confusing!)
```

**Why `payload`?** It's the standard name everyone agrees on. Redux Toolkit uses `payload` automatically, so stick with it.

### Example with `push(action.payload)`

It's just **adding an item to an array** — basic JavaScript!

```javascript
// Regular JavaScript - you know this:
const fruits = ['apple', 'banana'];
fruits.push('orange');
// Now fruits = ['apple', 'banana', 'orange']
```

In Redux Toolkit:

```javascript
const todoSlice = createSlice({
  name: 'todos',
  initialState: [],        // Start with empty array: []
  reducers: {
    addTodo: (state, action) => {
      // state = the current array, like ['Buy milk']
      // action.payload = the new item you're adding, like 'Learn Redux'
      
      state.push(action.payload);
      
      // Now state = ['Buy milk', 'Learn Redux']
    }
  }
});

// When you call:
dispatch(addTodo('Learn Redux'));
//                 ↑
//                 This becomes action.payload
```

**So `state.push(action.payload)` just means:** "Add the new item to the array."

---

## What is a Slice?

A **slice** is a "piece" of your store that handles ONE feature.

Think of your store as a **pizza** 🍕:
- The whole pizza = your entire Redux store
- One slice = one feature (users, todos, cart, etc.)

```javascript
// WITHOUT slices (old way) - you write 3 separate things:
const ADD_TODO = 'ADD_TODO';                                    // 1. Action type
const addTodo = (text) => ({ type: ADD_TODO, payload: text });  // 2. Action creator
const todoReducer = (state, action) => {...}                    // 3. Reducer

// WITH createSlice (new way) - ONE object does all 3:
const todoSlice = createSlice({
  name: 'todos',
  initialState: [],
  reducers: {
    addTodo: (state, action) => {
      state.push(action.payload);  // This auto-generates the action too!
    }
  }
});

// It auto-creates:
// - todoSlice.actions.addTodo  → the action creator
// - todoSlice.reducer          → the reducer
```

**In short:** A slice = action types + action creators + reducer, all bundled together for one feature.

---

## useSelector and useDispatch

### useSelector — READ from the Store

It's a React hook to **READ** data from the Redux store.

```javascript
// Think of it as asking: "Hey store, give me THIS specific piece of data"

import { useSelector } from 'react-redux';

function MyComponent() {
  // "Go into the store, find state.counter.value, and give it to me"
  const count = useSelector((state) => state.counter.value);
  
  // Now you can use it
  return <p>Count: {count}</p>;
}
```

The function you pass `(state) => state.counter.value` is called a **selector** — it "selects" what you want from the big state object.

```javascript
// Your store might look like:
{
  counter: { value: 5 },
  user: { name: 'Andres', loggedIn: true },
  todos: ['Learn Redux', 'Build app']
}

// Different selectors grab different pieces:
useSelector((state) => state.counter.value)     // → 5
useSelector((state) => state.user.name)         // → 'Andres'
useSelector((state) => state.todos)             // → ['Learn Redux', 'Build app']
```

---

### useDispatch — WRITE to the Store

It's a React hook to **SEND** actions to the store.

```javascript
// Think of it as: "Hey store, HERE'S something that happened!"

import { useDispatch } from 'react-redux';
import { increment, decrement } from './counterSlice';

function MyComponent() {
  const dispatch = useDispatch();  // Get the "sender" function
  
  return (
    <div>
      {/* When clicked, SEND the increment action to the store */}
      <button onClick={() => dispatch(increment())}>+</button>
      <button onClick={() => dispatch(decrement())}>-</button>
    </div>
  );
}
```

### Summary Table

| Hook | Purpose | Direction |
|------|---------|-----------|
| `useSelector` | READ from store | Store → Component |
| `useDispatch` | WRITE to store | Component → Store |

---

## How to Send Actions to the Store

Three simple steps — think of it like **sending a letter** ✉️:

### Step 1: Get the "mailbox" (useDispatch)

```javascript
import { useDispatch } from 'react-redux';

function MyComponent() {
  const dispatch = useDispatch();  // ← This is your mailbox
```

### Step 2: Import your "letter template" (the action)

```javascript
import { increment } from './counterSlice';  // ← The action you want to send
```

### Step 3: Send it! (dispatch the action)

```javascript
  return (
    <button onClick={() => dispatch(increment())}>
      Click me
    </button>
  );
}
//                         ↑
//            dispatch( action ) = SEND the action to the store
```

### Visual Flow

```
   You click button
         │
         ▼
   dispatch(addAmount(5))
         │
         │  ┌─────────────────────────────┐
         └─►│  ACTION CREATED:            │
            │  {                          │
            │    type: 'counter/addAmount',
            │    payload: 5    ◄───────────── The number you passed
            │  }                          │
            └───────────┬─────────────────┘
                        │
                        ▼
            ┌───────────────────────────┐
            │  REDUCER RUNS:            │
            │                           │
            │  state.value += action.payload
            │  state.value += 5         │
            │                           │
            └───────────┬───────────────┘
                        │
                        ▼
            ┌───────────────────────────┐
            │  STORE UPDATES:           │
            │  { value: 5 }             │
            └───────────┬───────────────┘
                        │
                        ▼
            Component re-renders with new value!
```

---

## Complete Working Example

### File 1: `store.js`

```javascript
import { configureStore } from '@reduxjs/toolkit';
import counterReducer from './counterSlice';

export const store = configureStore({
  reducer: {
    counter: counterReducer,
  },
});
```

### File 2: `counterSlice.js`

```javascript
import { createSlice } from '@reduxjs/toolkit';

const counterSlice = createSlice({
  name: 'counter',
  initialState: { value: 0 },
  reducers: {
    increment: (state) => {
      state.value += 1;
    },
    decrement: (state) => {
      state.value -= 1;
    },
    addAmount: (state, action) => {
      state.value += action.payload;  // payload = the number you send
    }
  }
});

export const { increment, decrement, addAmount } = counterSlice.actions;
export default counterSlice.reducer;
```

### File 3: `Counter.jsx`

```javascript
import { useSelector, useDispatch } from 'react-redux';
import { increment, decrement, addAmount } from './counterSlice';

function Counter() {
  // READ from store
  const count = useSelector((state) => state.counter.value);
  
  // GET the sender function
  const dispatch = useDispatch();

  return (
    <div>
      <p>Count: {count}</p>
      
      {/* SEND actions to store */}
      <button onClick={() => dispatch(increment())}>
        +1
      </button>
      
      <button onClick={() => dispatch(decrement())}>
        -1
      </button>
      
      <button onClick={() => dispatch(addAmount(5))}>
        +5
      </button>
      {/*                              ↑
                            This 5 becomes action.payload */}
    </div>
  );
}

export default Counter;
```

### File 4: `App.jsx`

```javascript
import { Provider } from 'react-redux';
import { store } from './store';
import Counter from './Counter';

function App() {
  return (
    <Provider store={store}>
      <Counter />
    </Provider>
  );
}

export default App;
```

---

## Redux vs React Context

**Neither is "better"** — they solve different problems.

### React Context

| Pros ✅ | Cons ❌ |
|---------|---------|
| Built into React (no extra library) | Re-renders ALL consumers when ANY value changes |
| Simple to set up | No built-in debugging tools |
| Great for: themes, auth, language, small apps | Gets messy with complex state logic |

### Redux

| Pros ✅ | Cons ❌ |
|---------|---------|
| Only re-renders components that use CHANGED data | More boilerplate (though Toolkit helps) |
| Amazing DevTools (time travel, action log) | Extra dependency to install |
| Predictable (strict rules = fewer bugs) | Overkill for simple apps |
| Great for: large apps, complex state, teams | |

### The Performance Difference (Key!)

```javascript
// CONTEXT: If user.name changes, EVERYTHING re-renders
<UserContext.Provider value={{ name, email, settings, preferences }}>
  <App />  {/* ALL children re-render, even if they only use "name" */}
</UserContext.Provider>

// REDUX: Only components using the changed value re-render
const name = useSelector(state => state.user.name);  
// ↑ This component ONLY re-renders if "name" changes, not email/settings
```

### Quick Decision Guide

| Scenario | Use |
|----------|-----|
| Theme switching (dark/light) | Context |
| User authentication status | Context |
| Shopping cart with many operations | Redux |
| App with 20+ components sharing state | Redux |
| Dashboard with real-time updates | Redux |
| Small app with 5 components | Context |
| Need time-travel debugging | Redux |

---

## Quick Reference Table

| Concept | What It Is | Analogy |
|---------|-----------|---------|
| **Store** | Single object holding all state | The one toy box |
| **Action** | Plain object describing an event | A note saying what happened |
| **Reducer** | Pure function that returns new state | Manager with rule book |
| **Dispatch** | Method to send actions to store | Handing the note to the manager |
| **Selector** | Function to read specific state | Asking "how many red toys?" |
| **Slice** | Bundle of actions + reducer for one feature | One pizza slice = one feature |
| **Payload** | The data inside an action | The details of your order |

---

## When to Use Redux

### ✅ Use Redux when:

- Multiple components need the same data
- State changes frequently
- App is medium/large size
- You want predictable state management
- You need debugging tools (time travel)

### ❌ Don't need Redux when:

- Small app with simple state
- Data doesn't need to be shared widely
- React Context + useState is enough

---

## Pizza Analogy 🍕

Think of ordering pizza:

| Redux Term | Pizza Analogy |
|------------|---------------|
| `dispatch` | Calling the pizza place |
| `action` | Your order ("I want pepperoni") |
| `action.payload` | The details ("large size, extra cheese") |
| `reducer` | The chef who makes the pizza |
| `store` | The pizza place's kitchen |
| `useSelector` | Checking "Is my pizza ready?" |

```javascript
// You're calling (dispatch) to place an order (action)
dispatch(orderPizza('pepperoni'));
//                      ↑
//                  This is the payload (what kind of pizza)
```

---

## Key Principles to Remember

1. **Single source of truth:** One store for the entire app
2. **State is read-only:** Only way to change state is by dispatching actions
3. **Pure reducers:** Reducers must be pure functions (same input = same output)
4. **Immutability:** Never mutate state directly; always return new objects

---

*Happy coding! 🚀*
---

## ✅ BLOCK 3 CHECKLIST
- [ ] I understand JSX syntax
- [ ] I can create functional components with props
- [ ] I can use useState for state management
- [ ] I can use useEffect for side effects and API calls
- [ ] I understand event handling in React
- [ ] I can do conditional rendering
- [ ] I can render lists with keys
- [ ] I can build a complete CRUD application

---

# BLOCK 4: SYSTEM DESIGN INTRO (1 hour)

## Microservices vs Monolith

### Monolithic Architecture
```
┌─────────────────────────────────────┐
│           MONOLITH APP              │
│  ┌─────────┬─────────┬─────────┐   │
│  │  User   │  Order  │ Payment │   │
│  │ Module  │ Module  │ Module  │   │
│  └─────────┴─────────┴─────────┘   │
│           Single Database           │
└─────────────────────────────────────┘
```

**Pros:**
- Simple to develop and deploy
- Easy debugging
- No network latency between modules

**Cons:**
- Hard to scale individual components
- Single point of failure
- Technology lock-in
- Large codebase becomes unwieldy

### Microservices Architecture
```
┌──────────┐   ┌──────────┐   ┌──────────┐
│   User   │   │  Order   │   │ Payment  │
│ Service  │   │ Service  │   │ Service  │
│   (DB)   │   │   (DB)   │   │   (DB)   │
└────┬─────┘   └────┬─────┘   └────┬─────┘
     │              │              │
     └──────────────┼──────────────┘
                    │
              API Gateway
```

**Pros:**
- Independent scaling
- Technology flexibility
- Fault isolation
- Easier to understand individual services

**Cons:**
- Network complexity
- Data consistency challenges
- Operational overhead
- Debugging distributed systems is harder

---

## REST API Design Principles

1. **Resource-Based URLs:** `/users`, `/orders`, `/products`
2. **HTTP Methods for Actions:** GET, POST, PUT, DELETE
3. **Stateless:** Each request contains all needed information
4. **Consistent Response Format:** Always return JSON with consistent structure

---

## ✅ BLOCK 4 CHECKLIST
- [ ] I can explain monolith vs microservices
- [ ] I know pros and cons of each approach
- [ ] I understand REST API design principles

---

# DAY 4 - MONDAY JANUARY 12, 2026
## SPRING SECURITY + JWT + UNIT TESTING

---

# BLOCK 1: SPRING SECURITY BASICS (1.5 hours)

## What is Spring Security?

Spring Security is a framework that provides:
- **Authentication** - Who are you? (login)
- **Authorization** - What can you do? (permissions)
- **Protection** against common attacks (CSRF, session fixation, etc.)

---

## How Spring Security Works

```
HTTP Request
     │
     ▼
┌─────────────────────────────────────┐
│        Security Filter Chain         │
│  ┌─────────────────────────────────┐│
│  │ 1. CorsFilter                   ││
│  │ 2. CsrfFilter                   ││
│  │ 3. UsernamePasswordAuthFilter   ││
│  │ 4. BasicAuthenticationFilter    ││
│  │ 5. AuthorizationFilter          ││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
     │
     ▼
  Controller
```

---

## Basic Configuration

### Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### Security Configuration (Spring Boot 3.x)
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST APIs
            .csrf(csrf -> csrf.disable())
            
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                
                // Admin only endpoints
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // User endpoints
                .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            
            // Use HTTP Basic authentication (for simplicity)
            .httpBasic(basic -> {});
            
        return http.build();
    }
}
```

---

## User Details and Authentication

### UserDetailsService
```java
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())  // Must be encoded!
            .roles(user.getRoles().toArray(new String[0]))
            .build();
    }
}
```

### Password Encoding
```java
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

// Using the encoder
@Service
public class UserService {
    
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User createUser(String username, String rawPassword) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));  // Encode!
        return userRepository.save(user);
    }
}
```

---

## Authentication vs Authorization

```java
@RestController
@RequestMapping("/api")
public class SecuredController {

    // Anyone can access (authentication not required)
    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello, Public!";
    }

    // Must be authenticated (any logged in user)
    @GetMapping("/user/profile")
    public String userProfile(Authentication authentication) {
        return "Hello, " + authentication.getName();
    }

    // Must have ADMIN role
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "Admin Dashboard";
    }

    // Must have specific authority
    @GetMapping("/reports")
    @PreAuthorize("hasAuthority('VIEW_REPORTS')")
    public String viewReports() {
        return "Reports";
    }

    // Complex authorization
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public void deleteUser(@PathVariable Long id) {
        // Admin can delete anyone, users can only delete themselves
    }
}
```

---

## ✅ BLOCK 1 CHECKLIST
- [ ] I understand what Spring Security does
- [ ] I know the difference between authentication and authorization
- [ ] I can configure SecurityFilterChain
- [ ] I understand UserDetailsService
- [ ] I know how to encode passwords with BCrypt
- [ ] I can use @PreAuthorize for method-level security

---

# BLOCK 2: JWT AUTHENTICATION (1.5 hours)

## What is JWT?

JWT (JSON Web Token) is a compact, URL-safe token format for securely transmitting information.

### JWT Structure
```
xxxxx.yyyyy.zzzzz
  │      │     │
  │      │     └── Signature
  │      └── Payload (claims)
  └── Header
```

### Example JWT
```
Header (Base64):
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload (Base64):
{
  "sub": "user123",
  "name": "John Doe",
  "roles": ["USER", "ADMIN"],
  "iat": 1516239022,
  "exp": 1516242622
}

Signature:
HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret
)
```

---

## JWT Implementation

### Dependencies
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
```

### JWT Utility Class
```java
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;  // in milliseconds

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey())
            .compact();
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic claim extractor
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    // Check if token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
```

### JWT Filter
```java
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        
        // Get Authorization header
        final String authHeader = request.getHeader("Authorization");
        
        String username = null;
        String jwt = null;

        // Extract JWT from "Bearer <token>"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Invalid token - continue without authentication
            }
        }

        // If we have a username and no authentication exists yet
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate token
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Set authentication in context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

### Updated Security Configuration
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                         UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) 
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### Auth Controller
```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    // Constructor...

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Generate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(user));
    }
}

// DTOs
record LoginRequest(String username, String password) {}
record AuthResponse(String token) {}
record RegisterRequest(
    @NotBlank String username,
    @Email String email,
    @Size(min = 8) String password
) {}
```

---

## ✅ REAL PROJECT FLOW (THIS REPO): JWT IN A HttpOnly COOKIE

In this repo we implemented **JWT auth using an HttpOnly cookie** instead of returning a token to the frontend and storing it in `localStorage`.

### Why “HttpOnly cookie” changes the mental model

- With **`Authorization: Bearer <token>`**, the frontend code “holds” the token and attaches it to requests.
- With an **HttpOnly cookie**, the browser holds the token and automatically attaches it to requests.

That means:

- The frontend **does NOT read the JWT** (JavaScript cannot access HttpOnly cookies).
- The frontend **does NOT manually set** `Authorization: Bearer ...`.
- The frontend must send requests with **cookies enabled**.

### JWT storage options in browsers (tradeoffs)

| Where the token lives | Good | Risk / downside |
|---|---|---|
| `localStorage` | Simple | High XSS impact (token can be stolen by JS) |
| `sessionStorage` | Clears on tab close | Still readable by JS → still XSS risk |
| In-memory (JS variable) | Best XSS posture among JS options | Refresh loses auth; needs refresh-token strategy |
| HttpOnly cookie | JS cannot read token (strong vs XSS theft) | Needs CSRF strategy for browser requests |

### Backend endpoints (current behavior)

- `POST /api/auth/login`
    - Validates username/password
    - Generates JWT
    - Responds with:
        - `Set-Cookie: access_token=<JWT>; HttpOnly; Path=/; SameSite=Lax; Max-Age=...`
- `GET /api/auth/me`
    - Returns current user info **if** the cookie/JWT is valid
- `POST /api/auth/logout`
    - Clears the cookie by sending `Set-Cookie: access_token=; Max-Age=0; ...`

### Browser flow (what actually happens)

1) **Login**
     - Frontend sends `POST /api/auth/login`
     - Server replies with `Set-Cookie: access_token=...`
     - Browser stores the cookie

2) **Later requests**
     - Browser automatically sends:
         - `Cookie: access_token=...`
     - The backend reads the cookie and authenticates the request

### Frontend requirement (React/Vue)

When calling the API from fetch/axios, ensure cookies are included:

```js
fetch('/api/products', {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name: 'Keyboard', description: 'Mechanical', price: 99.9 }),
})
```

If you forget `credentials: 'include'` (especially in cross-origin setups), the cookie won’t be sent and you’ll see `401 Unauthorized`.

### Filter behavior (cookie-first + Bearer fallback)

Our JWT filter resolves the token from:

1) `Cookie: access_token=...` (primary)
2) `Authorization: Bearer ...` (fallback, useful for Postman/mobile clients)

---

## CSRF (Cross-Site Request Forgery) — WHY IT MATTERS WITH COOKIES

**CSRF** is when a malicious site tricks a user’s browser into sending a request to your API **while the user is logged in**.

Key point:

- Cookies are **automatically attached** by the browser.
- So cookie-based auth needs a CSRF story for **state-changing** endpoints (POST/PUT/PATCH/DELETE).

Important note:

- Many REST tutorials do `.csrf(csrf -> csrf.disable())` because they assume **non-browser** clients (mobile/CLI/Postman) that send tokens in the `Authorization` header.
- For a **browser app using cookies for auth**, disabling CSRF is usually not what you want (unless you rely on a very strict same-site model and fully understand the tradeoffs).

### Common mitigations (from simple → stronger)

1) **SameSite cookies** (you’re using `SameSite=Lax`)
     - Blocks many cross-site requests
     - Not a full solution for every scenario

2) **CSRF token** (recommended for “cookie auth”)
     - Example pattern: *double-submit cookie*
         - Backend sets `XSRF-TOKEN` cookie (NOT HttpOnly)
         - Frontend echoes it in a header: `X-XSRF-TOKEN`
         - Backend verifies header token matches cookie token

3) **BFF pattern** (often simplest operationally)
     - Browser talks only to BFF (same-origin)
     - BFF holds tokens server-side
     - CSRF becomes clearer and CORS disappears for the browser

---

## BFF (Backend For Frontend) — WHAT IT IS

A **BFF** is a backend made for a specific frontend.

Instead of:

```
Browser  -->  Spring API
```

You do:

```
Browser  -->  BFF  -->  Spring API
```

### Why BFF is “more advanced” for SPAs

- The browser only has a **BFF session cookie** (HttpOnly)
- Access/refresh tokens (OIDC) can be stored **server-side**
- Browser never needs to understand or store tokens
- CORS is simpler (browser only calls one origin)

---

## OIDC (OpenID Connect) — WHAT IT ADDS ON TOP OF JWT

Important distinction:

- **JWT** is a *token format* (how the token looks).
- **OIDC** is a *standard login protocol* (how users log in and how tokens are issued/validated).

With OIDC you typically use an external Identity Provider (IdP) such as Auth0/Okta/Azure AD/Keycloak.

### Typical “best practice” evolution for real products

- Use an **external OIDC provider** (IdP) instead of rolling your own username/password auth
- Use **short-lived access tokens** (minutes)
- Use **refresh token rotation** (detect replay)
- Choose either:
    - **SPA direct** (OIDC + PKCE in the browser) → more frontend complexity
    - **OIDC + BFF** (recommended for “best-of-the-best” browser security) → browser has only a session cookie


---

## ✅ BLOCK 2 CHECKLIST
- [ ] I understand JWT structure (header, payload, signature)
- [ ] I can create a JWT utility class
- [ ] I can implement a JWT filter
- [ ] I can configure stateless session management
- [ ] I can create login and register endpoints

---

# BLOCK 3: UNIT TESTING (2 hours)

## JUnit 5 Basics

### Test Annotations
```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private Calculator calculator;

    @BeforeAll  // Run once before all tests
    static void setupAll() {
        System.out.println("Starting tests...");
    }

    @BeforeEach  // Run before each test
    void setup() {
        calculator = new Calculator();
    }

    @AfterEach  // Run after each test
    void teardown() {
        // Cleanup
    }

    @AfterAll  // Run once after all tests
    static void teardownAll() {
        System.out.println("All tests completed.");
    }

    @Test
    @DisplayName("Should add two positive numbers")
    void testAddPositive() {
        assertEquals(5, calculator.add(2, 3));
    }

    @Test
    @DisplayName("Should subtract two numbers")
    void testSubtract() {
        assertEquals(1, calculator.subtract(3, 2));
    }

    @Test
    @Disabled("Not implemented yet")
    void testDivide() {
        // TODO: Implement
    }
}
```

### Assertions
```java
import static org.junit.jupiter.api.Assertions.*;

@Test
void testAssertions() {
    // Basic assertions
    assertEquals(4, 2 + 2);
    assertNotEquals(5, 2 + 2);
    assertTrue(5 > 3);
    assertFalse(3 > 5);
    assertNull(null);
    assertNotNull("Hello");

    // Object equality
    assertSame(obj1, obj1);  // Same reference
    assertNotSame(obj1, obj2);

    // Array equality
    assertArrayEquals(new int[]{1, 2, 3}, new int[]{1, 2, 3});

    // Exception testing
    assertThrows(IllegalArgumentException.class, () -> {
        calculator.divide(10, 0);
    });

    // With custom message
    assertEquals(4, 2 + 2, "Math is broken!");

    // Multiple assertions (all run even if one fails)
    assertAll(
        () -> assertEquals(4, 2 + 2),
        () -> assertTrue(5 > 3),
        () -> assertNotNull("Hello")
    );

    // Timeout
    assertTimeout(Duration.ofSeconds(1), () -> {
        // Code that should complete within 1 second
    });
}
```

---

## Mockito - Mocking Dependencies

### Basic Mocking
```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock  // Create mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks  // Inject mocks into this
    private UserService userService;

    @Test
    void shouldFindUserById() {
        // Arrange - setup mock behavior
        User mockUser = new User(1L, "alice", "alice@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act - call method under test
        Optional<User> result = userService.findById(1L);

        // Assert - verify results
        assertTrue(result.isPresent());
        assertEquals("alice", result.get().getUsername());
        
        // Verify mock was called
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateUser() {
        // Arrange
        User newUser = new User(null, "bob", "bob@example.com");
        User savedUser = new User(1L, "bob", "bob@example.com");
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        doNothing().when(emailService).sendWelcomeEmail(anyString());

        // Act
        User result = userService.createUser(newUser);

        // Assert
        assertNotNull(result.getId());
        assertEquals("bob", result.getUsername());
        
        // Verify interactions
        verify(userRepository).save(any(User.class));
        verify(emailService).sendWelcomeEmail("bob@example.com");
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            userService.getById(999L);
        });
    }
}
```

### Mockito Methods
```java
// Stubbing return values
when(mock.method()).thenReturn(value);
when(mock.method(any())).thenReturn(value);
when(mock.method(eq("specific"))).thenReturn(value);

// Stubbing void methods
doNothing().when(mock).voidMethod();
doThrow(new RuntimeException()).when(mock).voidMethod();

// Argument matchers
any()            // Any object
any(User.class)  // Any User
anyLong()        // Any long
anyString()      // Any string
eq("exact")      // Exact match
argThat(arg -> arg.length() > 5)  // Custom matcher

// Verification
verify(mock).method();                    // Called once
verify(mock, times(2)).method();          // Called exactly 2 times
verify(mock, never()).method();           // Never called
verify(mock, atLeastOnce()).method();     // Called at least once
verify(mock, atMost(3)).method();         // Called at most 3 times
verifyNoMoreInteractions(mock);           // No other methods called
```

---

## Testing Spring Boot Controllers

### @WebMvcTest - Controller Layer Testing
```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void shouldGetAllProducts() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(
            new Product(1L, "Product 1", 10.0),
            new Product(2L, "Product 2", 20.0)
        );
        when(productService.findAll()).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("Product 1"))
            .andExpect(jsonPath("$[1].price").value(20.0));
    }

    @Test
    void shouldGetProductById() throws Exception {
        // Arrange
        Product product = new Product(1L, "Test Product", 99.99);
        when(productService.findById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        mockMvc.perform(get("/api/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Test Product"))
            .andExpect(jsonPath("$.price").value(99.99));
    }

    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {
        // Arrange
        when(productService.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/products/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateProduct() throws Exception {
        // Arrange
        Product product = new Product(1L, "New Product", 50.0);
        when(productService.save(any(Product.class))).thenReturn(product);

        String jsonBody = """
            {
                "name": "New Product",
                "price": 50.0
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("New Product"));

        verify(productService).save(any(Product.class));
    }

    @Test
    void shouldValidateProductOnCreate() throws Exception {
        String invalidJson = """
            {
                "name": "",
                "price": -10.0
            }
            """;

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        // Arrange
        when(productService.existsById(1L)).thenReturn(true);
        doNothing().when(productService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/products/1"))
            .andExpect(status().isNoContent());

        verify(productService).deleteById(1L);
    }
}
```

---

## Testing Spring Data JPA Repositories

### @DataJpaTest
```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldFindProductByName() {
        // Arrange
        Product product = new Product("Test Product", 99.99);
        entityManager.persistAndFlush(product);

        // Act
        List<Product> found = productRepository.findByName("Test Product");

        // Assert
        assertEquals(1, found.size());
        assertEquals("Test Product", found.get(0).getName());
    }

    @Test
    void shouldFindProductsByPriceRange() {
        // Arrange
        entityManager.persist(new Product("Cheap", 10.0));
        entityManager.persist(new Product("Medium", 50.0));
        entityManager.persist(new Product("Expensive", 100.0));
        entityManager.flush();

        // Act
        List<Product> found = productRepository.findByPriceBetween(20.0, 80.0);

        // Assert
        assertEquals(1, found.size());
        assertEquals("Medium", found.get(0).getName());
    }

    @Test
    void shouldSaveProduct() {
        // Arrange
        Product product = new Product("New Product", 25.0);

        // Act
        Product saved = productRepository.save(product);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("New Product", saved.getName());
    }
}
```

---

## Test Best Practices

### 1. Follow AAA Pattern
```java
@Test
void testMethod() {
    // Arrange - setup test data and mocks
    
    // Act - call method under test
    
    // Assert - verify results
}
```

### 2. One Assertion Per Concept
```java
// BAD - testing multiple things
@Test
void testUser() {
    User user = userService.create("alice", "alice@example.com");
    assertEquals("alice", user.getUsername());
    assertEquals("alice@example.com", user.getEmail());
    assertNotNull(user.getCreatedAt());
    assertTrue(user.isActive());
}

// GOOD - separate tests
@Test
void shouldCreateUserWithUsername() { ... }

@Test
void shouldCreateUserWithEmail() { ... }

@Test
void shouldSetCreatedAtOnCreate() { ... }

@Test
void shouldBeActiveByDefault() { ... }
```

### 3. Use Descriptive Test Names
```java
// BAD
@Test
void test1() { }

// GOOD
@Test
@DisplayName("Should return empty Optional when user not found")
void shouldReturnEmptyWhenUserNotFound() { }
```

### 4. Test Edge Cases
```java
@Test
void shouldHandleEmptyList() { }

@Test
void shouldHandleNullInput() { }

@Test
void shouldHandleMaxValue() { }

@Test
void shouldThrowOnInvalidInput() { }
```

---

## ✅ BLOCK 3 CHECKLIST
- [ ] I can write JUnit 5 tests with @Test, @BeforeEach, etc.
- [ ] I know common assertions (assertEquals, assertTrue, assertThrows)
- [ ] I can use Mockito to mock dependencies (@Mock, @InjectMocks)
- [ ] I understand when/thenReturn and verify
- [ ] I can test controllers with @WebMvcTest and MockMvc
- [ ] I can test repositories with @DataJpaTest
- [ ] I follow testing best practices (AAA pattern, descriptive names)

---

# BLOCK 4: SYSTEM DESIGN - LOAD BALANCING & CACHING (2 hours)

## Load Balancing

### What is Load Balancing?
Distributes incoming requests across multiple servers to:
- Handle more traffic
- Improve availability
- Prevent single point of failure

```
                    ┌─────────────┐
                    │   Client    │
                    └──────┬──────┘
                           │
                    ┌──────▼──────┐
                    │Load Balancer│
                    └──────┬──────┘
              ┌────────────┼────────────┐
              │            │            │
        ┌─────▼────┐ ┌─────▼────┐ ┌─────▼────┐
        │ Server 1 │ │ Server 2 │ │ Server 3 │
        └──────────┘ └──────────┘ └──────────┘
```

### Load Balancing Algorithms

1. **Round Robin** - Requests go to each server in turn
2. **Least Connections** - Send to server with fewest active connections
3. **IP Hash** - Same client always goes to same server
4. **Weighted** - More powerful servers get more requests

---

## Caching

### What is Caching?
Storing frequently accessed data in fast storage to reduce database load and latency.

```
Client → Cache (Redis) → Database
         ↑
         └── Cache Hit: Return immediately
             Cache Miss: Query DB, store in cache
```

### Cache Strategies

**1. Cache-Aside (Lazy Loading)**
```java
public Product getProduct(Long id) {
    // Check cache first
    Product product = cache.get("product:" + id);
    
    if (product == null) {
        // Cache miss - query database
        product = productRepository.findById(id).orElse(null);
        
        if (product != null) {
            // Store in cache for next time
            cache.set("product:" + id, product, 1, TimeUnit.HOURS);
        }
    }
    
    return product;
}
```

**2. Write-Through**
```java
public Product saveProduct(Product product) {
    // Save to database
    Product saved = productRepository.save(product);
    
    // Immediately update cache
    cache.set("product:" + saved.getId(), saved);
    
    return saved;
}
```

**3. Write-Behind (Write-Back)**
- Write to cache immediately
- Asynchronously write to database

### Redis with Spring Boot
```java
@Service
public class ProductService {

    @Autowired
    private RedisTemplate<String, Product> redisTemplate;

    @Autowired
    private ProductRepository productRepository;

    private static final String CACHE_PREFIX = "product:";

    public Product getProduct(Long id) {
        String key = CACHE_PREFIX + id;
        
        // Try cache first
        Product product = redisTemplate.opsForValue().get(key);
        
        if (product == null) {
            product = productRepository.findById(id).orElse(null);
            if (product != null) {
                redisTemplate.opsForValue().set(key, product, 1, TimeUnit.HOURS);
            }
        }
        
        return product;
    }

    public void invalidateCache(Long id) {
        redisTemplate.delete(CACHE_PREFIX + id);
    }
}
```

### Spring Cache Abstraction
```java
@Configuration
@EnableCaching
public class CacheConfig {
    // Configuration
}

@Service
public class ProductService {

    @Cacheable(value = "products", key = "#id")
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @CachePut(value = "products", key = "#product.id")
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void clearCache() {
        // Clears all products cache
    }
}
```

---

## ✅ BLOCK 4 CHECKLIST
- [ ] I understand load balancing and its algorithms
- [ ] I understand caching strategies (cache-aside, write-through)
- [ ] I can explain when to use caching
- [ ] I know about Redis and how to use it with Spring Boot

---

# DAY 3 & 4 RESOURCES

## Videos

| Topic | Link | Duration |
|-------|------|----------|
| Spring Data JPA | https://www.youtube.com/watch?v=8SGI_XS5OPw | 2h |
| Spring Security | https://www.youtube.com/watch?v=b9O9NI-RJ3o | 2h |
| JWT Tutorial | https://www.youtube.com/watch?v=KxqlJblhzfI | 1.5h |
| React Crash Course | https://www.youtube.com/watch?v=w7ejDZ8SWv8 | 2h |
| JUnit 5 & Mockito | https://www.youtube.com/watch?v=Geq60OVyBPg | 2h |

## Documentation
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- Spring Security: https://spring.io/projects/spring-security
- React Docs: https://react.dev/learn
- JUnit 5: https://junit.org/junit5/docs/current/user-guide/

---

# END OF DAY 3 & 4 GUIDE

## Summary

### Day 3 Covered:
- REST APIs (HTTP methods, status codes, ResponseEntity)
- Spring Data JPA (entities, repositories, queries, pagination)
- React Basics (useState, useEffect, components, props)
- System Design: Microservices vs Monolith

### Day 4 Covered:
- Spring Security (authentication, authorization)
- JWT (structure, implementation, filter)
- Unit Testing (JUnit 5, Mockito, @WebMvcTest, @DataJpaTest)
- System Design: Load Balancing & Caching

---

**KEEP GOING! YOU'RE HALFWAY THERE! 💪**
