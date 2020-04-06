package persistence.repositiories;

import persistence.AbstractRepository;
import persistence.CrudRepository;
import persistence.entities.ProjectEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository extends AbstractRepository implements CrudRepository<ProjectEntity> {
    @Override
    public void save(ProjectEntity arg) {
        String sql = "INSERT INTO agile.projects (name, description) VALUES (?, ?)";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, arg.getName());
            statement.setString(2, arg.getDescription());
            statement.executeUpdate();

            ResultSet resultKey = statement.getGeneratedKeys();

            if(resultKey.next())
                arg.setId(resultKey.getInt("id"));

            statement.close();
        });
    }

    @Override
    public void update(ProjectEntity arg) {
        String sql = "UPDATE agile.projects SET name = ?, description = ? WHERE id = ?";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, arg.getName());
            statement.setString(2, arg.getDescription());
            statement.setInt(3, arg.getId());
            statement.executeUpdate();

            statement.close();
        });
    }

    @Override
    public void delete(ProjectEntity arg) {
        String sql = "DELETE FROM agile.projects WHERE id = ?";
        dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, arg.getId());
            statement.executeUpdate();

            statement.close();
        });
    }

    @Override
    public ProjectEntity get(Integer arg) {
        String sql = "SELECT * FROM agile.projects WHERE id = ?";
        return dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, arg);
            ResultSet result = statement.executeQuery();

            ProjectEntity projectEntity = null;

            if(result.next()){
                projectEntity = new ProjectEntity();
                projectEntity.setId(result.getInt("id"));
                projectEntity.setName(result.getString("name"));
                projectEntity.setDescription(result.getString("description"));
            }

            statement.close();
            return projectEntity;
        });
    }

    @Override
    public List<ProjectEntity> getAll() {
        String sql = "SELECT * FROM agile.projects";
        return dataAccessLayer.executeQuery(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            List<ProjectEntity> projectEntities = new ArrayList<>();

            while (result.next()){
                ProjectEntity projectEntity = new ProjectEntity();
                projectEntity.setId(result.getInt("id"));
                projectEntity.setName(result.getString("name"));
                projectEntity.setDescription(result.getString("description"));

                projectEntities.add(projectEntity);
            }

            statement.close();
            return projectEntities;
        });
    }
}
