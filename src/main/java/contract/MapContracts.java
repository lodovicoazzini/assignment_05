package contract;

import ch.usi.si.codelounge.jsicko.Contract;
import ch.usi.si.codelounge.jsicko.ContractUtils;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static ch.usi.si.codelounge.jsicko.Contract.old;
import static ch.usi.si.codelounge.jsicko.ContractUtils.iff;
import static ch.usi.si.codelounge.jsicko.ContractUtils.implies;


public interface MapContracts<K, V> extends Map<K, V>, Contract {

    // Invariants
    @Invariant
    @Pure
    default boolean size_non_negative() {
        return size() >= 0;
    }

    /**
     * Method to know if the map supports null keys
     * @return true iff the map supports null keys
     */
    @Pure
    boolean supports_null_keys();

    default boolean raises_if_null_unsupported_and_null_item(Throwable raises, Object item) {
        return ContractUtils.implies(!supports_null_keys() && item == null,
                () -> raises instanceof NullPointerException);
    }

    @Pure
    boolean support_put_operation();

    @Pure
    boolean support_remove_operation();

    @Pure
    boolean support_clear_operation();

    // Query Operations

    /**
     * Returns the number of key-value mappings in this map.  If the
     * map contains more than {@code Integer.MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.
     *
     * @return the number of key-value mappings in this map
     */
    @Pure
    int size();

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if this map contains no key-value mappings
     */
    @Pure
    @Ensures("returns_iff_size_is_zero")
    boolean isEmpty();
    @Pure
    default boolean returns_iff_size_is_zero(boolean returns) {
        return returns == (size() == 0);
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified
     * key.  More formally, returns {@code true} if and only if
     * this map contains a mapping for a key {@code k} such that
     * {@code Objects.equals(key, k)}.  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified
     *         key
     * @throws ClassCastException if the key is of an inappropriate type for
     *         this map
     * (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *         does not permit null keys
     * (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Pure
    @Ensures({"raises_if_wrong_key_type", "raises_if_null_unsupported_and_null_item"})
    boolean containsKey(Object key);
    @Pure
    default boolean raises_if_wrong_key_type(Throwable raises, Object key) { // TODO: Check if it's possible to get a collection type at runtime, problems if the map is empty
        return ContractUtils.implies(!isEmpty() && // if the map is not empty
                        key.getClass() != keySet().stream().findAny().getClass(), // and the parameter is not of the same type of the keys
                () -> raises instanceof ClassCastException); // throws a ClassCastException
    }

    /**
     * Returns {@code true} if this map maps one or more keys to the
     * specified value.  More formally, returns {@code true} if and only if
     * this map contains at least one mapping to a value {@code v} such that
     * {@code Objects.equals(value, v)}.  This operation
     * will probably require time linear in the map size for most
     * implementations of the {@code Map} interface.
     *
     * @param value value whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more keys to the
     *         specified value
     * @throws ClassCastException if the value is of an inappropriate type for
     *         this map
     * (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified value is null and this
     *         map does not permit null values
     * (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Pure
    @Ensures({"raises_if_wrong_value_type", "raises_if_null_unsupported_and_null_item"})
    boolean containsValue(Object value);
    @Pure
    default boolean raises_if_wrong_value_type(Throwable raises, Object value) {
        return ContractUtils.implies(!isEmpty() &&
                        value.getClass() != values().stream().findAny().getClass(),
                () -> raises instanceof ClassCastException);
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that
     * {@code Objects.equals(key, k)},
     * then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     *
     * <p>If this map permits null values, then a return value of
     * {@code null} does not <i>necessarily</i> indicate that the map
     * contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to {@code null}.  The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     * @throws ClassCastException if the key is of an inappropriate type for
     *         this map
     * (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *         does not permit null keys
     * (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Pure
    @Ensures({"returns_value_iff_key", "raises_if_wrong_key_type", "raises_if_null_unsupported_and_null_item"})
    V get(Object key);
    @Pure
    default boolean returns_value_iff_key(V returns, Object key) {
        return iff(containsKey(key), returns == get(key));
    }

    // Modification Operations

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.  (A map
     * {@code m} is said to contain a mapping for a key {@code k} if and only
     * if {@link #containsKey(Object) m.containsKey(k)} would return
     * {@code true}.)
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}.
     *         (A {@code null} return can also indicate that the map
     *         previously associated {@code null} with {@code key},
     *         if the implementation supports {@code null} values.)
     * @throws UnsupportedOperationException if the {@code put} operation
     *         is not supported by this map
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     * @throws NullPointerException if the specified key or value is null
     *         and this map does not permit null keys or values
     * @throws IllegalArgumentException if some property of the specified key
     *         or value prevents it from being stored in this map
     */
    @Requires({"raises_if_wrong_key_type", "raises_if_wrong_value_type"})
    @Ensures({"null_if_not_key_else_value",
            "contains_entry_key_value",
            "size_increases_iff_value_not_contained",
            "raises_if_unsupported_put_operation",
            "raises_if_null_unsupported_and_null_item"})
    V put(K key, V value);
    @Pure
    default boolean null_if_not_key_else_old_value(V returns, K key) {
        return implies(!old(this).containsKey(key),
                () -> returns == null,
                () -> returns == old(this).get(key));
    }
    @Pure
    default boolean contains_entry_key_value(K key, V value) {
        return (containsKey(key) && get(key) == value);
    }
    @Pure
    default boolean size_increases_iff_value_not_contained(V returns, K key, V value) {
        return ContractUtils.implies(!old(this).containsKey(key),
                () -> this.size() == old(this).size() + 1,
                () -> this.size() == old(this).size());
    }
    @Pure
    default boolean raises_if_unsupported_put_operation(Throwable raises) {
        return ContractUtils.implies(!support_put_operation(), () -> raises instanceof UnsupportedOperationException);
    }


    /**
     * Removes the mapping for a key from this map if it is present
     * (optional operation).   More formally, if this map contains a mapping
     * from key {@code k} to value {@code v} such that
     * {@code Objects.equals(key, k)}, that mapping
     * is removed.  (The map can contain at most one such mapping.)
     *
     * <p>Returns the value to which this map previously associated the key,
     * or {@code null} if the map contained no mapping for the key.
     *
     * <p>If this map permits null values, then a return value of
     * {@code null} does not <i>necessarily</i> indicate that the map
     * contained no mapping for the key; it's also possible that the map
     * explicitly mapped the key to {@code null}.
     *
     * <p>The map will not contain a mapping for the specified key once the
     * call returns.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}.
     * @throws UnsupportedOperationException if the {@code remove} operation
     *         is not supported by this map
     * @throws ClassCastException if the key is of an inappropriate type for
     *         this map
     * (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this
     *         map does not permit null keys
     * (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Ensures({"not_contains_key",
            "returns_value_iff_key",
            "size_decreased_iff_value_contained",
            "raises_if_wrong_key_type",
            "raises_if_null_unsupported_and_null_item",
            "raises_if_unsupported_remove_operation"})
    V remove(Object key);
    @Pure
    default boolean not_contains_key(Object key) {
        return !containsKey(key);
    }
    @Pure
    default boolean size_decreased_iff_value_contained(Object key) {
        return ContractUtils.implies(old(this).containsKey(key),
                () -> this.size() == old(this).size() - 1,
                () -> this.size() == old(this).size());
    }
    @Pure
    default boolean raises_if_unsupported_remove_operation(Throwable raises) {
        return ContractUtils.implies(!support_remove_operation(), () -> raises instanceof UnsupportedOperationException);
    }


    // Bulk Operations

    /**
     * Copies all of the mappings from the specified map to this map
     * (optional operation).  The effect of this call is equivalent to that
     * of calling {@link #put(Object,Object) put(k, v)} on this map once
     * for each mapping from key {@code k} to value {@code v} in the
     * specified map.  The behavior of this operation is undefined if the
     * specified map is modified while the operation is in progress.
     *
     * @param m mappings to be stored in this map
     * @throws UnsupportedOperationException if the {@code putAll} operation
     *         is not supported by this map
     * @throws ClassCastException if the class of a key or value in the
     *         specified map prevents it from being stored in this map
     * @throws NullPointerException if the specified map is null, or if
     *         this map does not permit null keys or values, and the
     *         specified map contains null keys or values
     * @throws IllegalArgumentException if some property of a key or value in
     *         the specified map prevents it from being stored in this map
     */
    @Ensures({"contains_all_entries_key_value",
            "size_increased_for_each_value_not_contained"})
    void putAll(Map<? extends K, ? extends V> m);
    @Pure
    default boolean contains_all_entries_key_value(Map<? extends K, ? extends V> other) {
        return other.entrySet().stream().allMatch(entry -> get(entry.getKey()) == entry.getValue());
    }
    @Pure
    default boolean size_increased_for_each_value_not_contained(Map<? extends K, ? extends V> other) {
        return size() == old(this).size() + other.keySet().stream().filter(key -> !containsKey(key)).count();
    }

    /**
     * Removes all of the mappings from this map (optional operation).
     * The map will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *         is not supported by this map
     */
    @Ensures({"map_is_empty", "raises_if_unsupported_clear_operation"})
    void clear();
    @Pure
    default boolean map_is_empty() {
        return isEmpty();
    }
    @Pure
    default boolean raises_if_unsupported_clear_operation(Throwable raises) {
        return ContractUtils.implies(!support_clear_operation(), () -> raises instanceof UnsupportedOperationException);
    }


    // Views

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own {@code remove} operation), the results of
     * the iteration are undefined.  The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * {@code Iterator.remove}, {@code Set.remove},
     * {@code removeAll}, {@code retainAll}, and {@code clear}
     * operations.  It does not support the {@code add} or {@code addAll}
     * operations.
     *
     * @return a set view of the keys contained in this map
     */
    @Ensures({"returns_set_of_keys"})
    Set<K> keySet();
    @Pure
    default boolean returns_set_of_keys(Set<K> returns) {
        return returns.stream().allMatch(key -> containsKey(key));

    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own {@code remove} operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the {@code Iterator.remove},
     * {@code Collection.remove}, {@code removeAll},
     * {@code retainAll} and {@code clear} operations.  It does not
     * support the {@code add} or {@code addAll} operations.
     *
     * @return a collection view of the values contained in this map
     */
    @Ensures("returns_set_of_values")
    Collection<V> values();
    @Pure
    default boolean returns_set_of_values(Collection<V> returns) {
        return returns.stream().allMatch(value -> containsValue(value));
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own {@code remove} operation, or through the
     * {@code setValue} operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the {@code Iterator.remove},
     * {@code Set.remove}, {@code removeAll}, {@code retainAll} and
     * {@code clear} operations.  It does not support the
     * {@code add} or {@code addAll} operations.
     *
     * @return a set view of the mappings contained in this map
     */
    @Ensures({"returns_set_of_key_values"})
    Set<Map.Entry<K, V>> entrySet();
    @Pure
    default boolean returns_set_of_key_values(Set<Map.Entry<K, V>> returns) {
        return returns.stream().allMatch(entry -> get(entry.getKey()) == entry.getValue());
    }

    /**
     * A map entry (key-value pair).  The {@code Map.entrySet} method returns
     * a collection-view of the map, whose elements are of this class.  The
     * <i>only</i> way to obtain a reference to a map entry is from the
     * iterator of this collection-view.  These {@code Map.Entry} objects are
     * valid <i>only</i> for the duration of the iteration; more formally,
     * the behavior of a map entry is undefined if the backing map has been
     * modified after the entry was returned by the iterator, except through
     * the {@code setValue} operation on the map entry.
     *
     * @see Map#entrySet()
     * @since 1.2
     */
    interface Entry<K, V> {
        /**
         * Returns the key corresponding to this entry.
         *
         * @return the key corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *         required to, throw this exception if the entry has been
         *         removed from the backing map.
         */
        K getKey();

        /**
         * Returns the value corresponding to this entry.  If the mapping
         * has been removed from the backing map (by the iterator's
         * {@code remove} operation), the results of this call are undefined.
         *
         * @return the value corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *         required to, throw this exception if the entry has been
         *         removed from the backing map.
         */
        V getValue();

        /**
         * Replaces the value corresponding to this entry with the specified
         * value (optional operation).  (Writes through to the map.)  The
         * behavior of this call is undefined if the mapping has already been
         * removed from the map (by the iterator's {@code remove} operation).
         *
         * @param value new value to be stored in this entry
         * @return old value corresponding to the entry
         * @throws UnsupportedOperationException if the {@code put} operation
         *         is not supported by the backing map
         * @throws ClassCastException if the class of the specified value
         *         prevents it from being stored in the backing map
         * @throws NullPointerException if the backing map does not permit
         *         null values, and the specified value is null
         * @throws IllegalArgumentException if some property of this value
         *         prevents it from being stored in the backing map
         * @throws IllegalStateException implementations may, but are not
         *         required to, throw this exception if the entry has been
         *         removed from the backing map.
         */
        V setValue(V value);

        /**
         * Compares the specified object with this entry for equality.
         * Returns {@code true} if the given object is also a map entry and
         * the two entries represent the same mapping.  More formally, two
         * entries {@code e1} and {@code e2} represent the same mapping
         * if<pre>
         *     (e1.getKey()==null ?
         *      e2.getKey()==null : e1.getKey().equals(e2.getKey()))  &amp;&amp;
         *     (e1.getValue()==null ?
         *      e2.getValue()==null : e1.getValue().equals(e2.getValue()))
         * </pre>
         * This ensures that the {@code equals} method works properly across
         * different implementations of the {@code Map.Entry} interface.
         *
         * @param o object to be compared for equality with this map entry
         * @return {@code true} if the specified object is equal to this map
         *         entry
         */
        @Ensures({"pairwise_equals"})
        boolean equals(Object o);
        @Pure
        default boolean pairwise_equals(boolean returns, Object o) {
            if (o instanceof Entry) {
                Entry<K, V> other = (Entry<K, V>) o;
                if (other.getKey().getClass() == getKey().getClass() &&
                        other.getValue().getClass() == getValue().getClass()) {

                    return (other.getKey() == null ? getKey() == null : other.getKey() == getKey()) &&
                            (other.getValue() == null ? getValue() == null : other.getValue() == getValue());
                }
            }
            return !returns;
        }

        /**
         * Returns the hash code value for this map entry.  The hash code
         * of a map entry {@code e} is defined to be: <pre>
         *     (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
         *     (e.getValue()==null ? 0 : e.getValue().hashCode())
         * </pre>
         * This ensures that {@code e1.equals(e2)} implies that
         * {@code e1.hashCode()==e2.hashCode()} for any two Entries
         * {@code e1} and {@code e2}, as required by the general
         * contract of {@code Object.hashCode}.
         *
         * @return the hash code value for this map entry
         * @see Object#hashCode()
         * @see Object#equals(Object)
         * @see #equals(Object)
         */
        @Pure
        @Ensures({"hashcode_is_valid"})
        int hashCode();
        @Pure
        default boolean hashcode_is_valid(int returns) {
            return returns == ((getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode()));
        }

        /**
         * Returns a comparator that compares {@link Map.Entry} in natural order on key.
         *
         * <p>The returned comparator is serializable and throws {@link
         * NullPointerException} when comparing an entry with a null key.
         *
         * @param  <K> the {@link Comparable} type of then map keys
         * @param  <V> the type of the map values
         * @return a comparator that compares {@link Map.Entry} in natural order on key.
         * @see Comparable
         * @since 1.8
         */
        public static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K, V>> comparingByKey() {
            return (Comparator<Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> c1.getKey().compareTo(c2.getKey());
        }

        /**
         * Returns a comparator that compares {@link Map.Entry} in natural order on value.
         *
         * <p>The returned comparator is serializable and throws {@link
         * NullPointerException} when comparing an entry with null values.
         *
         * @param <K> the type of the map keys
         * @param <V> the {@link Comparable} type of the map values
         * @return a comparator that compares {@link Map.Entry} in natural order on value.
         * @see Comparable
         * @since 1.8
         */
        public static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K, V>> comparingByValue() {
            return (Comparator<Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> c1.getValue().compareTo(c2.getValue());
        }

        /**
         * Returns a comparator that compares {@link Map.Entry} by key using the given
         * {@link Comparator}.
         *
         * <p>The returned comparator is serializable if the specified comparator
         * is also serializable.
         *
         * @param  <K> the type of the map keys
         * @param  <V> the type of the map values
         * @param  cmp the key {@link Comparator}
         * @return a comparator that compares {@link Map.Entry} by the key.
         * @since 1.8
         */
        public static <K, V> Comparator<Map.Entry<K, V>> comparingByKey(Comparator<? super K> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
        }

        /**
         * Returns a comparator that compares {@link Map.Entry} by value using the given
         * {@link Comparator}.
         *
         * <p>The returned comparator is serializable if the specified comparator
         * is also serializable.
         *
         * @param  <K> the type of the map keys
         * @param  <V> the type of the map values
         * @param  cmp the value {@link Comparator}
         * @return a comparator that compares {@link Map.Entry} by the value.
         * @since 1.8
         */
        public static <K, V> Comparator<Map.Entry<K, V>> comparingByValue(Comparator<? super V> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
        }
    }

    // Comparison and hashing

    /**
     * Compares the specified object with this map for equality.  Returns
     * {@code true} if the given object is also a map and the two maps
     * represent the same mappings.  More formally, two maps {@code m1} and
     * {@code m2} represent the same mappings if
     * {@code m1.entrySet().equals(m2.entrySet())}.  This ensures that the
     * {@code equals} method works properly across different implementations
     * of the {@code Map} interface.
     *
     * @param o object to be compared for equality with this map
     * @return {@code true} if the specified object is equal to this map
     */
    @Ensures({"pairwise_equals"})
    boolean equals(Object o);
    @Pure
    default boolean pairwise_equals(boolean returns, Object o) {
        if (o instanceof Map) {
            Map<K, V> other = (Map<K, V>) o;

            if (size() == other.size()) {
                return entrySet().stream().allMatch(entry -> other.get(entry.getKey()) == entry.getValue());
            }
        }
        return !returns;
    }

    /**
     * Returns the hash code value for this map.  The hash code of a map is
     * defined to be the sum of the hash codes of each entry in the map's
     * {@code entrySet()} view.  This ensures that {@code m1.equals(m2)}
     * implies that {@code m1.hashCode()==m2.hashCode()} for any two maps
     * {@code m1} and {@code m2}, as required by the general contract of
     * {@link Object#hashCode}.
     *
     * @return the hash code value for this map
     * @see Map.Entry#hashCode()
     * @see Object#equals(Object)
     * @see #equals(Object)
     */
    @Pure
    @Ensures({"hashcode_is_valid"})
    int hashCode();
    @Pure
    default boolean hashcode_is_valid(int returns) {
        return returns == entrySet().stream().map(Map.Entry::hashCode).reduce(0, Integer::sum);
    }

    // Defaultable methods

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     *
     * @implSpec
     * The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param key the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key
     * @throws ClassCastException if the key is of an inappropriate type for
     * this map
     * (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     * does not permit null keys
     * (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @since 1.8
     */
    @Ensures({"returns_value_if_key", "returns_default_if_not_key",
            "raises_if_null_unsupported_and_null_item", "raises_if_wrong_key_type"})
    default V getOrDefault(Object key, V defaultValue) {
        V v;
        return (((v = get(key)) != null) || containsKey(key))
                ? v
                : defaultValue;
    }
    @Pure
    default boolean returns_value_if_key(V returns, Object key) {
        return implies(containsKey(key), () -> returns == get(key));
    }
    @Pure
    default boolean returns_default_if_not_key(V returns, Object key, V defaultValue) {
        return implies(!containsKey(key), () -> returns == defaultValue);
    }

    /**
     * Performs the given action for each entry in this map until all entries
     * have been processed or the action throws an exception.   Unless
     * otherwise specified by the implementing class, actions are performed in
     * the order of entry set iteration (if an iteration order is specified.)
     * Exceptions thrown by the action are relayed to the caller.
     *
     * @implSpec
     * The default implementation is equivalent to, for this {@code map}:
     * <pre> {@code
     * for (Map.Entry<K, V> entry : map.entrySet())
     *     action.accept(entry.getKey(), entry.getValue());
     * }</pre>
     *
     * The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param action The action to be performed for each entry
     * @throws NullPointerException if the specified action is null
     * @throws ConcurrentModificationException if an entry is found to be
     * removed during iteration
     * @since 1.8
     */
    default void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<K, V> entry : entrySet()) {
            K k;
            V v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }

    /**
     * Replaces each entry's value with the result of invoking the given
     * function on that entry until all entries have been processed or the
     * function throws an exception.  Exceptions thrown by the function are
     * relayed to the caller.
     *
     * @implSpec
     * <p>The default implementation is equivalent to, for this {@code map}:
     * <pre> {@code
     * for (Map.Entry<K, V> entry : map.entrySet())
     *     entry.setValue(function.apply(entry.getKey(), entry.getValue()));
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param function the function to apply to each entry
     * @throws UnsupportedOperationException if the {@code set} operation
     * is not supported by this map's entry set iterator.
     * @throws ClassCastException if the class of a replacement value
     * prevents it from being stored in this map
     * @throws NullPointerException if the specified function is null, or the
     * specified replacement value is null, and this map does not permit null
     * values
     * @throws ClassCastException if a replacement value is of an inappropriate
     *         type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if function or a replacement value is null,
     *         and this map does not permit null keys or values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of a replacement value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ConcurrentModificationException if an entry is found to be
     * removed during iteration
     * @since 1.8
     */
    @Ensures({"size_not_changed", "key_set_not_changed", "values_changed_with_function"})
    default void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Objects.requireNonNull(function);
        for (Map.Entry<K, V> entry : entrySet()) {
            K k;
            V v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }

            // ise thrown from function is not a cme.
            v = function.apply(k, v);

            try {
                entry.setValue(v);
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
        }
    }
    @Pure
    default boolean key_set_not_changed() {
        return old(this).keySet().stream().allMatch(this::containsKey);
    }
    @Pure
    default boolean values_changed_with_function(BiFunction<? super K, ? super V, ? extends V> function) {
        return keySet().stream().allMatch(key -> get(key) == function.apply(key, old(this)get(key)));
    }

    /**
     * If the specified key is not already associated with a value (or is mapped
     * to {@code null}) associates it with the given value and returns
     * {@code null}, else returns the current value.
     *
     * @implSpec
     * The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * V v = map.get(key);
     * if (v == null)
     *     v = map.put(key, value);
     *
     * return v;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with the specified key, or
     *         {@code null} if there was no mapping for the key.
     *         (A {@code null} return can also indicate that the map
     *         previously associated {@code null} with the key,
     *         if the implementation supports null values.)
     * @throws UnsupportedOperationException if the {@code put} operation
     *         is not supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the key or value is of an inappropriate
     *         type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key or value is null,
     *         and this map does not permit null keys or values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key
     *         or value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @since 1.8
     */
    @Ensures({"null_if_not_key_else_value",
            "size_increases_iff_value_not_contained", "value_changed_only_if_not_key"})
    default V putIfAbsent(K key, V value) {
        V v = get(key);
        if (v == null) {
            v = put(key, value);
        }

        return v;
    }
    @Pure
    default boolean value_changed_only_if_not_key(K key, V value) {
        return implies(old(this).containsKey(key),
                () -> get(key) == old(this).get(key),
                () -> get(key) == value);
    }

    /**
     * Removes the entry for the specified key only if it is currently
     * mapped to the specified value.
     *
     * @implSpec
     * The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * if (map.containsKey(key) && Objects.equals(map.get(key), value)) {
     *     map.remove(key);
     *     return true;
     * } else
     *     return false;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param key key with which the specified value is associated
     * @param value value expected to be associated with the specified key
     * @return {@code true} if the value was removed
     * @throws UnsupportedOperationException if the {@code remove} operation
     *         is not supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the key or value is of an inappropriate
     *         type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key or value is null,
     *         and this map does not permit null keys or values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @since 1.8
     */
    default boolean remove(Object key, Object value) {
        Object curValue = get(key);
        if (!Objects.equals(curValue, value) ||
                (curValue == null && !containsKey(key))) {
            return false;
        }
        remove(key);
        return true;
    }

    /**
     * Replaces the entry for the specified key only if currently
     * mapped to the specified value.
     *
     * @implSpec
     * The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * if (map.containsKey(key) && Objects.equals(map.get(key), oldValue)) {
     *     map.put(key, newValue);
     *     return true;
     * } else
     *     return false;
     * }</pre>
     *
     * The default implementation does not throw NullPointerException
     * for maps that do not support null values if oldValue is null unless
     * newValue is also null.
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param key key with which the specified value is associated
     * @param oldValue value expected to be associated with the specified key
     * @param newValue value to be associated with the specified key
     * @return {@code true} if the value was replaced
     * @throws UnsupportedOperationException if the {@code put} operation
     *         is not supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of a specified key or value
     *         prevents it from being stored in this map
     * @throws NullPointerException if a specified key or newValue is null,
     *         and this map does not permit null keys or values
     * @throws NullPointerException if oldValue is null and this map does not
     *         permit null values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of a specified key
     *         or value prevents it from being stored in this map
     * @since 1.8
     */
    default boolean replace(K key, V oldValue, V newValue) {
        Object curValue = get(key);
        if (!Objects.equals(curValue, oldValue) ||
                (curValue == null && !containsKey(key))) {
            return false;
        }
        put(key, newValue);
        return true;
    }

    /**
     * Replaces the entry for the specified key only if it is
     * currently mapped to some value.
     *
     * @implSpec
     * The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * if (map.containsKey(key)) {
     *     return map.put(key, value);
     * } else
     *     return null;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param key key with which the specified value is associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with the specified key, or
     *         {@code null} if there was no mapping for the key.
     *         (A {@code null} return can also indicate that the map
     *         previously associated {@code null} with the key,
     *         if the implementation supports null values.)
     * @throws UnsupportedOperationException if the {@code put} operation
     *         is not supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key or value is null,
     *         and this map does not permit null keys or values
     * @throws IllegalArgumentException if some property of the specified key
     *         or value prevents it from being stored in this map
     * @since 1.8
     */
    @Ensures({"returns_value_iff_key", "size_not_changed", "contains_entry_key_value"})
    default V replace(K key, V value) {
        V curValue;
        if (((curValue = get(key)) != null) || containsKey(key)) {
            curValue = put(key, value);
        }
        return curValue;
    }
    default boolean size_not_changed() {
        return size() == old(this).size();
    }


    /**
     * If the specified key is not already associated with a value (or is mapped
     * to {@code null}), attempts to compute its value using the given mapping
     * function and enters it into this map unless {@code null}.
     *
     * <p>If the mapping function returns {@code null}, no mapping is recorded.
     * If the mapping function itself throws an (unchecked) exception, the
     * exception is rethrown, and no mapping is recorded.  The most
     * common usage is to construct a new object serving as an initial
     * mapped value or memoized result, as in:
     *
     * <pre> {@code
     * map.computeIfAbsent(key, k -> new Value(f(k)));
     * }</pre>
     *
     * <p>Or to implement a multi-value map, {@code Map<K,Collection<V>>},
     * supporting multiple values per key:
     *
     * <pre> {@code
     * map.computeIfAbsent(key, k -> new HashSet<V>()).add(v);
     * }</pre>
     *
     * <p>The mapping function should not modify this map during computation.
     *
     * @implSpec
     * The default implementation is equivalent to the following steps for this
     * {@code map}, then returning the current value or {@code null} if now
     * absent:
     *
     * <pre> {@code
     * if (map.get(key) == null) {
     *     V newValue = mappingFunction.apply(key);
     *     if (newValue != null)
     *         map.put(key, newValue);
     * }
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about detecting if the
     * mapping function modifies this map during computation and, if
     * appropriate, reporting an error. Non-concurrent implementations should
     * override this method and, on a best-effort basis, throw a
     * {@code ConcurrentModificationException} if it is detected that the
     * mapping function modifies this map during computation. Concurrent
     * implementations should override this method and, on a best-effort basis,
     * throw an {@code IllegalStateException} if it is detected that the
     * mapping function modifies this map during computation and as a result
     * computation would never complete.
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties. In particular, all implementations of
     * subinterface {@link java.util.concurrent.ConcurrentMap} must document
     * whether the mapping function is applied once atomically only if the value
     * is not present.
     *
     * @param key key with which the specified value is to be associated
     * @param mappingFunction the mapping function to compute a value
     * @return the current (existing or computed) value associated with
     *         the specified key, or null if the computed value is null
     * @throws NullPointerException if the specified key is null and
     *         this map does not support null keys, or the mappingFunction
     *         is null
     * @throws UnsupportedOperationException if the {@code put} operation
     *         is not supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key
     *         or value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @since 1.8
     */
    default V computeIfAbsent(K key,
                              Function<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        V v;
        if ((v = get(key)) == null) {
            V newValue;
            if ((newValue = mappingFunction.apply(key)) != null) {
                put(key, newValue);
                return newValue;
            }
        }

        return v;
    }

    /**
     * If the value for the specified key is present and non-null, attempts to
     * compute a new mapping given the key and its current mapped value.
     *
     * <p>If the remapping function returns {@code null}, the mapping is removed.
     * If the remapping function itself throws an (unchecked) exception, the
     * exception is rethrown, and the current mapping is left unchanged.
     *
     * <p>The remapping function should not modify this map during computation.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this {@code map}, then returning the current value or
     * {@code null} if now absent:
     *
     * <pre> {@code
     * if (map.get(key) != null) {
     *     V oldValue = map.get(key);
     *     V newValue = remappingFunction.apply(key, oldValue);
     *     if (newValue != null)
     *         map.put(key, newValue);
     *     else
     *         map.remove(key);
     * }
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about detecting if the
     * remapping function modifies this map during computation and, if
     * appropriate, reporting an error. Non-concurrent implementations should
     * override this method and, on a best-effort basis, throw a
     * {@code ConcurrentModificationException} if it is detected that the
     * remapping function modifies this map during computation. Concurrent
     * implementations should override this method and, on a best-effort basis,
     * throw an {@code IllegalStateException} if it is detected that the
     * remapping function modifies this map during computation and as a result
     * computation would never complete.
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties. In particular, all implementations of
     * subinterface {@link java.util.concurrent.ConcurrentMap} must document
     * whether the remapping function is applied once atomically only if the
     * value is not present.
     *
     * @param key key with which the specified value is to be associated
     * @param remappingFunction the remapping function to compute a value
     * @return the new value associated with the specified key, or null if none
     * @throws NullPointerException if the specified key is null and
     *         this map does not support null keys, or the
     *         remappingFunction is null
     * @throws UnsupportedOperationException if the {@code put} operation
     *         is not supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key
     *         or value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @since 1.8
     */
    default V computeIfPresent(K key,
                               BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue;
        if ((oldValue = get(key)) != null) {
            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue != null) {
                put(key, newValue);
                return newValue;
            } else {
                remove(key);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Attempts to compute a mapping for the specified key and its current
     * mapped value (or {@code null} if there is no current mapping). For
     * example, to either create or append a {@code String} msg to a value
     * mapping:
     *
     * <pre> {@code
     * map.compute(key, (k, v) -> (v == null) ? msg : v.concat(msg))}</pre>
     * (Method {@link #merge merge()} is often simpler to use for such purposes.)
     *
     * <p>If the remapping function returns {@code null}, the mapping is removed
     * (or remains absent if initially absent).  If the remapping function
     * itself throws an (unchecked) exception, the exception is rethrown, and
     * the current mapping is left unchanged.
     *
     * <p>The remapping function should not modify this map during computation.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this {@code map}, then returning the current value or
     * {@code null} if absent:
     *
     * <pre> {@code
     * V oldValue = map.get(key);
     * V newValue = remappingFunction.apply(key, oldValue);
     * if (oldValue != null) {
     *    if (newValue != null)
     *       map.put(key, newValue);
     *    else
     *       map.remove(key);
     * } else {
     *    if (newValue != null)
     *       map.put(key, newValue);
     *    else
     *       return null;
     * }
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about detecting if the
     * remapping function modifies this map during computation and, if
     * appropriate, reporting an error. Non-concurrent implementations should
     * override this method and, on a best-effort basis, throw a
     * {@code ConcurrentModificationException} if it is detected that the
     * remapping function modifies this map during computation. Concurrent
     * implementations should override this method and, on a best-effort basis,
     * throw an {@code IllegalStateException} if it is detected that the
     * remapping function modifies this map during computation and as a result
     * computation would never complete.
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties. In particular, all implementations of
     * subinterface {@link java.util.concurrent.ConcurrentMap} must document
     * whether the remapping function is applied once atomically only if the
     * value is not present.
     *
     * @param key key with which the specified value is to be associated
     * @param remappingFunction the remapping function to compute a value
     * @return the new value associated with the specified key, or null if none
     * @throws NullPointerException if the specified key is null and
     *         this map does not support null keys, or the
     *         remappingFunction is null
     * @throws UnsupportedOperationException if the {@code put} operation
     *         is not supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key
     *         or value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @since 1.8
     */
    default V compute(K key,
                      BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue = get(key);

        V newValue = remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            // delete mapping
            if (oldValue != null || containsKey(key)) {
                // something to remove
                remove(key);
                return null;
            } else {
                // nothing to do. Leave things as they were.
                return null;
            }
        } else {
            // add or replace old mapping
            put(key, newValue);
            return newValue;
        }
    }

    /**
     * If the specified key is not already associated with a value or is
     * associated with null, associates it with the given non-null value.
     * Otherwise, replaces the associated value with the results of the given
     * remapping function, or removes if the result is {@code null}. This
     * method may be of use when combining multiple mapped values for a key.
     * For example, to either create or append a {@code String msg} to a
     * value mapping:
     *
     * <pre> {@code
     * map.merge(key, msg, String::concat)
     * }</pre>
     *
     * <p>If the remapping function returns {@code null}, the mapping is removed.
     * If the remapping function itself throws an (unchecked) exception, the
     * exception is rethrown, and the current mapping is left unchanged.
     *
     * <p>The remapping function should not modify this map during computation.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this {@code map}, then returning the current value or
     * {@code null} if absent:
     *
     * <pre> {@code
     * V oldValue = map.get(key);
     * V newValue = (oldValue == null) ? value :
     *              remappingFunction.apply(oldValue, value);
     * if (newValue == null)
     *     map.remove(key);
     * else
     *     map.put(key, newValue);
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about detecting if the
     * remapping function modifies this map during computation and, if
     * appropriate, reporting an error. Non-concurrent implementations should
     * override this method and, on a best-effort basis, throw a
     * {@code ConcurrentModificationException} if it is detected that the
     * remapping function modifies this map during computation. Concurrent
     * implementations should override this method and, on a best-effort basis,
     * throw an {@code IllegalStateException} if it is detected that the
     * remapping function modifies this map during computation and as a result
     * computation would never complete.
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties. In particular, all implementations of
     * subinterface {@link java.util.concurrent.ConcurrentMap} must document
     * whether the remapping function is applied once atomically only if the
     * value is not present.
     *
     * @param key key with which the resulting value is to be associated
     * @param value the non-null value to be merged with the existing value
     *        associated with the key or, if no existing value or a null value
     *        is associated with the key, to be associated with the key
     * @param remappingFunction the remapping function to recompute a value if
     *        present
     * @return the new value associated with the specified key, or null if no
     *         value is associated with the key
     * @throws UnsupportedOperationException if the {@code put} operation
     *         is not supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key
     *         or value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *         does not support null keys or the value or remappingFunction is
     *         null
     * @since 1.8
     */
    default V merge(K key, V value,
                    BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);
        V oldValue = get(key);
        V newValue = (oldValue == null) ? value :
                remappingFunction.apply(oldValue, value);
        if (newValue == null) {
            remove(key);
        } else {
            put(key, newValue);
        }
        return newValue;
    }
}
