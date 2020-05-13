package pl.kuglin.agile;

import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.DataAccessLayer;
import pl.kuglin.agile.persistence.ReadRepository;
import pl.kuglin.agile.persistence.cache.LettuceRedisClient;
import pl.kuglin.agile.persistence.cache.RedisCacheProxy;
import pl.kuglin.agile.persistence.entities.*;
import pl.kuglin.agile.persistence.repositiories.*;
import pl.kuglin.agile.reactive.ActionRunnerFactory;
import pl.kuglin.agile.reactive.CallableRunnerFactory;
import pl.kuglin.agile.reactive.CompletableRunnerFactory;
import pl.kuglin.agile.reactive.SingleRunnerFactory;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.window.MainWindow;
import pl.kuglin.agile.utils.FilePropertyLoader;
import pl.kuglin.agile.utils.PropertyLoader;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Properties;

public class AgileTaskManager {
    public static void main(String[] args) throws FileNotFoundException, SQLException, InterruptedException {



        PropertyLoader redisPropertiesLoader = new FilePropertyLoader( "src/main/resources/redis.properties");
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

        AbstractWindow window = new MainWindow(repositoryPack, callableRunner, actionRunner);
//
//        ProjectEntity pr = new ProjectEntity();
//        pr.setName("TEST");
//        pr.setDescription("TEST");

//        callableRunner.createAndRun(() -> projectRepository.get(1), projectEntity -> System.out.println(projectEntity), Throwable::printStackTrace);
//        actionRunner.createAndRun(() -> projectRepository.save(pr), () -> System.out.println("Saved"), Throwable::printStackTrace);
//        callableRunner.createAndRun(() -> projectRepository.get(pr.getId()), projectEntity -> System.out.println(projectEntity), Throwable::printStackTrace);

//        TimeUnit.SECONDS.sleep(5);

//        client.closeClient();
    }
}
