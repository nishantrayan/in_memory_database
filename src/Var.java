import java.util.Stack;


public class Var {
  Stack<String> _values;

  public Var() {
    _values = new Stack<>();
  }

  public Var(String value, boolean newTransaction) {
    this();
    this.updateValue(value, newTransaction);
  }

  public void addValue(String value) {
    _values.push(value);
  }

  public void updateValue(String value, boolean newTransaction) {
    if (!newTransaction) {
      if (!_values.isEmpty()) {
        _values.pop();
      }
    }
    _values.push(value);
  }

  public String getValue() {
    return _values.peek();
  }

  public void popValue() {
    _values.pop();
  }
}
