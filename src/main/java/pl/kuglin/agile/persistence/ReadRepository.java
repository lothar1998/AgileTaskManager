package pl.kuglin.agile.persistence;

import java.sql.SQLException;
import java.util.List;

public interface ReadRepository<T> {
    T get(Integer arg) throws SQLException;
    List<T> getAll() throws SQLException;
}
