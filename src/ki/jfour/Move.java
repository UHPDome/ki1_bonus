package ki.jfour;

public class Move {
  public final int column;

  public Move(int column) {
    this.column = column;
  } 

  @Override
  public boolean equals(Object other) {
    return other instanceof Move && ((Move) other).column == column;
  }

  @Override
  public int hashCode() {
    return this.column;
  }

  @Override
  public String toString() {
    return "Move(" + column + ")";
  }
}
