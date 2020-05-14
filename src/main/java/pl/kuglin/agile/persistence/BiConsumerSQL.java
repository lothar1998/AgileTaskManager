package pl.kuglin.agile.persistence;

import java.sql.SQLException;

@FunctionalInterface
public interface BiConsumerSQL<T, S> {
    void accept(T arg1, S arg2) throws SQLException;
}
