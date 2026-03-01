// ================================================================
//   JAVA STRINGS — Complete Concepts Guide
//   All Methods + Complexity Analysis
// ================================================================
//   Compile:  javac StringConcepts.java
//   Run:      java StringConcepts
// ================================================================
//
//   WHAT IS A STRING IN JAVA?
//   A String is a SEQUENCE OF CHARACTERS stored as an object.
//   In Java, String is IMMUTABLE — once created, it CANNOT be changed.
//   Every "modification" creates a NEW String object in memory.
//
//   KEY CLASSES:
//   String        → Immutable. Thread-safe. Stored in String Pool.
//   StringBuilder → Mutable. NOT thread-safe. Fast for single-thread.
//   StringBuffer  → Mutable. Thread-safe. Slower (synchronized).
//
//   TOPICS COVERED:
//   1. String Creation & the String Pool
//   2. String Comparison (== vs .equals() vs .compareTo())
//   3. String Methods (length, charAt, substring, indexOf, etc.)
//   4. String Immutability — why it matters
//   5. String Concatenation — + vs concat vs StringBuilder
//   6. StringBuilder & StringBuffer
//   7. String Formatting
//   8. String Splitting & Joining
//   9. Common String Algorithms (palindrome, anagram, frequency)
//  10. Real-World Use Cases
// ================================================================

import java.util.*;

public class StringConcepts {

    static void section(String title) {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.printf ("║  %-56s║%n", title);
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    static void sub(String title) {
        System.out.println("\n  ── " + title + " ──");
    }


    // ============================================================
    // SECTION 1 — STRING CREATION & THE STRING POOL
    // ============================================================
    //
    //   TWO WAYS to create a String:
    //
    //   a) String Literal  →  String s = "hello";
    //      Java looks in the STRING POOL (a special memory area in heap).
    //      If "hello" already exists there, Java REUSES the same object.
    //      If not, it creates a new one and adds it to the pool.
    //
    //   b) new keyword  →  String s = new String("hello");
    //      Forces creation of a NEW object in regular heap memory,
    //      BYPASSING the string pool — always a new object, even if
    //      "hello" already exists in the pool.
    //
    //   MEMORY DIAGRAM:
    //
    //   String Pool (inside Heap):
    //   ┌──────────────────────────────┐
    //   │  "hello"  ←── s1, s3 point here (same object!)
    //   │  "world"  ←── s4 points here
    //   └──────────────────────────────┘
    //
    //   Regular Heap:
    //   ┌──────────────────────────────┐
    //   │  "hello" ←── s2 (new object, different address)
    //   └──────────────────────────────┘
    //
    //   TIME:  O(1) to create a String literal (pool lookup/insert)
    //   SPACE: O(n) where n = number of characters
    // ============================================================

    static void demoStringCreation() {
        section("SECTION 1 — String Creation & the String Pool");

        String s1 = "hello";           // literal — goes to String Pool
        String s2 = new String("hello"); // new — goes to regular heap
        String s3 = "hello";           // literal — REUSES s1 from pool

        sub("== operator — compares MEMORY ADDRESSES (references)");
        System.out.println("  s1 = \"hello\" (literal)");
        System.out.println("  s2 = new String(\"hello\") (heap)");
        System.out.println("  s3 = \"hello\" (literal)");
        System.out.println();
        System.out.println("  s1 == s3 : " + (s1 == s3) +
                "  ← same pool object, same address");
        System.out.println("  s1 == s2 : " + (s1 == s2) +
                "  ← different memory locations");

        sub(".equals() — compares CONTENT (character by character)");
        System.out.println("  s1.equals(s2) : " + s1.equals(s2) +
                "  ← same characters, always use this for content comparison");
        System.out.println("  s1.equals(s3) : " + s1.equals(s3));

        sub("String.intern() — force pool storage for heap strings");
        String s4 = s2.intern(); // moves s2's content into pool (or returns existing)
        System.out.println("  s4 = s2.intern()");
        System.out.println("  s1 == s4 : " + (s1 == s4) +
                "  ← now pointing to same pool object");

        sub("Complexity");
        System.out.println("  Creation (literal)  : O(n) time, O(n) space — n = char count");
        System.out.println("  .equals(other)      : O(n) time — compares each character");
        System.out.println("  == comparison       : O(1) time — just compares addresses");
    }


    // ============================================================
    // SECTION 2 — STRING COMPARISON
    // ============================================================
    //
    //   THREE WAYS to compare Strings:
    //
    //   1. ==              → compares REFERENCES (addresses) — rarely what you want
    //   2. .equals()       → compares CONTENT — case sensitive
    //   3. .equalsIgnoreCase() → compares CONTENT — case insensitive
    //   4. .compareTo()    → lexicographic comparison — returns int
    //                        negative → s1 comes before s2
    //                        zero     → s1 equals s2
    //                        positive → s1 comes after s2
    //
    //   TIME: O(n) for equals/compareTo — worst case compare all n chars
    //   SPACE: O(1) — no extra memory needed
    // ============================================================

    static void demoStringComparison() {
        section("SECTION 2 — String Comparison");

        sub("equals() vs equalsIgnoreCase()");
        String city1 = "Chennai";
        String city2 = "chennai";
        System.out.println("  city1 = \"Chennai\", city2 = \"chennai\"");
        System.out.println("  city1.equals(city2)           : " + city1.equals(city2));
        System.out.println("  city1.equalsIgnoreCase(city2) : " + city1.equalsIgnoreCase(city2));

        sub("compareTo() — lexicographic ordering (like dictionary sort)");
        String a = "Apple";
        String b = "Banana";
        String c = "Apple";
        int result1 = a.compareTo(b); // negative: A comes before B
        int result2 = b.compareTo(a); // positive: B comes after A
        int result3 = a.compareTo(c); // zero: same content

        System.out.printf("  \"Apple\".compareTo(\"Banana\") = %d  → Apple comes first (negative)%n", result1);
        System.out.printf("  \"Banana\".compareTo(\"Apple\") = %d  → Banana comes last (positive)%n", result2);
        System.out.printf("  \"Apple\".compareTo(\"Apple\")  = %d   → Equal%n", result3);

        sub("Sorting strings with compareTo — real use: sorting names");
        String[] students = {"Ravi", "Navaneeth", "Priya", "Ananya", "Karthik"};
        Arrays.sort(students); // uses compareTo internally → O(n log n)
        System.out.println("  Sorted: " + Arrays.toString(students));

        sub("Complexity");
        System.out.println("  .equals()         : O(n) — compares char by char");
        System.out.println("  .compareTo()      : O(n) — until mismatch found");
        System.out.println("  Arrays.sort(str[]) : O(n log n) — n strings sorted");
    }


    // ============================================================
    // SECTION 3 — CORE STRING METHODS
    // ============================================================
    //
    //   Java's String class has 60+ methods.
    //   These are the ones used most frequently in engineering work.
    //
    //   REAL-WORLD USE CASE: User profile data validation & processing
    //   → Clean up inputs (trim, toLowerCase)
    //   → Extract parts (substring, split)
    //   → Validate format (contains, startsWith, endsWith, isEmpty)
    //   → Search within strings (indexOf, lastIndexOf)
    // ============================================================

    static void demoCoreStringMethods() {
        section("SECTION 3 — Core String Methods | Use Case: User Profile Processing");

        String raw = "  Navaneeth Kumar  "; // simulating user input with extra spaces

        sub("length(), trim(), strip() — O(n)");
        System.out.println("  raw input   : \"" + raw + "\"");
        System.out.println("  raw.length(): " + raw.length() + "  ← includes spaces");
        String trimmed = raw.trim(); // removes leading/trailing whitespace
        System.out.println("  trimmed     : \"" + trimmed + "\"");
        System.out.println("  trimmed.length(): " + trimmed.length());

        sub("toUpperCase(), toLowerCase() — O(n)");
        System.out.println("  toUpperCase(): \"" + trimmed.toUpperCase() + "\"");
        System.out.println("  toLowerCase(): \"" + trimmed.toLowerCase() + "\"");
        System.out.println("  Use case: normalize emails before saving to DB");

        sub("charAt(index) — O(1)");
        String name = "Navaneeth";
        System.out.println("  name = \"Navaneeth\"");
        System.out.println("  name.charAt(0) = '" + name.charAt(0) + "'  ← first character");
        System.out.println("  name.charAt(name.length()-1) = '" +
                name.charAt(name.length() - 1) + "'  ← last character");

        sub("substring(start, end) — O(n)");
        String email = "navaneeth@gmail.com";
        int atIndex  = email.indexOf('@');
        String user  = email.substring(0, atIndex);       // before @
        String domain = email.substring(atIndex + 1);     // after @
        System.out.println("  email  : " + email);
        System.out.println("  user   : " + user   + "  (substring 0 to @)");
        System.out.println("  domain : " + domain + "  (substring after @)");

        sub("indexOf(), lastIndexOf() — O(n)");
        String url = "https://www.github.com/navaneeth/signbridge";
        System.out.println("  url: " + url);
        System.out.println("  indexOf('/')         = " + url.indexOf('/') +
                "  ← first slash");
        System.out.println("  lastIndexOf('/')      = " + url.lastIndexOf('/') +
                "  ← last slash (repo name starts after)");
        System.out.println("  repo name: \"" +
                url.substring(url.lastIndexOf('/') + 1) + "\"");

        sub("contains(), startsWith(), endsWith() — O(n)");
        String filename = "signbridge_v1.java";
        System.out.println("  filename: \"" + filename + "\"");
        System.out.println("  contains(\"v1\")        : " + filename.contains("v1"));
        System.out.println("  startsWith(\"sign\")    : " + filename.startsWith("sign"));
        System.out.println("  endsWith(\".java\")     : " + filename.endsWith(".java"));

        sub("replace(), replaceAll() — O(n)");
        String phone = "98765-43210";
        String cleaned = phone.replace("-", ""); // remove dashes — O(n)
        System.out.println("  phone    : " + phone);
        System.out.println("  cleaned  : " + cleaned + "  ← replace(\"-\", \"\")");

        String input = "Hello   World   Java";
        String normalized = input.replaceAll("\\s+", " "); // regex: multiple spaces → one
        System.out.println("  multi-space input : \"" + input + "\"");
        System.out.println("  normalized        : \"" + normalized + "\"");

        sub("isEmpty(), isBlank() — O(1) / O(n)");
        System.out.println("  \"\".isEmpty()    : " + "".isEmpty() +
                "  ← O(1): checks length == 0");
        System.out.println("  \"  \".isEmpty()  : " + "  ".isEmpty() +
                "  ← false! spaces count");
        System.out.println("  \"  \".isBlank()  : " + "  ".isBlank() +
                "  ← O(n): checks all chars are whitespace");

        sub("toCharArray() — O(n)");
        char[] chars = name.toCharArray();
        System.out.print("  \"" + name + "\" as char array: [");
        for (int i = 0; i < chars.length; i++) {
            System.out.print("'" + chars[i] + "'" + (i < chars.length-1 ? ", " : ""));
        }
        System.out.println("]");

        sub("Complexity Summary — Core Methods");
        System.out.println("  Method                   Time   Space  Note");
        System.out.println("  ────────────────────────────────────────────────────");
        System.out.println("  length()                 O(1)   O(1)   Stored as field");
        System.out.println("  charAt(i)                O(1)   O(1)   Direct array access");
        System.out.println("  indexOf(str)             O(n*m) O(1)   n=length, m=pattern");
        System.out.println("  substring(s, e)          O(n)   O(n)   Creates new String");
        System.out.println("  toLowerCase/Upper        O(n)   O(n)   New String created");
        System.out.println("  trim() / strip()         O(n)   O(n)   New String created");
        System.out.println("  replace(old, new)        O(n)   O(n)   New String created");
        System.out.println("  contains(str)            O(n)   O(1)   Searches internally");
        System.out.println("  toCharArray()            O(n)   O(n)   Copies all chars");
        System.out.println("  isEmpty()                O(1)   O(1)   Checks length == 0");
    }


    // ============================================================
    // SECTION 4 — STRING IMMUTABILITY
    // ============================================================
    //
    //   Once a String is created, its content CANNOT change.
    //   Every method that "modifies" a String actually creates a NEW one.
    //
    //   WHY IMMUTABLE?
    //   → Thread safety: multiple threads can read the same String safely
    //   → String Pool works: pool only makes sense if strings never change
    //   → Security: passwords/paths can't be altered after validation
    //   → Caching: hashCode computed once and cached (used in HashMap keys)
    //
    //   THE TRAP: String concatenation in a loop
    //   Each "+" creates a new String object — O(n²) total!
    // ============================================================

    static void demoImmutability() {
        section("SECTION 4 — String Immutability");

        sub("Proof: every 'change' returns a NEW object");
        String original = "hello";
        String upper    = original.toUpperCase(); // new object
        String replaced = original.replace("l", "r"); // new object

        System.out.println("  original  : \"" + original + "\"  (unchanged)");
        System.out.println("  upper     : \"" + upper + "\"  (new object)");
        System.out.println("  replaced  : \"" + replaced + "\"  (new object)");
        System.out.println("  original after all changes: \"" + original +
                "\"  ← still \"hello\"!");

        sub("TRAP: String + in a loop → O(n²) time!");
        System.out.println("  Building a string of 5 words with + operator:");
        System.out.println("  Each step creates a new String — old one becomes garbage:");

        String result = "";
        String[] words = {"Hello", " ", "World", " ", "Java"};
        for (String w : words) {
            result = result + w;
            // Step 1: "" + "Hello"        → new object "Hello"          copy: 5
            // Step 2: "Hello" + " "       → new object "Hello "         copy: 6
            // Step 3: "Hello " + "World"  → new object "Hello World"    copy: 11
            // Step 4: "Hello World" + " " → new object "Hello World "   copy: 12
            // Step 5: "Hello World " + "Java" → "Hello World Java"      copy: 16
            // TOTAL COPIES: 5+6+11+12+16 = 50 → scales as O(n²) for n words
            System.out.println("    result = \"" + result + "\"  ← new String object each time");
        }

        sub("WHY this is O(n²): total characters copied");
        System.out.println("  Iteration 1: copy 5 chars");
        System.out.println("  Iteration 2: copy 6 chars");
        System.out.println("  Iteration 3: copy 11 chars ...");
        System.out.println("  For n words of avg length k: copies = k + 2k + 3k... = O(n²k) = O(n²)");
        System.out.println("  For 10,000 words: ~50 million character copies!");

        sub("Solution: StringBuilder — O(n) total");
        System.out.println("  Use StringBuilder when building strings in loops.");
        System.out.println("  (See Section 6 for full demo)");
    }


    // ============================================================
    // SECTION 5 — STRING CONCATENATION METHODS
    // ============================================================
    //
    //   Method 1: + operator    → creates new String each time → O(n²) in loops
    //   Method 2: .concat()     → same as +, slightly different internals
    //   Method 3: StringBuilder → append() is amortized O(1), best for loops
    //   Method 4: String.join() → clean way to join with delimiter
    //   Method 5: String.format()→ formatted output
    //
    //   TIME COMPLEXITY:
    //   + in loop of n strings  → O(n²) total
    //   StringBuilder in loop   → O(n)  total
    // ============================================================

    static void demoConcatenation() {
        section("SECTION 5 — Concatenation: + vs concat vs StringBuilder");

        sub("Live timing: + operator vs StringBuilder (10,000 iterations)");

        int N = 10_000;

        // String + in loop — O(n²)
        long t1 = System.currentTimeMillis();
        String s = "";
        for (int i = 0; i < N; i++) {
            s = s + "a"; // new String each iteration!
        }
        long time1 = System.currentTimeMillis() - t1;

        // StringBuilder in loop — O(n)
        long t2 = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            sb.append("a"); // modifies internal buffer, no new object
        }
        String sbResult = sb.toString();
        long time2 = System.currentTimeMillis() - t2;

        System.out.printf("  String + (loop, %,d iters)       : %d ms  — O(n²)%n", N, time1);
        System.out.printf("  StringBuilder (loop, %,d iters)  : %d ms  — O(n)%n", N, time2);
        System.out.println("  Both produce same length string: " + (s.length() == sbResult.length()));
        System.out.println("  ✓ StringBuilder is dramatically faster for large n.");

        sub("String.join() — clean joining with delimiter — O(n)");
        String joined1 = String.join(", ", "Navaneeth", "Priya", "Ravi", "Ananya");
        String joined2 = String.join(" | ", "Java", "Python", "JavaScript");
        List<String> tags = Arrays.asList("DSA", "OOP", "Recursion", "Strings");
        String joinedList = String.join(" → ", tags);

        System.out.println("  CSV names   : " + joined1);
        System.out.println("  Skills      : " + joined2);
        System.out.println("  Topic chain : " + joinedList);

        sub("String.format() — formatted output — O(n)");
        String name    = "Navaneeth";
        double gpa     = 9.2;
        int rank       = 1;
        String formatted = String.format(
                "Student: %-12s | GPA: %.1f | Rank: #%d", name, gpa, rank);
        System.out.println("  " + formatted);

        String receipt = String.format("%-20s %5.2f", "Biryani (1 plate)", 280.00);
        System.out.println("  " + receipt);

        sub("Complexity Summary — Concatenation");
        System.out.println("  Method              Loop of n  Single Op  Note");
        System.out.println("  ───────────────────────────────────────────────────────");
        System.out.println("  s = s + other       O(n²)      O(n)       New obj each time");
        System.out.println("  sb.append()         O(n)       O(1) amort Best for loops");
        System.out.println("  String.join()       O(n)       O(n)       Clean, readable");
        System.out.println("  String.format()     O(n)       O(n)       Formatted output");
    }


    // ============================================================
    // SECTION 6 — STRINGBUILDER & STRINGBUFFER
    // ============================================================
    //
    //   StringBuilder: mutable sequence of characters
    //   Internal buffer: char[] that grows when needed (like ArrayList)
    //   Default capacity: 16 characters
    //   When full: new capacity = (old × 2) + 2
    //
    //   KEY METHODS:
    //   append(x)         → add to end         → O(1) amortized
    //   insert(i, x)      → insert at index    → O(n)
    //   delete(s, e)      → remove range       → O(n)
    //   replace(s, e, x)  → replace range      → O(n)
    //   reverse()         → reverse contents   → O(n)
    //   charAt(i)         → read char at index → O(1)
    //   toString()        → convert to String  → O(n)
    //   length()          → current length     → O(1)
    //
    //   StringBuilder vs StringBuffer:
    //   StringBuilder → NOT synchronized. Use in single-threaded code.
    //   StringBuffer  → Synchronized (thread-safe). Use in multi-threaded code.
    //   In practice, StringBuilder is used ~99% of the time.
    // ============================================================

    static void demoStringBuilder() {
        section("SECTION 6 — StringBuilder & StringBuffer");

        sub("Building a dynamic SQL query — real use case");
        StringBuilder query = new StringBuilder("SELECT * FROM students");

        // Simulate adding WHERE conditions dynamically
        boolean filterByGrade = true;
        boolean filterByCity  = true;
        String  gradeValue    = "A";
        String  cityValue     = "Chennai";

        if (filterByGrade) query.append(" WHERE grade = '").append(gradeValue).append("'");
        if (filterByCity)  query.append(" AND city = '").append(cityValue).append("'");
        query.append(" ORDER BY name ASC").append(";");

        System.out.println("  Built query:");
        System.out.println("  " + query.toString());
        System.out.println("  Length: " + query.length());

        sub("append(), insert(), delete(), replace() — methods");
        StringBuilder sb = new StringBuilder("Hello World");
        System.out.println("  Initial        : \"" + sb + "\"");

        sb.append("!!!");
        System.out.println("  After append() : \"" + sb + "\"  → O(1) amortized");

        sb.insert(5, ",");
        System.out.println("  After insert(5,',') : \"" + sb + "\"  → O(n)");

        sb.delete(5, 6);
        System.out.println("  After delete(5,6)   : \"" + sb + "\"  → O(n)");

        sb.replace(6, 11, "Java");
        System.out.println("  After replace(6,11,'Java') : \"" + sb + "\"  → O(n)");

        sub("reverse() — O(n) | Use case: string manipulation");
        StringBuilder rev = new StringBuilder("Navaneeth");
        System.out.println("  Original : \"" + rev + "\"");
        rev.reverse();
        System.out.println("  Reversed : \"" + rev + "\"");

        sub("StringBuilder vs StringBuffer — thread safety");
        System.out.println("  StringBuilder: NOT thread-safe, FASTER");
        System.out.println("  StringBuffer : Thread-safe (synchronized), SLOWER");
        System.out.println("  Rule: Use StringBuilder unless you explicitly need thread safety.");

        // StringBuffer demo
        StringBuffer buffer = new StringBuffer("Thread");
        buffer.append("Safe");
        buffer.append("Buffer");
        System.out.println("  StringBuffer result: \"" + buffer + "\"");

        sub("Complexity Summary — StringBuilder");
        System.out.println("  Method            Time            Space  Note");
        System.out.println("  ──────────────────────────────────────────────────────");
        System.out.println("  append(x)         O(1) amortized  O(1)   Resize: O(n)");
        System.out.println("  insert(i, x)      O(n)            O(1)   Shifts right");
        System.out.println("  delete(s, e)      O(n)            O(1)   Shifts left");
        System.out.println("  replace(s, e, x)  O(n)            O(n)   May resize");
        System.out.println("  reverse()         O(n)            O(1)   In-place swap");
        System.out.println("  charAt(i)         O(1)            O(1)   Array access");
        System.out.println("  toString()        O(n)            O(n)   New String");
        System.out.println("  length()          O(1)            O(1)   Stored field");
    }


    // ============================================================
    // SECTION 7 — STRING SPLIT & JOIN
    // ============================================================
    //
    //   split(regex)  → splits string into String[] by delimiter  → O(n)
    //   String.join() → joins array/list with delimiter           → O(n)
    //
    //   REAL-WORLD USE CASE: CSV data parsing
    //   → Read a CSV line → split by comma → process each field
    // ============================================================

    static void demoSplitJoin() {
        section("SECTION 7 — String Split & Join | Use Case: CSV Parsing");

        sub("split() — O(n)");
        String csvLine = "Navaneeth,24,Chennai,Engineer,9.2";
        String[] fields = csvLine.split(",");

        System.out.println("  CSV line: \"" + csvLine + "\"");
        System.out.println("  After split by \",\":");
        String[] labels = {"Name", "Age", "City", "Role", "GPA"};
        for (int i = 0; i < fields.length; i++) {
            System.out.printf("    fields[%d] = %-12s (%-5s)%n", i, fields[i], labels[i]);
        }

        sub("split with limit — O(n)");
        String logLine = "2024-12-01 ERROR NullPointerException: obj is null";
        String[] parts = logLine.split(" ", 3); // max 3 parts
        System.out.println("  Log line: \"" + logLine + "\"");
        System.out.println("  split(\" \", 3):");
        System.out.println("    [0] date  : " + parts[0]);
        System.out.println("    [1] level : " + parts[1]);
        System.out.println("    [2] msg   : " + parts[2]);

        sub("split with regex — O(n)");
        String mixed = "one1two2three3four";
        String[] words = mixed.split("[0-9]"); // split on any digit
        System.out.println("  \"" + mixed + "\" split on digits:");
        System.out.println("  " + Arrays.toString(words));

        sub("join() — O(n) | Rebuild CSV from array");
        String rebuilt = String.join(",", fields);
        System.out.println("  Rebuilt CSV: \"" + rebuilt + "\"");
        System.out.println("  Original == Rebuilt: " + csvLine.equals(rebuilt));

        sub("join a List<String> — O(n)");
        List<String> skills = Arrays.asList("Java", "DSA", "OOP", "Strings", "Git");
        String resume = "Skills: " + String.join(" | ", skills);
        System.out.println("  " + resume);
    }


    // ============================================================
    // SECTION 8 — COMMON STRING ALGORITHMS
    // ============================================================
    //
    //   These are the algorithms most commonly asked in:
    //   → Engineering exams
    //   → Technical interviews
    //   → Competitive programming
    //
    //   1. Check Palindrome       → O(n) time, O(1) space
    //   2. Check Anagram          → O(n) time, O(1) space (fixed 26 chars)
    //   3. Character Frequency    → O(n) time, O(1) space
    //   4. Reverse Words          → O(n) time, O(n) space
    //   5. Count Vowels           → O(n) time, O(1) space
    // ============================================================

    static void demoStringAlgorithms() {
        section("SECTION 8 — Common String Algorithms");

        // ── 1. PALINDROME CHECK
        sub("1. Palindrome Check — O(n) time, O(1) space");
        System.out.println("  (A string that reads the same forwards and backwards)");
        String[] testWords = {"racecar", "level", "hello", "madam", "Navaneeth"};
        for (String word : testWords) {
            System.out.printf("  isPalindrome(\"%-10s\") = %s%n",
                    word + "\"", isPalindrome(word));
        }

        // ── 2. ANAGRAM CHECK
        sub("2. Anagram Check — O(n) time, O(1) space");
        System.out.println("  (Two strings with same characters in different order)");
        String[][] anagramPairs = {
            {"listen", "silent"},
            {"triangle", "integral"},
            {"hello", "world"},
            {"dusty", "study"}
        };
        for (String[] pair : anagramPairs) {
            System.out.printf("  isAnagram(\"%s\", \"%s\") = %s%n",
                    pair[0], pair[1], isAnagram(pair[0], pair[1]));
        }

        // ── 3. CHARACTER FREQUENCY
        sub("3. Character Frequency Count — O(n) time, O(1) space");
        String sentence = "engineering";
        Map<Character, Integer> freq = charFrequency(sentence);
        System.out.println("  \"" + sentence + "\" character frequencies:");
        // Sort by frequency descending for readability
        freq.entrySet().stream()
            .sorted((a, b) -> b.getValue() - a.getValue())
            .forEach(e -> System.out.printf("    '%c' → %d%n", e.getKey(), e.getValue()));

        // ── 4. REVERSE WORDS
        sub("4. Reverse Words in a Sentence — O(n) time, O(n) space");
        String sentence2 = "Java is awesome for engineering";
        String reversed  = reverseWords(sentence2);
        System.out.println("  Original : \"" + sentence2 + "\"");
        System.out.println("  Reversed : \"" + reversed + "\"");

        // ── 5. VOWEL COUNT
        sub("5. Count Vowels — O(n) time, O(1) space");
        String[] testStrings = {"Navaneeth", "SignBridge", "Engineering"};
        for (String s : testStrings) {
            System.out.printf("  vowelCount(\"%-14s\") = %d%n", s + "\"", countVowels(s));
        }

        // ── 6. LONGEST COMMON PREFIX
        sub("6. Longest Common Prefix — O(n×m) time, O(m) space");
        String[] arr1 = {"flower", "flow", "flight"};
        String[] arr2 = {"interview", "interact", "interface"};
        System.out.println("  " + Arrays.toString(arr1) + " → \"" + longestCommonPrefix(arr1) + "\"");
        System.out.println("  " + Arrays.toString(arr2) + " → \"" + longestCommonPrefix(arr2) + "\"");
    }

    // ── Helper: Palindrome — two pointer technique
    static boolean isPalindrome(String s) {
        s = s.toLowerCase();
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
        // Time: O(n/2) = O(n)  |  Space: O(1) — only two pointers
    }

    // ── Helper: Anagram — frequency array of 26 letters
    static boolean isAnagram(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        int[] count = new int[26]; // fixed 26 — O(1) space regardless of input
        for (char c : s1.toCharArray()) count[c - 'a']++;
        for (char c : s2.toCharArray()) count[c - 'a']--;
        for (int n : count) if (n != 0) return false;
        return true;
        // Time: O(n)  |  Space: O(1) — fixed 26-element array
    }

    // ── Helper: Character Frequency
    static Map<Character, Integer> charFrequency(String s) {
        Map<Character, Integer> map = new LinkedHashMap<>();
        for (char c : s.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        return map;
        // Time: O(n)  |  Space: O(k) — k unique characters
    }

    // ── Helper: Reverse Words
    static String reverseWords(String sentence) {
        String[] words = sentence.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            sb.append(words[i]);
            if (i > 0) sb.append(" ");
        }
        return sb.toString();
        // Time: O(n)  |  Space: O(n) — new string
    }

    // ── Helper: Count Vowels
    static int countVowels(String s) {
        int count = 0;
        String vowels = "aeiouAEIOU";
        for (char c : s.toCharArray()) {
            if (vowels.indexOf(c) >= 0) count++;
        }
        return count;
        // Time: O(n)  |  Space: O(1)
    }

    // ── Helper: Longest Common Prefix
    static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) return "";
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++) {
            while (!strs[i].startsWith(prefix)) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) return "";
            }
        }
        return prefix;
        // Time: O(n×m) n=strings, m=avg length  |  Space: O(m)
    }


    // ============================================================
    //   MAIN
    // ============================================================
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║    JAVA STRINGS — Complete Concepts Guide                ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        demoStringCreation();
        demoStringComparison();
        demoCoreStringMethods();
        demoImmutability();
        demoConcatenation();
        demoStringBuilder();
        demoSplitJoin();
        demoStringAlgorithms();

        // ── GRAND SUMMARY
        section("FINAL SUMMARY — All String Concepts");
        System.out.println();
        System.out.println("  ┌─────────────────────────────┬────────────┬───────────┬──────────────────┐");
        System.out.println("  │  Concept                    │  Time      │  Space    │  Key Point       │");
        System.out.println("  ├─────────────────────────────┼────────────┼───────────┼──────────────────┤");
        System.out.println("  │  String creation (literal)  │  O(n)      │  O(n)     │  Pool reuse      │");
        System.out.println("  │  charAt(i)                  │  O(1)      │  O(1)     │  Direct access   │");
        System.out.println("  │  length()                   │  O(1)      │  O(1)     │  Stored field    │");
        System.out.println("  │  equals() / compareTo()     │  O(n)      │  O(1)     │  Char by char    │");
        System.out.println("  │  substring(s, e)            │  O(n)      │  O(n)     │  New object      │");
        System.out.println("  │  indexOf(str)               │  O(n×m)    │  O(1)     │  Pattern search  │");
        System.out.println("  │  replace() / replaceAll()   │  O(n)      │  O(n)     │  New object      │");
        System.out.println("  │  split(regex)               │  O(n)      │  O(n)     │  Returns array   │");
        System.out.println("  │  String + in loop           │  O(n²)     │  O(n)     │  AVOID! Use SB   │");
        System.out.println("  │  StringBuilder.append()     │  O(1) amrt │  O(1)     │  Best for loops  │");
        System.out.println("  │  StringBuilder.reverse()    │  O(n)      │  O(1)     │  In-place        │");
        System.out.println("  │  Palindrome check           │  O(n)      │  O(1)     │  Two pointers    │");
        System.out.println("  │  Anagram check              │  O(n)      │  O(1)     │  Freq array[26]  │");
        System.out.println("  └─────────────────────────────┴────────────┴───────────┴──────────────────┘");
        System.out.println();
        System.out.println("  THE THREE RULES OF JAVA STRINGS:");
        System.out.println("  1. Always use .equals() for content comparison, NEVER ==");
        System.out.println("  2. Always use StringBuilder for string building in loops");
        System.out.println("  3. Every String method returns a NEW object — original unchanged");
    }
}
