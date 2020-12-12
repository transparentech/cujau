package org.cujau.utils;

import java.util.Collection;
import java.util.List;

/**
 * Utility methods for working with Collections (List, Iterable, Set, etc).
 */
public class CollectionUtils {
    public static <E> E getFirst(List<E> list) {
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public static <E> E getLast(List<E> list) {
        if (list != null && !list.isEmpty()) {
            return list.get(list.size() - 1);
        }
        return null;
    }

    public static boolean isEmpty(Collection<?> list) {
        return (list == null || list.isEmpty());
    }
}
