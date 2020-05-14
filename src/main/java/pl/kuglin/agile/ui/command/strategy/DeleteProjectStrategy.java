package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.ConsumerSQL;
import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.entities.ProjectEntity;
import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

public class DeleteProjectStrategy implements ConsumerSQL<AbstractWindow> {
    @Override
    public void accept(AbstractWindow window) throws SQLException {
        int row = window.getTable().getSelectedRow();

        AbstractTable table = window.getTable();

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId((Integer)table.getValueAt(row, 0));
        projectEntity.setName((String)table.getValueAt(row, 1));
        projectEntity.setDescription((String)table.getValueAt(row, 2));

        CrudRepository<ProjectEntity, Integer> projectRepository = window.getRepositoryPack().getProjectRepository();

        projectRepository.delete(projectEntity);
        ((DefaultTableModel)window.getTable().getModel()).removeRow(row);
    }
}
