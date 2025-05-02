package pwrup.frc.core.types;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClassTriple<T1, T2, T3> {

  private T1 one;
  private T2 two;
  private T3 three;

  public T1 one() {
    return one;
  }

  public T2 two() {
    return two;
  }

  public T3 three() {
    return three;
  }
}
