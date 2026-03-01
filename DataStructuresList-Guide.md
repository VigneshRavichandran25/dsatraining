# Java Data Structures — Arrays, List, ArrayList & LinkedList
### Real-World Use Cases + Complexity Analysis

---

## How to Run

```bash
javac DataStructures.java
java DataStructures
```

> Requires Java 8 or higher. Check with `java -version`.

---

## What is a Data Structure?

A **data structure** is a way to organize and store data in memory so it can be accessed and modified efficiently. Choosing the wrong data structure is one of the most common sources of performance bugs in real engineering systems.

> A 10x difference in execution speed is often not about a faster computer —  
> it's about choosing the right data structure.

---

## File Structure

```
DataStructures.java
│
├── demo1DArray()               → 1D Array — Student exam scores
├── demo2DArray()               → 2D Array — Marks sheet grid
├── demoArrayList()             → List interface + ArrayList — Food delivery orders
├── demoArrayListVsLinkedList() → ArrayList vs LinkedList — Performance comparison
│
└── main()                      → Runs all 4 demos + final summary table
```

---

## Section 1 — 1D Array

### What it is

A **1D array** is a fixed-size, ordered collection of elements of the same type stored in **contiguous (side-by-side) memory locations**.

```java
int[]    scores = {90, 85, 78, 92, 88};
String[] names  = {"Navaneeth", "Priya", "Ravi", "Ananya", "Karthik"};
```

### Memory Layout

```
Index:   [  0  ] [  1  ] [  2  ] [  3  ] [  4  ]
Value:   [  90 ] [  85 ] [  78 ] [  92 ] [  88 ]
Address: [ 1000] [ 1004] [ 1008] [ 1012] [ 1016]
                    ↑
              4 bytes apart (int)
```

Because the elements sit at predictable memory addresses, any element's address is:

```
address(i) = base_address + (i × element_size)
```

This arithmetic is why array access is **always O(1)** — it's just multiplication and addition, not a loop.

---

### Real-World Use Case — Student Exam Score System

The demo models a school system that:
- Stores scores for a fixed batch of students (`int[] scores`)
- Does one O(n) pass to find the **highest scorer, lowest scorer, and class average**
- Sorts students by rank using `Arrays.sort()` in O(n log n)

```java
int[] scores = {90, 85, 78, 92, 88};

// O(1) — direct address calculation
System.out.println(scores[3]);   // 92 — no searching, just arithmetic

// O(n) — must visit every element to find max
int max = scores[0];
for (int i = 1; i < scores.length; i++) {
    if (scores[i] > max) max = scores[i];
}
```

---

### Time & Space Complexity — 1D Array

| Operation | Time | Space | Why |
|-----------|------|-------|-----|
| Access `arr[i]` | **O(1)** | O(1) | Direct address: `base + i × size` |
| Linear search | O(n) | O(1) | May check every element |
| Sort (`Arrays.sort`) | O(n log n) | O(1) | Dual-Pivot Quicksort |
| Insert at end | O(1) | O(1) | Assign to known index |
| Insert at middle | O(n) | O(1) | Must shift all right-side elements |
| Delete at middle | O(n) | O(1) | Must shift all right-side elements |
| **Total storage** | — | **O(n)** | n elements in memory |

### The Fundamental Limitation

```java
int[] scores = new int[5];  // fixed at creation
// Cannot add a 6th student without:
int[] newScores = new int[6];
System.arraycopy(scores, 0, newScores, 0, scores.length); // O(n) copy
scores = newScores;
```

This limitation — **fixed size** — is exactly why `ArrayList` was invented.

---

## Section 2 — 2D Array

### What it is

A **2D array** is an array of arrays. It creates a **grid/matrix** structure — rows and columns.

```java
int[][] marks = {
    {88, 76, 95, 90},   // row 0: Navaneeth
    {72, 85, 80, 78},   // row 1: Priya
    {91, 68, 74, 85}    // row 2: Ravi
};
```

### Memory Model

Java's 2D arrays are actually **arrays of references** to separate row arrays:

```
marks ──→ [ ref0 | ref1 | ref2 ]
             ↓       ↓       ↓
           [88,76,95,90] [72,85,80,78] [91,68,74,85]
```

Access `marks[r][c]` involves two lookups: get the reference to row `r`, then index into that row at `c`. Still **O(1)**.

---

### Real-World Use Case — Student Marks Sheet

The demo models a marks sheet where:
- Rows = students, Columns = subjects
- Full traversal in O(r × c) prints the entire grid
- Column traversal in O(r) finds the top scorer per subject

```java
// O(1) — specific cell, two index lookups
int javascore = marks[0][2];   // Navaneeth's Java score

// O(r × c) — full grid traversal
for (int i = 0; i < students.length; i++) {
    for (int j = 0; j < subjects.length; j++) {
        System.out.print(marks[i][j] + " ");
    }
}
```

---

### Jagged Arrays

Java allows rows of **different lengths** — called a jagged (or ragged) array:

```java
int[][] jagged = {
    {1},
    {2, 3},
    {4, 5, 6},
    {7, 8, 9, 10}
};
```

Useful for triangular matrices, Pascal's triangle, or when different rows need different amounts of data.

---

### Time & Space Complexity — 2D Array

| Operation | Time | Space | Why |
|-----------|------|-------|-----|
| Access `grid[r][c]` | **O(1)** | O(1) | Two direct index lookups |
| Full traversal | O(r × c) | O(1) | Every cell visited once |
| Row traversal | O(c) | O(1) | One row, c columns |
| Column traversal | O(r) | O(1) | r rows, one column each |
| **Total storage** | — | **O(r × c)** | All cells in memory |

---

## Section 3 — The List Interface & ArrayList

### What is the List Interface?

`List<T>` is a **contract** (Java interface) in the Collections Framework. It defines *what* operations a list must support, without specifying *how* they are implemented.

```java
// List contract — defines: add(), get(), remove(), size(), contains()...
interface List<E> {
    boolean add(E element);
    E get(int index);
    E remove(int index);
    int size();
    boolean contains(Object o);
    // ... more
}
```

`ArrayList` and `LinkedList` both implement `List` — same API, different internals.

**Best practice:** declare variables as `List<T>`, not `ArrayList<T>`:

```java
List<String> orders = new ArrayList<>();  // ✅ program to the interface
// Later if you need LinkedList, just change one word:
List<String> orders = new LinkedList<>();  // ✅ rest of code unchanged
```

---

### What is ArrayList?

An `ArrayList` is a **dynamic resizable array**. It solves the fixed-size limitation of plain arrays.

**Internally:**
1. Java allocates an array with default capacity of **10**
2. When you add the 11th element, Java creates a new array of size **15** (1.5× growth)
3. Copies all 10 elements to the new array
4. Adds the new element
5. Discards the old array

```
Initial:   [A][B][C][D][E][F][G][H][I][J][ ][ ][ ][ ][ ]   ← size=10, capacity=15
Add K:     [A][B][C][D][E][F][G][H][I][J][K][ ][ ][ ][ ]   ← no resize needed
11th elem: OK, capacity=15, still room
...
16th elem: resize! new capacity = 22
```

This amortized growth strategy means that over many `add()` calls, the **average cost per add is O(1)** even though occasional resizes cost O(n).

---

### Real-World Use Case — Food Delivery Order History

The demo models a delivery app where:
- Orders arrive dynamically (we don't know how many upfront)
- Users can view, search, and cancel orders at any time
- Priority orders can be inserted at the front

```java
List<String> orders = new ArrayList<>();

// O(1) amortized — add to end
orders.add("Order#101 - Biryani from Behrouz");

// O(1) — direct index access (same as array)
String first = orders.get(0);

// O(n) — insert at front: all existing orders shift right
orders.add(0, "PRIORITY: Medicine");

// O(n) — remove by index: elements shift left
String cancelled = orders.remove(2);

// O(n) — linear scan to find matching order
boolean found = orders.contains("Pizza");
```

---

### Time & Space Complexity — ArrayList

| Operation | Time | Space | Why |
|-----------|------|-------|-----|
| `add(element)` | **O(1) amortized** | O(1) | Append to end; rare O(n) resize |
| `add(index, element)` | O(n) | O(1) | Shifts all right-side elements |
| `get(index)` | **O(1)** | O(1) | Direct array index access |
| `remove(index)` | O(n) | O(1) | Shifts all right-side elements |
| `remove(object)` | O(n) | O(1) | Linear scan + shift |
| `contains(object)` | O(n) | O(1) | Linear scan |
| `size()` | O(1) | O(1) | Stored as a field, not counted |
| `sort()` | O(n log n) | O(n) | TimSort |
| **Total storage** | — | **O(n)** | Plus unused capacity buffer |

**What "amortized O(1)" means:**

Most `add()` calls are truly O(1). But every time a resize happens, it's O(n). If you add n elements, resizes happen at 10, 15, 22, 33... (roughly log(n) times), each copying O(n) total elements. Spread over n operations: total work = O(n), divided by n operations = **O(1) per operation on average**.

---

## Section 4 — ArrayList vs LinkedList (Glimpse)

### Internal Structure

**ArrayList** — backed by a contiguous array:
```
[A][B][C][D][E]
 0  1  2  3  4  ← indices, contiguous memory addresses
```

**LinkedList** — backed by doubly-linked nodes with pointers:
```
null ← [A] ↔ [B] ↔ [C] ↔ [D] → null
```

Each node stores:
```java
class Node<E> {
    E data;
    Node<E> next;  // pointer to next node
    Node<E> prev;  // pointer to previous node (doubly-linked)
}
```

Nodes can be **anywhere in memory** — they are connected by pointers, not by physical proximity.

---

### Why get(index) is O(n) in LinkedList

There are no indices in a LinkedList. To get the element at position `i`, Java must **start from the head and follow pointers** `i` times:

```
get(3) → start at head → follow next → follow next → follow next → return data
```

For a list of 1,000,000 elements, `get(999999)` follows 999,999 pointers. This is why LinkedList is catastrophically slow for random access.

```java
// LinkedList get() — O(n) per call — AVOID in loops
for (int i = 0; i < linkedList.size(); i++) {
    linkedList.get(i);  // each call traverses from head!
}

// Correct way to iterate LinkedList — O(n) total
for (String item : linkedList) { ... }  // uses Iterator, moves one step at a time
```

---

### Why addFirst() is O(1) in LinkedList

Adding at the front of a LinkedList only requires updating two pointers — no shifting:

```
Before: null ← [A] ↔ [B] ↔ [C] → null

Add X at front:
Step 1: Create new node [X]
Step 2: X.next = A
Step 3: A.prev = X
Step 4: head = X

After:  null ← [X] ↔ [A] ↔ [B] ↔ [C] → null
```

Two pointer assignments, regardless of list size → **O(1)**.

Compare to ArrayList's `add(0, X)`:
```
Before: [A][B][C]
Step 1: Shift C right: [A][B][_][C]
Step 2: Shift B right: [A][_][B][C]
Step 3: Shift A right: [_][A][B][C]
Step 4: Insert X:      [X][A][B][C]
```
Every element shifts → **O(n)**.

---

### Real-World Use Cases

| Use Case | Best Choice | Why |
|----------|-------------|-----|
| Browser history | `ArrayList` | Jump to nth page — frequent `get(i)` |
| Music playlist | `LinkedList` | Add/remove songs frequently |
| Shopping cart | `ArrayList` | Display all items, iterate often |
| Undo/Redo stack | `LinkedList` | Push/pop from front only |
| Search results | `ArrayList` | Display by page (index-based access) |
| Print queue | `LinkedList` | Add jobs at back, remove from front |

---

### Head-to-Head Comparison

| Operation | ArrayList | LinkedList | Winner |
|-----------|-----------|------------|--------|
| `get(index)` | **O(1)** | O(n) | ArrayList |
| `add(element)` at end | **O(1)** amort | O(1) | Tie |
| `add(0, elem)` at front | O(n) | **O(1)** | LinkedList |
| `remove(index)` | O(n) | O(n)* | Tie |
| `remove()` first/last | O(n) | **O(1)** | LinkedList |
| `contains()` | O(n) | O(n) | Tie |
| Memory per element | ~4 bytes | ~20 bytes | ArrayList |
| Cache performance | ✅ Excellent | ❌ Poor | ArrayList |
| Iteration speed | **Faster** | Slower | ArrayList |

*LinkedList `remove(index)` is O(n) because it traverses to find the node. But removing via an `Iterator` is O(1) because you already hold the node reference.

---

### Memory and Cache — The Hidden Advantage of ArrayList

This is often overlooked in complexity analysis.

Modern CPUs cache recently accessed memory in **L1/L2/L3 cache**. When you access `arrayList.get(5)`, the CPU loads a block of neighboring memory into cache — so `get(6)`, `get(7)`, `get(8)` are served from cache, essentially free.

LinkedList nodes are scattered across RAM. Every `next` pointer dereference is a **cache miss** — the CPU must go to main memory, which is 100–200× slower than cache.

This is why in benchmarks, **ArrayList often outperforms LinkedList even for operations where LinkedList is theoretically O(1)** — because real hardware doesn't behave like the abstract Big-O model.

```
Practical rule: ArrayList wins in 90% of real-world use cases.
Use LinkedList only when you have a proven bottleneck at head/tail insertions
and your profiler confirms LinkedList is faster.
```

---

## Final Summary — All Data Structures at a Glance

| Structure | Access | Insert | Delete | Space | Best For |
|-----------|--------|--------|--------|-------|----------|
| 1D Array | O(1) | O(n) | O(n) | O(n) | Fixed-size collections |
| 2D Array | O(1) | O(n²) | O(n²) | O(r×c) | Grids, matrices, tables |
| ArrayList | O(1) | O(1)* | O(n) | O(n) | Dynamic lists, most use cases |
| LinkedList | O(n) | O(1)** | O(1)** | O(n) | Queue-like access patterns |

*ArrayList `add()` at end is O(1) amortized (O(n) during resize)
**LinkedList `add/remove` at head/tail only; traversal to middle is O(n)

---

## Common Mistakes to Avoid

```java
// ❌ 1. Accessing out-of-bounds index
int[] arr = new int[5];
arr[5] = 10;  // ArrayIndexOutOfBoundsException — valid indices are 0–4

// ❌ 2. Using get(i) in a loop on LinkedList — O(n²) total!
for (int i = 0; i < linkedList.size(); i++) {
    linkedList.get(i);  // each get() traverses from head → O(n²) loop!
}
// ✅ Use enhanced for-loop or iterator — O(n) total
for (String item : linkedList) { ... }

// ❌ 3. Declaring as concrete type instead of interface
ArrayList<String> list = new ArrayList<>();  // locked in to ArrayList
List<String> list = new ArrayList<>();       // ✅ flexible — can swap to LinkedList

// ❌ 4. Modifying list while iterating — ConcurrentModificationException
for (String item : orders) {
    if (item.contains("cancel")) orders.remove(item);  // 💀 exception!
}
// ✅ Use Iterator.remove() or collect then remove
Iterator<String> it = orders.iterator();
while (it.hasNext()) {
    if (it.next().contains("cancel")) it.remove();  // safe
}

// ❌ 5. Confusing size() with length
arr.length        // arrays use .length  (field, no parentheses)
list.size()       // collections use .size()  (method, with parentheses)
```
