import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class DataStoreTest {
  private DataStore _dataStore;

  @Before
  public void setup() {
    _dataStore = new DataStore();
  }

  @Test
  public void testGetReturnsNull() {
    Assert.assertNull("Get returns value even though it was not set", new DataStore().get("a"));
  }

  @Test
  public void testGetReturnsSetValue() {
    _dataStore.set("a", "10");
    Assert.assertEquals("10", _dataStore.get("a"));
  }

  @Test
  public void testCountReturnsZero() {
    Assert.assertEquals(0, _dataStore.count("10"));
  }

  @Test
  public void testCountReturnsActualCount() {
    _dataStore.set("a", "10");
    _dataStore.set("b", "10");
    Assert.assertEquals(2, _dataStore.count("10"));
  }

  @Test
  public void testCountReturnsActualCountAfterOverwritting() {
    _dataStore.set("a", "10");
    _dataStore.set("a", "20");
    Assert.assertEquals(0, _dataStore.count("10"));
  }

  @Test
  public void testGetAfterDelete() {
    _dataStore.set("a", "10");
    _dataStore.delete("a");
    Assert.assertNull("Get after delete still returns data", _dataStore.get("a"));
  }

  @Test
  public void testCountAfterDelete() {
    _dataStore.set("a", "10");
    _dataStore.delete("a");
    Assert.assertEquals("Count after delete still returns data", 0, _dataStore.count("10"));
  }

  @Test
  public void testGetAfterRollbackSet() {
    _dataStore.set("a", "10");
    _dataStore.begin();
    _dataStore.set("a", "20");
    _dataStore.rollback();
    Assert.assertEquals("Rollback doesn't seem to have rolled back set operation correctly", "10", _dataStore.get("a"));
  }

  @Test
  public void testGetAfterSetTransaction() {
    _dataStore.set("a", "10");
    _dataStore.begin();
    _dataStore.set("a", "20");
    Assert.assertEquals("Set in transaction doesn't seem to have overridden value", "20", _dataStore.get("a"));
  }

  @Test
  public void testGetAfterDeleteTransaction() {
    _dataStore.set("a", "10");
    _dataStore.begin();
    _dataStore.delete("a");
    Assert.assertNull("Delete in transaction doesn't seem to deleted value", _dataStore.get("a"));
  }

  @Test
  public void testGetAfterRollbackDelete() {
    _dataStore.set("a", "10");
    _dataStore.begin();
    _dataStore.delete("a");
    _dataStore.rollback();
    Assert.assertEquals("Rollback doesn't seem to have rolled back delete operation correctly", "10",
        _dataStore.get("a"));
  }

  @Test
  public void testCountAfterRollbackSet() {
    _dataStore.set("a", "10");
    _dataStore.begin();
    _dataStore.set("b", "10");
    _dataStore.rollback();
    Assert.assertEquals("Rollback doesn't seem to have rolled back count operation correctly", 1,
        _dataStore.count("10"));
  }

  @Test
  public void testCountAfterBegin() {
    _dataStore.set("a", "10");
    _dataStore.begin();
    _dataStore.set("b", "10");
    Assert.assertEquals("Transaction doesn't seem to have counted  correctly", 2, _dataStore.count("10"));
  }

  @Test
  public void testCountAfterNestedBegin() {
    _dataStore.set("a", "10");
    _dataStore.begin();
    _dataStore.set("b", "10");
    _dataStore.begin();
    _dataStore.set("c", "10");
    Assert.assertEquals("Transaction doesn't seem to have counted  correctly", 3, _dataStore.count("10"));
  }

  @Test
  public void testCountUpdateAfterNestedBegin() {
    _dataStore.set("a", "10");
    _dataStore.begin();
    _dataStore.set("a", "20");
    Assert.assertEquals("Transaction doesn't seem to have counted  correctly", 0, _dataStore.count("10"));
    Assert.assertEquals("Transaction doesn't seem to have counted  correctly", 1, _dataStore.count("20"));
  }

  @Test
  public void testCountAfterDeleteInNestedBegin() {
    _dataStore.set("a", "10");
    _dataStore.begin();
    _dataStore.set("a", "20");
    _dataStore.delete("a");
    Assert.assertEquals("Transaction doesn't seem to have counted  correctly", 0, _dataStore.count("10"));
    Assert.assertEquals("Transaction doesn't seem to have counted  correctly", 0, _dataStore.count("20"));
  }

  @Test
  public void testCountAfterRollbackSetSameVar() {
    _dataStore.set("a", "10");
    _dataStore.begin();
    _dataStore.set("a", "20");
    _dataStore.rollback();
    Assert.assertEquals("Count doesn't seem to have been updated on rollback", 1, _dataStore.count("10"));
    Assert.assertEquals("Count doesn't seem to have been updated on rollback", 0, _dataStore.count("20"));
  }

  @Test
  public void testGetAfterCommit() {
    _dataStore.set("a", "10");
    _dataStore.begin();
    _dataStore.set("b", "20");
    _dataStore.commit();
    Assert.assertEquals("Get after commit is broken", "10", _dataStore.get("a"));
    Assert.assertEquals("Get after commit is broken", "20", _dataStore.get("b"));
  }

  @Test
  public void testCountAfterRollbackDelete() {
    _dataStore.set("a", "10");
    _dataStore.delete("a");
    _dataStore.begin();
    _dataStore.set("b", "20");
    _dataStore.delete("b");
    _dataStore.rollback();
    Assert.assertEquals("Count after rollback delete is not working", 0, _dataStore.count("10"));
    Assert.assertEquals("Count after rollback delete is not working", 0, _dataStore.count("20"));
  }

  @Test
  public void testRollbackWithoutTransactions() {
    _dataStore.set("a", "10");
    Assert.assertFalse("Rollback should not be possible without transactions", _dataStore.rollback());
  }

  @Test
  public void testRollbackAfterCommit() {
    _dataStore.set("a", "10");
    _dataStore.begin();
    _dataStore.set("b", "10");
    _dataStore.commit();
    Assert.assertFalse("Rollback should not be possible immediately after commit", _dataStore.rollback());
  }

  @Test
  public void testRollbackWhenAlreadyRolledback() {
    _dataStore.set("a", "10");
    _dataStore.begin();
    _dataStore.set("b", "10");
    _dataStore.rollback();
    Assert.assertFalse("Rollback should not be possible immediately after commit", _dataStore.rollback());
  }

  @Test
  public void testDeleteReturnsFalseIfAlreadyDeleted() {
    _dataStore.set("a", "10");
    _dataStore.delete("a");
    Assert.assertFalse("Deleting an already deleted variable should return false", _dataStore.delete("a"));
  }
}
