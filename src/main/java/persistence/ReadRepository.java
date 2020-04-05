package persistence;

import java.util.List;

public interface ReadRepository<T> {
    T get(Integer arg);
    List<T> getAll();
}
