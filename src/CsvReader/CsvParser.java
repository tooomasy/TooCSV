package CsvReader;

import java.util.List;

public class CsvParser {

  private final CsvRowParser rowParser;
  private final boolean skipEmptyLine = false;

  private int previousFieldsCount = -1;

  public CsvParser(final CsvLexer lexer) {
    this.rowParser = new CsvRowParser(lexer);
  }

  public List<String> readRow() {
    List<String> row = rowParser.readRow();
    if (row == null)
      return null;

    final int currentRowLength = row.size();
    if (previousFieldsCount == -1) {
      previousFieldsCount = currentRowLength;
    } else {
      if (previousFieldsCount != currentRowLength) {
        throw new RuntimeException("The CSV file contains " + previousFieldsCount + " fields in row 1");
      }
    }
    return row;
  }
}
