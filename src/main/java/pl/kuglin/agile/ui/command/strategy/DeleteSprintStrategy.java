package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.ConsumerSQL;
import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.entities.SprintEntity;
import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

public class DeleteSprintStrategy implements ConsumerSQL<AbstractWindow> {
    @Override
    public void accept(AbstractWindow window) throws SQLException {
        int row = window.getTable().getSelectedRow();

        AbstractTable table = window.getTable();

        SprintEntity sprintEntity = new SprintEntity();
        sprintEntity.setId((Integer)table.getValueAt(row, 0));
        sprintEntity.setNo((Integer) table.getValueAt(row, 1));
        sprintEntity.setName((String)table.getValueAt(row, 2));
        sprintEntity.setProjectId(window.getProjectId());

        CrudRepository<SprintEntity, Integer> sprintRepository = window.getRepositoryPack().getSprintRepository();

        sprintRepository.delete(sprintEntity);
        ((DefaultTableModel)window.getTable().getModel()).removeRow(row);
    }
}
