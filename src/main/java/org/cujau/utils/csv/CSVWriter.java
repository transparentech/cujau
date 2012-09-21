package org.cujau.utils.csv;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CSVWriter {
    private CSVSymbols symbols;
    private Writer writer;

    public CSVWriter( Writer parentWriter ) {
        this( parentWriter, new CSVSymbols() );
    }

    public CSVWriter( Writer parentWriter, CSVSymbols symbols ) {
        this.symbols = symbols;
        this.writer = parentWriter;
    }

    public CSVSymbols getCSVSymbols() {
        return symbols;
    }
    
    public void renderLine( List<String> records )
            throws IOException {
        StringCSVRenderer renderer = new StringCSVRenderer();
        renderLine( renderer, records );
    }

    public void renderLine( CSVRenderer renderer, List<Object> recordValues, Object... accessoryData )
            throws IOException {
        boolean needSep = false;
        for ( Object value : recordValues ) {
            String s = renderer.renderRecord( value, accessoryData );
            needSep = writeRecord( s, needSep );
        }
        writer.write( symbols.getLineSeparator() );
    }

    public <E> void renderLine( CSVTypedRenderer<E> renderer, List<E> recordValues, Object... accessoryData )
            throws IOException {
        boolean needSep = false;
        for ( E value : recordValues ) {
            String s = renderer.renderRecord( value, accessoryData );
            needSep = writeRecord( s, needSep );
        }
        writer.write( symbols.getLineSeparator() );
    }

    boolean writeRecord( String s, boolean needSep )
            throws IOException {
        if ( needSep ) {
            writer.write( symbols.getRecordSeparator() );
        }
        writer.write( quoteIfNecessary( escapeDoubleQuotes( s ) ) );
        return true;
    }
    
    String escapeDoubleQuotes( String str ) {
        return str.replace( "\"", "\"\"" );
    }

    String quoteIfNecessary( String str ) {
        if ( str.indexOf( symbols.getRecordSeparator() ) != -1 ||
                str.indexOf( symbols.getLineSeparator() ) != -1 ) {
            return "\"" + str + "\"";
        }
        return str;
    }
    
    private class StringCSVRenderer implements CSVTypedRenderer<String> {
        @Override
        public String renderRecord( String value, Object... accessoryData ) {
            return value;
        }
    }
}
