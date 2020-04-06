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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjectRepositoryTest {

    @Mock
    private static DataAccessLayer dataAccessLayer;
    @InjectMocks
    private static CrudRepository<ProjectEntity> projectRepository = new ProjectRepository();

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
                "values (1, 'project 1', 'project description 1')\\;";
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

        ProjectEntity expectedEntity = new ProjectEntity();
        expectedEntity.setId(1);
        expectedEntity.setName("project 1");
        expectedEntity.setDescription("project description 1");

        ProjectEntity projectEntity = projectRepository.get(1);

        assertEquals(expectedEntity, projectEntity);
    }

    @Test
    void getAllTest() throws SQLException {
        when(dataAccessLayer.executeQuery(ArgumentMatchers.<FunctionSQL<Connection, ProjectEntity>>any())).thenAnswer(invocation -> {
            FunctionSQL<Connection, ProjectEntity> arg = invocation.getArgument(0);
            return arg.apply(conn);
        });

        List<ProjectEntity> expectedList = new ArrayList<>();

        ProjectEntity expectedEntity = new ProjectEntity();
        expectedEntity.setId(1);
        expectedEntity.setName("project 1");
        expectedEntity.setDescription("project description 1");

        expectedList.add(expectedEntity);

        List<ProjectEntity> projectEntityList = projectRepository.getAll();

        assertTrue(expectedList.containsAll(projectEntityList));
        assertTrue(projectEntityList.containsAll(expectedList));
    }

    @Test
    void saveTest() throws SQLException {
        doAnswer(invocation -> {
            ConsumerSQL<Connection> arg = invocation.getArgument(0);
            arg.accept(conn);
            return null;
        }).when(dataAccessLayer).executeQuery(ArgumentMatchers.<ConsumerSQL<Connection>>any());

        ProjectEntity toSaveEntity = new ProjectEntity();
        toSaveEntity.setName("name of project");
        toSaveEntity.setDescription("description of project");

        projectRepository.save(toSaveEntity);

        String sql = "SELECT * FROM agile.projects WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, toSaveEntity.getId());
        ResultSet result = statement.executeQuery();

        ProjectEntity projectEntity = null;

        if(result.next()){
            projectEntity = new ProjectEntity();
            projectEntity.setId(result.getInt("id"));
            projectEntity.setName(result.getString("name"));
            projectEntity.setDescription(result.getString("description"));
        }

        statement.close();

        assertEquals(toSaveEntity, projectEntity);
    }

    @Test
    void updateTest() throws SQLException {
        doAnswer(invocation -> {
            ConsumerSQL<Connection> arg = invocation.getArgument(0);
            arg.accept(conn);
            return null;
        }).when(dataAccessLayer).executeQuery(ArgumentMatchers.<ConsumerSQL<Connection>>any());

        String sql = "SELECT * FROM agile.projects WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, 1);
        ResultSet result = statement.executeQuery();

        ProjectEntity projectEntity = new ProjectEntity();

        if(result.next()){
            projectEntity.setId(result.getInt("id"));
            projectEntity.setName(result.getString("name"));
            projectEntity.setDescription(result.getString("description"));
        }

        statement.close();

        projectEntity.setName("new name");
        projectEntity.setDescription("new description");

        projectRepository.update(projectEntity);


        PreparedStatement resultStatement = conn.prepareStatement(sql);

        resultStatement.setInt(1, 1);
        ResultSet resultOfUpdate = resultStatement.executeQuery();

        ProjectEntity resultOfUpdateEntity = new ProjectEntity();

        if(resultOfUpdate.next()){
            resultOfUpdateEntity.setId(resultOfUpdate.getInt("id"));
            resultOfUpdateEntity.setName(resultOfUpdate.getString("name"));
            resultOfUpdateEntity.setDescription(resultOfUpdate.getString("description"));
        }

        statement.close();

        assertEquals(projectEntity, resultOfUpdateEntity);
    }

    @Test
    void deleteTest() throws SQLException {
        doAnswer(invocation -> {
            ConsumerSQL<Connection> arg = invocation.getArgument(0);
            arg.accept(conn);
            return null;
        }).when(dataAccessLayer).executeQuery(ArgumentMatchers.<ConsumerSQL<Connection>>any());

        String sql = "SELECT * FROM agile.projects WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, 1);
        ResultSet result = statement.executeQuery();

        ProjectEntity projectEntity = new ProjectEntity();

        if(result.next()){
            projectEntity.setId(result.getInt("id"));
            projectEntity.setName(result.getString("name"));
            projectEntity.setDescription(result.getString("description"));
        }

        statement.close();

        projectRepository.delete(projectEntity);


        PreparedStatement resultStatement = conn.prepareStatement(sql);

        resultStatement.setInt(1, 1);
        ResultSet resultOfUpdate = resultStatement.executeQuery();

        ProjectEntity resultOfUpdateEntity = null;

        if(resultOfUpdate.next()){
            resultOfUpdateEntity = new ProjectEntity();
            resultOfUpdateEntity.setId(resultOfUpdate.getInt("id"));
            resultOfUpdateEntity.setName(resultOfUpdate.getString("name"));
            resultOfUpdateEntity.setDescription(resultOfUpdate.getString("description"));
        }

        statement.close();

        assertNull(resultOfUpdateEntity);
    }
}