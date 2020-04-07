package persistence.repositiories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import persistence.*;
import persistence.entities.SprintEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SprintRepositoryTest {

    @Mock
    private static DataAccessLayer dataAccessLayer;
    @InjectMocks
    private static CrudRepository<SprintEntity> sprintRepository = new SprintRepository();

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
                "values (1, 'project 1', 'project description 1')\\;" +
                "create table agile.sprints\n" +
                "(\n" +
                "    id         serial primary key,\n" +
                "    name       varchar(255),\n" +
                "    no         integer                                not null,\n" +
                "    project_id integer references agile.projects (id)\n" +
                ")\\;\n" +
                "\n" +
                "insert into agile.sprints\n" +
                "values (1, 'sprint 1', 1, 1)\\;" +
                "insert into agile.sprints\n" +
                "values (2, 'sprint 2', 2, 1)\\;";

        conn = DriverManager.getConnection("jdbc:h2:mem:test;INIT=CREATE SCHEMA IF NOT EXISTS agile\\;SET SCHEMA agile\\;" + sql, "admin", "admin");
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

        SprintEntity expectedEntity = new SprintEntity();
        expectedEntity.setId(1);
        expectedEntity.setName("sprint 1");
        expectedEntity.setNo(1);
        expectedEntity.setProjectId(1);

        SprintEntity sprintEntity = sprintRepository.get(1);

        assertEquals(expectedEntity, sprintEntity);
    }

    @Test
    void getAllTest() throws SQLException {
        when(dataAccessLayer.executeQuery(ArgumentMatchers.<FunctionSQL<Connection, SprintEntity>>any())).thenAnswer(invocation -> {
            FunctionSQL<Connection, SprintEntity> arg = invocation.getArgument(0);
            return arg.apply(conn);
        });

        List<SprintEntity> expectedList = new ArrayList<>();

        SprintEntity entity1 = new SprintEntity();
        entity1.setId(1);
        entity1.setName("sprint 1");
        entity1.setNo(1);
        entity1.setProjectId(1);

        SprintEntity entity2 = new SprintEntity();
        entity2.setId(2);
        entity2.setName("sprint 2");
        entity2.setNo(2);
        entity2.setProjectId(1);

        expectedList.add(entity1);
        expectedList.add(entity2);

        List<SprintEntity> resultList = sprintRepository.getAll();

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

        SprintEntity toSaveEntity = new SprintEntity();
        toSaveEntity.setName("name");
        toSaveEntity.setNo(154);
        toSaveEntity.setProjectId(1);

        sprintRepository.save(toSaveEntity);

        String sql = "SELECT * FROM agile.sprints WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, toSaveEntity.getId());
        ResultSet result = statement.executeQuery();

        SprintEntity sprintEntity = null;

        if(result.next()){
            sprintEntity = new SprintEntity();
            sprintEntity.setId(result.getInt("id"));
            sprintEntity.setName(result.getString("name"));
            sprintEntity.setNo(result.getInt("no"));
            sprintEntity.setProjectId(result.getInt("project_id"));
        }

        statement.close();

        assertEquals(toSaveEntity, sprintEntity);
    }

    @Test
    void updateTest() throws SQLException {
        doAnswer(invocation -> {
            ConsumerSQL<Connection> arg = invocation.getArgument(0);
            arg.accept(conn);
            return null;
        }).when(dataAccessLayer).executeQuery(ArgumentMatchers.<ConsumerSQL<Connection>>any());

        String sql = "SELECT * FROM agile.sprints WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1,1);
        ResultSet result = statement.executeQuery();

        SprintEntity sprintEntity = new SprintEntity();

        if(result.next()){
            sprintEntity.setId(result.getInt("id"));
            sprintEntity.setName(result.getString("name"));
            sprintEntity.setNo(result.getInt("no"));
            sprintEntity.setProjectId(result.getInt("project_id"));
        }

        statement.close();

        sprintEntity.setName("new name");
        sprintEntity.setNo(32);

        sprintRepository.update(sprintEntity);

        PreparedStatement resultStatement = conn.prepareStatement(sql);

        resultStatement.setInt(1,1);
        ResultSet resultOfUpdate = resultStatement.executeQuery();

        SprintEntity resultOfUpdateEntity = new SprintEntity();

        if(resultOfUpdate.next()){
            resultOfUpdateEntity.setId(resultOfUpdate.getInt("id"));
            resultOfUpdateEntity.setName(resultOfUpdate.getString("name"));
            resultOfUpdateEntity.setNo(resultOfUpdate.getInt("no"));
            resultOfUpdateEntity.setProjectId(resultOfUpdate.getInt("project_id"));
        }

        resultStatement.close();

        assertEquals(sprintEntity, resultOfUpdateEntity);
    }

    @Test
    void deleteTest() throws SQLException {
        doAnswer(invocation -> {
            ConsumerSQL<Connection> arg = invocation.getArgument(0);
            arg.accept(conn);
            return null;
        }).when(dataAccessLayer).executeQuery(ArgumentMatchers.<ConsumerSQL<Connection>>any());

        String sql = "SELECT * FROM agile.sprints WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1,1);
        ResultSet result = statement.executeQuery();

        SprintEntity sprintEntity = new SprintEntity();

        if(result.next()){
            sprintEntity.setId(result.getInt("id"));
            sprintEntity.setName(result.getString("name"));
            sprintEntity.setNo(result.getInt("no"));
            sprintEntity.setProjectId(result.getInt("project_id"));
        }

        statement.close();

        sprintRepository.delete(sprintEntity);


        PreparedStatement resultStatement = conn.prepareStatement(sql);

        resultStatement.setInt(1, 1);
        ResultSet resultOfUpdate = resultStatement.executeQuery();

        SprintEntity resultOfUpdateEntity = null;

        if(resultOfUpdate.next()){
            resultOfUpdateEntity = new SprintEntity();
        }

        resultStatement.close();

        assertNull(resultOfUpdateEntity);
    }
}