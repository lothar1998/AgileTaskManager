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

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectRepositoryTest {

    @Mock
    private static DataAccessLayer dataAccessLayer;
    @InjectMocks
    private static final CrudRepository<ProjectEntity, Integer> projectRepository = new ProjectRepository(dataAccessLayer);

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

        ProjectEntity expectedEntity1 = new ProjectEntity();
        expectedEntity1.setId(1);
        expectedEntity1.setName("project 1");
        expectedEntity1.setDescription("project description 1");

        ProjectEntity expectedEntity2 = new ProjectEntity();
        expectedEntity2.setId(2);
        expectedEntity2.setName("project 2");
        expectedEntity2.setDescription("project description 2");

        List<ProjectEntity> expectedList = new ArrayList<>(Arrays.asList(expectedEntity1, expectedEntity2));

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

        resultStatement.close();

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

        statement.setInt(1, 2);
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

        resultStatement.setInt(1, 2);
        ResultSet resultOfUpdate = resultStatement.executeQuery();

        ProjectEntity resultOfUpdateEntity = null;

        if(resultOfUpdate.next()){
            resultOfUpdateEntity = new ProjectEntity();
        }

        resultStatement.close();

        assertNull(resultOfUpdateEntity);
    }
}