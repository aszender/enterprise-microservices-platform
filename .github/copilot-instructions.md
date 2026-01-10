# Java Spring Learning Repository - Copilot Instructions

## Project Purpose
This is a **Java fundamentals and OOP learning repository** preparing for a Java/Spring interview (Impact.com, Jan 16, 2026). It demonstrates core Java concepts through practical examples, not production code.

## Architecture & Structure

### Core Components
- **`App.java`** - Entry point; demonstrates interop between modules
  - Imports `JavaCore` (fundamental concepts) and `oop` (OOP patterns)
  - Uses `Scanner` for user input (String parsing to primitives)
  - Creates nested inner class instances (`new oop().new Dog()`)
  
- **`src/JavaCore.java`** - Pre-OOP fundamentals (208 lines)
  - Organized by concept with clear section headers: PRIMITIVES, ARRAYS, COLLECTIONS, SWITCH, LOOPS, EXCEPTIONS
  - Single `executeValues()` entry method chains demonstrations in order
  - Demonstrates distinctions: `==` vs `.equals()`, pass-by-value semantics, ArrayList vs Arrays

- **`src/oop.java`** - OOP patterns via nested inner classes
  - `Animal` base class with constructor, getters, `toString()` override, abstract-like `makeSound()`
  - `Dog` extends `Animal`, overrides `makeSound()` showing inheritance/polymorphism

### Key Conventions
1. **Method organization** - Each demonstration isolated in private method; no complex control flow
2. **Printed output** - All concepts print to stdout; no assertions or unit tests
3. **Package structure** - `src.*` packages; classes imported at entry point
4. **Nested classes** - Inner classes used for OOP demos, instantiated as `new oop().new Dog()`

## Developer Workflows

### Running Code
```bash
# Compile all Java files (IntelliJ/VS Code Java extension handles this)
javac App.java src/*.java

# Run entry point
java App
```

### Debugging
- Use VS Code Java extension breakpoints in `App.java` or `src/JavaCore.java`
- Debug session configured (see git history: recent debug commits)
- Focus on `executeValues()` flow to trace concept demonstrations

### Test Strategy
- **No automated tests** - Learning repo focused on manual verification of printed output
- Verify output by inspecting console logs when running `App.java`

## Common Patterns & Anti-Patterns

### DO:
- Add demonstrations as **private methods** in `JavaCore` following existing sections
- Use **section headers** (`/* === CONCEPT NAME === */`) to organize code
- Call new methods from `executeValues()` to integrate into flow
- Use **generic types** in collections: `List<String>`, `Map<String, Integer>`
- Override `toString()` and use descriptive output for verification

### DON'T:
- Add complex business logic - keep demonstrations isolated and simple
- Use static methods for demonstrations (keep state in instance fields)
- Add external dependencies - stick to `java.util.*` and Java 17 stdlib
- Create separate test files - append demonstrations to existing classes

## Interview Preparation Context
This repo covers **7-day crash course** (see `Study_Plan.md`):
- Days 1-2: Java Core (this repo's focus)
- Days 3-4: Java 8+ features (Lambdas, Streams, Optional) - extend existing patterns
- Days 5-7: Spring Framework (new repo likely needed)

When extending: prioritize demonstrating Java concepts used in Spring (Generics, Interfaces, Inheritance).

## File References for Key Patterns
- Exception handling: [JavaCore.java#L180-L186](src/JavaCore.java#L180-L186)
- Collection iteration: [JavaCore.java#L108-L130](src/JavaCore.java#L108-L130)
- Inheritance/Polymorphism: [oop.java#L28-L36](src/oop.java#L28-L36)
