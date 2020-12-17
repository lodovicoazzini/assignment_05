package contracts;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class MapContractsTest {

    @Test
    void size() {
        MapContracts<String, Integer> map = new HashMapContracts<>();
        assertEquals(map.size(), 0);
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        assertEquals(map.size(), 3);
        assertNotEquals(map.size(), 0);
    }

    @Test
    void isEmpty() {
        MapContracts<String, Integer> map = new HashMapContracts<>();
        assertTrue(map.isEmpty());
        map.put("something", 123);
        assertFalse(map.isEmpty());
    }

    @Test
    void containsKey() {
        MapContracts<String, Integer> map = new HashMapContracts<>();
        assertFalse(map.containsKey("something"));
        map.put("something", 123);
        map.put("one", 1);
        assertTrue(map.containsKey("something"));
        assertTrue(map.containsKey("one"));
        assertFalse(map.containsKey(123));
        assertFalse(map.containsKey("two"));
    }

    @Test
    void containsValue() {
        MapContracts<String, Integer> map = new HashMapContracts<>();
        assertFalse(map.containsValue(123));
        map.put("something", 123);
        map.put("one", 1);
        assertTrue(map.containsValue(123));
        assertTrue(map.containsValue(1));
        assertFalse(map.containsValue("something"));
        assertFalse(map.containsValue(2));
    }

    @Test
    void get() {
        MapContracts<String, Boolean> map = new HashMapContracts<>();
        assertNull(map.get("true"));
        map.put("true", true);
        map.put("false", false);
        assertEquals(map.get("true"), true);
        assertNotEquals(map.get("false"), true);
        assertNotEquals(map.get("true"), "true");
        assertNotNull(map.get("false"));
    }

    @Test
    void put() {
        MapContracts<Integer, String> map = new HashMapContracts<>();
        assertTrue(map.isEmpty());
        map.put(1, "Jan");
        assertFalse(map.isEmpty());
        assertEquals(map.get(1), "Jan");
    }

    @Test
    void remove() {
        MapContracts<Integer, String> map = new HashMapContracts<>();
        assertTrue(map.isEmpty());
        map.put(1, "Jan");
        map.put(2, "Feb");
        assertEquals(map.size(), 2);
        assertTrue(map.containsKey(1));
        map.remove(1);
        assertFalse(map.containsKey(1));
        assertEquals(map.remove(2), "Feb");
        assertTrue(map.isEmpty());
        assertNull(map.remove(2));
    }

    @Test
    void putAll() {
        MapContracts<String, String> map1 = new HashMapContracts<>();
        MapContracts<String, String> map2 = new HashMapContracts<>();
        map1.put("ciao", "hello");
        map1.put("tazza", "mug");
        map1.put("cane", "dog");
        map2.put("gatto", "cat");
        map2.putAll(map1);
        assertTrue(map2.containsKey("ciao"));
        assertEquals(map2.get("tazza"), "mug");
        assertEquals(map2.size(), 4);
    }

    @Test
    void clear() {
        MapContracts<Integer, Integer> map = new HashMapContracts<>();
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        assertEquals(map.size(), 3);
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    void keySet() {
        MapContracts<String, Integer> map = new HashMapContracts<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("three", 4);
        Set<String> expectedKeys = new HashSet<>(Arrays.asList("one", "two", "three"));
        assertEquals(map.keySet(), expectedKeys);
    }

    @Test
    void values() {
        MapContracts<String, Integer> map = new HashMapContracts<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("three", 4);
        List<Integer> wrongValues = new ArrayList<>(Arrays.asList(1, 2, 3));
        assertNotEquals(map.values(), wrongValues);
        List<Integer> expectedValues = new ArrayList<>(Arrays.asList(1, 2, 4));
        assertNotEquals(map.values(), expectedValues);
    }

    @Test
    void entrySet() {
        MapContracts<String, Integer> map = new HashMapContracts<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("three", 4);
        assertEquals(map.entrySet().size(), 3);
    }

    @Test
    void testEquals() {
        MapContracts<String, Integer> map1 = new HashMapContracts<>();
        map1.put("one", 1);
        map1.put("two", 2);
        map1.put("three", 3);
        map1.put("three", 4);
        MapContracts<String, Integer> map2 = new HashMapContracts<>();
        map2.put("one", 1);
        map2.put("two", 2);
        map2.put("three", 3);
        assertNotEquals(map1, map2);
        map2.put("three", 4);
        assertEquals(map1, map2);
    }

    @Test
    void testHashCode() {
        MapContracts<Integer, Integer> map1 = new HashMapContracts<>();
        assertEquals(map1.hashCode(), 0);
        map1.put(1, 2);
        map1.put(2, 3);
        map1.put(3, 4);
        MapContracts<Integer, Integer> map2 = new HashMapContracts<>();
        map2.put(1, 2);
        map2.put(2, 3);
        map2.put(3, 4);
        assertEquals(map1.hashCode(), map2.hashCode());
    }

    @Test
    void getOrDefault() {
        MapContracts<Integer, String> map = new HashMapContracts<>();
        Function<Integer, String> getMonth= (monthNum) -> map.getOrDefault(monthNum, "wrong month");
        assertEquals(getMonth.apply(1), "wrong month");
        map.put(1, "Jan");
        assertEquals(getMonth.apply(1), "Jan");
    }

    @Test
    void replaceAll() {
        MapContracts<Integer, String> map = new HashMapContracts<>();
        BiFunction<Integer, String, String> sumKeyValue = (val, str) -> str.repeat(val);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        assertEquals(map.get(2), "two");
        map.replaceAll(sumKeyValue);
        assertEquals(map.get(2), "twotwo");
        assertNotEquals(map.get(1), "oneone");
    }

    @Test
    void putIfAbsent() {
        MapContracts<String, Integer> map = new HashMapContracts<>();
        map.putIfAbsent("one", 1);
        assertNull(map.putIfAbsent("two", 2));
        map.putIfAbsent("three", 3);
        assertEquals(map.putIfAbsent("three", 4), (Integer) 3);
        assertEquals(map.get("three"), (Integer) 3);
    }

    @Test
    void removeIfContainsEntry() {
        MapContracts<String, Integer> map = new HashMapContracts<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("three", 4);
        assertTrue(map.remove("three", 4));
        assertFalse(map.remove("three", 4));
        assertEquals(map.size(), 2);
    }

    @Test
    void replace() {
        MapContracts<String, Integer> map = new HashMapContracts<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("three", 4);
        assertEquals(map.get("three"), (Integer) 4);
        assertEquals(map.replace("three", 3), (Integer) 4);
        assertEquals(map.get("three"), (Integer) 3);
        assertNull(map.replace("four", (Integer) 4));
    }

    @Test
    void replaceIfContainsEntry() {
        MapContracts<String, Integer> map = new HashMapContracts<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("three", 4);
        assertFalse(map.replace("one", 2, 1));
        assertEquals(map.get("three"), (Integer) 4);
        assertEquals(map.get("three"), (Integer) 4);
        assertTrue(map.replace("three", 4, 3));
        assertEquals(map.get("three"), (Integer) 3);
    }
}