package CsvReader;

public class CsvParser {

  private final CsvRowParser rowParser;
  private final boolean skipEmptyLine = false;

  private int previousFieldsCount = -1;

  public CsvParser(final CsvLexer lexer) {
    this.rowParser = new CsvRowParser(lexer);
  }

  public String[] readRow() {
    String[] row = rowParser.readRow();
    if (row == null)
      return null;

    final int currentRowLength = row.length;
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
