package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.entities.SprintEntity;
import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.window.DecisionDialog;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateSprintsStrategy extends AbstractUpdateStrategy {
    @Override
    public void accept(AbstractWindow window) {
        AbstractTable table = window.getTable();

        List<SprintEntity> toUpdate = new LinkedList<>();

        for (Integer r : table.getRowsEditedId()) {

            int row = getRowNoByValue(window, r);

            SprintEntity sprintEntity = new SprintEntity();
            sprintEntity.setId((Integer) table.getValueAt(row, 0));
            sprintEntity.setNo((Integer) table.getValueAt(row, 1));
            sprintEntity.setName((String) table.getValueAt(row, 2));
            sprintEntity.setProjectId(window.getProjectId());

            toUpdate.add(sprintEntity);
        }

        new DecisionDialog(generateDialogWindowMessage(toUpdate.stream().map(SprintEntity::getName).collect(Collectors.toList())), window, w -> {
            CrudRepository<SprintEntity, Integer> repository = window.getRepositoryPack().getSprintRepository();
            for (SprintEntity s : toUpdate)
                repository.update(s);
        });
    }
}
