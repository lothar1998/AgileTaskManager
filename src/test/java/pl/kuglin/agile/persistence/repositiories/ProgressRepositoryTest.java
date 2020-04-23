package pl.kuglin.agile.persistence.repositiories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kuglin.agile.persistence.DataAccessLayer;
import pl.kuglin.agile.persistence.FunctionSQL;
import pl.kuglin.agile.persistence.ReadRepository;
import pl.kuglin.agile.persistence.entities.ProgressEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProgressRepositoryTest {

    @Mock
    private static DataAccessLayer dataAccessLayer;
    @InjectMocks
    private static final ReadRepository<ProgressEntity> progressRepository = new ProgressRepository(dataAccessLayer);

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
    void getAllTest() throws SQLException {
        when(dataAccessLayer.executeQuery(ArgumentMatchers.<FunctionSQL<Connection, ProgressEntity>>any())).then(invocationOnMock -> {
            FunctionSQL<Connection, ProgressEntity> arg = invocationOnMock.getArgument(0);
            return arg.apply(conn);
        });

        ProgressEntity progressEntity1 = new ProgressEntity();
        progressEntity1.setId(1);
        progressEntity1.setName("TO DO");
        progressEntity1.setDescription("to do elements");

        ProgressEntity progressEntity2 = new ProgressEntity();
        progressEntity2.setId(2);
        progressEntity2.setName("IN PROGRESS");
        progressEntity2.setDescription("tasks in progress");

        ProgressEntity progressEntity3 = new ProgressEntity();
        progressEntity3.setId(3);
        progressEntity3.setName("REVIEW");
        progressEntity3.setDescription("task reported to review");

        ProgressEntity progressEntity4 = new ProgressEntity();
        progressEntity4.setId(4);
        progressEntity4.setName("APPROVED REVIEW");
        progressEntity4.setDescription("task after positive review");

        ProgressEntity progressEntity5 = new ProgressEntity();
        progressEntity5.setId(5);
        progressEntity5.setName("REVIEW REJECTED");
        progressEntity5.setDescription("rejected review");

        ProgressEntity progressEntity6 = new ProgressEntity();
        progressEntity6.setId(6);
        progressEntity6.setName("DONE");
        progressEntity6.setDescription("done task");

        List<ProgressEntity> progressEntities = new ArrayList<>(Arrays.asList(progressEntity1, progressEntity2, progressEntity3, progressEntity4, progressEntity5, progressEntity6));

        assertTrue(progressEntities.containsAll(progressRepository.getAll()));
        assertTrue(progressRepository.getAll().containsAll(progressEntities));
    }
}