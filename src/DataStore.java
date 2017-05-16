import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;


public class DataStore {

  Map<String, Var> _varValue;
  Map<String, Var> _counts;
  Stack<Transaction> _transactions;

  public DataStore() {
    _varValue = new HashMap<>();
    _counts = new HashMap<>();
    _transactions = new Stack<>();
    _transactions.push(new Transaction());
  }

  public void set(String varName, String value) {
    Transaction transaction = _transactions.peek();
    boolean newTransaction = !transaction.getAffectedVars().contains(varName);
    if (!_varValue.containsKey(varName)) {
      Var var = new Var();
      var.addValue(value);
      _varValue.put(varName, var);
    } else {
      Var var = _varValue.get(varName);
      changeCount(var.getValue(), -1);
      var.updateValue(value, newTransaction);
    }
    transaction.addAffectedVariable(varName);
    changeCount(_varValue.get(varName).getValue(), 1);
  }

  public String get(String varName) {
    Var var = _varValue.get(varName);
    return var == null ? null : var.getValue();
  }

  public int count(String value) {
    return _counts.get(value) == null ? 0 : Integer.parseInt(_counts.get(value).getValue());
  }

  public boolean delete(String varName) {
    Var var = _varValue.get(varName);
    if (var == null || var.getValue() == null) {
      return false;
    }
    changeCount(_varValue.get(varName).getValue(), -1);
    Transaction transaction = _transactions.peek();
    transaction.addAffectedVariable(varName);
    var.updateValue(null, true);
    return true;
  }

  public void begin() {
    _transactions.push(new Transaction());
  }

  public boolean rollback() {
    if (_transactions.size() <= 1) {
      return false;
    }
    Transaction transaction = _transactions.pop();
    transaction.getAffectedVars()
        .forEach(varName -> Optional.ofNullable(_varValue.get(varName)).ifPresent(Var::popValue));

    transaction.getAffectedCounts()
        .forEach(varName -> Optional.ofNullable(_counts.get(varName)).ifPresent(Var::popValue));

    return true;
  }

  public void commit() {
    while (_transactions.size() > 1) {
      _transactions.pop();
    }
  }

  private void changeCount(String value, int change) {
    Transaction transaction = _transactions.peek();
    boolean newTransaction = !transaction.getAffectedCounts().contains(value);
    _counts.putIfAbsent(value, new Var("0", newTransaction));
    Var var = _counts.get(value);
    var.updateValue(String.valueOf(Integer.parseInt(var.getValue()) + change), newTransaction);
    transaction.addAffectedCount(value);
  }
}
