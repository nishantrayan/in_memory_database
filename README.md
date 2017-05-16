# in_memory_database
## Supports
1. Get
2. Set
3. Delete
4. Begin
  4a. Rollback
5. Commit


## Example
### Simple Set/Get ###

```java
DataStore dataStore = new DataStore();
dataStore.set("a", "123");
dataStore.get("a"); //returns "123"
```
### Simple Set/Delete ###

```java
DataStore dataStore = new DataStore();
dataStore.set("a", "123");
dataStore.delete("a");
dataStore.get("a"); //returns null
```
### Simple Transaction rollback ###

```java
DataStore dataStore = new DataStore();
dataStore.set("a", "123");
dataStore.begin();
dataStore.delete("a");
dataStore.get("a"); //returns null
dataStore.rollback();
dataStore.get("a"); //returns "123"
```
