package pl.kuglin.agile;

import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.DataAccessLayer;
import pl.kuglin.agile.persistence.ReadRepository;
import pl.kuglin.agile.persistence.RepositoryPack;
import pl.kuglin.agile.persistence.cache.LettuceRedisClient;
import pl.kuglin.agile.persistence.cache.RedisCacheProxy;
import pl.kuglin.agile.persistence.entities.ProgressEntity;
import pl.kuglin.agile.persistence.entities.ProjectEntity;
import pl.kuglin.agile.persistence.entities.SprintEntity;
import pl.kuglin.agile.persistence.entities.TaskEntity;
import pl.kuglin.agile.persistence.repositiories.ProgressRepository;
import pl.kuglin.agile.persistence.repositiories.ProjectRepository;
import pl.kuglin.agile.persistence.repositiories.SprintRepository;
import pl.kuglin.agile.persistence.repositiories.TaskRepository;
import pl.kuglin.agile.reactive.ActionRunnerFactory;
import pl.kuglin.agile.reactive.CallableRunnerFactory;
import pl.kuglin.agile.reactive.CompletableRunnerFactory;
import pl.kuglin.agile.reactive.SingleRunnerFactory;
import pl.kuglin.agile.ui.window.MainWindow;
import pl.kuglin.agile.utils.FilePropertyLoader;
import pl.kuglin.agile.utils.PropertyLoader;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.Properties;

public class AgileTaskManager {
    public static void main(String[] args) throws FileNotFoundException {

        PropertyLoader redisPropertiesLoader = new FilePropertyLoader("src/main/resources/redis.properties");
        redisPropertiesLoader.loadProperties();
        Properties redisProperties = redisPropertiesLoader.getProperties();

        LettuceRedisClient client = new LettuceRedisClient(redisProperties);

        PropertyLoader postgresPropertyLoader = new FilePropertyLoader("src/main/resources/jdbc.properties");
        postgresPropertyLoader.loadProperties();
        Properties postgresProperties = postgresPropertyLoader.getProperties();

        DataAccessLayer dataAccessLayer = new DataAccessLayer(postgresProperties);

        CrudRepository<ProjectEntity, Integer> projectRepository = new RedisCacheProxy<>(client, new ProjectRepository(dataAccessLayer), ProjectEntity.class);
        CrudRepository<SprintEntity, Integer> sprintRepository = new RedisCacheProxy<>(client, new SprintRepository(dataAccessLayer), SprintEntity.class);
        CrudRepository<TaskEntity, Integer> taskRepository = new RedisCacheProxy<>(client, new TaskRepository(dataAccessLayer), TaskEntity.class);
        ReadRepository<ProgressEntity, Integer> progressRepository = new ProgressRepository(dataAccessLayer);

        RepositoryPack repositoryPack = new RepositoryPack();
        repositoryPack.setProjectRepository(projectRepository);
        repositoryPack.setSprintRepository(sprintRepository);
        repositoryPack.setTaskRepository(taskRepository);
        repositoryPack.setProgressRepository(progressRepository);

        CallableRunnerFactory callableRunner = new SingleRunnerFactory();
        ActionRunnerFactory actionRunner = new CompletableRunnerFactory();

        SwingUtilities.invokeLater(() -> new MainWindow(repositoryPack, callableRunner, actionRunner));

        Runtime.getRuntime().addShutdownHook(new Thread(client::closeClient));
    }
}
