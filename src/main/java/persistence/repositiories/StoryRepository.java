package persistence.repositiories;

import persistence.AbstractRepository;
import persistence.CrudRepository;
import persistence.entities.StoryEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StoryRepository extends AbstractRepository implements CrudRepository<StoryEntity> {
    @Override
    public void save(StoryEntity arg) throws SQLException {
        String sql = "INSERT INTO agile.stories (description, backlog_id) VALUES (?, ?)";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, arg.getDescription());
            statement.setInt(2, arg.getBacklogId());

            statement.executeUpdate();

            ResultSet resultKey = statement.getGeneratedKeys();

            if (resultKey.next())
                arg.setId(resultKey.getInt("id"));

            statement.close();
        });
    }

    @Override
    public void update(StoryEntity arg) throws SQLException {
        String sql = "UPDATE agile.stories SET description = ?, backlog_id = ? WHERE id = ?";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, arg.getDescription());
            statement.setInt(2, arg.getBacklogId());
            statement.setInt(3, arg.getId());

            statement.executeUpdate();

            statement.close();
        });
    }

    @Override
    public void delete(StoryEntity arg) throws SQLException {
        String sql = "DELETE FROM agile.stories WHERE id = ?";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, arg.getId());

            statement.executeUpdate();

            statement.close();
        });
    }

    @Override
    public StoryEntity get(Integer arg) throws SQLException {
        String sql = "SELECT * FROM agile.stories WHERE id = ?";
        return dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, arg);
            ResultSet result = statement.executeQuery();


            StoryEntity storyEntity = new StoryEntity();

            if (result.next()) {
                storyEntity.setId(result.getInt("id"));
                storyEntity.setDescription(result.getString("description"));
                storyEntity.setBacklogId(result.getInt("backlog_id"));
            }

            statement.close();

            return storyEntity;
        });
    }

    @Override
    public List<StoryEntity> getAll() throws SQLException {
        String sql = "SELECT * FROM agile.stories";
        return dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            List<StoryEntity> stories = new ArrayList<>();

            while (result.next()) {
                StoryEntity storyEntity = new StoryEntity();
                storyEntity.setId(result.getInt("id"));
                storyEntity.setDescription(result.getString("description"));
                storyEntity.setBacklogId(result.getInt("backlog_id"));

                stories.add(storyEntity);
            }

            statement.close();

            return stories;
        });
    }
}
