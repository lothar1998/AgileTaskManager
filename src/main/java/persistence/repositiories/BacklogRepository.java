package persistence.repositiories;

import persistence.AbstractRepository;
import persistence.CrudRepository;
import persistence.entities.BacklogEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BacklogRepository extends AbstractRepository implements CrudRepository<BacklogEntity> {
    @Override
    public void save(BacklogEntity arg) throws SQLException {
        String sql = "INSERT INTO agile.backlogs (description, project_id) VALUES (?, ?)";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, arg.getDescription());
            statement.setInt(2, arg.getProjectId());

            statement.executeUpdate();

            ResultSet resultKey = statement.getGeneratedKeys();

            if(resultKey.next())
                arg.setId(resultKey.getInt("id"));

            statement.close();
        });
    }

    @Override
    public void update(BacklogEntity arg) throws SQLException {
        String sql = "UPDATE agile.backlogs SET description = ?, project_id = ? WHERE id = ?";
        dataAccessLayer.executeQuery(connection ->{
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, arg.getDescription());
            statement.setInt(2, arg.getProjectId());
            statement.setInt(3, arg.getId());

            statement.executeUpdate();

            statement.close();
        });
    }

    @Override
    public void delete(BacklogEntity arg) throws SQLException {
        String sql = "DELETE FROM agile.backlogs WHERE id = ?";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, arg.getId());

            statement.executeUpdate();

            statement.close();
        });
    }

    @Override
    public BacklogEntity get(Integer arg) throws SQLException {
        String sql = "SELECT * FROM agile.backlogs WHERE id = ?";
        return dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setObject(1, arg);

            ResultSet result = statement.executeQuery();

            BacklogEntity backlogEntity = null;

            if(result.next()){
                backlogEntity = new BacklogEntity();
                backlogEntity.setId(result.getInt("id"));
                backlogEntity.setDescription(result.getString("description"));
                backlogEntity.setProjectId(result.getInt("project_id"));
            }

            statement.close();

            return backlogEntity;
        });
    }

    @Override
    public List<BacklogEntity> getAll() throws SQLException {
        String sql = "SELECT * FROM agile.backlogs";
        return dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            List<BacklogEntity> entityList = new ArrayList<>();

            while (resultSet.next()){
                BacklogEntity backlogEntity = new BacklogEntity();
                backlogEntity.setId(resultSet.getInt("id"));
                backlogEntity.setDescription(resultSet.getString("description"));
                backlogEntity.setProjectId(resultSet.getInt("project_id"));

                entityList.add(backlogEntity);
            }

            return entityList;
        });
    }
}
