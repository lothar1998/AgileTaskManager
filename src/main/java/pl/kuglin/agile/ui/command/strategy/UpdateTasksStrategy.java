package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.ConsumerSQL;
import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.entities.ProgressEntity;
import pl.kuglin.agile.persistence.entities.TaskEntity;
import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;

import java.sql.SQLException;
import java.util.List;

public class UpdateTasksStrategy implements ConsumerSQL<AbstractWindow> {
    @Override
    public void accept(AbstractWindow window) throws SQLException {
        AbstractTable table = window.getTable();
        CrudRepository<TaskEntity, Integer> repository = window.getRepositoryPack().getTaskRepository();

        List<ProgressEntity> progresses = window.getRepositoryPack().getProgressRepository().getAll();

        for (Integer r : table.getRowsEditedId()) {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setId((Integer) table.getValueAt(r, 0));
            taskEntity.setDescription((String) table.getValueAt(r, 1));
            taskEntity.setEstimation((Integer) table.getValueAt(r, 2));
            taskEntity.setSprintId(window.getSprintId());

            Integer progressId = progresses.stream().filter(p -> p.getName().equals(table.getValueAt(r, 3))).map(ProgressEntity::getId).iterator().next();

            taskEntity.setProgressId(progressId);
            repository.update(taskEntity);
        }
    }
}
