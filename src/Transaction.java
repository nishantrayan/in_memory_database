import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;


public class Transaction {
  Set<String> _vars;
  Set<String> _counts;

  public Transaction() {
    _vars = new HashSet<>();
    _counts = new HashSet<>();
  }

  public void addAffectedVariable(String varName) {
    _vars.add(varName);
  }

  public void addAffectedCount(String value) {
    _counts.add(value);
  }

  public Set<String> getAffectedVars() {
    return _vars;
  }

  public Set<String> getAffectedCounts() {
    return _counts;
  }
}
