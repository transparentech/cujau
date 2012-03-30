package org.cujau.utils;

import java.util.List;

/**
 * A hashmap that can store more than one value for a given key. The values are stored in a List in
 * the order in which they were added. Retrieving a value returns a List of values.
 * <p>
 * NOTE: The extended class, {@link MultiHashMap}, already uses a List as the Collection
 * implementation. This class overrides methods that return a Collection in the parent class so that
 * they return a List. The overridden methods simply cast the Collection to a List because we know
 * the implementation of the parent class. See default implementation of
 * {@link MultiHashMap#createEmptyCollection()}.
 * </p>
 * <p>
 * This class is basically a convenience class on top of the original {@link MultiHashMap}
 * implementation. Having the hash values available in a List makes accessing those values more
 * convenient, especially when there is normally only one hashed value in the List (for example,
 * <tt>map.get( mykey ).get( 0 );</tt>).
 * </p>
 * 
 * @param <K>
 *            The Java type used for the hash keys.
 * @param <V>
 *            The Java type used for the hash values.
 */
public class MultiHashMapWithList<K, V> extends MultiHashMap<K, V> {

    @Override
    public List<V> get( K key ) {
        return (List<V>) super.get( key );
    }

    @Override
    public List<V> remove( K key ) {
        return (List<V>) super.remove( key );
    }

    @Override
    public List<V> values() {
        return (List<V>) super.values();
    }
}
