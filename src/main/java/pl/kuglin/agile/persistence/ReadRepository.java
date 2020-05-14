package pl.kuglin.agile.persistence;

import java.sql.SQLException;
import java.util.List;

public interface ReadRepository<T, S> {
    T get(S arg) throws SQLException;

    List<T> getAll() throws SQLException;
}
