package ki.jfour;

import java.util.*;

public abstract class AI {
  private Optional<Move> bestMove = Optional.empty();

  public synchronized void setBestMove(Move move) {
    bestMove = Optional.of(move);
  }

  public synchronized Optional<Move> getBestMove() {
    return bestMove;
  }

  abstract public void start(Board b);
  abstract public String getDescription();


}
