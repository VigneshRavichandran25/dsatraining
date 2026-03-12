# 🧩 Programming Assignment: Object-Oriented Programming & SOLID Principles

## 📌 Objective
This assignment is designed to help students practice **Object-Oriented Programming (OOP)** concepts and apply **SOLID design principles** in software design.

Students should demonstrate understanding of:

### OOP Features
- Encapsulation
- Abstraction
- Inheritance
- Polymorphism

### SOLID Principles
- Single Responsibility Principle (SRP)
- Open/Closed Principle (OCP)
- Liskov Substitution Principle (LSP)
- Interface Segregation Principle (ISP)
- Dependency Inversion Principle (DIP)

---

# 🔹 Question 1: Smart Home Device System (OOP Concepts)

Design a **Smart Home Device Management System**.

### Requirements

Create an abstract base class:

`Device`

**Attributes**
- deviceName
- powerStatus (ON/OFF)

**Functions**
- turnOn()
- turnOff()
- displayStatus()

---

### Derived Classes

Create at least **two derived classes**:

1. `Light`
2. `Thermostat`

Each device should implement its own behavior for `displayStatus()`.

---

### Requirements

Your program must demonstrate:

- **Encapsulation**
  - Keep attributes private and access them using getter/setter methods.

- **Abstraction**
  - Use an abstract class or interface for `Device`.

- **Inheritance**
  - `Light` and `Thermostat` inherit from `Device`.

- **Polymorphism**
  - Override the `displayStatus()` method in derived classes.

---

### In `main()`:

- Create objects of both devices.
- Turn devices on/off.
- Display their status.

---

# 🔹 Question 2: Payment Processing System (SOLID Principles)

Design a **Payment Processing System** for an online store.

Customers can pay using different payment methods.

---

### Requirements

Create an interface:

`PaymentMethod`

**Function**
- processPayment(double amount)

---

### Implement the following classes:

1. `CreditCardPayment`
2. `PayPalPayment`
3. `UPIPayment`

Each class must implement `processPayment()` differently.

---

### Create a service class:

`PaymentProcessor`

This class should **accept a PaymentMethod object** and process the payment.

---

### SOLID Principles to Demonstrate

- **Open/Closed Principle (OCP)**
  - New payment methods should be added without modifying existing code.

- **Dependency Inversion Principle (DIP)**
  - `PaymentProcessor` should depend on the `PaymentMethod` interface, not concrete implementations.

---

### In `main()`:

- Create objects of different payment methods.
- Process payments using the `PaymentProcessor`.

---

# 🔹 Question 3: Notification System (SOLID Principles)

Design a **Notification System** used by an application to send alerts.

Notifications can be sent using different communication channels.

---

### Requirements

Create small, focused interfaces:

- `EmailSender`
- `SMSSender`
- `PushNotificationSender`

Each interface should define a method:

- sendEmail()
- sendSMS()
- sendPushNotification()

---

### Implement the following classes:

1. `EmailNotification`
2. `SMSNotification`
3. `MobileAppNotification`

Each class should implement only the interface it needs.

---

### SOLID Principles to Demonstrate

- **Interface Segregation Principle (ISP)**
  - Classes should not implement methods they do not use.

- **Single Responsibility Principle (SRP)**
  - Each class should have only one responsibility (sending a specific type of notification).

---

### In `main()`:

- Create objects of each notification type.
- Send sample notifications.

---

# 📌 Submission Requirements

Students must submit:

- Complete source code
- Proper class structure
- Clear use of OOP concepts
- Code comments explaining design choices

---

# 🎯 Evaluation Criteria

| Criteria | Marks |
|--------|--------|
| Correct use of OOP concepts | 30 |
| Implementation of SOLID principles | 30 |
| Code structure and readability | 20 |
| Program correctness | 20 |

Total: **100 Marks**

---

# 💡 Bonus (Optional)

Add an additional feature to any one problem that improves scalability or maintainability using **another SOLID principle**.