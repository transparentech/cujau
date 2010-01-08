package org.cujau.utils.csv;

public interface CSVRenderer {
    String renderRecord( Object value, Object... accessoryData );
}
