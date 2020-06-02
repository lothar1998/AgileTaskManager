package pl.kuglin.agile.ui.command.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.entities.ProjectEntity;
import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.command.GetSprintsByProjectIdCommand;
import pl.kuglin.agile.ui.window.DecisionDialog;

import javax.swing.table.DefaultTableModel;

public class DeleteProjectStrategy extends AbstractDeleteStrategy {

    private static final Logger log = LoggerFactory.getLogger(DeleteProjectStrategy.class);

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
