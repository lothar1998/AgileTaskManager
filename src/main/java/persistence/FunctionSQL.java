package persistence;

import java.sql.SQLException;

@FunctionalInterface
public interface FunctionSQL<T, R> {
    R apply(T arg) throws SQLException;
}
