package persistence;

import java.sql.SQLException;

public interface CrudRepository<T> extends ReadRepository<T> {
    void save(T arg) throws SQLException;
    void update(T arg) throws SQLException;
    void delete(T arg) throws SQLException;
}
