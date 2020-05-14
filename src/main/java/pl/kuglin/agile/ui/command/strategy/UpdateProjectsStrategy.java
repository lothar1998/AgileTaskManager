package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.ConsumerSQL;
import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.entities.ProjectEntity;
import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;

import java.sql.SQLException;

public class UpdateProjectsStrategy implements ConsumerSQL<AbstractWindow> {
    @Override
    public void accept(AbstractWindow window) throws SQLException {
        AbstractTable table = window.getTable();
        CrudRepository<ProjectEntity, Integer> repository = window.getRepositoryPack().getProjectRepository();
        for (Integer r : table.getRowsEditedId()) {
            ProjectEntity projectEntity = new ProjectEntity();
            projectEntity.setId((Integer) table.getValueAt(r, 0));
            projectEntity.setName((String) table.getValueAt(r, 1));
            projectEntity.setDescription((String) table.getValueAt(r, 2));
            repository.update(projectEntity);
        }
    }
}
