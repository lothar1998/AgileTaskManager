package pl.kuglin.agile.persistence;

import java.sql.SQLException;

public interface CrudRepository<T, S> extends ReadRepository<T, S> {
    void save(T arg) throws SQLException;
    void update(T arg) throws SQLException;
    void delete(T arg) throws SQLException;
}
