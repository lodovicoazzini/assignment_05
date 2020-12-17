package contracts;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;

public class ConcurrentHashMapContracts<K, V> extends ConcurrentHashMap<K, V> implements MapContracts<K, V> {

    // ==========================================================================================
    // ==============================                              ==============================
    // ==============================        Helper Methods        ==============================
    // ==============================                              ==============================
    // ==========================================================================================

//    @Override
//    public boolean supports_null_items() {
//        return false;
//    }

    // ==========================================================================================
    // ==============================                              ==============================
    // ==============================          Invariants          ==============================
    // ==============================                              ==============================
    // ==========================================================================================

    @Invariant
    @Pure
    public boolean none_null() {
        return keySet().stream().noneMatch(Objects::isNull) && values().stream().noneMatch(Objects::isNull);
    }

    // ==========================================================================================
    // ==========================================================================================
    // ==========================================================================================

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public V get(Object key) {
        return super.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    @Override
    public V put(@NotNull K key, @NotNull V value) {
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        super.putAll(m);
    }

    @Override
    public V remove(@NotNull Object key) {
        return super.remove(key);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public KeySetView<K, V> keySet() {
        return super.keySet();
    }

    @Override
    public Collection<V> values() {
        return super.values();
    }

//    @Override
//    public Set<Map.Entry<K, V>> entrySet() {
//        return super.entrySet();
//    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return super.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return super.remove(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return super.replace(key, oldValue, newValue);
    }

    @Override
    public V replace(K key, V value) {
        return super.replace(key, value);
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return super.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        super.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        super.replaceAll(function);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return super.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return super.computeIfPresent(key, remappingFunction);
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return super.compute(key, remappingFunction);
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return super.merge(key, value, remappingFunction);
    }

    @Override
    public boolean contains(Object value) {
        return super.contains(value);
    }

    @Override
    public Enumeration<K> keys() {
        return super.keys();
    }

    @Override
    public Enumeration<V> elements() {
        return super.elements();
    }

//    @Override
//    public long mappingCount() {
//        return super.mappingCount();
//    }

    @Override
    public KeySetView<K, V> keySet(V mappedValue) {
        return super.keySet(mappedValue);
    }

    @Override
    public void forEach(long parallelismThreshold, BiConsumer<? super K, ? super V> action) {
        super.forEach(parallelismThreshold, action);
    }

    @Override
    public <U> void forEach(long parallelismThreshold, BiFunction<? super K, ? super V, ? extends U> transformer, Consumer<? super U> action) {
        super.forEach(parallelismThreshold, transformer, action);
    }

    @Override
    public <U> U search(long parallelismThreshold, BiFunction<? super K, ? super V, ? extends U> searchFunction) {
        return super.search(parallelismThreshold, searchFunction);
    }

    @Override
    public <U> U reduce(long parallelismThreshold, BiFunction<? super K, ? super V, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
        return super.reduce(parallelismThreshold, transformer, reducer);
    }

    @Override
    public double reduceToDouble(long parallelismThreshold, ToDoubleBiFunction<? super K, ? super V> transformer, double basis, DoubleBinaryOperator reducer) {
        return super.reduceToDouble(parallelismThreshold, transformer, basis, reducer);
    }

//    @Override
//    public long reduceToLong(long parallelismThreshold, ToLongBiFunction<? super K, ? super V> transformer, long basis, LongBinaryOperator reducer) {
//        return super.reduceToLong(parallelismThreshold, transformer, basis, reducer);
//    }

    @Override
    public int reduceToInt(long parallelismThreshold, ToIntBiFunction<? super K, ? super V> transformer, int basis, IntBinaryOperator reducer) {
        return super.reduceToInt(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    public void forEachKey(long parallelismThreshold, Consumer<? super K> action) {
        super.forEachKey(parallelismThreshold, action);
    }

    @Override
    public <U> void forEachKey(long parallelismThreshold, Function<? super K, ? extends U> transformer, Consumer<? super U> action) {
        super.forEachKey(parallelismThreshold, transformer, action);
    }

    @Override
    public <U> U searchKeys(long parallelismThreshold, Function<? super K, ? extends U> searchFunction) {
        return super.searchKeys(parallelismThreshold, searchFunction);
    }

    @Override
    public K reduceKeys(long parallelismThreshold, BiFunction<? super K, ? super K, ? extends K> reducer) {
        return super.reduceKeys(parallelismThreshold, reducer);
    }

    @Override
    public <U> U reduceKeys(long parallelismThreshold, Function<? super K, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
        return super.reduceKeys(parallelismThreshold, transformer, reducer);
    }

    @Override
    public double reduceKeysToDouble(long parallelismThreshold, ToDoubleFunction<? super K> transformer, double basis, DoubleBinaryOperator reducer) {
        return super.reduceKeysToDouble(parallelismThreshold, transformer, basis, reducer);
    }

//    @Override
//    public long reduceKeysToLong(long parallelismThreshold, ToLongFunction<? super K> transformer, long basis, LongBinaryOperator reducer) {
//        return super.reduceKeysToLong(parallelismThreshold, transformer, basis, reducer);
//    }

    @Override
    public int reduceKeysToInt(long parallelismThreshold, ToIntFunction<? super K> transformer, int basis, IntBinaryOperator reducer) {
        return super.reduceKeysToInt(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    public void forEachValue(long parallelismThreshold, Consumer<? super V> action) {
        super.forEachValue(parallelismThreshold, action);
    }

    @Override
    public <U> void forEachValue(long parallelismThreshold, Function<? super V, ? extends U> transformer, Consumer<? super U> action) {
        super.forEachValue(parallelismThreshold, transformer, action);
    }

    @Override
    public <U> U searchValues(long parallelismThreshold, Function<? super V, ? extends U> searchFunction) {
        return super.searchValues(parallelismThreshold, searchFunction);
    }

    @Override
    public V reduceValues(long parallelismThreshold, BiFunction<? super V, ? super V, ? extends V> reducer) {
        return super.reduceValues(parallelismThreshold, reducer);
    }

    @Override
    public <U> U reduceValues(long parallelismThreshold, Function<? super V, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
        return super.reduceValues(parallelismThreshold, transformer, reducer);
    }

    @Override
    public double reduceValuesToDouble(long parallelismThreshold, ToDoubleFunction<? super V> transformer, double basis, DoubleBinaryOperator reducer) {
        return super.reduceValuesToDouble(parallelismThreshold, transformer, basis, reducer);
    }

//    @Override
//    public long reduceValuesToLong(long parallelismThreshold, ToLongFunction<? super V> transformer, long basis, LongBinaryOperator reducer) {
//        return super.reduceValuesToLong(parallelismThreshold, transformer, basis, reducer);
//    }

    @Override
    public int reduceValuesToInt(long parallelismThreshold, ToIntFunction<? super V> transformer, int basis, IntBinaryOperator reducer) {
        return super.reduceValuesToInt(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    public void forEachEntry(long parallelismThreshold, Consumer<? super Map.Entry<K, V>> action) {
        super.forEachEntry(parallelismThreshold, action);
    }

    @Override
    public <U> void forEachEntry(long parallelismThreshold, Function<Map.Entry<K, V>, ? extends U> transformer, Consumer<? super U> action) {
        super.forEachEntry(parallelismThreshold, transformer, action);
    }

    @Override
    public <U> U searchEntries(long parallelismThreshold, Function<Map.Entry<K, V>, ? extends U> searchFunction) {
        return super.searchEntries(parallelismThreshold, searchFunction);
    }

    @Override
    public Map.Entry<K, V> reduceEntries(long parallelismThreshold, BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer) {
        return super.reduceEntries(parallelismThreshold, reducer);
    }

    @Override
    public <U> U reduceEntries(long parallelismThreshold, Function<Map.Entry<K, V>, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
        return super.reduceEntries(parallelismThreshold, transformer, reducer);
    }

    @Override
    public double reduceEntriesToDouble(long parallelismThreshold, ToDoubleFunction<Map.Entry<K, V>> transformer, double basis, DoubleBinaryOperator reducer) {
        return super.reduceEntriesToDouble(parallelismThreshold, transformer, basis, reducer);
    }

//    @Override
//    public long reduceEntriesToLong(long parallelismThreshold, ToLongFunction<Map.Entry<K, V>> transformer, long basis, LongBinaryOperator reducer) {
//        return super.reduceEntriesToLong(parallelismThreshold, transformer, basis, reducer);
//    }

    @Override
    public int reduceEntriesToInt(long parallelismThreshold, ToIntFunction<Map.Entry<K, V>> transformer, int basis, IntBinaryOperator reducer) {
        return super.reduceEntriesToInt(parallelismThreshold, transformer, basis, reducer);
    }
}
