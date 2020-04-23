package pl.kuglin.agile.persistence;

import java.sql.SQLException;

@FunctionalInterface
public interface ConsumerSQL<T> {
    void accept(T arg) throws SQLException;
}
