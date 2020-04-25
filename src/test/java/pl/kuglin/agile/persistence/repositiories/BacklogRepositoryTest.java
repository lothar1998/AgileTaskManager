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
import pl.kuglin.agile.persistence.entities.BacklogEntity;
import pl.kuglin.agile.persistence.entities.SprintEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BacklogRepositoryTest {

    @Mock
    private static DataAccessLayer dataAccessLayer;
    @InjectMocks
    private static final CrudRepository<BacklogEntity, Integer> backlogRepository = new BacklogRepository(dataAccessLayer);

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

        BacklogEntity expectedEntity = new BacklogEntity();
        expectedEntity.setId(1);
        expectedEntity.setDescription("backlog 1 description");
        expectedEntity.setProjectId(1);

        BacklogEntity backlogEntity = backlogRepository.get(1);

        assertEquals(expectedEntity, backlogEntity);
    }

    @Test
    void getAllTest() throws SQLException {
        when(dataAccessLayer.executeQuery(ArgumentMatchers.<FunctionSQL<Connection, SprintEntity>>any())).thenAnswer(invocation -> {
            FunctionSQL<Connection, SprintEntity> arg = invocation.getArgument(0);
            return arg.apply(conn);
        });

        List<BacklogEntity> expectedList = new ArrayList<>();

        BacklogEntity expectedEntity = new BacklogEntity();
        expectedEntity.setId(1);
        expectedEntity.setDescription("backlog 1 description");
        expectedEntity.setProjectId(1);

        BacklogEntity expectedEntity2 = new BacklogEntity();
        expectedEntity2.setId(2);
        expectedEntity2.setDescription("backlog 2 description");
        expectedEntity2.setProjectId(1);

        expectedList.add(expectedEntity);
        expectedList.add(expectedEntity2);

        List<BacklogEntity> resultList = backlogRepository.getAll();

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

        BacklogEntity toSaveEntity = new BacklogEntity();
        toSaveEntity.setDescription("desc");
        toSaveEntity.setProjectId(1);

        backlogRepository.save(toSaveEntity);

        String sql = "SELECT * FROM agile.backlogs WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, toSaveEntity.getId());
        ResultSet result = statement.executeQuery();

        BacklogEntity backlogEntity = null;

        if(result.next()){
            backlogEntity = new BacklogEntity();
            backlogEntity.setId(result.getInt("id"));
            backlogEntity.setDescription(result.getString("description"));
            backlogEntity.setProjectId(result.getInt("project_id"));
        }

        statement.close();

        assertEquals(toSaveEntity, backlogEntity);
    }

    @Test
    void updateTest() throws SQLException {
        doAnswer(invocation -> {
            ConsumerSQL<Connection> arg = invocation.getArgument(0);
            arg.accept(conn);
            return null;
        }).when(dataAccessLayer).executeQuery(ArgumentMatchers.<ConsumerSQL<Connection>>any());

        String sql = "SELECT * FROM agile.backlogs WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, 1);
        ResultSet result = statement.executeQuery();

        BacklogEntity backlogEntity = new BacklogEntity();

        if(result.next()){
            backlogEntity.setId(result.getInt("id"));
            backlogEntity.setDescription(result.getString("description"));
            backlogEntity.setProjectId(result.getInt("project_id"));
        }

        statement.close();

        backlogEntity.setDescription("new description");

        backlogRepository.update(backlogEntity);

        PreparedStatement resultStatement = conn.prepareStatement(sql);
        resultStatement.setInt(1, 1);

        ResultSet resultOfUpdate = resultStatement.executeQuery();

        BacklogEntity resultOfUpdateEntity = new BacklogEntity();

        if(resultOfUpdate.next()){
            resultOfUpdateEntity.setId(resultOfUpdate.getInt("id"));
            resultOfUpdateEntity.setDescription(resultOfUpdate.getString("description"));
            resultOfUpdateEntity.setProjectId(resultOfUpdate.getInt("project_id"));
        }

        statement.close();

        assertEquals(backlogEntity, resultOfUpdateEntity);
    }

    @Test
    void deleteTest() throws SQLException {
        doAnswer(invocation -> {
            ConsumerSQL<Connection> arg = invocation.getArgument(0);
            arg.accept(conn);
            return null;
        }).when(dataAccessLayer).executeQuery(ArgumentMatchers.<ConsumerSQL<Connection>>any());

        String sql = "SELECT * FROM agile.backlogs WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, 2);
        ResultSet result = statement.executeQuery();

        BacklogEntity backlogEntity = new BacklogEntity();

        if(result.next()){
            backlogEntity.setId(result.getInt("id"));
            backlogEntity.setDescription(result.getString("description"));
            backlogEntity.setProjectId(result.getInt("project_id"));
        }

        statement.close();

        backlogRepository.delete(backlogEntity);

        PreparedStatement resultStatement = conn.prepareStatement(sql);

        resultStatement.setInt(1, 2);
        ResultSet resultOfUpdate = resultStatement.executeQuery();

        BacklogEntity resultOfUpdateEntity = null;

        if(resultOfUpdate.next()){
            resultOfUpdateEntity = new BacklogEntity();
        }

        resultStatement.close();

        assertNull(resultOfUpdateEntity);
    }

}