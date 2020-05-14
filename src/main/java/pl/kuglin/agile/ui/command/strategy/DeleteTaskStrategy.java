package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.ConsumerSQL;
import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.entities.TaskEntity;
import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

public class DeleteTaskStrategy implements ConsumerSQL<AbstractWindow> {
    @Override
    public void accept(AbstractWindow window) throws SQLException {
        int row = window.getTable().getSelectedRow();

        AbstractTable table = window.getTable();

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId((Integer)table.getValueAt(row, 0));
        taskEntity.setDescription((String)table.getValueAt(row, 1));
        taskEntity.setEstimation((Integer) table.getValueAt(row, 2));
        taskEntity.setSprintId(window.getSprintId());

        CrudRepository<TaskEntity, Integer> taskRepository = window.getRepositoryPack().getTaskRepository();

        taskRepository.delete(taskEntity);
        ((DefaultTableModel)window.getTable().getModel()).removeRow(row);
    }
}
