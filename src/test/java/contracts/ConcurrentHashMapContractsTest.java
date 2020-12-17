package contracts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ConcurrentHashMapContractsTest {

    @Test
    void nullElementsThrows() {
        MapContracts<String, Integer> map = new ConcurrentHashMapContracts<>();
        map.put("something", 123);
        assertThrows(NullPointerException.class, () -> map.get(null));
    }
}