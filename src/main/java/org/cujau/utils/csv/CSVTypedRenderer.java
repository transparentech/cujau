package org.cujau.utils.csv;

public interface CSVTypedRenderer<E> {
    String renderRecord( E value, Object... accessoryData );
}
