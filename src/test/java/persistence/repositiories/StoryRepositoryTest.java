package persistence.repositiories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import persistence.ConsumerSQL;
import persistence.CrudRepository;
import persistence.DataAccessLayer;
import persistence.FunctionSQL;
import persistence.entities.ProjectEntity;
import persistence.entities.StoryEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoryRepositoryTest {

    @Mock
    private static DataAccessLayer dataAccessLayer;
    @InjectMocks
    private static CrudRepository<StoryEntity> storyRepository = new StoryRepository();

    private static Connection conn;

    @BeforeEach
    void connecting() throws SQLException {
        String sql = "create table agile.projects\n" +
                "(\n" +
                "    id          serial primary key,\n" +
                "    name        varchar(255),\n" +
                "    description text\n" +
                ")\\;\n" +
                "\n" +
                "insert into agile.projects\n" +
                "values (1, 'project 1', 'project description 1')\\;\n" +
                "\n" +
                "create table agile.backlogs\n" +
                "(\n" +
                "    id          serial primary key,\n" +
                "    description text,\n" +
                "    project_id  integer references agile.projects (id)\n" +
                ")\\;\n" +
                "\n" +
                "insert into agile.backlogs\n" +
                "values (1, 'backlog 1 description', 1)\\;" +
                "create table agile.stories\n" +
                "(\n" +
                "    id          serial primary key,\n" +
                "    description text,\n" +
                "    backlog_id  integer references agile.backlogs (id)\n" +
                ")\\;\n" +
                "\n" +
                "insert into agile.stories\n" +
                "values (1, 'story 1 description', 1)\\;" +
                "insert into agile.stories\n" +
                "values (2, 'story 2 description', 1)\\;";
        conn = DriverManager.getConnection("jdbc:h2:mem:test;INIT=CREATE SCHEMA IF NOT EXISTS agile\\;SET SCHEMA agile\\;" + sql, "admin", "admin");
    }

    @AfterEach
    void disconnecting() throws SQLException {
        conn.close();
    }

    @Test
    void getTest() throws SQLException {
        when(dataAccessLayer.executeQuery(ArgumentMatchers.<FunctionSQL<Connection, ProjectEntity>>any())).thenAnswer(invocation -> {
            FunctionSQL<Connection, ProjectEntity> arg = invocation.getArgument(0);
            return arg.apply(conn);
        });

        StoryEntity expectedEntity = new StoryEntity();
        expectedEntity.setId(1);
        expectedEntity.setDescription("story 1 description");
        expectedEntity.setBacklogId(1);

        StoryEntity storyEntity = storyRepository.get(1);

        assertEquals(expectedEntity, storyEntity);
    }

    @Test
    void getAllTest() throws SQLException {
        when(dataAccessLayer.executeQuery(ArgumentMatchers.<FunctionSQL<Connection, ProjectEntity>>any())).thenAnswer(invocation -> {
            FunctionSQL<Connection, ProjectEntity> arg = invocation.getArgument(0);
            return arg.apply(conn);
        });

        List<StoryEntity> expectedList = new ArrayList<>();

        StoryEntity expectedEntity = new StoryEntity();
        expectedEntity.setId(1);
        expectedEntity.setDescription("story 1 description");
        expectedEntity.setBacklogId(1);

        StoryEntity expectedEntity2 = new StoryEntity();
        expectedEntity2.setId(2);
        expectedEntity2.setDescription("story 2 description");
        expectedEntity2.setBacklogId(1);

        expectedList.add(expectedEntity);
        expectedList.add(expectedEntity2);

        List<StoryEntity> storyEntityList = storyRepository.getAll();

        assertTrue(expectedList.containsAll(storyEntityList));
        assertTrue(storyEntityList.containsAll(expectedList));
    }

    @Test
    void saveTest() throws SQLException {
        doAnswer(invocation -> {
            ConsumerSQL<Connection> arg = invocation.getArgument(0);
            arg.accept(conn);
            return null;
        }).when(dataAccessLayer).executeQuery(ArgumentMatchers.<ConsumerSQL<Connection>>any());

        StoryEntity toSaveEntity = new StoryEntity();
        toSaveEntity.setDescription("description of story");
        toSaveEntity.setBacklogId(1);

        storyRepository.save(toSaveEntity);

        String sql = "SELECT * FROM agile.stories WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, toSaveEntity.getId());
        ResultSet result = statement.executeQuery();

        StoryEntity storyEntity = null;

        if (result.next()) {
            storyEntity = new StoryEntity();
            storyEntity.setId(result.getInt("id"));
            storyEntity.setDescription(result.getString("description"));
            storyEntity.setBacklogId(result.getInt("backlog_id"));
        }

        statement.close();

        assertEquals(toSaveEntity, storyEntity);
    }

    @Test
    void updateTest() throws SQLException {
        doAnswer(invocation -> {
            ConsumerSQL<Connection> arg = invocation.getArgument(0);
            arg.accept(conn);
            return null;
        }).when(dataAccessLayer).executeQuery(ArgumentMatchers.<ConsumerSQL<Connection>>any());

        String sql = "SELECT * FROM agile.stories WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, 1);
        ResultSet result = statement.executeQuery();

        StoryEntity storyEntity = new StoryEntity();

        if (result.next()) {
            storyEntity.setId(result.getInt("id"));
            storyEntity.setDescription(result.getString("description"));
            storyEntity.setBacklogId(result.getInt("backlog_id"));
        }

        statement.close();

        storyEntity.setDescription("new description");

        storyRepository.update(storyEntity);


        PreparedStatement resultStatement = conn.prepareStatement(sql);

        resultStatement.setInt(1, 1);
        ResultSet resultOfUpdate = resultStatement.executeQuery();

        StoryEntity resultOfUpdateEntity = new StoryEntity();

        if (resultOfUpdate.next()) {
            resultOfUpdateEntity.setId(resultOfUpdate.getInt("id"));
            resultOfUpdateEntity.setDescription(resultOfUpdate.getString("description"));
            resultOfUpdateEntity.setBacklogId(resultOfUpdate.getInt("backlog_id"));
        }

        resultStatement.close();

        assertEquals(storyEntity, resultOfUpdateEntity);
    }

    @Test
    void deleteTest() throws SQLException {
        doAnswer(invocation -> {
            ConsumerSQL<Connection> arg = invocation.getArgument(0);
            arg.accept(conn);
            return null;
        }).when(dataAccessLayer).executeQuery(ArgumentMatchers.<ConsumerSQL<Connection>>any());

        String sql = "SELECT * FROM agile.stories WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, 1);
        ResultSet result = statement.executeQuery();

        StoryEntity storyEntity = new StoryEntity();

        if (result.next()) {
            storyEntity.setId(result.getInt("id"));
            storyEntity.setDescription(result.getString("description"));
            storyEntity.setBacklogId(result.getInt("backlog_id"));
        }

        statement.close();

        storyRepository.delete(storyEntity);


        PreparedStatement resultStatement = conn.prepareStatement(sql);

        resultStatement.setInt(1, 1);
        ResultSet resultOfUpdate = resultStatement.executeQuery();

        StoryEntity resultOfUpdateEntity = null;

        if (resultOfUpdate.next()) {
            resultOfUpdateEntity = new StoryEntity();
        }

        resultStatement.close();

        assertNull(resultOfUpdateEntity);
    }
}