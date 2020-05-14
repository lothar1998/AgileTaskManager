package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.entities.ProgressEntity;
import pl.kuglin.agile.persistence.entities.TaskEntity;
import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.window.DecisionDialog;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateTasksStrategy extends AbstractUpdateStrategy {
    @Override
    public void accept(AbstractWindow window) throws SQLException {
        AbstractTable table = window.getTable();

        List<ProgressEntity> progresses = window.getRepositoryPack().getProgressRepository().getAll();

        List<TaskEntity> toUpdate = new LinkedList<>();

        for (Integer r : table.getRowsEditedId()) {

            int row = getRowNoByValue(window, r);

            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setId((Integer) table.getValueAt(row, 0));
            taskEntity.setDescription((String) table.getValueAt(row, 1));
            taskEntity.setEstimation((Integer) table.getValueAt(row, 2));
            taskEntity.setSprintId(window.getSprintId());

            Integer progressId = progresses.stream().filter(p -> p.getName().equals(table.getValueAt(row, 3))).map(ProgressEntity::getId).iterator().next();

            taskEntity.setProgressId(progressId);
            toUpdate.add(taskEntity);
        }

        new DecisionDialog(generateDialogWindowMessage(toUpdate.stream().map(t -> "task no: " + t.getId()).collect(Collectors.toList())), window, w -> {
            CrudRepository<TaskEntity, Integer> repository = window.getRepositoryPack().getTaskRepository();
            for (TaskEntity t : toUpdate)
                repository.update(t);
        });
    }
}
