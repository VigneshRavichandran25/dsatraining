# OOP Concepts in Java — Complete Executable Guide
### 10 Core Concepts

---

## How to Run

```bash
javac OOPConcepts.java
java OOPConcepts
```

> Requires Java 8 or higher. Check with `java -version`.

---

## File Structure at a Glance

```
OOPConcepts.java
│
├── Animal                  → Class & Object demo
├── BankAccount             → Encapsulation demo
├── Vehicle / Car / ElectricCar  → Inheritance (multi-level)
├── Calculator              → Method Overloading
├── Shape / Circle / Rectangle / Triangle  → Method Overriding
├── Employee / Manager / Developer  → Abstract Class
├── Flyable / Swimmable / Duck  → Interfaces
├── Counter                 → Static Members
├── MathConstants           → Final Keyword
├── Student                 → Constructor Types
├── OuterClass              → Inner Classes
├── Box<T>                  → Generics
│
└── OOPConcepts (main)      → Runs all 10 demos
```

---

## Concept 1 — Class & Object

**What it is:** A `class` is a blueprint. An `object` is a real instance built from that blueprint.

```java
class Animal {
    String name;   // field
    int age;       // field

    Animal(String name, int age) {   // constructor
        this.name = name;
        this.age = age;
    }

    void introduce() {
        System.out.println("I am " + name + ", age " + age);
    }
}
```

**In main():**
```java
Animal dog = new Animal("Bruno", 3);   // object created from blueprint
dog.introduce();                        // calling method on the object
```

**Key points:**
- `this.name` refers to the current object's field — distinguishes it from the constructor parameter of the same name
- `new Animal(...)` calls the constructor and allocates memory for the object
- You can create as many objects as you want from the same class — each has its own `name` and `age`

---

## Concept 2 — Encapsulation

**What it is:** Hide internal data using `private`, and expose controlled access through `public` getters and setters.

```java
class BankAccount {
    private String owner;    // cannot be accessed from outside directly
    private double balance;

    public double getBalance() {        // controlled READ
        return balance;
    }

    public void deposit(double amount) { // controlled WRITE with validation
        if (amount > 0) {
            balance += amount;
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Insufficient funds!");
        }
    }
}
```

**In main():**
```java
BankAccount acc = new BankAccount("Navaneeth", 10000);
acc.balance = 99999;      // ❌ COMPILE ERROR — private field
acc.deposit(5000);        // ✅ controlled access
acc.withdraw(50000);      // ✅ validation catches this
```

**Why it matters:** Without encapsulation, any code anywhere could set `balance = -99999`. The `private` keyword + setters create a protective wall around sensitive data.

---

## Concept 3 — Inheritance

**What it is:** A child class `extends` a parent class and automatically gets all its fields and methods. Avoids duplicating code.

```java
class Vehicle {             // PARENT
    String brand;
    int speed;
    void move() { ... }
}

class Car extends Vehicle { // CHILD — inherits brand, speed, move()
    int doors;
    Car(String brand, int speed, int doors) {
        super(brand, speed); // calls Vehicle's constructor
        this.doors = doors;
    }
}

class ElectricCar extends Car { // GRANDCHILD — multi-level
    int batteryRange;
}
```

**In main():**
```java
ElectricCar tesla = new ElectricCar("Tesla", 200, 4, 500);
tesla.move();          // inherited from Vehicle (grandparent)
tesla.carInfo();       // inherited from Car (parent)
tesla.electricInfo();  // own method
```

**Inheritance chain:** `ElectricCar → Car → Vehicle`

`super(...)` must be the **first line** in the constructor when calling the parent constructor. It passes the required arguments up the chain.

---

## Concept 4 — Polymorphism

**What it is:** "One name, many forms." The same method name behaves differently depending on context.

### 4a — Method Overloading (Compile-time)

Same method name, different parameters. Java decides which to call at **compile time** based on the arguments.

```java
class Calculator {
    int add(int a, int b)              { return a + b; }
    double add(double a, double b)     { return a + b; }  // different types
    int add(int a, int b, int c)       { return a + b + c; } // different count
}
```

```java
calc.add(2, 3);       // → calls int version
calc.add(2.5, 3.5);   // → calls double version
calc.add(1, 2, 3);    // → calls 3-param version
```

### 4b — Method Overriding (Runtime)

A child class redefines a method from the parent. Java decides which to call at **runtime** based on the actual object type.

```java
class Shape       { void draw() { ... } }
class Circle      extends Shape { @Override void draw() { ... } }
class Rectangle   extends Shape { @Override void draw() { ... } }
```

```java
Shape[] shapes = { new Circle(7), new Rectangle(4, 5), new Triangle(6, 8) };

for (Shape s : shapes) {
    s.draw();  // correct draw() chosen at RUNTIME based on actual type
}
```

**`@Override` annotation:** Not required but strongly recommended. It tells the compiler "I intend to override" — if you misspell the method name, the compiler will catch it instead of silently creating a new method.

---

## Concept 5 — Abstraction

**What it is:** Hide the *how*, expose only the *what*. Force subclasses to implement specific behaviour.

### 5a — Abstract Class

```java
abstract class Employee {
    abstract double calculateBonus();   // NO body — subclass MUST implement this

    void displaySalary() {              // concrete method — shared by all
        double total = baseSalary + calculateBonus();
        System.out.println(name + " total: ₹" + total);
    }
}

class Manager extends Employee {
    @Override
    double calculateBonus() { return baseSalary * 0.30; }  // 30%
}

class Developer extends Employee {
    @Override
    double calculateBonus() { return baseSalary * 0.20; }  // 20%
}
```

You **cannot** do `new Employee()` — abstract classes cannot be instantiated directly. They exist only to be extended.

### 5b — Interface

```java
interface Flyable {
    int MAX_ALTITUDE = 40000;  // implicitly public static final
    void fly();                 // implicitly public abstract
    void land();

    default void status() {    // default method (Java 8+) — optional to override
        System.out.println("Aircraft is operational");
    }
}

interface Swimmable {
    void swim();
}

// A class can implement MULTIPLE interfaces (unlike extends, which allows only one)
class Duck extends Animal implements Flyable, Swimmable {
    @Override public void fly()  { ... }
    @Override public void land() { ... }
    @Override public void swim() { ... }
}
```

| | Abstract Class | Interface |
|--|--|--|
| Can have concrete methods | ✅ Yes | ✅ Yes (via `default`) |
| Can have fields | ✅ Yes | ❌ Only constants |
| Multiple inheritance | ❌ One only | ✅ Multiple |
| Use when | Sharing common code | Defining a contract/capability |

---

## Concept 6 — Static Members

**What it is:** `static` fields and methods belong to the **class itself**, not to any individual object. All objects share the same static data.

```java
class Counter {
    static int count = 0;  // ONE copy, shared by ALL Counter objects

    Counter() {
        count++;           // increments the shared count
        this.id = count;
    }

    static void showCount() {       // called on the class, not an object
        System.out.println("Total: " + count);
    }
}
```

```java
Counter c1 = new Counter();   // count = 1
Counter c2 = new Counter();   // count = 2
Counter c3 = new Counter();   // count = 3

Counter.showCount();   // "Total: 3" — called on CLASS not instance
```

**Real-world use:** Database connection pools, configuration settings, utility methods (`Math.sqrt()`, `Arrays.sort()`) — all static because they don't need per-object state.

---

## Concept 7 — Final Keyword

**What it is:** `final` locks something — prevents reassignment, overriding, or extension.

```java
final class MathConstants {          // cannot be extended (subclassed)
    static final double PI = 3.14159265358979;  // cannot be reassigned
    static final double E  = 2.71828182845904;
}
```

| Applied to | Effect |
|--|--|
| `final` variable | Value cannot be changed after assignment |
| `final` method | Cannot be `@Override`-d in a subclass |
| `final` class | Cannot be `extend`-ed at all |

**Why `String` is final in Java:** If `String` wasn't final, someone could extend it and override its behaviour — making `"hello".equals("hello")` return `false`. `final` guarantees predictability.

---

## Concept 8 — Constructor Types

**What it is:** Constructors initialize objects. Java supports multiple types.

```java
class Student {

    // DEFAULT constructor — no arguments, sets default values
    Student() {
        this.name = "Unknown";
        this.grade = 0;
    }

    // PARAMETERIZED constructor — caller provides values
    Student(String name, int grade) {
        this.name = name;
        this.grade = grade;
        this.school = "Default School";
    }

    // CHAINED constructor — calls another constructor using this()
    Student(String name, int grade, String school) {
        this(name, grade);       // calls the 2-param constructor above
        this.school = school;    // then adds its own logic
    }
}
```

```java
Student s1 = new Student();                            // default
Student s2 = new Student("Ravi", 10);                 // parameterized
Student s3 = new Student("Priya", 12, "IIT School");  // chained
```

**Constructor chaining rule:** `this(...)` must always be the **first statement** in the constructor body. It prevents duplicate initialization code across constructors.

---

## Concept 9 — Inner Classes

**What it is:** Classes defined inside other classes. Four types exist.

```java
class OuterClass {
    private String secret = "outer secret";

    // 1. REGULAR INNER CLASS — tied to an OuterClass instance
    class InnerClass {
        void reveal() {
            System.out.println(secret); // can access outer private fields!
        }
    }

    // 2. STATIC NESTED CLASS — independent of OuterClass instance
    static class StaticNested {
        void greet() { System.out.println("Static nested!"); }
    }

    // 3. LOCAL CLASS — defined inside a method, only usable there
    void demonstrateLocalClass() {
        class LocalClass {
            void say() { System.out.println("I am local!"); }
        }
        new LocalClass().say();
    }
}
```

```java
// 4. ANONYMOUS INNER CLASS — inline one-time class with no name
Shape anonymousShape = new Shape() {
    @Override
    void draw() { System.out.println("Drawing anonymously!"); }
};
anonymousShape.draw();
```

**How to instantiate:**
```java
OuterClass outer = new OuterClass();
OuterClass.InnerClass inner = outer.new InnerClass();  // needs outer instance
OuterClass.StaticNested nested = new OuterClass.StaticNested(); // no outer needed
```

**When to use anonymous inner classes:** When you need a one-time custom implementation of an interface or abstract class without creating a whole new file. Heavily used in GUI event listeners and callbacks.

---

## Concept 10 — Generics

**What it is:** Write one class or method that works safely with **any type**. The `<T>` is a type placeholder filled in at compile time.

```java
class Box<T> {              // T = type parameter (placeholder)
    private T value;

    Box(T value) { this.value = value; }

    T getValue() { return value; }

    void showType() {
        System.out.println("Value: " + value + " | Type: " + value.getClass().getSimpleName());
    }
}
```

```java
Box<Integer> intBox    = new Box<>(42);
Box<String>  strBox    = new Box<>("Hello OOP");
Box<Double>  doubleBox = new Box<>(3.14);

intBox.showType();    // Value: 42       | Type: Integer
strBox.showType();    // Value: Hello    | Type: String
doubleBox.showType(); // Value: 3.14     | Type: Double
```

**Without generics (old way):**
```java
Box rawBox = new Box("hello");
Integer val = (Integer) rawBox.getValue();  // ClassCastException at RUNTIME 💀
```

**With generics:**
```java
Box<Integer> typedBox = new Box<>("hello"); // ❌ COMPILE ERROR — caught early ✅
```

Generics shift type errors from **runtime crashes** to **compile-time errors**. That's the entire point.

---

## How `main()` Works

The `main()` method in `OOPConcepts` does not contain any logic — it is purely a **demonstration runner**. It creates objects from each class, calls their methods, and prints formatted output so you can see every concept in action in sequence.

The `printHeader()` helper method simply prints a separator line with a title — it exists only to make the output readable in the terminal.

---

## Key Takeaways

| # | Concept | One-line Summary |
|---|---------|-----------------|
| 1 | Class & Object | Class = blueprint, Object = built thing |
| 2 | Encapsulation | `private` data + `public` controlled access |
| 3 | Inheritance | `extends` to reuse parent code |
| 4 | Polymorphism | Same name, different behaviour (overload vs override) |
| 5 | Abstraction | Hide the how, force the what |
| 6 | Static | Belongs to the class, shared by all objects |
| 7 | Final | Lock variables, methods, or classes |
| 8 | Constructors | Default → Parameterized → Chained |
| 9 | Inner Classes | Regular, Static, Local, Anonymous |
| 10 | Generics | Type-safe reusable code |

---

## Common Mistakes to Avoid

```java
// ❌ Forgetting super() in child constructor
class Car extends Vehicle {
    Car(String brand) {
        // forgot super(brand, 0) → compile error if Vehicle has no default constructor
    }
}

// ❌ Calling static method on instance
Counter c = new Counter();
c.showCount();        // works but misleading — suggests it's instance-specific
Counter.showCount();  // ✅ correct — makes it clear it's class-level

// ❌ Overloading vs Overriding confusion
class Parent  { void show(int x) { } }
class Child extends Parent {
    void show(String x) { }   // ❌ this is OVERLOADING, not overriding!
    @Override void show(int x) { }  // ✅ this is OVERRIDING
}
```
