package org.cujau.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MultiHashMap<K,V> {

    private Map<K, Collection<V>> map = new HashMap<K, Collection<V>>();
    
    public boolean containsValue( V val ) {
        Collection<V> allValues = values();
        return allValues.contains( val );
    }
    
    public Collection<V> get( K key ) {
        return map.get( key );
    }
    
    public void put( K key, V val ) {
        Collection<V> valCol = map.get( key );
        if ( valCol == null ) {
            valCol = new ArrayList<V>();
            map.put( key, valCol );
        }
        valCol.add( val );
    }
    
    public Collection<V> remove( K key ) {
        return map.remove( key );
    }
    
    public V remove( K key, V val ) {
        Collection<V> valCol = map.get( key );
        if ( valCol == null ) {
            return null;
        }
        if ( valCol.remove( val ) ) {
            return val;
        }
        return null;
    }
    
    public void clear() {
        map.clear();
    }
    
    public int size() {
        return map.size();
    }
    
    public Collection<V> values() {
        ArrayList<V> ret = new ArrayList<V>();
        for ( Map.Entry<K, Collection<V>> ent : map.entrySet() ) {
            ret.addAll( ent.getValue() );
        }
        return ret;
    }
}
