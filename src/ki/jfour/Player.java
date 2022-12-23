package ki.jfour;

public enum Player {
  NONE,
  RED,
  BLUE;

  public Player next() {
    switch (this) {
      case NONE: return NONE;
      case RED:  return BLUE;
      case BLUE: return RED;
    }

    throw new RuntimeException("should never happen");
  }
}
