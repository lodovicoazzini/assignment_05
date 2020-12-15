package contract;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashMapContractsTest {

    @Test
    void nullElementsNotThrows() {
        MapContracts<String, Integer> map = new HashMapContracts<>();
        map.put("what", null);
        assertDoesNotThrow(() -> map.get(null));
        assertNull(map.get("what"));
    }
}