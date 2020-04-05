package persistence;

public interface CrudRepository<T> extends ReadRepository<T> {
    void save(T arg);
    void update(T arg);
    void delete(T arg);
}
