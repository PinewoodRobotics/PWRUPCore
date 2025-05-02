package pwrup.frc.core.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ClassDouble<T1, T2> {

  private T1 one;
  private T2 two;

  public T1 one() {
    return one;
  }

  public T2 two() {
    return two;
  }
}
