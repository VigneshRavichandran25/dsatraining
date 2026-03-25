import java.util.*;

/**
 * ============================================================
 *  LIST DEEP DIVE — ArrayList vs LinkedList
 *  Run: javac ListDeepDive.java && java ListDeepDive
 * ============================================================
 *
 *  WHAT IS A LIST?
 *  ---------------
 *  List is an ordered collection (also called a sequence).
 *
 *  Key characteristics:
 *    - Allows DUPLICATE elements
 *    - Maintains INSERTION ORDER
 *    - Provides INDEX-BASED access (list.get(0), list.get(1) ...)
 *
 *  Main implementations:
 *    - ArrayList  → backed by a dynamic array
 *    - LinkedList → backed by a doubly linked list
 *    - Vector     → legacy, thread-safe version of ArrayList (rarely used today)
 *
 *  Common List methods:
 *    add(E e)           → appends element at end
 *    add(int i, E e)    → inserts at index i
 *    get(int i)         → returns element at index i
 *    set(int i, E e)    → replaces element at index i
 *    remove(int i)      → removes element at index i
 *    remove(Object o)   → removes first occurrence of o
 *    size()             → number of elements
 *    contains(Object o) → true if element exists
 */
public class ListDeepDive {

    public static void main(String[] args) {

        System.out.println("=== LIST DEEP DIVE: ArrayList vs LinkedList ===\n");

        section1_ArrayListBasics();
        section2_ArrayListInternals();
        section3_ArrayListTimComplexity();
        section4_LinkedListBasics();
        section5_LinkedListInternals();
        section6_LinkedListTimeComplexity();
        section7_ArrayListVsLinkedList();
        section8_IterationAndRemoval();
        section9_RealWorldChatApp();
        section10_LRUCacheSimulation();
        section11_PerformanceThoughtExercise();
        section12_CommonMistakes();
        section13_InterviewSummary();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 1 — ArrayList Basics
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * ARRAYLIST OVERVIEW
     * ------------------
     * Internally backed by:  Object[] elementData
     *
     *   transient Object[] elementData;
     *
     * "transient" means this field is skipped during Java serialization.
     * The actual elements are serialized via a custom writeObject() method.
     *
     * Default initial capacity = 10
     * When full → new capacity = oldCapacity + (oldCapacity >> 1)
     *           = oldCapacity * 1.5  (approximately)
     *
     * Example:
     *   10 → 15 → 22 → 33 → 49 → ...
     */
    static void section1_ArrayListBasics() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 1: ArrayList Basics");
        System.out.println("─────────────────────────────────────────");

        // Declaration using List interface — always program to interface, not implementation
        List<String> fruits = new ArrayList<>();

        // add() — O(1) amortized (explained in section 2)
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Mango");
        fruits.add("Banana"); // duplicates are allowed

        System.out.println("List: " + fruits);
        System.out.println("Size: " + fruits.size());

        // get(index) — O(1) direct array access
        System.out.println("Element at index 1: " + fruits.get(1)); // Banana

        // set(index, value) — O(1) direct array replacement
        fruits.set(2, "Grapes");
        System.out.println("After set(2, Grapes): " + fruits);

        // contains() — O(n) linear scan
        System.out.println("Contains Apple? " + fruits.contains("Apple"));
        System.out.println("Contains Papaya? " + fruits.contains("Papaya"));

        // remove by index — O(n) because elements after index shift left
        fruits.remove(0);
        System.out.println("After remove(0): " + fruits);

        // remove by object — O(n) scan + shift
        fruits.remove("Banana");
        System.out.println("After remove('Banana'): " + fruits);

        // add at specific index — O(n) because elements shift right
        fruits.add(0, "Strawberry");
        System.out.println("After add(0, 'Strawberry'): " + fruits);

        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 2 — ArrayList Internals and Resizing
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * AMORTIZED O(1) — WHY add() IS CONSIDERED CONSTANT TIME
     * --------------------------------------------------------
     *
     * Most of the time, add() just does:
     *   elementData[size] = element;
     *   size++;
     * → O(1)
     *
     * But occasionally, when the array is full:
     *   1. Allocate new array of size * 1.5
     *   2. Copy all existing elements to new array   ← O(n)
     *   3. Add the new element
     *
     * How many total operations for n adds?
     *   n adds + (1 + 2 + 4 + 8 + ... + n) copy operations
     *   = n + 2n = 3n total operations
     *   = O(n) total / n operations = O(1) average per add
     *
     * This is called AMORTIZED O(1) — expensive occasionally,
     * but the cost is spread across many operations.
     */
    static void section2_ArrayListInternals() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 2: ArrayList Internals & Resizing");
        System.out.println("─────────────────────────────────────────");

        // Simulate capacity growth
        // Default capacity = 10
        // newCapacity = oldCapacity + (oldCapacity >> 1)
        int capacity = 10;
        System.out.println("Simulating ArrayList internal capacity growth:");
        System.out.printf("  Initial capacity: %d%n", capacity);

        for (int i = 0; i < 6; i++) {
            int newCapacity = capacity + (capacity >> 1); // >> 1 = divide by 2
            System.out.printf("  Resize: %d → %d (added %d slots)%n",
                              capacity, newCapacity, newCapacity - capacity);
            capacity = newCapacity;
        }

        System.out.println();

        // Pre-allocate capacity if you know the size upfront
        // This avoids all resize overhead
        List<Integer> preallocated = new ArrayList<>(10_000);
        System.out.println("Pre-allocated ArrayList with capacity 10,000 — no resize cost.");

        // Trim to actual size when memory matters
        ((ArrayList<Integer>) preallocated).trimToSize();
        System.out.println("trimToSize() called — releases unused array slots.\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 3 — Time Complexity Demo
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * ARRAYLIST TIME COMPLEXITY
     * --------------------------
     *
     *  Operation          Time Complexity
     *  ---------          ---------------
     *  get(index)         O(1)   → direct array index access
     *  add(end)           O(1)*  → amortized (resize is rare)
     *  add(index)         O(n)   → shifts all elements right of index
     *  remove(index)      O(n)   → shifts all elements left of index
     *  remove(Object)     O(n)   → linear scan + shift
     *  contains(Object)   O(n)   → linear scan
     *  set(index, value)  O(1)   → direct array replacement
     *
     *  * amortized O(1)
     */
    static void section3_ArrayListTimComplexity() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 3: Time Complexity Demo");
        System.out.println("─────────────────────────────────────────");

        List<Integer> list = new ArrayList<>(List.of(10, 20, 30, 40, 50));
        System.out.println("Starting list: " + list);

        // O(1) — direct index access, no traversal
        long start = System.nanoTime();
        int val = list.get(4); // last element, still O(1)
        long end = System.nanoTime();
        System.out.printf("get(4) = %d  [O(1)] → %d ns%n", val, (end - start));

        // O(1) amortized — appends at the end
        start = System.nanoTime();
        list.add(60);
        end = System.nanoTime();
        System.out.printf("add(60) at end     [O(1)*] → %d ns%n", (end - start));

        // O(n) — inserts at index 0, shifts ALL elements right
        start = System.nanoTime();
        list.add(0, 5);
        end = System.nanoTime();
        System.out.printf("add(0, 5) at head  [O(n)] → %d ns%n", (end - start));

        // O(n) — removes index 0, shifts ALL elements left
        start = System.nanoTime();
        list.remove(0);
        end = System.nanoTime();
        System.out.printf("remove(0) at head  [O(n)] → %d ns%n", (end - start));

        System.out.println("Final list: " + list);
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 4 — LinkedList Basics
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * LINKEDLIST OVERVIEW
     * --------------------
     * Internally backed by a DOUBLY LINKED LIST.
     *
     * Each node looks like this:
     *
     *   private static class Node<E> {
     *       E item;       // the actual data
     *       Node<E> next; // pointer to next node
     *       Node<E> prev; // pointer to previous node
     *   }
     *
     * Structure visualization:
     *
     *   null ← [prev|10|next] ↔ [prev|20|next] ↔ [prev|30|next] → null
     *               ↑                                    ↑
     *             head                                 tail
     *
     * LinkedList also implements Deque — so it can act as:
     *   - Queue  (FIFO: addLast / removeFirst)
     *   - Stack  (LIFO: addFirst / removeFirst)
     *   - Deque  (both ends)
     */
    static void section4_LinkedListBasics() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 4: LinkedList Basics");
        System.out.println("─────────────────────────────────────────");

        LinkedList<Integer> list = new LinkedList<>();

        // O(1) — only updates head pointer + node references
        list.addFirst(10);
        list.addFirst(5);

        // O(1) — only updates tail pointer + node references
        list.addLast(20);
        list.addLast(30);

        System.out.println("After addFirst(10), addFirst(5), addLast(20), addLast(30): " + list);

        // O(1) — removes head node, updates head pointer
        int removed = list.removeFirst();
        System.out.println("removeFirst() → " + removed + " | List: " + list);

        // O(1) — removes tail node, updates tail pointer
        int removedLast = list.removeLast();
        System.out.println("removeLast() → " + removedLast + " | List: " + list);

        // O(n) — must traverse from head/tail to reach index
        list.addLast(40);
        list.addLast(50);
        System.out.println("get(1) = " + list.get(1) + "  [O(n) — traversal required]");

        // Peek — O(1), does not remove
        System.out.println("peekFirst() = " + list.peekFirst());
        System.out.println("peekLast()  = " + list.peekLast());

        System.out.println("Final LinkedList: " + list);
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 5 — LinkedList Memory Overhead
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * MEMORY COMPARISON
     * ------------------
     *
     * ArrayList:
     *   - Only stores the elements
     *   - Object[] uses ~4-8 bytes per reference (depending on JVM / compressed oops)
     *   - Contiguous memory → CPU cache friendly
     *
     * LinkedList:
     *   - Each node object carries:
     *       item  (8 bytes reference)
     *       next  (8 bytes reference)
     *       prev  (8 bytes reference)
     *       object header overhead (~16 bytes)
     *     Total: ~40 bytes per node vs ~8 bytes in ArrayList
     *
     *   - Nodes scattered in heap → CPU cache unfriendly (pointer chasing)
     *
     * For 1 million integers:
     *   ArrayList  ≈  4 MB  (int references in contiguous array)
     *   LinkedList ≈ 40 MB  (nodes scattered in heap with pointers)
     */
    static void section5_LinkedListInternals() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 5: Memory Overhead Comparison");
        System.out.println("─────────────────────────────────────────");

        int n = 100_000;

        // ArrayList memory
        List<Integer> arrayList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) arrayList.add(i);

        // LinkedList memory
        List<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < n; i++) linkedList.add(i);

        // Iteration speed comparison — ArrayList wins due to cache locality
        long start = System.nanoTime();
        long sum1 = 0;
        for (int val : arrayList) sum1 += val;
        long arrayTime = System.nanoTime() - start;

        start = System.nanoTime();
        long sum2 = 0;
        for (int val : linkedList) sum2 += val;
        long linkedTime = System.nanoTime() - start;

        System.out.printf("Iterate %,d elements:%n", n);
        System.out.printf("  ArrayList  (cache-friendly):  %,d ns  (sum=%d)%n", arrayTime, sum1);
        System.out.printf("  LinkedList (pointer-chasing): %,d ns  (sum=%d)%n", linkedTime, sum2);
        System.out.printf("  LinkedList was %.1fx slower for iteration%n%n",
                          (double) linkedTime / arrayTime);

        System.out.println("Why? ArrayList stores elements contiguously in memory.");
        System.out.println("     CPU cache loads a block at once → 'cache hit' for next element.");
        System.out.println("     LinkedList nodes are scattered → each node = 'cache miss'.");
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 6 — LinkedList Time Complexity
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * LINKEDLIST TIME COMPLEXITY
     * ---------------------------
     *
     *  Operation           Time Complexity
     *  ---------           ---------------
     *  addFirst()          O(1)  → update head pointer only
     *  addLast()           O(1)  → update tail pointer only
     *  removeFirst()       O(1)  → update head pointer only
     *  removeLast()        O(1)  → update tail pointer only
     *  get(index)          O(n)  → traverse from head or tail to index
     *  add(index, e)       O(n)  → traverse to index, then O(1) insert
     *  remove(index)       O(n)  → traverse to index, then O(1) remove
     *  contains(Object)    O(n)  → linear scan
     *
     *  Note: Even though the pointer update is O(1),
     *        finding the position first costs O(n).
     *        So middle insert/remove = O(n) for LinkedList too.
     *        This surprises many people — LinkedList is NOT always faster.
     */
    static void section6_LinkedListTimeComplexity() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 6: LinkedList vs ArrayList — Head Operations");
        System.out.println("─────────────────────────────────────────");

        int n = 50_000;

        // Head insert benchmark — LinkedList should win here
        List<Integer> arrayList  = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        long start = System.nanoTime();
        for (int i = 0; i < n; i++) arrayList.add(0, i);  // O(n) each → O(n²) total
        long arrayTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < n; i++) ((LinkedList<Integer>) linkedList).addFirst(i); // O(1) each → O(n) total
        long linkedTime = System.nanoTime() - start;

        System.out.printf("Insert at HEAD %,d times:%n", n);
        System.out.printf("  ArrayList  (O(n) per insert = O(n²) total): %,d ns%n", arrayTime);
        System.out.printf("  LinkedList (O(1) per insert = O(n) total):  %,d ns%n", linkedTime);
        System.out.printf("  LinkedList was %.1fx FASTER for head inserts%n%n",
                          (double) arrayTime / linkedTime);
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 7 — ArrayList vs LinkedList Decision Guide
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * WHEN TO USE ARRAYLIST?
     * -----------------------
     *  - Frequent reads by index (get operations)
     *  - Iteration over entire list
     *  - Mostly appending at end
     *  - Memory efficiency matters
     *  - CPU cache performance matters
     *
     * WHEN TO USE LINKEDLIST?
     * -----------------------
     *  - Frequent insert/remove at HEAD or TAIL
     *  - Implementing a Queue or Deque
     *  - When index-based access is not needed
     *  - When you need addFirst/removeFirst to be O(1)
     *
     * REALITY CHECK:
     * In practice, ArrayList outperforms LinkedList in most benchmarks
     * due to cache locality — even for cases where theory says LinkedList
     * should win. Always benchmark your specific use case.
     */
    static void section7_ArrayListVsLinkedList() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 7: Decision Matrix");
        System.out.println("─────────────────────────────────────────");

        System.out.println("""
                Feature              ArrayList        LinkedList
                ───────────────────────────────────────────────
                Memory layout        Contiguous       Scattered
                Random access        O(1) ✅           O(n) ❌
                Insert at end        O(1)* ✅          O(1) ✅
                Insert at head       O(n) ❌           O(1) ✅
                Insert at middle     O(n)              O(n)
                Remove at head       O(n) ❌           O(1) ✅
                Iteration speed      Fast ✅           Slow ❌
                Memory per element   Low ✅            High ❌
                Cache friendly       Yes ✅            No ❌
                Implements Deque     No                Yes ✅
                """);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 8 — Safe Removal During Iteration
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * CONCURRENTMODIFICATIONEXCEPTION
     * ---------------------------------
     * ArrayList tracks a modCount (modification count).
     * When you create an iterator, it captures the current modCount.
     * On every next() call, it checks if modCount changed.
     * If you remove inside a for-each loop, modCount changes → exception.
     *
     * WRONG — causes ConcurrentModificationException:
     *   for (Integer i : list) {
     *       if (i == 5) list.remove(i);  // ❌ modifies list during iteration
     *   }
     *
     * CORRECT approaches shown below:
     */
    static void section8_IterationAndRemoval() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 8: Safe Removal During Iteration");
        System.out.println("─────────────────────────────────────────");

        // ── Approach 1: Iterator.remove() ──────────────────────────────────
        // The CLASSIC safe approach. Iterator.remove() updates modCount properly.
        List<Integer> list1 = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8));
        Iterator<Integer> it = list1.iterator();
        while (it.hasNext()) {
            if (it.next() % 2 == 0) it.remove(); // removes even numbers safely
        }
        System.out.println("Approach 1 — Iterator.remove() (remove evens): " + list1);

        // ── Approach 2: removeIf() — Java 8+ ───────────────────────────────
        // Most readable and concise. Internally uses iterator or bulk array op.
        List<Integer> list2 = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8));
        list2.removeIf(n -> n % 2 == 0);
        System.out.println("Approach 2 — removeIf(n -> n%2==0):           " + list2);

        // ── Approach 3: Collect to new list (functional style) ─────────────
        List<Integer> list3 = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8));
        List<Integer> odds  = list3.stream()
                                   .filter(n -> n % 2 != 0)
                                   .toList();
        System.out.println("Approach 3 — stream().filter() (keep odds):   " + odds);

        // ── Approach 4: Iterate backwards by index ──────────────────────────
        // Safe for ArrayList because removing from end doesn't affect
        // lower indices. Useful when you need index context.
        List<Integer> list4 = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8));
        for (int i = list4.size() - 1; i >= 0; i--) {
            if (list4.get(i) % 2 == 0) list4.remove(i);
        }
        System.out.println("Approach 4 — reverse index loop (remove evens):" + list4);

        // ── WRONG — what NOT to do ──────────────────────────────────────────
        System.out.println("\nDemonstrating ConcurrentModificationException:");
        List<Integer> badList = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        try {
            for (Integer val : badList) {
                if (val == 3) badList.remove(val); // ❌ modifies during for-each
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("  ❌ Caught: ConcurrentModificationException");
            System.out.println("     Cause: Removing inside for-each without Iterator.remove()");
        }
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 9 — Real World: Chat Application
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * REAL WORLD SCENARIO — CHAT APPLICATION
     * ----------------------------------------
     * A chat window stores messages.
     *
     * Operations:
     *   - New message arrives → append to end
     *   - Render all messages → iterate all
     *   - User scrolls up → random access by index
     *   - "Delete last message" feature → remove from tail
     *
     * Verdict: ArrayList wins for most chat use cases.
     *   - Iteration is the dominant operation → ArrayList cache-friendly
     *   - Random access for scroll → ArrayList O(1)
     *   - LinkedList only wins if deleting first message frequently
     */
    static void section9_RealWorldChatApp() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 9: Real World — Chat Application");
        System.out.println("─────────────────────────────────────────");

        // ArrayList-backed chat — good for read-heavy, scroll, render
        List<String> chatMessages = new ArrayList<>();
        chatMessages.add("[10:01] Alice: Hey!");
        chatMessages.add("[10:02] Bob: Hi, how are you?");
        chatMessages.add("[10:03] Alice: I'm good, thanks!");
        chatMessages.add("[10:04] Bob: Working on that Java list article?");
        chatMessages.add("[10:05] Alice: Yes! ArrayList vs LinkedList.");

        System.out.println("Chat window (ArrayList — fast iteration and scroll):");
        for (String msg : chatMessages) {
            System.out.println("  " + msg);
        }

        // Random access for "jump to message N" — O(1) with ArrayList
        System.out.println("\nUser scrolled to message 3 (index 2): " + chatMessages.get(2));

        // Append new message — O(1) amortized
        chatMessages.add("[10:06] Bob: Nice! Which is better?");
        System.out.println("New message appended. Total: " + chatMessages.size());

        // LinkedList-backed — better if message queue with frequent removeFirst
        Queue<String> messageQueue = new LinkedList<>();
        messageQueue.offer("[10:07] Notification: User joined");
        messageQueue.offer("[10:08] Notification: User left");
        System.out.println("\nMessage queue (LinkedList as Queue):");
        while (!messageQueue.isEmpty()) {
            System.out.println("  Processing: " + messageQueue.poll()); // O(1)
        }
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 10 — LRU Cache Simulation
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * LRU CACHE — LinkedHashMap + Doubly Linked List concept
     * --------------------------------------------------------
     * LRU = Least Recently Used
     *
     * Classic implementation uses:
     *   HashMap     → O(1) lookup by key
     *   DoublyLinkedList → O(1) move-to-front on access
     *                      O(1) evict tail (least recently used)
     *
     * Java provides LinkedHashMap with accessOrder=true
     * which does exactly this internally.
     *
     * Capacity: 3 entries max
     * Access order: most recently used at tail, LRU at head
     */
    static void section10_LRUCacheSimulation() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 10: LRU Cache Simulation");
        System.out.println("─────────────────────────────────────────");

        final int CAPACITY = 3;

        // LinkedHashMap with accessOrder=true behaves like an LRU cache
        Map<Integer, String> lruCache = new LinkedHashMap<>(CAPACITY, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
                boolean shouldEvict = size() > CAPACITY;
                if (shouldEvict) {
                    System.out.println("  ♻ Evicting LRU entry: " + eldest.getKey()
                                       + " → " + eldest.getValue());
                }
                return shouldEvict;
            }
        };

        System.out.println("LRU Cache capacity: " + CAPACITY);

        // put operations
        lruCache.put(1, "Page A");  System.out.println("put(1, Page A): " + lruCache.keySet());
        lruCache.put(2, "Page B");  System.out.println("put(2, Page B): " + lruCache.keySet());
        lruCache.put(3, "Page C");  System.out.println("put(3, Page C): " + lruCache.keySet());

        // Access key 1 — moves it to most-recently-used position
        lruCache.get(1);
        System.out.println("get(1) — access Page A: " + lruCache.keySet() + " (1 moved to recent)");

        // Insert key 4 — evicts least recently used (key 2, since key 1 was just accessed)
        lruCache.put(4, "Page D");
        System.out.println("put(4, Page D): " + lruCache.keySet());

        System.out.println("\nWhy LinkedList matters here:");
        System.out.println("  - get() moves node to tail in O(1) — no shifting");
        System.out.println("  - evict() removes head node in O(1) — no shifting");
        System.out.println("  - HashMap provides O(1) key → node lookup");
        System.out.println("  Combined: O(1) get and put for LRU cache");
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 11 — Performance Thought Exercise
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * PERFORMANCE DECISION FRAMEWORK
     * --------------------------------
     *
     * Scenario A:
     *   Reads = 1,000,000   Inserts at middle = 10
     *   → ArrayList wins. 1M O(1) reads >> 10 O(n) inserts
     *
     * Scenario B:
     *   Insert at head frequently, no random access needed
     *   → LinkedList wins. O(1) addFirst every time
     *
     * Scenario C:
     *   Need Queue (FIFO) behaviour
     *   → LinkedList as Queue. O(1) offer/poll at both ends
     *
     * Scenario D:
     *   Product catalog — display 10,000 items, paginate, filter
     *   → ArrayList. Frequent iteration + index access.
     *
     * RULE OF THUMB:
     *   Start with ArrayList. Only switch to LinkedList if you
     *   benchmark and confirm frequent head/tail operations dominate.
     */
    static void section11_PerformanceThoughtExercise() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 11: Performance Thought Exercise");
        System.out.println("─────────────────────────────────────────");

        // Scenario A — read-heavy: ArrayList
        System.out.println("Scenario A: 1,000,000 reads + 10 middle inserts");
        List<Integer> productCatalog = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) productCatalog.add(i * 10);

        long start = System.nanoTime();
        long readSum = 0;
        for (int i = 0; i < 10_000; i++) readSum += productCatalog.get(i); // O(1) each
        System.out.printf("  ArrayList — read 10,000 items: %,d ns (sum=%d)%n",
                          System.nanoTime() - start, readSum);
        System.out.println("  → ArrayList is correct choice for read-heavy workloads\n");

        // Scenario B — queue/deque: LinkedList
        System.out.println("Scenario B: Frequent insert/remove at head (Queue/Deque)");
        Deque<String> taskQueue = new LinkedList<>();
        taskQueue.addLast("Task 1");
        taskQueue.addLast("Task 2");
        taskQueue.addLast("Task 3");
        taskQueue.addFirst("Priority Task"); // O(1) — only head pointer updated

        System.out.println("  Task queue (LinkedList as Deque): " + taskQueue);
        System.out.println("  Processing: " + taskQueue.pollFirst()); // O(1)
        System.out.println("  Remaining:  " + taskQueue);
        System.out.println("  → LinkedList correct for queue/stack/deque patterns\n");

        // Scenario C — product catalog (real-world ArrayList)
        System.out.println("Scenario C: Product catalog — iterate, filter, paginate");
        List<String> catalog = new ArrayList<>(List.of(
            "Laptop", "Phone", "Tablet", "Monitor", "Keyboard",
            "Mouse", "Headset", "Webcam", "Speaker", "Charger"
        ));

        // Page 2 of 3 items per page → O(1) index access
        int page = 2, pageSize = 3;
        int from = (page - 1) * pageSize;
        int to   = Math.min(from + pageSize, catalog.size());
        System.out.println("  Page " + page + " of catalog: " + catalog.subList(from, to));
        System.out.println("  → ArrayList random access makes pagination O(1)\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 12 — Common Mistakes
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * COMMON MISTAKES WITH LIST
     * --------------------------
     *
     * 1. Using LinkedList for random access
     *    → get(index) is O(n) — traversal every time
     *
     * 2. Forgetting resize cost for ArrayList
     *    → Pre-allocate with new ArrayList<>(expectedSize) when known
     *
     * 3. Removing elements inside a for-each loop
     *    → Use Iterator.remove() or removeIf() instead
     *
     * 4. Confusing List (interface) with Array (primitive)
     *    → int[] is a fixed-size array; List<Integer> is dynamic
     *
     * 5. Using List<Integer> when int[] would suffice
     *    → List<Integer> boxes each int (Integer object = ~16 bytes)
     *    → int[] uses 4 bytes per element — 4x more memory efficient
     *
     * 6. Assuming LinkedList insert at middle is fast
     *    → Finding middle position = O(n) traversal
     *    → Pointer update is O(1) but finding is O(n)
     *    → Net: middle insert is O(n) for both ArrayList and LinkedList
     */
    static void section12_CommonMistakes() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 12: Common Mistakes");
        System.out.println("─────────────────────────────────────────");

        // Mistake 1 — LinkedList for random access
        System.out.println("Mistake 1: LinkedList for random access");
        List<Integer> linked = new LinkedList<>();
        for (int i = 0; i < 10_000; i++) linked.add(i);

        long start = System.nanoTime();
        int val = linked.get(9_999); // O(n) — traverses 9999 nodes
        System.out.printf("  LinkedList.get(9999) = %d took %,d ns  ← O(n) traversal%n",
                          val, System.nanoTime() - start);

        start = System.nanoTime();
        List<Integer> array = new ArrayList<>(linked);
        int val2 = array.get(9_999); // O(1) — direct index
        System.out.printf("  ArrayList.get(9999)  = %d took %,d ns  ← O(1) direct%n%n",
                          val2, System.nanoTime() - start);

        // Mistake 2 — Integer boxing overhead
        System.out.println("Mistake 2: List<Integer> vs int[] for primitive storage");
        int n = 100_000;

        start = System.nanoTime();
        List<Integer> boxedList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) boxedList.add(i); // autoboxing each int
        long boxedTime = System.nanoTime() - start;

        start = System.nanoTime();
        int[] primitiveArray = new int[n];
        for (int i = 0; i < n; i++) primitiveArray[i] = i; // no boxing
        long primitiveTime = System.nanoTime() - start;

        System.out.printf("  List<Integer> fill %,d: %,d ns (boxing overhead)%n", n, boxedTime);
        System.out.printf("  int[] fill %,d:        %,d ns (no boxing)%n", n, primitiveTime);
        System.out.println("  → Use int[] when you have large amounts of primitives\n");

        // Mistake 3 — already shown in section 8 (ConcurrentModificationException)
        System.out.println("Mistake 3: Remove in for-each → See Section 8 for correct approaches");

        // Mistake 4 — Arrays.asList returns fixed-size list
        System.out.println("\nMistake 4: Arrays.asList() returns FIXED-SIZE list");
        List<String> fixed = Arrays.asList("A", "B", "C");
        try {
            fixed.add("D"); // ❌ throws UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("  ❌ Arrays.asList().add() → UnsupportedOperationException");
            System.out.println("  ✅ Use: new ArrayList<>(Arrays.asList(...)) for mutable copy");
            System.out.println("  ✅ Use: List.of(...) for immutable list (Java 9+)");
        }
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 13 — Interview Summary
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * INTERVIEW DISCUSSION POINTS — KEY ANSWERS
     * -------------------------------------------
     *
     * Q: Why is ArrayList faster than LinkedList for most operations?
     * A: Cache locality. ArrayList stores elements in contiguous memory.
     *    CPU caches a block of memory at once. Next element is already cached.
     *    LinkedList nodes are scattered in heap → each access = cache miss.
     *
     * Q: Why is LinkedList rarely used in practice?
     * A: Despite O(1) head insert on paper, cache misses make it slower
     *    than ArrayList in benchmarks even for those cases. ArrayList
     *    is preferred unless head/tail operations truly dominate AND
     *    you have profiling data to prove LinkedList wins.
     *
     * Q: What is amortized complexity?
     * A: The average cost per operation over a sequence of operations.
     *    ArrayList add() is O(1) amortized because most adds are O(1),
     *    and the rare O(n) resize cost is spread across all adds.
     *
     * Q: Why does LinkedList have poor cache performance?
     * A: Nodes are allocated at different heap locations. Accessing node N
     *    doesn't help the CPU cache load node N+1. Each access = potential
     *    cache miss = expensive memory fetch (100+ cycles vs 1-4 for cache hit).
     *
     * Q: How does ArrayList resizing work?
     * A: When capacity is full → allocate new array of size * 1.5 → copy
     *    all elements → add new element. Trigger: size == elementData.length.
     *    newCapacity = oldCapacity + (oldCapacity >> 1)
     */
    static void section13_InterviewSummary() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 13: Interview Cheat Sheet");
        System.out.println("─────────────────────────────────────────");

        System.out.println("""
                ARRAYLIST
                  Internal:    Object[] elementData (contiguous memory)
                  Resize:      newCapacity = old + (old >> 1)  ≈ 1.5x
                  get(i):      O(1)    → direct array index
                  add(end):    O(1)*   → amortized
                  add(i):      O(n)    → shift right
                  remove(i):   O(n)    → shift left
                  contains:    O(n)    → linear scan
                  Best for:    reads, iteration, random access
                
                LINKEDLIST
                  Internal:    Doubly linked nodes (scattered memory)
                  Node:        { item, prev, next }  ≈ 40 bytes each
                  addFirst:    O(1)    → update head pointer
                  addLast:     O(1)    → update tail pointer
                  get(i):      O(n)    → traverse to index
                  add(i):      O(n)    → traverse + O(1) pointer update
                  remove(i):   O(n)    → traverse + O(1) pointer update
                  Best for:    queue/deque, frequent head operations
                
                GOLDEN RULES
                  1. Default to ArrayList — it wins 90% of benchmarks
                  2. Pre-allocate: new ArrayList<>(expectedSize)
                  3. Never use LinkedList for get(index)
                  4. Use Iterator.remove() or removeIf() in loops
                  5. Profile first, optimize second
                  6. Use Deque<T> / ArrayDeque<T> over LinkedList for queues
                     ArrayDeque is faster than LinkedList even for head ops
                """);

        // ArrayDeque mention — often overlooked but faster than LinkedList
        System.out.println("BONUS — ArrayDeque vs LinkedList as Queue:");
        Deque<Integer> arrayDeque  = new ArrayDeque<>();
        Deque<Integer> linkedDeque = new LinkedList<>();

        int n = 50_000;

        long start = System.nanoTime();
        for (int i = 0; i < n; i++) { arrayDeque.addFirst(i); arrayDeque.pollLast(); }
        System.out.printf("  ArrayDeque  (head ops, n=%,d): %,d ns%n", n, System.nanoTime() - start);

        start = System.nanoTime();
        for (int i = 0; i < n; i++) { linkedDeque.addFirst(i); linkedDeque.pollLast(); }
        System.out.printf("  LinkedList  (head ops, n=%,d): %,d ns%n", n, System.nanoTime() - start);

        System.out.println("\n  → ArrayDeque uses a circular array internally.");
        System.out.println("    It is cache-friendly AND has O(1) head/tail ops.");
        System.out.println("    Prefer ArrayDeque over LinkedList for Queue/Deque use cases.");
        System.out.println("\n=== END OF LIST DEEP DIVE ===");
    }
}
