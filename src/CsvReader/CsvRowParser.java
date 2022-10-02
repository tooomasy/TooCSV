package CsvReader;
import java.util.ArrayList;
import java.util.List;

public class CsvRowParser {

  enum ParserState {
    Begin, QuotedText, FieldEnd
  }

  private final CsvLexer lexer;

  public CsvRowParser(CsvLexer lexer) {
    this.lexer = lexer;
  }

  public String[] readRow() {
    String[] row = parseLines();
    if (row.length == 0) return null;
    return row;
  }

  private String[] parseLines() {
    Token peekToken = lexer.peek();

    List<String> row = new ArrayList<>();
    if (peekToken.type.equals("eof"))
      return new String[0];
    StringBuilder textSegement = new StringBuilder();

    ParserState currentState = ParserState.Begin;
    boolean isEndOfRow = false;
    while (isEndOfRow == false) {
      Token token = lexer.getNextToken();

      switch (currentState) {
        case Begin: {
          if (tokenMatchAny(token, "\"")) {
            currentState = ParserState.QuotedText;
          } else if (tokenMatchAny(token, "text")) {
            row.add(token.value);
            currentState = ParserState.FieldEnd;
          } else if (tokenMatchAny(token, ",")) {
            row.add("");
            Token lookahead = lexer.peek();
            if (tokenMatchAny(lookahead, "eof", "cr", "lf", "crlf")) {
              row.add("");
            }
          } else {
            isEndOfRow = true;
          }
          break;
        }
        case QuotedText: {
          if (tokenMatchAny(token, "\"")) {
            Token lookahead = lexer.peek();
            if (tokenMatchAny(lookahead, "\"")) {
              lexer.getNextToken();
              textSegement.append("\"");
            } else {
              row.add(textSegement.toString());
              textSegement = new StringBuilder();
              currentState = ParserState.FieldEnd;
            }
          } else if (tokenMatchAny(token, "eof")) {
            fail("\"", token);
          } else {
            textSegement.append(token.value);
          }
          break;
        }
        case FieldEnd: {
          if (tokenMatchAny(token, ",")) {
            currentState = ParserState.Begin;
            Token lookahead = lexer.peek();
            if (tokenMatchAny(lookahead, "eof", "cr", "lf", "crlf")) {
              row.add("");
            }
          } else if (tokenMatchAny(token, "eof", "cr", "lf", "crlf")) {
            isEndOfRow = true;
          } else {
            // ...
          }
          break;
        }
      }
    }

    return row.toArray(new String[row.size()]);
  }

  private void fail(String expected, Token got) {
    throw new RuntimeException("Expect [" + expected + "] but got [" + got.type + "]");
  }

  private boolean tokenMatchAny(Token token, String... types) {
    for (final String type : types) {
      if (token.type.equals(type)) {
        return true;
      }
    }
    return false;
  }
}
