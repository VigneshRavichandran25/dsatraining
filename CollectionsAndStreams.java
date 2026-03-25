import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * ============================================================
 *  COLLECTIONS FRAMEWORK & STREAMS DEEP DIVE
 *  Run: javac CollectionsAndStreams.java && java CollectionsAndStreams
 * ============================================================
 *
 *  WHAT IS THE COLLECTIONS FRAMEWORK?
 *  ------------------------------------
 *  A unified architecture for storing, manipulating, and accessing data.
 *
 *  INTERFACE HIERARCHY:
 *
 *    Iterable<T>
 *      └── Collection<T>
 *            ├── List<T>       → ordered, duplicates allowed
 *            │     ├── ArrayList
 *            │     └── LinkedList
 *            ├── Set<T>        → no duplicates
 *            │     ├── HashSet
 *            │     ├── LinkedHashSet
 *            │     └── TreeSet
 *            └── Queue<T>      → FIFO ordering
 *                  ├── LinkedList
 *                  ├── PriorityQueue
 *                  └── Deque → ArrayDeque, LinkedList
 *
 *    Map<K,V>   (separate hierarchy — not a Collection)
 *          ├── HashMap
 *          ├── TreeMap
 *          ├── LinkedHashMap
 *          └── ConcurrentHashMap
 *
 *  KEY DESIGN PRINCIPLES:
 *    - All Collection implementations implement Iterable<T>
 *    - Algorithms (sort, shuffle, min, max) live in java.util.Collections
 *    - Streams (Java 8+) layer functional operations on top of any source
 */
public class CollectionsAndStreams {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== COLLECTIONS FRAMEWORK & STREAMS DEEP DIVE ===\n");

        section1_CollectionsFrameworkOverview();
        section2_IterableAndIterator();
        section3_FailFastVsFailSafe();
        section4_ComparableVsComparator();
        section5_CollectionsUtilityClass();
        section6_StreamPipelineBasics();
        section7_IntermediateOperations();
        section8_TerminalOperations();
        section9_MapVsFlatMap();
        section10_ReduceAndCollect();
        section11_FunctionalInterfaces();
        section12_MethodReferences();
        section13_Collectors();
        section14_OptionalClass();
        section15_ParallelStreams();
        section16_RealWorldLogProcessing();
        section17_RealWorldDataPipeline();
        section18_CommonMistakes();
        section19_PerformanceComparison();
        section20_InterviewSummary();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 1 — Collections Framework Overview
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * COLLECTIONS FRAMEWORK ARCHITECTURE
     * ------------------------------------
     * Three pillars:
     *   1. INTERFACES  — abstract data types (List, Set, Queue, Map)
     *   2. IMPLEMENTATIONS — concrete classes (ArrayList, HashSet, ...)
     *   3. ALGORITHMS  — static methods in Collections utility class
     *
     * WHY USE IT?
     *   - Reusable, well-tested implementations
     *   - Standardized API across all data structures
     *   - Interoperability: pass any List where Collection expected
     *   - Performance optimized for common use cases
     */
    static void section1_CollectionsFrameworkOverview() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 1: Collections Framework Overview");
        System.out.println("─────────────────────────────────────────");

        // List — ordered, indexed, duplicates allowed
        List<String> list = new ArrayList<>(List.of("Banana", "Apple", "Cherry", "Apple"));
        System.out.println("List (ordered, duplicates): " + list);
        System.out.println("  index-based access: list.get(1) = " + list.get(1));

        // Set — no duplicates, unordered (HashSet) or ordered (TreeSet)
        Set<String>  hashSet = new HashSet<>(list);
        Set<String>  treeSet = new TreeSet<>(list);
        System.out.println("\nHashSet (no dups, no order):  " + hashSet);
        System.out.println("TreeSet (no dups, sorted):    " + treeSet);

        // Queue — FIFO processing
        Queue<String> queue = new LinkedList<>(List.of("Task1", "Task2", "Task3"));
        System.out.println("\nQueue (FIFO): " + queue);
        System.out.println("  poll() removes head: " + queue.poll() + " → remaining: " + queue);

        // Deque — double-ended queue (stack or queue)
        Deque<String> deque = new ArrayDeque<>();
        deque.addFirst("B");
        deque.addFirst("A");
        deque.addLast("C");
        System.out.println("\nDeque (both ends): " + deque);
        System.out.println("  peekFirst=" + deque.peekFirst() + "  peekLast=" + deque.peekLast());

        // PriorityQueue — always dequeues the smallest element (min-heap)
        PriorityQueue<Integer> pq = new PriorityQueue<>(List.of(5, 1, 3, 2, 4));
        System.out.print("\nPriorityQueue poll order (min-heap): ");
        while (!pq.isEmpty()) System.out.print(pq.poll() + " ");
        System.out.println();

        // Map — key-value pairs (not a Collection)
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("Alice", 90); map.put("Bob", 75); map.put("Carol", 88);
        System.out.println("\nLinkedHashMap (insertion order): " + map);
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 2 — Iterable and Iterator
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * ITERABLE & ITERATOR
     * --------------------
     * Every Collection implements Iterable<T>, which has one method:
     *   Iterator<T> iterator()
     *
     * Iterator<T> has three methods:
     *   boolean hasNext()   → true if more elements remain
     *   T       next()      → returns next element, advances cursor
     *   void    remove()    → removes last element returned by next() — SAFE removal
     *
     * The for-each loop is syntactic sugar for an Iterator:
     *   for (T item : collection) → expands to Iterator usage
     *
     * ListIterator (only for List):
     *   Extends Iterator with:
     *   - hasPrevious() / previous() — bidirectional traversal
     *   - add() — insert at current position
     *   - set() — replace last returned element
     *   - nextIndex() / previousIndex()
     */
    static void section2_IterableAndIterator() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 2: Iterable and Iterator");
        System.out.println("─────────────────────────────────────────");

        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        // Basic Iterator
        System.out.println("Basic Iterator traversal:");
        Iterator<Integer> it = numbers.iterator();
        System.out.print("  ");
        while (it.hasNext()) {
            int val = it.next();
            System.out.print(val + " ");
        }
        System.out.println();

        // Safe removal via Iterator.remove()
        Iterator<Integer> removeIt = numbers.iterator();
        while (removeIt.hasNext()) {
            if (removeIt.next() % 2 == 0) removeIt.remove(); // remove evens safely
        }
        System.out.println("After removing evens via iterator: " + numbers);

        // ListIterator — bidirectional + add + set
        List<String> words = new ArrayList<>(List.of("cat", "dog", "bird"));
        ListIterator<String> lit = words.listIterator();
        System.out.println("\nListIterator — forward:");
        while (lit.hasNext()) {
            String w = lit.next();
            System.out.print("  [" + lit.previousIndex() + "]=" + w);
            lit.set(w.toUpperCase()); // replace in-place
        }
        System.out.println("\nAfter set() to uppercase: " + words);

        System.out.println("ListIterator — backward:");
        while (lit.hasPrevious()) {
            System.out.print("  " + lit.previous());
        }
        System.out.println();

        // Custom Iterable — demonstrate the contract
        // For-each desugars to:
        System.out.println("\nFor-each is syntactic sugar for iterator:");
        List<String> items = List.of("X", "Y", "Z");
        // Equivalent to:
        Iterator<String> sugar = items.iterator();
        while (sugar.hasNext()) {
            System.out.print("  " + sugar.next());
        }
        System.out.println(" ← same as for-each\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 3 — Fail-Fast vs Fail-Safe
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * FAIL-FAST BEHAVIOR
     * -------------------
     * Most java.util collections (ArrayList, HashMap, HashSet, ...) are FAIL-FAST.
     *
     * They maintain an internal modCount (modification count).
     * When an Iterator is created, it captures expectedModCount = modCount.
     * On every next() / hasNext(), it checks: modCount == expectedModCount?
     *   If NO → throw ConcurrentModificationException immediately (fail fast)
     *   If YES → continue
     *
     * This is a "best-effort" safeguard — not a guarantee of thread safety.
     * It exists to catch bugs early, not for production concurrency control.
     *
     * FAIL-SAFE COLLECTIONS
     * ----------------------
     * java.util.concurrent collections (CopyOnWriteArrayList, ConcurrentHashMap)
     * are FAIL-SAFE — they do NOT throw CME.
     *
     * CopyOnWriteArrayList: on every write, copies the entire array.
     *   Iterators see a snapshot of the array at iterator creation time.
     *   Writes are expensive (O(n)), reads are fast and never lock.
     *   Best for: read-heavy, rarely-written lists.
     *
     * ConcurrentHashMap iterator: weakly consistent — reflects state at or
     *   after iterator creation, but may or may not see concurrent writes.
     */
    static void section3_FailFastVsFailSafe() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 3: Fail-Fast vs Fail-Safe");
        System.out.println("─────────────────────────────────────────");

        // Fail-fast: ConcurrentModificationException
        System.out.println("Fail-fast (ArrayList):");
        List<Integer> failFast = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        try {
            for (Integer val : failFast) {
                if (val == 3) failFast.remove(val); // ❌ modifies modCount
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("  ❌ ConcurrentModificationException — modCount mismatch detected");
        }

        // Fix 1: Iterator.remove()
        List<Integer> list1 = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Iterator<Integer> it = list1.iterator();
        while (it.hasNext()) if (it.next() == 3) it.remove();
        System.out.println("  ✅ Fix 1 — iterator.remove():         " + list1);

        // Fix 2: removeIf (Java 8)
        List<Integer> list2 = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        list2.removeIf(v -> v == 3);
        System.out.println("  ✅ Fix 2 — removeIf(pred):            " + list2);

        // Fix 3: collect + reassign (streams)
        List<Integer> list3 = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        list3 = list3.stream().filter(v -> v != 3).collect(Collectors.toList());
        System.out.println("  ✅ Fix 3 — stream().filter():         " + list3);

        // Fail-safe: CopyOnWriteArrayList — NO CME
        System.out.println("\nFail-safe (CopyOnWriteArrayList):");
        List<Integer> failSafe = new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4, 5));
        for (Integer val : failSafe) {
            if (val == 3) failSafe.remove(val); // iterator sees snapshot — no CME
        }
        System.out.println("  ✅ No CME — iterator sees snapshot: " + failSafe);

        // Tradeoff — CopyOnWriteArrayList write cost
        System.out.println("\nCopyOnWriteArrayList tradeoff:");
        System.out.println("  Read:  O(1) — no locking, always safe");
        System.out.println("  Write: O(n) — copies entire array on every add/remove");
        System.out.println("  Best for: small, read-heavy, rarely-mutated lists");
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 4 — Comparable vs Comparator
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * COMPARABLE — NATURAL ORDERING (built into the class)
     * ------------------------------------------------------
     * Implement java.lang.Comparable<T> to define ONE natural ordering.
     * Used by:  TreeSet, TreeMap, Collections.sort(), Arrays.sort()
     *
     * compareTo() contract:
     *   negative → this comes BEFORE other
     *   zero     → this equals other (should be consistent with equals())
     *   positive → this comes AFTER other
     *
     * COMPARATOR — EXTERNAL ORDERING (separate from the class)
     * ---------------------------------------------------------
     * java.util.Comparator<T> defines any ordering externally.
     * Can define MULTIPLE orderings for the same class.
     * Passed to: TreeSet(comp), TreeMap(comp), Collections.sort(list, comp)
     *
     * Java 8+ Comparator factory methods:
     *   Comparator.comparing(fn)           → compare by field
     *   Comparator.comparingInt(fn)        → compare by int field (avoids boxing)
     *   comparator.thenComparing(fn)       → secondary sort
     *   comparator.reversed()             → flip direction
     *   Comparator.nullsFirst(comp)        → nulls before non-nulls
     *   Comparator.naturalOrder()          → uses Comparable.compareTo()
     */
    static void section4_ComparableVsComparator() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 4: Comparable vs Comparator");
        System.out.println("─────────────────────────────────────────");

        record Employee(String name, String dept, int salary, int age)
                implements Comparable<Employee> {
            // Natural ordering: by name alphabetically
            @Override public int compareTo(Employee other) {
                return this.name.compareTo(other.name);
            }
        }

        List<Employee> employees = new ArrayList<>(List.of(
            new Employee("Carol", "Engineering", 120_000, 35),
            new Employee("Alice", "Marketing",    80_000, 28),
            new Employee("Dave",  "HR",            70_000, 42),
            new Employee("Bob",   "Engineering",  110_000, 31)
        ));

        // Natural ordering (Comparable.compareTo — by name)
        Collections.sort(employees);
        System.out.println("Natural order (by name):");
        employees.forEach(e -> System.out.printf("  %-6s %-12s $%,d%n",
            e.name(), e.dept(), e.salary()));

        // External Comparator — by salary descending
        Comparator<Employee> bySalaryDesc =
            Comparator.comparingInt(Employee::salary).reversed();
        employees.sort(bySalaryDesc);
        System.out.println("\nComparator (by salary desc):");
        employees.forEach(e -> System.out.printf("  %-6s $%,d%n", e.name(), e.salary()));

        // Chained Comparator — by dept then by salary desc within dept
        Comparator<Employee> byDeptThenSalary =
            Comparator.comparing(Employee::dept)
                      .thenComparingInt(Employee::salary).reversed();
        employees.sort(byDeptThenSalary);
        System.out.println("\nComparator (by dept, then salary desc):");
        employees.forEach(e -> System.out.printf("  %-12s %-6s $%,d%n",
            e.dept(), e.name(), e.salary()));

        // Comparator with nulls
        List<String> withNulls = new ArrayList<>(
            Arrays.asList("Banana", null, "Apple", null, "Cherry")
        );
        withNulls.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        System.out.println("\nnullsFirst: " + withNulls);
        withNulls.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        System.out.println("nullsLast:  " + withNulls);
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 5 — Collections Utility Class
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * java.util.Collections — ALGORITHMS ON COLLECTIONS
     * ---------------------------------------------------
     * Static utility methods that operate on any Collection or List.
     *
     * Sorting:     sort(), reverseOrder(), unmodifiableList()
     * Searching:   binarySearch() → requires sorted list, O(log n)
     * Extremes:    min(), max()
     * Mutation:    shuffle(), reverse(), rotate(), swap(), fill(), copy()
     * Wrappers:    unmodifiableList/Set/Map, synchronizedList/Set/Map,
     *              singletonList(), emptyList(), nCopies()
     * Frequency:   frequency(), disjoint()
     *
     * NOTE: Collections.sort() uses TimSort — O(n log n) worst case,
     *       O(n) for nearly-sorted input. Stable sort.
     */
    static void section5_CollectionsUtilityClass() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 5: Collections Utility Class");
        System.out.println("─────────────────────────────────────────");

        List<Integer> nums = new ArrayList<>(List.of(3, 1, 4, 1, 5, 9, 2, 6, 5, 3));
        System.out.println("Original:   " + nums);

        Collections.sort(nums);
        System.out.println("sorted():   " + nums);

        Collections.reverse(nums);
        System.out.println("reverse():  " + nums);

        Collections.shuffle(nums, new Random(42));
        System.out.println("shuffle():  " + nums);

        Collections.sort(nums); // sort again for binarySearch
        int idx = Collections.binarySearch(nums, 5);
        System.out.println("binarySearch(5) in sorted: index=" + idx
                           + " value=" + nums.get(idx));

        System.out.println("min(): " + Collections.min(nums));
        System.out.println("max(): " + Collections.max(nums));
        System.out.println("frequency(5): " + Collections.frequency(nums, 5));

        // Rotate — shifts elements right by distance
        List<Integer> rotList = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Collections.rotate(rotList, 2);
        System.out.println("rotate([1..5], 2): " + rotList); // [4, 5, 1, 2, 3]

        // Unmodifiable wrapper
        List<Integer> immutable = Collections.unmodifiableList(nums);
        try {
            immutable.add(99);
        } catch (UnsupportedOperationException e) {
            System.out.println("unmodifiableList.add() → UnsupportedOperationException ✅");
        }

        // Singleton collections — immutable, exactly one element
        List<String> single = Collections.singletonList("only");
        Set<Integer> emptySet = Collections.emptySet();
        System.out.println("singletonList: " + single + "  emptySet: " + emptySet);
        System.out.println("nCopies(3, \"ha\"): " + Collections.nCopies(3, "ha"));
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 6 — Stream Pipeline Basics
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * STREAMS (Java 8+)
     * ------------------
     * A Stream<T> is a sequence of elements that supports sequential and
     * parallel aggregate operations. It is NOT a data structure — it does not
     * store elements. It is a pipeline that processes elements from a source.
     *
     * Three-part pipeline:
     *   SOURCE → INTERMEDIATE OPS (0 or more, lazy) → TERMINAL OP (1, eager)
     *
     * KEY PROPERTIES:
     *   LAZY:      Intermediate operations are NOT executed until a terminal
     *              operation is invoked. This enables short-circuit evaluation.
     *
     *   NON-REUSABLE: Once a terminal operation is called, the stream is consumed.
     *              Calling any method on a consumed stream → IllegalStateException.
     *
     *   NON-MUTATING: Streams do not modify the source collection.
     *              They produce new streams or results.
     *
     * SOURCES:
     *   collection.stream()           → from any Collection
     *   collection.parallelStream()   → parallel stream
     *   Stream.of(a, b, c)            → from varargs
     *   Stream.iterate(seed, fn)      → infinite stream
     *   Stream.generate(supplier)     → infinite stream
     *   Arrays.stream(array)          → from array
     *   IntStream.range(0, n)         → primitive int stream
     *   Files.lines(path)             → stream of file lines
     */
    static void section6_StreamPipelineBasics() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 6: Stream Pipeline Basics");
        System.out.println("─────────────────────────────────────────");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Basic pipeline: source → filter → map → collect
        List<Integer> result = numbers.stream()           // source
            .filter(n -> n % 2 == 0)                      // intermediate: keep evens
            .map(n -> n * n)                              // intermediate: square them
            .collect(Collectors.toList());                // terminal: gather
        System.out.println("filter(even).map(square): " + result);

        // Laziness demo — operations execute only when terminal is reached
        System.out.println("\nLaziness demo:");
        long count = numbers.stream()
            .filter(n -> {
                System.out.print("  filter(" + n + ") ");
                return n > 7;
            })
            .peek(n -> System.out.print("→ peek(" + n + ") "))
            .count(); // terminal triggers the pipeline
        System.out.println("\n  count = " + count);

        // Short-circuit — findFirst stops processing after first match
        System.out.println("\nShort-circuit (findFirst stops early):");
        Optional<Integer> first = numbers.stream()
            .filter(n -> {
                System.out.print("  check(" + n + ") ");
                return n > 5;
            })
            .findFirst();
        System.out.println("\n  found = " + first.get());

        // Primitive streams — avoid boxing overhead
        System.out.println("\nPrimitive streams (no boxing):");
        int sumInt = IntStream.rangeClosed(1, 10).sum();         // 1+2+...+10
        double avg = IntStream.rangeClosed(1, 10).average().orElse(0);
        System.out.println("  IntStream.rangeClosed(1,10).sum() = " + sumInt);
        System.out.println("  IntStream.rangeClosed(1,10).avg() = " + avg);

        // Stream.iterate — infinite stream (must limit!)
        System.out.println("\nStream.iterate (infinite — limited to 8):");
        List<Integer> powers = Stream.iterate(1, n -> n * 2)
            .limit(8)
            .collect(Collectors.toList());
        System.out.println("  Powers of 2: " + powers);

        // Stream.generate — infinite supplier
        List<Double> randoms = Stream.generate(Math::random)
            .limit(3)
            .map(d -> Math.round(d * 100) / 100.0)
            .collect(Collectors.toList());
        System.out.println("  Stream.generate(random).limit(3): " + randoms);
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 7 — Intermediate Operations
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * INTERMEDIATE OPERATIONS — all return Stream<T> (lazy)
     * -------------------------------------------------------
     * filter(Predicate)          → keep elements matching predicate
     * map(Function)              → transform each element (1-to-1)
     * flatMap(Function)          → transform + flatten (1-to-many)
     * sorted()                   → natural order sort (stateful, O(n log n))
     * sorted(Comparator)         → custom sort
     * distinct()                 → remove duplicates (uses equals/hashCode)
     * limit(n)                   → keep first n elements (short-circuits)
     * skip(n)                    → skip first n elements
     * peek(Consumer)             → side effect without consuming (debug tool)
     * mapToInt/Long/Double       → convert to primitive stream (avoids boxing)
     * boxed()                    → IntStream → Stream<Integer>
     * takeWhile(Predicate)       → take while predicate true (Java 9+, short-circuits)
     * dropWhile(Predicate)       → drop while predicate true (Java 9+)
     *
     * STATELESS vs STATEFUL operations:
     *   Stateless: filter, map, peek  → no memory across elements, parallelizable easily
     *   Stateful:  sorted, distinct, limit, skip → require buffering, limit parallelism
     */
    static void section7_IntermediateOperations() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 7: Intermediate Operations");
        System.out.println("─────────────────────────────────────────");

        List<String> words = List.of("banana", "apple", "cherry", "apple",
                                     "date", "elderberry", "fig", "banana");
        System.out.println("Input words: " + words);

        // filter — keep words longer than 4 characters
        List<String> filtered = words.stream()
            .filter(w -> w.length() > 4)
            .collect(Collectors.toList());
        System.out.println("filter(len>4):    " + filtered);

        // map — transform to uppercase
        List<String> mapped = words.stream()
            .map(String::toUpperCase)
            .distinct()
            .collect(Collectors.toList());
        System.out.println("map(upper)+distinct: " + mapped);

        // sorted — natural order; sorted(comp) — custom
        List<String> sortedByLength = words.stream()
            .distinct()
            .sorted(Comparator.comparingInt(String::length))
            .collect(Collectors.toList());
        System.out.println("sorted(by length): " + sortedByLength);

        // limit + skip — pagination
        List<String> page2 = words.stream()
            .distinct()
            .sorted()
            .skip(2)    // skip first 2
            .limit(3)   // take next 3
            .collect(Collectors.toList());
        System.out.println("skip(2).limit(3): " + page2);

        // peek — debugging pipeline (does NOT consume)
        System.out.println("peek (debug trace):");
        words.stream()
            .distinct()
            .filter(w -> w.length() > 5)
            .peek(w -> System.out.print("  after-filter: " + w))
            .map(String::toUpperCase)
            .peek(w -> System.out.print(" → after-map: " + w + "\n"))
            .limit(2)
            .count(); // trigger

        // takeWhile / dropWhile (Java 9+)
        List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> taken  = nums.stream().takeWhile(n -> n < 5).collect(Collectors.toList());
        List<Integer> dropped= nums.stream().dropWhile(n -> n < 5).collect(Collectors.toList());
        System.out.println("takeWhile(n<5): " + taken);
        System.out.println("dropWhile(n<5): " + dropped);

        // mapToInt — avoid Integer boxing, get stats
        IntSummaryStatistics stats = words.stream()
            .mapToInt(String::length)
            .summaryStatistics();
        System.out.printf("Word length stats: min=%d max=%d avg=%.1f sum=%d count=%d%n",
            stats.getMin(), stats.getMax(), stats.getAverage(),
            stats.getSum(), stats.getCount());
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 8 — Terminal Operations
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * TERMINAL OPERATIONS — consume the stream, trigger pipeline execution
     * ----------------------------------------------------------------------
     * forEach(Consumer)          → iterate, side-effect (no return value)
     * collect(Collector)         → gather into collection or other result
     * reduce(identity, BinaryOp) → fold to single value
     * count()                    → number of elements
     * min(Comparator)            → Optional<T> minimum
     * max(Comparator)            → Optional<T> maximum
     * findFirst()                → Optional<T> first element (short-circuits)
     * findAny()                  → Optional<T> any element (better for parallel)
     * anyMatch(Predicate)        → true if ANY match (short-circuits)
     * allMatch(Predicate)        → true if ALL match (short-circuits)
     * noneMatch(Predicate)       → true if NONE match (short-circuits)
     * toArray()                  → Object[] or T[]
     * sum() / average()          → on IntStream/LongStream/DoubleStream only
     *
     * SHORT-CIRCUIT TERMINAL OPS: findFirst, findAny, anyMatch, allMatch, noneMatch
     *   These stop processing as soon as the result is known.
     *   Example: anyMatch stops at the first matching element.
     */
    static void section8_TerminalOperations() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 8: Terminal Operations");
        System.out.println("─────────────────────────────────────────");

        List<Integer> nums = List.of(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5);
        System.out.println("Input: " + nums);

        // count
        long cnt = nums.stream().filter(n -> n > 4).count();
        System.out.println("count(n>4): " + cnt);

        // min / max
        Optional<Integer> min = nums.stream().min(Integer::compareTo);
        Optional<Integer> max = nums.stream().max(Integer::compareTo);
        System.out.println("min: " + min.get() + "  max: " + max.get());

        // findFirst — short-circuit, always returns first in encounter order
        Optional<Integer> first = nums.stream().filter(n -> n > 5).findFirst();
        System.out.println("findFirst(n>5): " + first.get());

        // anyMatch / allMatch / noneMatch — all short-circuit
        System.out.println("anyMatch(n>8):  " + nums.stream().anyMatch(n -> n > 8));
        System.out.println("allMatch(n>0):  " + nums.stream().allMatch(n -> n > 0));
        System.out.println("noneMatch(n>10):" + nums.stream().noneMatch(n -> n > 10));

        // toArray
        Integer[] arr = nums.stream().distinct().sorted().toArray(Integer[]::new);
        System.out.println("toArray (distinct sorted): " + Arrays.toString(arr));

        // forEach — side-effect only (no return value)
        System.out.print("forEach(n>7): ");
        nums.stream().filter(n -> n > 7).forEach(n -> System.out.print(n + " "));
        System.out.println();

        // Consuming a stream twice → IllegalStateException
        System.out.println("\nStream reuse (illegal):");
        Stream<Integer> stream = nums.stream().filter(n -> n > 5);
        stream.count(); // consumes the stream
        try {
            stream.count(); // ❌ stream already consumed
        } catch (IllegalStateException e) {
            System.out.println("  ❌ IllegalStateException: stream already operated upon");
        }
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 9 — map vs flatMap
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * MAP vs FLATMAP
     * ---------------
     * map(Function<T, R>)
     *   → Transforms each element T to one R.
     *   → Output stream has same number of elements as input stream.
     *   → If the function returns a Stream, you get Stream<Stream<R>>.
     *
     * flatMap(Function<T, Stream<R>>)
     *   → Transforms each element T to a Stream<R>.
     *   → FLATTENS all those streams into one Stream<R>.
     *   → Total elements = sum of sizes of all produced streams.
     *   → Essential for working with nested collections.
     *
     * Visual:
     *   map:     [1, 2, 3] → fn → [[a,b], [c], [d,e,f]]  → Stream<List<>>
     *   flatMap: [1, 2, 3] → fn → [a, b, c, d, e, f]      → Stream<>
     *
     * mapToInt/Long/Double: like map but produces primitive stream (no boxing).
     */
    static void section9_MapVsFlatMap() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 9: map vs flatMap");
        System.out.println("─────────────────────────────────────────");

        List<List<Integer>> nested = List.of(
            List.of(1, 2, 3),
            List.of(4, 5),
            List.of(6, 7, 8, 9)
        );

        // map → Stream<List<Integer>> — NOT what we usually want
        List<List<Integer>> mappedNested = nested.stream()
            .map(innerList -> innerList.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList()))
            .collect(Collectors.toList());
        System.out.println("map (keeps structure): " + mappedNested);

        // flatMap → Stream<Integer> — flattened
        List<Integer> flatMapped = nested.stream()
            .flatMap(Collection::stream) // each inner List → Stream
            .collect(Collectors.toList());
        System.out.println("flatMap (flattened):   " + flatMapped);

        // flatMap with filter — flatten AND filter in one step
        List<Integer> evenFlat = nested.stream()
            .flatMap(Collection::stream)
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("flatMap + filter evens: " + evenFlat);

        // Real use: sentences → words
        List<String> sentences = List.of(
            "hello world",
            "java streams are powerful",
            "flat map rocks"
        );
        List<String> allWords = sentences.stream()
            .flatMap(s -> Arrays.stream(s.split(" ")))
            .sorted()
            .distinct()
            .collect(Collectors.toList());
        System.out.println("\nSentences → words (flatMap + split):");
        System.out.println("  " + allWords);

        // Real use: orders → items
        record Order(String id, List<String> items) {}
        List<Order> orders = List.of(
            new Order("O1", List.of("Apple", "Banana")),
            new Order("O2", List.of("Cherry")),
            new Order("O3", List.of("Apple", "Date", "Elderberry"))
        );
        List<String> allItems = orders.stream()
            .flatMap(o -> o.items().stream())
            .distinct()
            .sorted()
            .collect(Collectors.toList());
        System.out.println("All ordered items (flatMap orders): " + allItems);
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 10 — Reduce and Collect
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * REDUCE — FOLD A STREAM TO A SINGLE VALUE
     * -----------------------------------------
     * reduce(identity, BinaryOperator<T>)
     *   → identity: starting value (returned if stream is empty)
     *   → BinaryOperator: (accumulated, current) → new accumulated
     *
     * reduce(BinaryOperator<T>)
     *   → No identity → returns Optional<T> (empty if stream empty)
     *
     * reduce(identity, BiFunction, BinaryOperator)
     *   → Three-arg form for parallel-safe reduction with type change
     *
     * For primitive streams: sum(), average(), min(), max(), summaryStatistics()
     *   are more efficient than reduce() because they avoid boxing.
     *
     * COLLECT — MUTABLE REDUCTION
     * ----------------------------
     * collect(Collector) gathers into a mutable result container.
     * More efficient than reduce() for building collections because
     * it reuses the same mutable container rather than creating
     * new immutable intermediate results.
     */
    static void section10_ReduceAndCollect() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 10: Reduce and Collect");
        System.out.println("─────────────────────────────────────────");

        List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println("Input: " + nums);

        // reduce — sum
        int sum = nums.stream().reduce(0, Integer::sum);
        System.out.println("reduce(0, Integer::sum) = " + sum);

        // reduce — product
        int product = nums.stream().reduce(1, (a, b) -> a * b);
        System.out.println("reduce(1, multiply)     = " + product);

        // reduce — max (no identity → Optional)
        Optional<Integer> max = nums.stream().reduce(Integer::max);
        System.out.println("reduce(max)             = " + max.get());

        // reduce — concatenate strings
        List<String> words = List.of("Java", "Streams", "Are", "Powerful");
        String sentence = words.stream().reduce("", (a, b) -> a + (a.isEmpty() ? "" : " ") + b);
        System.out.println("reduce(concat):         = " + sentence);

        // reduce — custom accumulation (sum of squares)
        int sumOfSquares = nums.stream().reduce(0, (acc, n) -> acc + n * n);
        System.out.println("reduce(sum of squares)  = " + sumOfSquares);

        // IntStream.sum() — prefer over reduce for primitives (no boxing)
        int intSum = nums.stream().mapToInt(Integer::intValue).sum();
        System.out.println("mapToInt().sum()        = " + intSum + " ← preferred for ints");

        // collect vs reduce for building lists
        System.out.println("\ncollect vs reduce for list building:");
        List<Integer> via_collect = nums.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("  collect (mutable reuse):  " + via_collect);

        // Running total using scan-like pattern
        System.out.println("\nRunning total (prefix sum) via Stream.iterate:");
        int[] arr = {1, 2, 3, 4, 5};
        int[] prefix = new int[arr.length];
        IntStream.range(0, arr.length).forEach(i ->
            prefix[i] = (i == 0 ? 0 : prefix[i-1]) + arr[i]
        );
        System.out.println("  Input:  " + Arrays.toString(arr));
        System.out.println("  Prefix: " + Arrays.toString(prefix));
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 11 — Functional Interfaces
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * FUNCTIONAL INTERFACES (java.util.function)
     * --------------------------------------------
     * A functional interface has EXACTLY ONE abstract method (SAM — Single Abstract Method).
     * Annotated with @FunctionalInterface (optional but recommended).
     * Can be implemented with a lambda or method reference.
     *
     * Core four:
     *   Predicate<T>        → T → boolean           test(T t)
     *   Function<T,R>       → T → R                 apply(T t)
     *   Consumer<T>         → T → void              accept(T t)
     *   Supplier<T>         → ()  → T               get()
     *
     * Variants:
     *   BiPredicate<T,U>    → (T,U) → boolean
     *   BiFunction<T,U,R>   → (T,U) → R
     *   BiConsumer<T,U>     → (T,U) → void
     *   UnaryOperator<T>    → T → T                 (Function<T,T>)
     *   BinaryOperator<T>   → (T,T) → T             (BiFunction<T,T,T>)
     *
     * Primitive specializations (avoid boxing):
     *   IntPredicate, IntFunction<R>, IntConsumer, IntSupplier
     *   IntUnaryOperator, IntBinaryOperator
     *   ToIntFunction<T>, IntToLongFunction, etc.
     *
     * Composition:
     *   Predicate:  and(), or(), negate()
     *   Function:   andThen(fn), compose(fn)
     *   Consumer:   andThen(consumer)
     */
    static void section11_FunctionalInterfaces() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 11: Functional Interfaces");
        System.out.println("─────────────────────────────────────────");

        // Predicate<T> — test condition, return boolean
        Predicate<Integer> isEven    = n -> n % 2 == 0;
        Predicate<Integer> isPositive= n -> n > 0;
        Predicate<String>  isLong    = s -> s.length() > 5;

        System.out.println("Predicate:");
        System.out.println("  isEven(4):       " + isEven.test(4));
        System.out.println("  isEven(7):       " + isEven.test(7));

        // Predicate composition
        Predicate<Integer> isEvenAndPositive = isEven.and(isPositive);
        Predicate<Integer> isEvenOrPositive  = isEven.or(isPositive);
        Predicate<Integer> isOdd             = isEven.negate();
        System.out.println("  isEven.and(isPositive).test(4):  " + isEvenAndPositive.test(4));
        System.out.println("  isEven.and(isPositive).test(-4): " + isEvenAndPositive.test(-4));
        System.out.println("  isEven.negate().test(3):         " + isOdd.test(3));

        List<Integer> nums = List.of(-3, -1, 0, 2, 4, 7, 9);
        List<Integer> evenAndPos = nums.stream().filter(isEvenAndPositive).collect(Collectors.toList());
        System.out.println("  filter(even AND positive): " + evenAndPos);

        // Function<T,R> — transform T → R
        Function<String, Integer> strlen   = String::length;
        Function<Integer, String> toStr    = Object::toString;
        Function<String, String>  addBang  = s -> s + "!";

        System.out.println("\nFunction:");
        System.out.println("  strlen(\"hello\"): " + strlen.apply("hello"));

        // Function composition: andThen (f then g), compose (g then f)
        Function<String, String> lenThenStr = strlen.andThen(toStr);
        System.out.println("  strlen.andThen(toStr).apply(\"hello\"): " + lenThenStr.apply("hello"));

        Function<String, String> upperThenBang =
            ((Function<String,String>) String::toUpperCase).andThen(addBang);
        System.out.println("  toUpper.andThen(addBang)(\"hello\"): " + upperThenBang.apply("hello"));

        // Consumer<T> — consume T, return void
        Consumer<String>  print    = System.out::println;
        Consumer<String>  printLen = s -> System.out.println("  len=" + s.length());
        Consumer<String>  both     = print.andThen(printLen); // chained consumers

        System.out.println("\nConsumer:");
        System.out.print("  print.andThen(printLen)(\"Java\"): ");
        both.accept("Java");

        // Supplier<T> — produce T from nothing
        Supplier<List<String>> listFactory  = ArrayList::new;
        Supplier<String>       greeting     = () -> "Hello, World!";
        Supplier<Double>       randomSupply = Math::random;

        System.out.println("Supplier:");
        System.out.println("  listFactory.get() type: " + listFactory.get().getClass().getSimpleName());
        System.out.println("  greeting.get():         " + greeting.get());

        // BiFunction<T,U,R>
        BiFunction<String, Integer, String> repeat = (s, n) -> s.repeat(n);
        System.out.println("\nBiFunction repeat(\"ha\", 3): " + repeat.apply("ha", 3));

        // UnaryOperator and BinaryOperator
        UnaryOperator<String>   toUpper  = String::toUpperCase;
        BinaryOperator<Integer> multiply = (a, b) -> a * b;
        System.out.println("UnaryOperator toUpper(\"java\"): " + toUpper.apply("java"));
        System.out.println("BinaryOperator multiply(6,7):  " + multiply.apply(6, 7));
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 12 — Method References
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * METHOD REFERENCES — shorthand for lambdas that call a single method
     * ---------------------------------------------------------------------
     * Four types:
     *
     * 1. Static method:          ClassName::staticMethod
     *    Lambda equivalent:      (args) -> ClassName.staticMethod(args)
     *    Example:                Integer::parseInt
     *
     * 2. Instance method on specific instance:  instance::instanceMethod
     *    Lambda equivalent:      (args) -> instance.instanceMethod(args)
     *    Example:                System.out::println
     *
     * 3. Instance method on arbitrary instance: ClassName::instanceMethod
     *    Lambda equivalent:      (obj, args) -> obj.instanceMethod(args)
     *    Example:                String::toUpperCase  (the String IS the arg)
     *
     * 4. Constructor:            ClassName::new
     *    Lambda equivalent:      (args) -> new ClassName(args)
     *    Example:                ArrayList::new
     *
     * Method references are NOT always shorter than lambdas, but they
     * are preferred when they make intent clearer. Use whichever reads better.
     */
    static void section12_MethodReferences() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 12: Method References");
        System.out.println("─────────────────────────────────────────");

        List<String> names = List.of("Alice", "Bob", "Carol", "Dave");

        // Type 1: Static method reference
        List<Integer> lengths = names.stream()
            .map(String::length)           // ClassName::instanceMethod (type 3)
            .collect(Collectors.toList());
        System.out.println("String::length (type 3): " + lengths);

        List<String> numStrings = List.of("1", "2", "3", "4", "5");
        List<Integer> parsed = numStrings.stream()
            .map(Integer::parseInt)        // ClassName::staticMethod (type 1)
            .collect(Collectors.toList());
        System.out.println("Integer::parseInt (type 1): " + parsed);

        // Type 2: Instance method on specific instance
        System.out.print("System.out::println (type 2): ");
        names.stream().forEach(System.out::println); // instance::instanceMethod

        // Type 3: Instance method on arbitrary instance (object is the parameter)
        List<String> upper = names.stream()
            .map(String::toUpperCase)      // equivalent to: s -> s.toUpperCase()
            .collect(Collectors.toList());
        System.out.println("String::toUpperCase (type 3): " + upper);

        boolean anyStartsA = names.stream()
            .anyMatch("Alice"::equals);    // specific instance (type 2)
        System.out.println("\"Alice\"::equals anyMatch: " + anyStartsA);

        // Type 4: Constructor reference
        List<List<Integer>> lists = Stream.of(
            List.of(3, 1, 2),
            List.of(6, 4, 5)
        ).map(ArrayList::new)             // new ArrayList<>(existing list)
         .collect(Collectors.toList());
        System.out.println("ArrayList::new (type 4): " + lists);

        // Supplier with constructor reference
        Supplier<StringBuilder> sbFactory = StringBuilder::new;
        StringBuilder sb = sbFactory.get();
        names.forEach(sb::append);
        System.out.println("StringBuilder::new Supplier: " + sb);

        // Comparator with method reference
        List<String> sorted = names.stream()
            .sorted(String::compareTo)     // method ref as Comparator
            .collect(Collectors.toList());
        System.out.println("sorted(String::compareTo): " + sorted);
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 13 — Collectors
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * COLLECTORS (java.util.stream.Collectors)
     * -----------------------------------------
     * Factory methods for common collection operations.
     *
     * Gathering:
     *   toList()              → ArrayList (unordered, allows dups)
     *   toSet()               → HashSet
     *   toUnmodifiableList()  → immutable List (Java 10+)
     *   toCollection(fn)      → any Collection type
     *   toMap(keyFn, valueFn) → HashMap (throws on duplicate keys)
     *   toMap(kFn, vFn, merge)→ HashMap with merge for duplicate keys
     *
     * Grouping:
     *   groupingBy(classifier)           → Map<K, List<T>>
     *   groupingBy(classifier, downstream) → Map<K, R> with downstream collector
     *   partitioningBy(predicate)        → Map<Boolean, List<T>>
     *
     * Aggregation:
     *   counting()            → Long
     *   summingInt/Long/Double(fn) → numeric sum
     *   averagingInt/Long/Double(fn) → Double average
     *   summarizingInt(fn)    → IntSummaryStatistics
     *   minBy(comp) / maxBy(comp) → Optional<T>
     *
     * String:
     *   joining()             → concatenate
     *   joining(delimiter)    → comma-separated
     *   joining(delim, prefix, suffix) → wrapped
     *
     * Transformation:
     *   mapping(fn, downstream) → transform then collect
     *   collectingAndThen(downstream, fn) → transform result after collecting
     *   teeing(c1, c2, fn)    → collect into two collectors, merge (Java 12+)
     */
    static void section13_Collectors() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 13: Collectors");
        System.out.println("─────────────────────────────────────────");

        record Product(String name, String category, double price, int stock) {}
        List<Product> products = List.of(
            new Product("Laptop",   "Electronics", 999.99, 50),
            new Product("Phone",    "Electronics", 699.99, 120),
            new Product("Desk",     "Furniture",   299.99, 30),
            new Product("Chair",    "Furniture",   199.99, 80),
            new Product("Notebook", "Stationery",    4.99, 500),
            new Product("Pen",      "Stationery",    1.99, 1000),
            new Product("Tablet",   "Electronics", 499.99, 75)
        );

        // toList / toSet / toUnmodifiableList
        List<String> names = products.stream()
            .map(Product::name).collect(Collectors.toList());
        System.out.println("toList (names): " + names);

        // joining
        String joined = products.stream()
            .map(Product::name)
            .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("joining: " + joined);

        // groupingBy — Map<category, List<Product>>
        Map<String, List<Product>> byCategory = products.stream()
            .collect(Collectors.groupingBy(Product::category));
        System.out.println("\ngroupingBy(category):");
        new TreeMap<>(byCategory).forEach((cat, prods) ->
            System.out.println("  " + cat + ": " +
                prods.stream().map(Product::name).collect(Collectors.joining(", "))));

        // groupingBy with downstream — count per category
        Map<String, Long> countByCategory = products.stream()
            .collect(Collectors.groupingBy(Product::category, Collectors.counting()));
        System.out.println("groupingBy + counting: " + new TreeMap<>(countByCategory));

        // groupingBy + averagingDouble
        Map<String, Double> avgPriceByCategory = products.stream()
            .collect(Collectors.groupingBy(
                Product::category,
                Collectors.averagingDouble(Product::price)));
        System.out.println("groupingBy + averagingDouble:");
        new TreeMap<>(avgPriceByCategory).forEach((cat, avg) ->
            System.out.printf("  %-12s $%.2f%n", cat, avg));

        // partitioningBy — two groups: expensive (>= 300) or not
        Map<Boolean, List<String>> partitioned = products.stream()
            .collect(Collectors.partitioningBy(
                p -> p.price() >= 300,
                Collectors.mapping(Product::name, Collectors.toList())));
        System.out.println("partitioningBy(price>=300):");
        System.out.println("  Expensive: " + partitioned.get(true));
        System.out.println("  Cheap:     " + partitioned.get(false));

        // toMap — name → price
        Map<String, Double> nameToPrice = products.stream()
            .collect(Collectors.toMap(Product::name, Product::price));
        System.out.println("\ntoMap (name→price) sample: Laptop=$"
                           + nameToPrice.get("Laptop"));

        // summarizingDouble — full stats in one pass
        DoubleSummaryStatistics priceStats = products.stream()
            .collect(Collectors.summarizingDouble(Product::price));
        System.out.printf("price stats: min=%.2f max=%.2f avg=%.2f sum=%.2f%n",
            priceStats.getMin(), priceStats.getMax(),
            priceStats.getAverage(), priceStats.getSum());

        // collectingAndThen — immutable result
        List<String> immutableNames = products.stream()
            .map(Product::name)
            .collect(Collectors.collectingAndThen(
                Collectors.toList(), Collections::unmodifiableList));
        System.out.println("collectingAndThen (unmodifiable): " + immutableNames.getClass().getSimpleName());
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 14 — Optional
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * OPTIONAL<T> — A CONTAINER THAT MAY OR MAY NOT CONTAIN A VALUE
     * ---------------------------------------------------------------
     * Introduced in Java 8 to reduce NullPointerExceptions.
     * Signals that a method might not return a value — making absence explicit.
     *
     * Creation:
     *   Optional.of(value)        → non-null value (NPE if null)
     *   Optional.ofNullable(val)  → value or empty if null
     *   Optional.empty()          → empty Optional
     *
     * Accessing:
     *   get()                     → value or NoSuchElementException
     *   orElse(default)           → value or default
     *   orElseGet(Supplier)       → value or computed default (lazy)
     *   orElseThrow(Supplier)     → value or throw custom exception
     *
     * Conditional:
     *   isPresent()               → true if value exists
     *   isEmpty()                 → true if empty (Java 11+)
     *   ifPresent(Consumer)       → run consumer if present
     *   ifPresentOrElse(c, runnable) → run one of two actions (Java 9+)
     *
     * Transformation:
     *   map(Function)             → transform value if present
     *   flatMap(Function)         → for when function returns Optional
     *   filter(Predicate)         → keep value if predicate passes
     *   stream()                  → Stream<T> of 0 or 1 element (Java 9+)
     *
     * Best practices:
     *   - Use as RETURN TYPE of methods, not as field type or parameter type
     *   - Never call get() without isPresent() check
     *   - Prefer orElse / orElseGet / orElseThrow over isPresent + get
     */
    static void section14_OptionalClass() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 14: Optional<T>");
        System.out.println("─────────────────────────────────────────");

        // Creation
        Optional<String> present = Optional.of("Hello");
        Optional<String> empty   = Optional.empty();
        Optional<String> nullable= Optional.ofNullable(null);

        System.out.println("present.isPresent(): " + present.isPresent());
        System.out.println("empty.isEmpty():     " + empty.isEmpty());

        // orElse vs orElseGet
        String val1 = empty.orElse("default");               // always evaluates "default"
        String val2 = empty.orElseGet(() -> "computed");     // lazy — only called if empty
        System.out.println("orElse:    " + val1);
        System.out.println("orElseGet: " + val2);

        // orElseThrow
        try {
            empty.orElseThrow(() -> new IllegalStateException("No value!"));
        } catch (IllegalStateException e) {
            System.out.println("orElseThrow: " + e.getMessage());
        }

        // map + filter chain (no null checks needed)
        Optional<String> upper = present
            .filter(s -> s.length() > 3)
            .map(String::toUpperCase);
        System.out.println("filter(len>3).map(upper): " + upper);

        Optional<String> shortEmpty = Optional.of("Hi")
            .filter(s -> s.length() > 3);   // filtered out
        System.out.println("filter(len>3) on \"Hi\":    " + shortEmpty);

        // Real use: findFirst in stream returns Optional
        List<String> names = List.of("Alice", "Bob", "Carol");
        String firstLong = names.stream()
            .filter(n -> n.length() > 4)
            .findFirst()
            .map(String::toUpperCase)
            .orElse("NONE");
        System.out.println("findFirst(len>4).upper: " + firstLong);

        // Optional.stream() — integrate with stream pipeline (Java 9+)
        List<Optional<String>> optionals = List.of(
            Optional.of("apple"), Optional.empty(), Optional.of("banana"), Optional.empty()
        );
        List<String> present2 = optionals.stream()
            .flatMap(Optional::stream)  // Optional.stream() = 0 or 1 element
            .collect(Collectors.toList());
        System.out.println("flatMap(Optional::stream): " + present2);
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 15 — Parallel Streams
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * PARALLEL STREAMS
     * -----------------
     * A parallel stream splits the source into multiple chunks, processes
     * each chunk on a separate thread from ForkJoinPool.commonPool(),
     * and merges the results.
     *
     * collection.parallelStream()       → parallel from start
     * stream.parallel()                 → convert sequential to parallel
     * stream.sequential()               → convert parallel to sequential
     * stream.isParallel()               → check if currently parallel
     *
     * WHEN PARALLEL HELPS:
     *   - Large datasets (100,000+ elements typically)
     *   - CPU-bound, computationally intensive operations
     *   - Stateless intermediate operations (filter, map)
     *   - Operations that can be split and merged (reduce, sum)
     *
     * WHEN PARALLEL HURTS (or gives WRONG results):
     *   - Small datasets — thread coordination overhead > work
     *   - I/O-bound tasks — threads block waiting for I/O, not CPU
     *   - Stateful operations that accumulate into shared mutable state
     *   - Ordered operations on unordered sources (findFirst vs findAny)
     *   - Operations on synchronized collections (Hashtable, synchronizedList)
     *
     * ORDERING:
     *   parallel() on an ordered source still produces ordered results
     *   for collect(), but at higher cost. Use unordered() to relax this.
     *
     * THREAD SAFETY:
     *   Lambda functions in parallel streams MUST be stateless and non-interfering.
     *   Never write to external shared variables inside parallel stream lambdas.
     */
    static void section15_ParallelStreams() throws InterruptedException {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 15: Parallel Streams");
        System.out.println("─────────────────────────────────────────");

        int n = 5_000_000;
        List<Integer> bigList = IntStream.range(1, n + 1)
            .boxed().collect(Collectors.toList());

        // Sequential vs parallel — sum of squares
        long start = System.nanoTime();
        long seqSum = bigList.stream()
            .mapToLong(x -> (long) x * x)
            .sum();
        long seqTime = System.nanoTime() - start;

        start = System.nanoTime();
        long parSum = bigList.parallelStream()
            .mapToLong(x -> (long) x * x)
            .sum();
        long parTime = System.nanoTime() - start;

        System.out.printf("Sum of squares (n=%,d):%n", n);
        System.out.printf("  Sequential: %,d ns  result=%,d%n", seqTime, seqSum);
        System.out.printf("  Parallel:   %,d ns  result=%,d%n", parTime, parSum);
        System.out.printf("  Speedup:    %.1fx (cores=%d)%n",
            (double) seqTime / parTime,
            Runtime.getRuntime().availableProcessors());

        // WRONG: parallel + shared mutable state
        System.out.println("\n❌ Parallel + shared mutable state (RACE CONDITION):");
        List<Integer> unsafeList = new ArrayList<>();
        try {
            IntStream.range(0, 1000).parallel()
                .forEach(unsafeList::add); // ❌ ArrayList not thread-safe
        } catch (Exception e) {
            System.out.println("  Exception: " + e.getClass().getSimpleName());
        }
        System.out.println("  unsafeList.size()=" + unsafeList.size()
                           + " (expected 1000, got " + unsafeList.size()
                           + " ← elements lost!)");

        // CORRECT: collect into thread-safe result
        System.out.println("✅ Parallel + collect (thread-safe):");
        List<Integer> safeList = IntStream.range(0, 1000).parallel()
            .boxed()
            .collect(Collectors.toList()); // collect handles thread-safety internally
        System.out.println("  safeList.size()=" + safeList.size() + " ← correct");

        // CORRECT: parallel + reduce (associative operation)
        long parallelSum = IntStream.range(1, 1001).parallel()
            .reduce(0, Integer::sum);
        System.out.println("  parallel reduce sum(1..1000)=" + parallelSum);

        // Small dataset — parallel is slower
        System.out.println("\nSmall dataset (n=100) — parallel overhead dominates:");
        List<Integer> small = IntStream.range(1, 101).boxed().collect(Collectors.toList());

        start = System.nanoTime();
        long s1 = small.stream().mapToLong(x -> x * x).sum();
        long t1 = System.nanoTime() - start;

        start = System.nanoTime();
        long s2 = small.parallelStream().mapToLong(x -> x * x).sum();
        long t2 = System.nanoTime() - start;

        System.out.printf("  Sequential: %,d ns%n", t1);
        System.out.printf("  Parallel:   %,d ns  (%.1fx SLOWER)%n", t2,
            t2 > t1 ? (double) t2 / t1 : 1.0);
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 16 — Real World: Log Processing
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * REAL WORLD — LOG FILE PROCESSING
     * ----------------------------------
     * Streams excel at filter-transform-aggregate pipelines on large datasets.
     * Log processing is a canonical use case:
     *   - Filter by level (ERROR, WARN, INFO)
     *   - Extract fields
     *   - Group by component or time window
     *   - Count occurrences
     */
    static void section16_RealWorldLogProcessing() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 16: Real World — Log Processing");
        System.out.println("─────────────────────────────────────────");

        record LogEntry(String timestamp, String level, String component, String message) {
            static LogEntry parse(String raw) {
                String[] p = raw.split("\\|");
                return new LogEntry(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim());
            }
        }

        List<String> rawLogs = List.of(
            "10:01:05 | INFO  | Auth    | User alice logged in",
            "10:01:12 | ERROR | DB      | Connection timeout after 30s",
            "10:01:18 | WARN  | Cache   | Cache miss rate above 80%",
            "10:01:25 | ERROR | Auth    | Invalid token for user bob",
            "10:01:31 | INFO  | API     | GET /products 200 OK",
            "10:01:45 | ERROR | DB      | Deadlock detected on table orders",
            "10:01:52 | WARN  | Auth    | Rate limit approaching for IP 192.168.1.1",
            "10:02:01 | INFO  | API     | POST /orders 201 Created",
            "10:02:15 | ERROR | Cache   | Redis connection refused",
            "10:02:22 | INFO  | Auth    | User carol logged in"
        );

        List<LogEntry> logs = rawLogs.stream()
            .map(LogEntry::parse)
            .collect(Collectors.toList());

        // Filter errors
        System.out.println("ERROR logs:");
        logs.stream()
            .filter(l -> l.level().equals("ERROR"))
            .forEach(l -> System.out.println("  [" + l.timestamp() + "] "
                                             + l.component() + ": " + l.message()));

        // Count by level
        Map<String, Long> countByLevel = logs.stream()
            .collect(Collectors.groupingBy(LogEntry::level, Collectors.counting()));
        System.out.println("\nLog counts by level: " + new TreeMap<>(countByLevel));

        // Errors by component
        Map<String, List<String>> errorsByComponent = logs.stream()
            .filter(l -> l.level().equals("ERROR"))
            .collect(Collectors.groupingBy(
                LogEntry::component,
                Collectors.mapping(LogEntry::message, Collectors.toList())));
        System.out.println("\nERRORs grouped by component:");
        new TreeMap<>(errorsByComponent).forEach((comp, msgs) -> {
            System.out.println("  " + comp + " (" + msgs.size() + " errors):");
            msgs.forEach(m -> System.out.println("    - " + m));
        });

        // Most problematic component
        Optional<Map.Entry<String, Long>> topProblem = logs.stream()
            .filter(l -> !l.level().equals("INFO"))
            .collect(Collectors.groupingBy(LogEntry::component, Collectors.counting()))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue());
        topProblem.ifPresent(e ->
            System.out.println("\nMost problematic component: "
                               + e.getKey() + " (" + e.getValue() + " warnings/errors)"));
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 17 — Real World: Data Pipeline
    // ─────────────────────────────────────────────────────────────────────────
    static void section17_RealWorldDataPipeline() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 17: Real World — Sales Data Pipeline");
        System.out.println("─────────────────────────────────────────");

        record SaleRecord(String region, String product, int quantity, double unitPrice) {
            double total() { return quantity * unitPrice; }
        }

        List<SaleRecord> sales = List.of(
            new SaleRecord("North", "Laptop",  5,  999.99),
            new SaleRecord("South", "Phone",   12, 699.99),
            new SaleRecord("North", "Tablet",  8,  499.99),
            new SaleRecord("East",  "Laptop",  3,  999.99),
            new SaleRecord("West",  "Phone",   20, 699.99),
            new SaleRecord("South", "Laptop",  7,  999.99),
            new SaleRecord("East",  "Tablet",  15, 499.99),
            new SaleRecord("North", "Phone",   10, 699.99),
            new SaleRecord("West",  "Laptop",  4,  999.99),
            new SaleRecord("East",  "Phone",   8,  699.99)
        );

        // Total revenue
        double totalRevenue = sales.stream()
            .mapToDouble(SaleRecord::total)
            .sum();
        System.out.printf("Total revenue: $%,.2f%n", totalRevenue);

        // Revenue by region (sorted desc)
        System.out.println("\nRevenue by region:");
        sales.stream()
            .collect(Collectors.groupingBy(
                SaleRecord::region,
                Collectors.summingDouble(SaleRecord::total)))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(e -> System.out.printf("  %-6s $%,.2f%n", e.getKey(), e.getValue()));

        // Top selling product by quantity
        System.out.println("\nQuantity by product:");
        sales.stream()
            .collect(Collectors.groupingBy(
                SaleRecord::product,
                Collectors.summingInt(SaleRecord::quantity)))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(e -> System.out.printf("  %-8s %d units%n", e.getKey(), e.getValue()));

        // Average order size by region
        System.out.println("\nAvg units per order by region:");
        sales.stream()
            .collect(Collectors.groupingBy(
                SaleRecord::region,
                Collectors.averagingInt(SaleRecord::quantity)))
            .entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(e -> System.out.printf("  %-6s %.1f units%n", e.getKey(), e.getValue()));

        // Orders exceeding $5000 in a single record
        System.out.println("\nHigh-value orders (total > $5,000):");
        sales.stream()
            .filter(s -> s.total() > 5000)
            .sorted(Comparator.comparingDouble(SaleRecord::total).reversed())
            .forEach(s -> System.out.printf("  %-6s %-8s qty=%-3d $%,.2f%n",
                s.region(), s.product(), s.quantity(), s.total()));
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 18 — Common Mistakes
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * COMMON MISTAKES WITH STREAMS AND COLLECTIONS
     * ----------------------------------------------
     *
     * 1. Mutating source or external state inside a lambda
     *    → Violates the non-interfering contract; undefined behavior in parallel
     *
     * 2. Reusing a consumed stream → IllegalStateException
     *
     * 3. Using parallelStream() blindly
     *    → Slower for small data; wrong results with shared mutable state
     *
     * 4. Infinite stream without limit/findFirst → hangs forever
     *
     * 5. Using forEach for transformation — use map instead
     *    → forEach is for side effects only
     *
     * 6. Sorting a large stream without need
     *    → sorted() is stateful (must see all elements) and O(n log n)
     *
     * 7. Calling Optional.get() without isPresent() check
     *    → NoSuchElementException
     *
     * 8. Streams of primitive wrappers (Stream<Integer>) instead of IntStream
     *    → Unnecessary boxing/unboxing overhead
     *
     * 9. peek() in production logic (it's a debugging tool, not a transform)
     *
     * 10. Not closing streams over I/O resources
     *     → Use try-with-resources for Files.lines(), etc.
     */
    static void section18_CommonMistakes() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 18: Common Mistakes");
        System.out.println("─────────────────────────────────────────");

        // Mistake 1 — mutating external state in lambda (wrong)
        System.out.println("Mistake 1: Mutating external state");
        List<Integer> nums = List.of(1, 2, 3, 4, 5);
        List<Integer> result = new ArrayList<>();
        // WRONG: side effect inside stream
        nums.stream().filter(n -> n % 2 == 0).forEach(result::add); // works but fragile
        System.out.println("  Works sequentially but fragile in parallel: " + result);
        // CORRECT: collect
        List<Integer> correct = nums.stream()
            .filter(n -> n % 2 == 0).collect(Collectors.toList());
        System.out.println("  ✅ Use collect: " + correct);

        // Mistake 2 — reusing consumed stream
        System.out.println("\nMistake 2: Reusing a stream");
        Stream<Integer> s = nums.stream();
        s.forEach(n -> {}); // consumes
        try { s.count(); } catch (IllegalStateException e) {
            System.out.println("  ❌ IllegalStateException: stream already consumed");
        }

        // Mistake 3 — infinite stream without terminal guard
        System.out.println("\nMistake 3: Infinite stream must have limit/findFirst");
        long count = Stream.iterate(0, n -> n + 1)
            .filter(n -> n % 7 == 0)
            .limit(5)                    // ← MUST have this
            .count();
        System.out.println("  ✅ First 5 multiples of 7 count: " + count);

        // Mistake 4 — Optional.get() without check
        System.out.println("\nMistake 4: Optional.get() without check");
        Optional<String> empty = Optional.empty();
        try { empty.get(); } catch (NoSuchElementException e) {
            System.out.println("  ❌ get() on empty → NoSuchElementException");
            System.out.println("  ✅ Use orElse / orElseGet / orElseThrow");
        }

        // Mistake 5 — Stream<Integer> vs IntStream
        System.out.println("\nMistake 5: Stream<Integer> vs IntStream boxing");
        int N = 1_000_000;
        long start = System.nanoTime();
        long s1 = Stream.iterate(1, n -> n + 1).limit(N)
            .mapToLong(Integer::longValue).sum(); // boxing per element
        long t1 = System.nanoTime() - start;

        start = System.nanoTime();
        long s2 = LongStream.rangeClosed(1, N).sum(); // no boxing
        long t2 = System.nanoTime() - start;

        System.out.printf("  Stream<Integer> (boxed) sum: %,d ns%n", t1);
        System.out.printf("  LongStream (primitive) sum:  %,d ns  (%.1fx faster)%n",
            t2, (double) t1 / t2);

        // Mistake 6 — forEach instead of map
        System.out.println("\nMistake 6: forEach for transformation (wrong)");
        List<String> words = List.of("hello", "world");
        List<String> bad = new ArrayList<>();
        words.forEach(w -> bad.add(w.toUpperCase())); // ❌ side-effect approach
        System.out.println("  ❌ forEach+add: " + bad);
        List<String> good = words.stream().map(String::toUpperCase).collect(Collectors.toList());
        System.out.println("  ✅ map+collect: " + good);
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 19 — Performance Comparison
    // ─────────────────────────────────────────────────────────────────────────
    static void section19_PerformanceComparison() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 19: Performance — Loop vs Stream vs Parallel");
        System.out.println("─────────────────────────────────────────");

        int n = 2_000_000;
        List<Integer> data = IntStream.range(1, n + 1).boxed().collect(Collectors.toList());

        // Task: filter evens, square them, sum
        // Traditional for-loop
        long start = System.nanoTime();
        long loopSum = 0;
        for (int val : data) { if (val % 2 == 0) loopSum += (long) val * val; }
        long loopTime = System.nanoTime() - start;

        // Sequential stream
        start = System.nanoTime();
        long seqSum = data.stream()
            .filter(v -> v % 2 == 0)
            .mapToLong(v -> (long) v * v)
            .sum();
        long seqTime = System.nanoTime() - start;

        // Parallel stream
        start = System.nanoTime();
        long parSum = data.parallelStream()
            .filter(v -> v % 2 == 0)
            .mapToLong(v -> (long) v * v)
            .sum();
        long parTime = System.nanoTime() - start;

        System.out.printf("Filter evens, square, sum (n=%,d):%n", n);
        System.out.printf("  For-loop:        %,8d ns  result=%,d%n", loopTime, loopSum);
        System.out.printf("  Stream seq:      %,8d ns  result=%,d%n", seqTime, seqSum);
        System.out.printf("  Stream parallel: %,8d ns  result=%,d%n", parTime, parSum);
        System.out.printf("%n  Loop vs Stream:    loop is %.1fx %s%n",
            loopTime > seqTime ? (double)loopTime/seqTime : (double)seqTime/loopTime,
            loopTime > seqTime ? "SLOWER" : "faster");
        System.out.printf("  Parallel speedup:  %.1fx over sequential%n",
            (double) seqTime / parTime);
        System.out.println("\nConclusion:");
        System.out.println("  Loop ≈ Stream (sequential) for simple numeric ops");
        System.out.println("  Parallel wins for CPU-heavy, large, stateless ops");
        System.out.println("  Stream wins for readability and complex pipelines");
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SECTION 20 — Interview Summary
    // ─────────────────────────────────────────────────────────────────────────
    static void section20_InterviewSummary() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("SECTION 20: Interview Cheat Sheet");
        System.out.println("─────────────────────────────────────────");

        System.out.println("""
                COLLECTIONS HIERARCHY
                  Iterable → Collection → List (ArrayList, LinkedList)
                                        → Set  (HashSet, LinkedHashSet, TreeSet)
                                        → Queue (LinkedList, PriorityQueue, ArrayDeque)
                  Map (separate)        → HashMap, TreeMap, LinkedHashMap, ConcurrentHashMap

                ITERATOR
                  hasNext() / next() / remove() — only safe mid-iteration removal
                  ListIterator adds: hasPrevious() / previous() / add() / set()
                  Fail-fast: modCount check → ConcurrentModificationException
                  Fail-safe: CopyOnWriteArrayList, ConcurrentHashMap — no CME

                COMPARABLE vs COMPARATOR
                  Comparable:  built into class, one natural order, compareTo()
                  Comparator:  external, multiple orderings, compare()
                  Both:        return neg/zero/pos; zero means "same" for TreeSet/TreeMap

                STREAMS
                  Source → Intermediate (lazy) → Terminal (triggers pipeline)
                  Intermediate: filter, map, flatMap, sorted, distinct, limit, skip, peek
                  Terminal:     collect, forEach, reduce, count, min, max, findFirst,
                                anyMatch, allMatch, noneMatch, toArray
                  Short-circuit: findFirst, findAny, anyMatch, allMatch, noneMatch, limit

                FUNCTIONAL INTERFACES
                  Predicate<T>      T → boolean    (test, and, or, negate)
                  Function<T,R>     T → R          (apply, andThen, compose)
                  Consumer<T>       T → void       (accept, andThen)
                  Supplier<T>       () → T         (get)
                  BiFunction<T,U,R> (T,U) → R
                  UnaryOperator<T>  T → T
                  BinaryOperator<T> (T,T) → T

                METHOD REFERENCES (4 types)
                  ClassName::staticMethod       (type 1)
                  instance::instanceMethod      (type 2)
                  ClassName::instanceMethod     (type 3)
                  ClassName::new                (type 4)

                COLLECTORS
                  toList, toSet, toMap, toUnmodifiableList
                  groupingBy(fn) → Map<K, List<T>>
                  groupingBy(fn, downstream) → Map<K, R>
                  partitioningBy(pred) → Map<Boolean, List<T>>
                  joining(delim, prefix, suffix)
                  counting, summingInt, averagingDouble, summarizingDouble
                  collectingAndThen, mapping

                PARALLEL STREAMS
                  Use when:    large data (100k+), CPU-bound, stateless, associative ops
                  Avoid when:  small data, I/O-bound, shared mutable state, ordered ops
                  Thread pool: ForkJoinPool.commonPool()
                  Always safe: collect(), reduce() with associative function

                GOLDEN RULES
                  1.  Prefer collect() over forEach() for transformations
                  2.  Use IntStream/LongStream/DoubleStream to avoid boxing
                  3.  Never modify source or external state inside lambda
                  4.  Never reuse a consumed stream
                  5.  Always add limit() to infinite streams before a non-short-circuit terminal
                  6.  Prefer orElse/orElseGet over Optional.get()
                  7.  Use method references when they are clearer than lambdas
                  8.  Benchmark before using parallelStream()
                  9.  Fail-fast collections detect concurrent modification, not prevent it
                  10. sorted() is stateful — it must buffer all elements before emitting any
                """);

        System.out.println("Q&A:");
        System.out.println("  Q: Difference between Collection and Stream?");
        System.out.println("     Collection = data STORAGE (persistent, reusable, sized)");
        System.out.println("     Stream     = data PROCESSING pipeline (no storage, consumed once)");
        System.out.println("  Q: Why are streams lazy?");
        System.out.println("     Enables short-circuit (stop early), avoids unnecessary work");
        System.out.println("  Q: map vs flatMap?");
        System.out.println("     map: 1-to-1 transform. flatMap: 1-to-many + flatten");
        System.out.println("  Q: When is parallelStream() harmful?");
        System.out.println("     Small data, I/O-bound, shared mutable state → slower or wrong");
        System.out.println("\n=== END OF COLLECTIONS & STREAMS DEEP DIVE ===");
    }
}
