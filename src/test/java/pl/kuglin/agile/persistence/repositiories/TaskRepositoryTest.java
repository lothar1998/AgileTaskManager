package pl.kuglin.agile.persistence.repositiories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kuglin.agile.persistence.ConsumerSQL;
import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.DataAccessLayer;
import pl.kuglin.agile.persistence.FunctionSQL;
import pl.kuglin.agile.persistence.entities.SprintEntity;
import pl.kuglin.agile.persistence.entities.TaskEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskRepositoryTest {

    @Mock
    private static DataAccessLayer dataAccessLayer;
    @InjectMocks
    private static final CrudRepository<TaskEntity> taskRepository = new TaskRepository(dataAccessLayer);

    private static Connection conn;

    @BeforeEach
    void connecting() throws SQLException {
        conn = DriverManager.getConnection("jdbc:h2:mem:test;INIT=runscript from './database_test.sql'\\;", "admin", "admin");
    }

    @AfterEach
    void disconnecting() throws SQLException {
        conn.close();
    }

    @Test
    void getTest() throws SQLException {
        when(dataAccessLayer.executeQuery(ArgumentMatchers.<FunctionSQL<Connection, SprintEntity>>any())).thenAnswer(invocation -> {
            FunctionSQL<Connection, SprintEntity> arg = invocation.getArgument(0);
            return arg.apply(conn);
        });

        TaskEntity expectedEntity = new TaskEntity();
        expectedEntity.setId(1);
        expectedEntity.setStoryId(1);
        expectedEntity.setDescription("task 1 description");
        expectedEntity.setEstimation(5);
        expectedEntity.setProgressId(1);
        expectedEntity.setSprintId(1);

        TaskEntity sprintEntity = taskRepository.get(1);

        assertEquals(expectedEntity, sprintEntity);
    }

    @Test
    void getAllTest() throws SQLException {
        when(dataAccessLayer.executeQuery(ArgumentMatchers.<FunctionSQL<Connection, SprintEntity>>any())).thenAnswer(invocation -> {
            FunctionSQL<Connection, SprintEntity> arg = invocation.getArgument(0);
            return arg.apply(conn);
        });

        TaskEntity expectedEntity1 = new TaskEntity();
        expectedEntity1.setId(1);
        expectedEntity1.setStoryId(1);
        expectedEntity1.setDescription("task 1 description");
        expectedEntity1.setEstimation(5);
        expectedEntity1.setProgressId(1);
        expectedEntity1.setSprintId(1);


        TaskEntity expectedEntity2 = new TaskEntity();
        expectedEntity2.setId(2);
        expectedEntity2.setStoryId(1);
        expectedEntity2.setDescription("task 2 description");
        expectedEntity2.setEstimation(7);
        expectedEntity2.setProgressId(1);
        expectedEntity2.setSprintId(1);


        List<TaskEntity> expectedList = new ArrayList<>(Arrays.asList(expectedEntity1, expectedEntity2));

        List<TaskEntity> resultList = taskRepository.getAll();

        assertTrue(expectedList.containsAll(resultList));
        assertTrue(resultList.containsAll(expectedList));
    }

    @Test
    void saveTest() throws SQLException {
        doAnswer(invocation -> {
            ConsumerSQL<Connection> arg = invocation.getArgument(0);
            arg.accept(conn);
            return null;
        }).when(dataAccessLayer).executeQuery(ArgumentMatchers.<ConsumerSQL<Connection>>any());

        TaskEntity toSaveEntity = new TaskEntity();
        toSaveEntity.setStoryId(1);
        toSaveEntity.setDescription("new task");
        toSaveEntity.setEstimation(22);
        toSaveEntity.setProgressId(1);
        toSaveEntity.setSprintId(1);

        taskRepository.save(toSaveEntity);

        String sql = "SELECT * FROM agile.tasks WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, toSaveEntity.getId());
        ResultSet result = statement.executeQuery();

        TaskEntity taskEntity = null;

        if (result.next()) {
            taskEntity = new TaskEntity();
            taskEntity.setId(result.getInt("id"));
            taskEntity.setStoryId(result.getInt("story_id"));
            taskEntity.setDescription(result.getString("description"));
            taskEntity.setEstimation(result.getInt("estimation"));
            taskEntity.setProgressId(result.getInt("progress_id"));
            taskEntity.setSprintId(result.getInt("sprint_id"));
        }

        statement.close();

        assertEquals(toSaveEntity, taskEntity);
    }

    @Test
    void updateTest() throws SQLException {
        doAnswer(invocation -> {
            ConsumerSQL<Connection> arg = invocation.getArgument(0);
            arg.accept(conn);
            return null;
        }).when(dataAccessLayer).executeQuery(ArgumentMatchers.<ConsumerSQL<Connection>>any());

        String sql = "SELECT * FROM agile.tasks WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, 1);
        ResultSet result = statement.executeQuery();

        TaskEntity taskEntity = new TaskEntity();

        if (result.next()) {
            taskEntity = new TaskEntity();
            taskEntity.setId(result.getInt("id"));
            taskEntity.setStoryId(result.getInt("story_id"));
            taskEntity.setDescription(result.getString("description"));
            taskEntity.setEstimation(result.getInt("estimation"));
            taskEntity.setProgressId(result.getInt("progress_id"));
            taskEntity.setSprintId(result.getInt("sprint_id"));
        }

        statement.close();

        taskEntity.setDescription("new name");
        taskEntity.setEstimation(31);

        taskRepository.update(taskEntity);

        PreparedStatement resultStatement = conn.prepareStatement(sql);

        resultStatement.setInt(1, 1);
        ResultSet resultOfUpdate = resultStatement.executeQuery();

        TaskEntity resultOfUpdateEntity = new TaskEntity();

        if (resultOfUpdate.next()) {
            resultOfUpdateEntity.setId(resultOfUpdate.getInt("id"));
            resultOfUpdateEntity.setStoryId(resultOfUpdate.getInt("story_id"));
            resultOfUpdateEntity.setDescription(resultOfUpdate.getString("description"));
            resultOfUpdateEntity.setEstimation(resultOfUpdate.getInt("estimation"));
            resultOfUpdateEntity.setProgressId(resultOfUpdate.getInt("progress_id"));
            resultOfUpdateEntity.setSprintId(resultOfUpdate.getInt("sprint_id"));
        }

        resultStatement.close();

        assertEquals(taskEntity, resultOfUpdateEntity);
    }

    @Test
    void deleteTest() throws SQLException {
        doAnswer(invocation -> {
            ConsumerSQL<Connection> arg = invocation.getArgument(0);
            arg.accept(conn);
            return null;
        }).when(dataAccessLayer).executeQuery(ArgumentMatchers.<ConsumerSQL<Connection>>any());

        String sql = "SELECT * FROM agile.tasks WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, 2);
        ResultSet result = statement.executeQuery();

        TaskEntity taskEntity = new TaskEntity();

        if (result.next()) {
            taskEntity = new TaskEntity();
            taskEntity.setId(result.getInt("id"));
            taskEntity.setStoryId(result.getInt("story_id"));
            taskEntity.setDescription(result.getString("description"));
            taskEntity.setEstimation(result.getInt("estimation"));
            taskEntity.setProgressId(result.getInt("progress_id"));
            taskEntity.setSprintId(result.getInt("sprint_id"));
        }

        statement.close();

        taskRepository.delete(taskEntity);


        PreparedStatement resultStatement = conn.prepareStatement(sql);

        resultStatement.setInt(1, 2);
        ResultSet resultOfUpdate = resultStatement.executeQuery();

        SprintEntity resultOfUpdateEntity = null;

        if (resultOfUpdate.next()) {
            resultOfUpdateEntity = new SprintEntity();
        }

        resultStatement.close();

        assertNull(resultOfUpdateEntity);
    }
}