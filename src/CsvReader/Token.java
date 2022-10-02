package CsvReader;
public class Token {

  public final String type;
  public final String value; 

  Token(final String type) {
    this(type, "");
  }

  Token(final String type, final String value) {
    this.type = type;
    this.value = value;
  }

  @Override
  public String toString() {
    return "Token [type=" + type + ", value=" + value + "]";
  }
}
