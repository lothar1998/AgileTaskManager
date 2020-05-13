package pl.kuglin.agile.persistence.repositiories;

import pl.kuglin.agile.persistence.AbstractRepository;
import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.DataAccessLayer;
import pl.kuglin.agile.persistence.entities.TaskEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository extends AbstractRepository implements CrudRepository<TaskEntity, Integer> {
    public TaskRepository(DataAccessLayer dataAccessLayer) {
        super(dataAccessLayer);
    }

    @Override
    public void save(TaskEntity arg) throws SQLException {
        String sql = "INSERT INTO agile.tasks (description, estimation, progress_id, sprint_id) VALUES (?, ? ,? ,?)";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, arg.getDescription());
            statement.setInt(2, arg.getEstimation());
            statement.setInt(3, arg.getProgressId());
            statement.setInt(4, arg.getSprintId());
            statement.executeUpdate();

            ResultSet resultKey = statement.getGeneratedKeys();

            if (resultKey.next())
                arg.setId(resultKey.getInt("id"));

            statement.close();
        });
    }

    @Override
    public void update(TaskEntity arg) throws SQLException {
        String sql = "UPDATE agile.tasks SET description = ?, estimation = ?, progress_id = ?, sprint_id = ? WHERE id = ?";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, arg.getDescription());
            statement.setInt(2, arg.getEstimation());
            statement.setInt(3, arg.getProgressId());
            statement.setInt(4, arg.getSprintId());
            statement.setInt(5, arg.getId());
            statement.executeUpdate();

            statement.close();
        });
    }

    @Override
    public void delete(TaskEntity arg) throws SQLException {
        String sql = "DELETE FROM agile.tasks WHERE id = ?";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, arg.getId());
            statement.executeUpdate();

            statement.close();
        });
    }

    @Override
    public TaskEntity get(Integer arg) throws SQLException {
        String sql = "SELECT * FROM agile.tasks WHERE id = ?";
        return dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, arg);

            ResultSet result = statement.executeQuery();

            TaskEntity taskEntity = null;

            if (result.next()) {
                taskEntity = new TaskEntity();
                taskEntity.setId(result.getInt("id"));
                taskEntity.setDescription(result.getString("description"));
                taskEntity.setEstimation(result.getInt("estimation"));
                taskEntity.setSprintId(result.getInt("sprint_id"));
                taskEntity.setProgressId(result.getInt("progress_id"));
            }

            statement.close();

            return taskEntity;
        });
    }

    @Override
    public List<TaskEntity> getAll() throws SQLException {
        String sql = "SELECT * FROM agile.tasks";
        return dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            List<TaskEntity> entities = new ArrayList<>();

            while (result.next()) {
                TaskEntity taskEntity = new TaskEntity();
                taskEntity.setId(result.getInt("id"));
                taskEntity.setDescription(result.getString("description"));
                taskEntity.setEstimation(result.getInt("estimation"));
                taskEntity.setSprintId(result.getInt("sprint_id"));
                taskEntity.setProgressId(result.getInt("progress_id"));

                entities.add(taskEntity);
            }

            statement.close();

            return entities;
        });
    }
}
