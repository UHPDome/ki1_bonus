package ki.jfour;

import java.util.*;
import java.util.function.*;

public class Util {
  public static <T> Optional<T> returnFirst(Supplier<Optional<T>>... suppliers) {
    for (Supplier<Optional<T>> s : suppliers) {
      final Optional<T> result = s.get();
      if (result.isPresent()) {
        return result;
      }
    }

    return Optional.empty();
  }
}
