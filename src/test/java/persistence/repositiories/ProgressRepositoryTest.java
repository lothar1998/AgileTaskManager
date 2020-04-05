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
import persistence.entities.ProgressEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProgressRepositoryTest {

    @Mock
    private static DataAccessLayer dataAccessLayer;
    @InjectMocks
    private static ReadRepository<ProgressEntity> progressRepository = new ProgressRepository();

    private static Connection conn;

    @BeforeEach
    void connecting() throws SQLException {
        String sql = "create table agile.progresses\n" +
                "(\n" +
                "    id          integer primary key,\n" +
                "    name        varchar(255) not null,\n" +
                "    description varchar(255) not null\n" +
                ")\\;\n" +
                "insert into agile.progresses\n" +
                "values (1, 'TO DO', 'to do elements'),\n" +
                "       (2, 'IN PROGRESS', 'tasks in progress')\\;\n";

        conn = DriverManager.getConnection("jdbc:h2:mem:test;INIT=CREATE SCHEMA IF NOT EXISTS agile\\;SET SCHEMA agile\\;" + sql, "admin", "admin");
    }

    @AfterEach
    void disconnecting() throws SQLException {
        conn.close();
    }

    @Test
    void getTest(){
        when(dataAccessLayer.executeQuery(ArgumentMatchers.<FunctionSQL<Connection, ProgressEntity>>any())).then(invocationOnMock -> {
            FunctionSQL<Connection, ProgressEntity> arg = invocationOnMock.getArgument(0);
            return arg.apply(conn);
        });

        ProgressEntity progressEntity = new ProgressEntity();
        progressEntity.setId(1);
        progressEntity.setName("TO DO");
        progressEntity.setDescription("to do elements");

        assertEquals(progressEntity, progressRepository.get(1));
    }

    @Test
    void getAllTest(){
        when(dataAccessLayer.executeQuery(ArgumentMatchers.<FunctionSQL<Connection, ProgressEntity>>any())).then(invocationOnMock -> {
            FunctionSQL<Connection, ProgressEntity> arg = invocationOnMock.getArgument(0);
            return arg.apply(conn);
        });

        List<ProgressEntity> progressEntities = new ArrayList<>();
        ProgressEntity progressEntity1 = new ProgressEntity();
        progressEntity1.setId(1);
        progressEntity1.setName("TO DO");
        progressEntity1.setDescription("to do elements");

        ProgressEntity progressEntity2 = new ProgressEntity();
        progressEntity2.setId(2);
        progressEntity2.setName("IN PROGRESS");
        progressEntity2.setDescription("tasks in progress");

        progressEntities.add(progressEntity1);
        progressEntities.add(progressEntity2);

        assertTrue(progressEntities.containsAll(progressRepository.getAll()));
        assertTrue(progressRepository.getAll().containsAll(progressEntities));
    }
}