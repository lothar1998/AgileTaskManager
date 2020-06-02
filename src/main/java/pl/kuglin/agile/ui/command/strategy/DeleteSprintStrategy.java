package pl.kuglin.agile.ui.command.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.entities.SprintEntity;
import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.window.DecisionDialog;

import javax.swing.table.DefaultTableModel;

public class DeleteSprintStrategy extends AbstractDeleteStrategy {

    private static final Logger log = LoggerFactory.getLogger(DeleteSprintStrategy.class);

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

        SprintEntity sprintEntity = new SprintEntity();
        sprintEntity.setId((Integer) table.getValueAt(row, 0));
        sprintEntity.setNo((Integer) table.getValueAt(row, 1));
        sprintEntity.setName((String) table.getValueAt(row, 2));
        sprintEntity.setProjectId(window.getProjectId());

        new DecisionDialog(generateDialogWindowMessage(sprintEntity.getName()), window, w -> {
            CrudRepository<SprintEntity, Integer> sprintRepository = window.getRepositoryPack().getSprintRepository();

            sprintRepository.delete(sprintEntity);
            ((DefaultTableModel) window.getTable().getModel()).removeRow(row);
            window.getTable().getRowsEditedId().remove(getRowNoByValue(window, sprintEntity.getId()));
        });
    }
}
