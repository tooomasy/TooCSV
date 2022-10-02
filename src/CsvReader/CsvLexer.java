package CsvReader;

public class CsvLexer {

  static final char CR = '\r';
  static final char LF = '\n';
  static final char DQUOTE = '"';
  static final char COMMA = ',';

  enum LexerState {
    BEGIN, CR_PREFIX, TEXT
  }

  private final TextBuffer buffer;

  private boolean peekConsumedToken = false;
  private Token peekToken = null;

  public CsvLexer(TextBuffer buffer) {
    this.buffer = buffer;
  }

  public Token peek() {
    Token token = getNextToken();
    peekConsumedToken = true;
    peekToken = token;
    return token;
  }

  public Token getNextToken() {
    if (peekConsumedToken) {
      peekConsumedToken = false;
      return peekToken;
    }

    Token token = scan();
    buffer.prepareForNextToken();

    return token;
  }

  private Token scan() {
    LexerState currentState = LexerState.BEGIN;

    if (buffer.isEndOfBuffer()) {
      return new Token("eof");
    }

    for (;; buffer.increaseForwards()) {
      char ch = buffer.getNextChar();

      switch (currentState) {
        case BEGIN:
          if (ch == DQUOTE) return new Token("\"");
          else if (ch == COMMA) return new Token(",", ",");
          else if (ch == LF) return new Token("lf", "\n");
          else if (ch == CR) {
            currentState = LexerState.CR_PREFIX;
          } else {
            currentState = LexerState.TEXT;
          }
          break;
        case CR_PREFIX:
          if (ch == LF) return new Token("crlf", "\r\n");
          retract();
          return new Token("cr", "\r");
        case TEXT:
          if (ch == DQUOTE || ch == COMMA || ch == LF || ch == CR || ch == '\0') {
            Token token = new Token("text", buffer.formToken());
            retract();
            return token;
          }
          break;
      }
    }
  }

  private void retract() {
    buffer.retract();
  }
}
