package ki.jfour;

import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class Board {
  private Player turn = Player.RED;
  private Player[][] board;
  private int width;
  private int height;

  public Board(int height, int width) {
    this.width = width;
    this.height = height;
    this.board = new Player[height][width];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        this.board[y][x] = Player.NONE;
      }
    }
  }

  private Board(int height, int width, Player[][] board, Player turn) {
    this.height = height;
    this.width = width;
    this.board = board;
    this.turn = turn;
  }

  public Player getCurrentPlayer() {
    return turn;
  }

  public Board executeMove(Move m) {
    if (m.column >= width || m.column < 0) {
      throw new RuntimeException("invalid column " + m.column);
    } else if (board[0][m.column] != Player.NONE) {
      throw new RuntimeException("placed chip into full column " + m.column);
    } else {
      final Player[][] board = getState();
      for (int y = height - 1; y >= 0; y--) {
        if (board[y][m.column] == Player.NONE) {
          board[y][m.column] = turn;
          break;
        }
      }
      return new Board(height, width, board, turn.next());
    }
  }

  public List<Move> possibleMoves() {
    return IntStream.range(0, width)
      .filter(i -> board[0][i] == Player.NONE)
      .mapToObj(i -> new Move(i))
      .collect(Collectors.toList());
  }

  public Player[][] getState() {
    Player[][] board = new Player[height][width];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        board[y][x] = this.board[y][x];
      }
    }

    return board;
  }

  public Player getWinner() {
    return Stream.<Supplier<Player>>of(
        this::getVerticalWinner,
        this::getHorizontalWinner,
        this::getDiagonalWinner45,
        this::getDiagonalWinner315)
      .map(Supplier::get)
      .filter(p -> p != Player.NONE)
      .findFirst()
      .orElse(Player.NONE);
  }

  private Player getVerticalWinner() {
    for (int y = height - 1; y >= 3; y--) {
      for (int x = 0; x < width; x++) {
        if (board[y][x] != Player.NONE &&
            board[y][x] == board[y - 1][x] &&
            board[y][x] == board[y - 2][x] &&
            board[y][x] == board[y - 3][x])
        {
          return board[y][x];
        }
      }
    }

    return Player.NONE;
  }

  private Player getHorizontalWinner() {
    for (int x = 0; x < width - 3; x++) {
      for (int y = height - 1; y >= 0; y--) {
        if (board[y][x] != Player.NONE &&
            board[y][x] == board[y][x + 1] &&
            board[y][x] == board[y][x + 2] &&
            board[y][x] == board[y][x + 3])
        {
          return board[y][x];
        }
      }
    }

    return Player.NONE;
  }

  private Player getDiagonalWinner45() {
    for (int y = height - 1; y >= 3; y--) {
      for (int x = 0; x < width - 3; x++) {
        if (board[y][x] != Player.NONE &&
            board[y][x] == board[y - 1][x + 1] &&
            board[y][x] == board[y - 2][x + 2] &&
            board[y][x] == board[y - 3][x + 3])
        {
          return board[y][x];
        }
      }
    }

    return Player.NONE;
  }

  private Player getDiagonalWinner315() {
    for (int y = height - 1; y >= 3; y--) {
      for (int x = width - 1; x >= 3; x--) {
        if (board[y][x] != Player.NONE &&
            board[y][x] == board[y - 1][x - 1] &&
            board[y][x] == board[y - 2][x - 2] &&
            board[y][x] == board[y - 3][x - 3])
        {
          return board[y][x];
        }
      }
    }

    return Player.NONE;
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Board && Arrays.deepEquals(((Board) other).board, board);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(board);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();

    for (int y = 0; y < height; y++) {
      if (y != 0) {
        sb.append("\n");
      }
      for (int x = 0; x < width; x ++) {
        if (board[y][x] == Player.NONE) {
          sb.append(". ");
        } else if (board[y][x] == Player.RED) {
          sb.append("r ");
        } else {
          sb.append("b ");
        }
      }
    }

    sb.append("\n===================");
    sb.append("\n1 2 3 4 5 6 7 8 9 0");
    return sb.toString();
  }
}
