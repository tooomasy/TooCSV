package CsvReader;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class CsvReader implements Closeable {

  private final TextBuffer buffer;
  private final CsvLexer lexer;
  private final CsvParser parser;

  public CsvReader(Reader reader) {
    this.buffer = new TextBuffer(reader);
    this.lexer = new CsvLexer(buffer);
    this.parser = new CsvParser(lexer);
  }

  public CsvReader(String data) {
    this(new StringReader(data));
  }

  public List<String> readRow() {
    return parser.readRow();
  }

  public void close() throws IOException {
    buffer.close();
  }
}
