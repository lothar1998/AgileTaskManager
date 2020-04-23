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
import pl.kuglin.agile.persistence.entities.ProjectEntity;
import pl.kuglin.agile.persistence.entities.StoryEntity;

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
    private static final CrudRepository<StoryEntity> storyRepository = new StoryRepository(dataAccessLayer);

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

        statement.setInt(1, 2);
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

        resultStatement.setInt(1, 2);
        ResultSet resultOfUpdate = resultStatement.executeQuery();

        StoryEntity resultOfUpdateEntity = null;

        if (resultOfUpdate.next()) {
            resultOfUpdateEntity = new StoryEntity();
        }

        resultStatement.close();

        assertNull(resultOfUpdateEntity);
    }
}