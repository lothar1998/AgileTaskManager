package pl.kuglin.agile.persistence.repositiories;

import pl.kuglin.agile.persistence.AbstractRepository;
import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.DataAccessLayer;
import pl.kuglin.agile.persistence.entities.SprintEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SprintRepository extends AbstractRepository implements CrudRepository<SprintEntity, Integer> {

    public SprintRepository(DataAccessLayer dataAccessLayer) {
        super(dataAccessLayer);
    }

    @Override
    public void save(SprintEntity arg) throws SQLException {
        String sql = "INSERT INTO agile.sprints (name, no, project_id) VALUES (?, ?, ?)";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, arg.getName());
            statement.setInt(2, arg.getNo());
            statement.setInt(3, arg.getProjectId());
            statement.executeUpdate();

            ResultSet resultKey = statement.getGeneratedKeys();

            if (resultKey.next())
                arg.setId(resultKey.getInt("id"));

            statement.close();
        });
    }

    @Override
    public void update(SprintEntity arg) throws SQLException {
        String sql = "UPDATE agile.sprints SET name = ?, no = ?, project_id = ? WHERE id = ?";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, arg.getName());
            statement.setInt(2, arg.getNo());
            statement.setInt(3, arg.getProjectId());
            statement.setInt(4, arg.getId());
            statement.executeUpdate();

            statement.close();
        });
    }

    @Override
    public void delete(SprintEntity arg) throws SQLException {
        String sql = "DELETE FROM agile.sprints WHERE id = ?";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, arg.getId());
            statement.executeUpdate();

            statement.close();
        });
    }

    @Override
    public SprintEntity get(Integer arg) throws SQLException {
        String sql = "SELECT * FROM agile.sprints WHERE id = ?";
        return dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, arg);

            ResultSet result = statement.executeQuery();

            SprintEntity sprintEntity = null;

            if (result.next()) {
                sprintEntity = new SprintEntity();
                sprintEntity.setId(result.getInt("id"));
                sprintEntity.setName(result.getString("name"));
                sprintEntity.setNo(result.getInt("no"));
                sprintEntity.setProjectId(result.getInt("project_id"));
            }

            statement.close();

            return sprintEntity;
        });
    }

    @Override
    public List<SprintEntity> getAll() throws SQLException {
        String sql = "SELECT * FROM agile.sprints";
        return dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            List<SprintEntity> entityList = new ArrayList<>();

            while (result.next()) {
                SprintEntity sprintEntity = new SprintEntity();
                sprintEntity.setId(result.getInt("id"));
                sprintEntity.setName(result.getString("name"));
                sprintEntity.setNo(result.getInt("no"));
                sprintEntity.setProjectId(result.getInt("project_id"));

                entityList.add(sprintEntity);
            }

            statement.close();

            return entityList;
        });
    }
}
