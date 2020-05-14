package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.entities.ProjectEntity;
import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.window.DecisionDialog;

import javax.swing.table.DefaultTableModel;

public class DeleteProjectStrategy extends AbstractDeleteStrategy {
    @Override
    public void accept(AbstractWindow window) {
        int row = window.getTable().getSelectedRow();

        if (isUpdated(window, row))
            return;

        AbstractTable table = window.getTable();

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId((Integer) table.getValueAt(row, 0));
        projectEntity.setName((String) table.getValueAt(row, 1));
        projectEntity.setDescription((String) table.getValueAt(row, 2));

        new DecisionDialog(generateDialogWindowMessage(projectEntity.getName()), window, w -> {
            CrudRepository<ProjectEntity, Integer> projectRepository = window.getRepositoryPack().getProjectRepository();

            projectRepository.delete(projectEntity);
            ((DefaultTableModel) window.getTable().getModel()).removeRow(row);
            window.getTable().getRowsEditedId().remove(getRowNoByValue(window, projectEntity.getId()));
        });
    }
}
