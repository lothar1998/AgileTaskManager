package persistence;

public abstract class AbstractRepository {
    protected DataAccessLayer dataAccessLayer;

    public AbstractRepository() {
        dataAccessLayer = DataAccessLayer.getInstance();
    }
}
