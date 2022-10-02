package CsvReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

public class TextBuffer implements Closeable {
  private static int BUFFER_SIZE = 1024 * 4;

  private Reader reader;

  char[] buffers = new char[BUFFER_SIZE * 2];
  boolean isBufferFilled = false;
  int curBufferSize = 0;

  int lexemBegin = 0;
  int forwards = 0;

  public TextBuffer(Reader reader) {
    this.reader = reader;
  }

  public void increaseForwards() {
    forwards++;
  }

  public char getNextChar() {
    if (isBufferFilled == false) {
      initBuffers();
    }

    if (forwards >= curBufferSize) {
      if (curBufferSize < BUFFER_SIZE * 2) return '\0';
      moveRightBufferToLeft();
      int n = fillRightBuffer();
      curBufferSize = BUFFER_SIZE + n;
      forwards -= BUFFER_SIZE;
      lexemBegin -= BUFFER_SIZE;
    }

    return buffers[forwards];
  }

  public String formToken() {
    return new String(buffers, lexemBegin, forwards-lexemBegin);
  }

  public void prepareForNextToken() {
    lexemBegin = forwards = forwards + 1;
  }

  public void retract() {
    forwards--;
  }

  public boolean isEndOfBuffer() {
    if (isBufferFilled == false) {
      initBuffers();
    }

    return forwards >= curBufferSize && curBufferSize < BUFFER_SIZE * 2;
  }

  public void close() throws IOException {
    reader.close();
  }

  private void initBuffers() {
    try {
      int n = reader.read(buffers);
      curBufferSize = n;
      isBufferFilled = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void moveRightBufferToLeft() {
    System.arraycopy(buffers, BUFFER_SIZE, buffers, 0, BUFFER_SIZE);
  }

  private int fillRightBuffer() {
    char[] tmpBuffer = new char[BUFFER_SIZE];
    int n = 0;
    try {
      n = reader.read(tmpBuffer);
      if (n == -1) {
        reader.close();
        return 0;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.arraycopy(tmpBuffer, 0, buffers, BUFFER_SIZE, n);
    return n;
  }
}
