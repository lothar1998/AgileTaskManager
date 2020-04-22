package persistence;

public abstract class AbstractRepository {
    protected DataAccessLayer dataAccessLayer;

    public AbstractRepository(DataAccessLayer dataAccessLayer) {
        this.dataAccessLayer = dataAccessLayer;
    }
}
