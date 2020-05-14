package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.entities.ProjectEntity;
import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;
import pl.kuglin.agile.ui.window.DecisionDialog;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateProjectsStrategy extends AbstractUpdateStrategy {
    @Override
    public void accept(AbstractWindow window) {
        AbstractTable table = window.getTable();

        List<ProjectEntity> toUpdate = new LinkedList<>();

        for (Integer r : table.getRowsEditedId()) {

            int row = getRowNoByValue(window, r);

            ProjectEntity projectEntity = new ProjectEntity();
            projectEntity.setId((Integer) table.getValueAt(row, 0));
            projectEntity.setName((String) table.getValueAt(row, 1));
            projectEntity.setDescription((String) table.getValueAt(row, 2));

            toUpdate.add(projectEntity);
        }

        new DecisionDialog(generateDialogWindowMessage(toUpdate.stream().map(ProjectEntity::getName).collect(Collectors.toList())), window, w -> {
            CrudRepository<ProjectEntity, Integer> repository = window.getRepositoryPack().getProjectRepository();
            for (ProjectEntity p : toUpdate)
                repository.update(p);
        });
    }
}
