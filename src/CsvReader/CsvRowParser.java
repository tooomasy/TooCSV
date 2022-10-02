package CsvReader;
import java.util.ArrayList;
import java.util.List;

public class CsvRowParser {

  enum ParserState {
    NormalText, QuotedText, FieldEnd
  }

  private final CsvLexer lexer;

  public CsvRowParser(CsvLexer lexer) {
    this.lexer = lexer;
  }

  public List<String> readRow() {
    List<String> row = parseLines();
    if (row.size() == 0) return null;
    return row;
  }

  private List<String> parseLines() {
    Token peekToken = lexer.peek();

    List<String> row = new ArrayList<>();
    if (peekToken.type.equals("eof"))
      return row;
    StringBuilder textSegment = new StringBuilder();

    ParserState currentState = ParserState.NormalText;
    boolean isEndOfRow = false;
    while (!isEndOfRow) {
      Token token = lexer.getNextToken();

      switch (currentState) {
        case NormalText: {
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
              textSegment.append("\"");
            } else {
              row.add(textSegment.toString());
              textSegment = new StringBuilder();
              currentState = ParserState.FieldEnd;
            }
          } else if (tokenMatchAny(token, "eof")) {
            fail("\"", token);
          } else {
            textSegment.append(token.value);
          }
          break;
        }
        case FieldEnd: {
          if (tokenMatchAny(token, ",")) {
            currentState = ParserState.NormalText;
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

    return row;
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
