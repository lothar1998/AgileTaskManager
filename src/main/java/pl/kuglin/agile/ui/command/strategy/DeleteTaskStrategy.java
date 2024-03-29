package pl.kuglin.agile.ui.command.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.entities.TaskEntity;
import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.window.DecisionDialog;

import javax.swing.table.DefaultTableModel;

public class DeleteTaskStrategy extends AbstractDeleteStrategy {

    private static final Logger log = LoggerFactory.getLogger(DeleteTaskStrategy.class);

    @Override
    public void accept(AbstractWindow window) {
        int row = window.getTable().getSelectedRow();

        if(row < 0){
            log.warn("{}", "Row has not been selected");
            return;
        }

        if (isUpdated(window, row))
            return;

        AbstractTable table = window.getTable();

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId((Integer) table.getValueAt(row, 0));
        taskEntity.setDescription((String) table.getValueAt(row, 1));
        taskEntity.setEstimation((Integer) table.getValueAt(row, 2));
        taskEntity.setSprintId(window.getSprintId());

        new DecisionDialog(generateDialogWindowMessage("task no: " + taskEntity.getId()), window, w -> {
            CrudRepository<TaskEntity, Integer> taskRepository = window.getRepositoryPack().getTaskRepository();

            taskRepository.delete(taskEntity);
            ((DefaultTableModel) window.getTable().getModel()).removeRow(row);
            window.getTable().getRowsEditedId().remove(getRowNoByValue(window, taskEntity.getId()));
        });
    }
}
