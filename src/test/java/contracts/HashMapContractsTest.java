package contracts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class HashMapContractsTest {

    @Test
    void nullElementsNotThrows() {
        MapContracts<String, Integer> map = new HashMapContracts<>();
        map.put("what", null);
        assertNull(map.get("what"));
    }
}