package collections;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class CollectionsCheatSheet {
    public static void main(String[] args) {
        header("LIST (ArrayList, LinkedList)");
        demoList();

        header("SET (HashSet, LinkedHashSet, TreeSet)");
        demoSet();

        header("QUEUE / DEQUE (ArrayDeque, PriorityQueue)");
        demoQueueDeque();

        header("MAP (HashMap, LinkedHashMap, TreeMap)");
        demoMap();

        header("ITERATING SAFELY (Iterator, ListIterator, forEach)");
        demoIterating();

        header("UTILITIES (Collections.*)");
        demoUtilities();

        header("IMMUTABLE FACTORIES (List.of / Set.of / Map.of)");
        demoImmutableFactories();

        header("STREAMS INTEROP");
        demoStreamsInterop();

        header("CONCURRENT NOTE (ConcurrentHashMap)");
        demoConcurrentNote();
    }

    private static void header(String title) {
        System.out.println("\n=== " + title + " ===");
    }

    // -------------------------------- LIST --------------------------------
    private static void demoList() {
        // ArrayList: contiguous array-backed, fast random access (get/set), amortized O(1) add at end
        List<String> cities = new ArrayList<>();
        cities.add("Krakow");
        cities.add("Warsaw");
        cities.add(1, "Gdansk"); // insert at index -> O(n)
        System.out.println("ArrayList: " + cities);

        // Bulk ops
        cities.addAll(Arrays.asList("Poznan", "Wroclaw"));
        System.out.println("after addAll: " + cities);

        // Access & update
        String first = cities.get(0); // O(1)
        cities.set(0, first.toUpperCase());
        System.out.println("get/set: " + cities);

        // Remove by index and by value
        cities.remove(2);           // remove index 2
        cities.remove("Poznan");   // remove first occurrence of value
        System.out.println("after remove: " + cities + ", size=" + cities.size());

        // removeIf (predicate)
        cities.removeIf(s -> s.length() > 7);
        System.out.println("after removeIf(len>7): " + cities);

        // LinkedList implements List + Deque (fast at ends; random access is O(n))
        LinkedList<String> ll = new LinkedList<>(cities);
        ll.addFirst("Lodz");
        ll.addLast("Szczecin");
        System.out.println("LinkedList with Deque ops: " + ll);

        // subList is a view (changes reflect back)
        List<String> view = ll.subList(1, Math.min(3, ll.size()));
        view.clear(); // removes those elements from original list as well
        System.out.println("after subList.clear(): " + ll);
    }

    // -------------------------------- SET ---------------------------------
    private static void demoSet() {
        // HashSet: no duplicates, no order guarantees
        Set<Integer> set = new HashSet<>();
        Collections.addAll(set, 3, 1, 2, 2, 1);
        System.out.println("HashSet (unique): " + set + " contains 2? " + set.contains(2));

        // LinkedHashSet preserves insertion order
        Set<String> lhs = new LinkedHashSet<>(Arrays.asList("b", "a", "c", "a"));
        System.out.println("LinkedHashSet (insertion order): " + lhs);

        // TreeSet sorts elements (natural order) and supports navigable ops
        TreeSet<String> ts = new TreeSet<>(Arrays.asList("delta", "alpha", "charlie", "bravo"));
        System.out.println("TreeSet (sorted): " + ts + ", first=" + ts.first() + ", higher(\"bravo\")=" + ts.higher("bravo"));
    }

    // --------------------------- QUEUE / DEQUE -----------------------------
    private static void demoQueueDeque() {
        // Queue via ArrayDeque (preferred over LinkedList for stack/queue usage)
        Queue<Integer> q = new ArrayDeque<>();
        q.offer(10); // offer returns false instead of throwing on failure
        q.offer(20);
        q.offer(30);
        System.out.println("Queue offer: " + q + ", peek=" + q.peek()); // peek does not remove
        System.out.println("poll -> " + q.poll() + ", after poll: " + q);

        // Deque: double-ended queue
        Deque<String> dq = new ArrayDeque<>();
        dq.addFirst("head");
        dq.addLast("tail");
        dq.offerFirst("H2");
        dq.offerLast("T2");
        System.out.println("Deque: " + dq + ", pollLast=" + dq.pollLast() + ", after: " + dq);

        // PriorityQueue: min-heap by default (natural order)
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        Collections.addAll(pq, 5, 1, 4, 2, 3);
        System.out.print("PriorityQueue poll order: ");
        while (!pq.isEmpty()) System.out.print(pq.poll() + " ");
        System.out.println();
    }

    // -------------------------------- MAP ---------------------------------
    private static void demoMap() {
        // HashMap: O(1) average for put/get/remove; no order guarantees
        Map<String, Integer> ages = new HashMap<>();
        ages.put("Alice", 30);
        ages.put("Bob", 25);
        ages.put("Charlie", 35);

        // putIfAbsent / getOrDefault
        ages.putIfAbsent("Alice", 31); // no change because key exists
        int danaAge = ages.getOrDefault("Dana", 0);
        System.out.println("HashMap: " + ages + ", getOrDefault Dana= " + danaAge);

        // replace / remove(key, value)
        ages.replace("Bob", 26);
        boolean removed = ages.remove("Charlie", 34); // false (value mismatch)
        System.out.println("after replace/remove: " + ages + ", removed? " + removed);

        // computeIfAbsent / merge
        ages.computeIfAbsent("Eve", k -> 28);
        ages.merge("Alice", 1, Integer::sum); // Alice becomes 31

        // Iteration patterns
        for (Map.Entry<String, Integer> e : ages.entrySet()) {
            System.out.print("[" + e.getKey() + "->" + e.getValue() + "] ");
        }
        System.out.println();

        ages.forEach((k, v) -> System.out.print(k + ":" + v + " "));
        System.out.println();

        // LinkedHashMap preserves insertion order
        Map<String, Integer> lhm = new LinkedHashMap<>();
        lhm.put("x", 1); lhm.put("y", 2); lhm.put("z", 3);
        System.out.println("LinkedHashMap (order): " + lhm);

        // TreeMap sorts by key (natural order)
        TreeMap<String, Integer> tm = new TreeMap<>(lhm);
        System.out.println("TreeMap (sorted by key): " + tm + ", higherKey(\"x\")=" + tm.higherKey("x"));
    }

    // ------------------------------ ITERATING ------------------------------
    private static void demoIterating() {
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));

        // 1) for-each (read-only)
        for (String s : list) {
            System.out.print(s + " ");
        }
        System.out.println();

        // 2) Iterator for safe removal during iteration
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (Objects.equals(it.next(), "b")) it.remove();
        }
        System.out.println("after Iterator.remove: " + list);

        // 3) ListIterator supports bidirectional moves and in-place add/set
        ListIterator<String> li = list.listIterator();
        while (li.hasNext()) {
            String val = li.next();
            if (val.equals("c")) li.set("C"); // replace current element
        }
        System.out.println("after ListIterator.set: " + list);
    }

    // ------------------------------ UTILITIES ------------------------------
    private static void demoUtilities() {
        List<Integer> nums = new ArrayList<>(Arrays.asList(5, 3, 4, 1, 2));
        Collections.sort(nums); // ascending
        System.out.println("sort: " + nums);

        Collections.reverse(nums);
        System.out.println("reverse: " + nums);

        Collections.shuffle(nums); // random order
        System.out.println("shuffle: " + nums);

        Collections.sort(nums);
        int idx = Collections.binarySearch(nums, 3); // requires sorted list
        System.out.println("binarySearch(3) => index " + idx);

        // Synchronized wrappers (coarse-grained locking)
        List<Integer> threadSafe = Collections.synchronizedList(new ArrayList<>(nums));
        synchronized (threadSafe) { // explicit sync needed for compound actions
            threadSafe.add(999);
        }
        System.out.println("synchronizedList: " + threadSafe);

        // Unmodifiable view
        List<Integer> unmod = Collections.unmodifiableList(nums);
        System.out.println("unmodifiableList view (mutations will throw): " + unmod);
        // unmod.add(42); // -> UnsupportedOperationException
    }

    // -------------------------- IMMUTABLE FACTORIES ------------------------
    private static void demoImmutableFactories() {
        List<String> ilist = List.of("A", "B", "C");
        Set<String> iset = Set.of("A", "B", "C");
        Map<String, Integer> imap = Map.of("A", 1, "B", 2, "C", 3);

        System.out.println("List.of: " + ilist);
        System.out.println("Set.of: " + iset);
        System.out.println("Map.of: " + imap);

        // All are truly unmodifiable; structural updates throw
        // ilist.add("D"); // UnsupportedOperationException
        // iset.remove("A");
        // imap.put("D", 4);
    }

    // ---------------------------- STREAMS INTEROP --------------------------
    private static void demoStreamsInterop() {
        List<String> words = Arrays.asList("apple", "banana", "avocado", "blueberry", "apricot");

        // Filter/map/collect to List
        List<String> aWordsUpper = words.stream()
                .filter(w -> w.startsWith("a"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
        System.out.println("stream -> List: " + aWordsUpper);

        // toSet (distinct automatically), toMap with merge function
        Set<Character> initials = words.stream()
                .map(w -> w.charAt(0))
                .collect(Collectors.toSet());
        System.out.println("stream -> Set: " + initials);

        Map<Integer, Long> lengthFreq = words.stream()
                .collect(Collectors.groupingBy(String::length, Collectors.counting()));
        System.out.println("groupingBy length -> count: " + lengthFreq);

        // Build a map with computeIfAbsent pattern
        Map<Character, List<String>> byInitial = new HashMap<>();
        for (String w : words) {
            byInitial.computeIfAbsent(w.charAt(0), k -> new ArrayList<>()).add(w);
        }
        System.out.println("computeIfAbsent bucketing: " + byInitial);
    }

    // ---------------------------- CONCURRENT NOTE --------------------------
    private static void demoConcurrentNote() {
        // ConcurrentHashMap provides thread-safe concurrent access with better scalability
        // than synchronizedMap for high-contention scenarios. Avoid locking the world.
        ConcurrentHashMap<String, Integer> chm = new ConcurrentHashMap<>();
        chm.put("hits", 0);
        chm.merge("hits", 1, Integer::sum);
        chm.compute("hits", (k, v) -> v == null ? 1 : v + 1);
        System.out.println("ConcurrentHashMap sample: " + chm);

        // For producer-consumer, consider: ArrayBlockingQueue, LinkedBlockingQueue, etc.
        // For sorted concurrent sets/maps: ConcurrentSkipListSet/Map (log n ops).
    }
}
