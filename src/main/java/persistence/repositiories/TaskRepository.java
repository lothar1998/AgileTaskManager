package persistence.repositiories;

import persistence.AbstractRepository;
import persistence.CrudRepository;
import persistence.entities.TaskEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository extends AbstractRepository implements CrudRepository<TaskEntity> {
    @Override
    public void save(TaskEntity arg) throws SQLException {
        String sql = "INSERT INTO agile.tasks (story_id, description, estimation, progress_id, sprint_id) VALUES (?, ?, ? ,? ,?)";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, arg.getStoryId());
            statement.setString(2, arg.getDescription());
            statement.setInt(3, arg.getEstimation());
            statement.setInt(4, arg.getProgressId());
            statement.setInt(5, arg.getSprintId());
            statement.executeUpdate();

            ResultSet resultKey = statement.getGeneratedKeys();

            if (resultKey.next())
                arg.setId(resultKey.getInt("id"));

            statement.close();
        });
    }

    @Override
    public void update(TaskEntity arg) throws SQLException {
        String sql = "UPDATE agile.tasks SET story_id = ?, description = ?, estimation = ?, progress_id = ?, sprint_id = ? WHERE id = ?";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, arg.getStoryId());
            statement.setString(2, arg.getDescription());
            statement.setInt(3, arg.getEstimation());
            statement.setInt(4, arg.getProgressId());
            statement.setInt(5, arg.getSprintId());
            statement.setInt(6, arg.getId());
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
                taskEntity.setStoryId(result.getInt("story_id"));
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
                taskEntity.setStoryId(result.getInt("story_id"));
                taskEntity.setSprintId(result.getInt("sprint_id"));
                taskEntity.setProgressId(result.getInt("progress_id"));

                entities.add(taskEntity);
            }

            statement.close();

            return entities;
        });
    }
}
