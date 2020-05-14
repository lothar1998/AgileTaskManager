package pl.kuglin.agile.ui.command.strategy;

import pl.kuglin.agile.persistence.ConsumerSQL;
import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.entities.SprintEntity;
import pl.kuglin.agile.ui.AbstractTable;
import pl.kuglin.agile.ui.AbstractWindow;

import java.sql.SQLException;

public class UpdateSprintsStrategy implements ConsumerSQL<AbstractWindow> {
    @Override
    public void accept(AbstractWindow window) throws SQLException {
        AbstractTable table = window.getTable();
        CrudRepository<SprintEntity, Integer> repository = window.getRepositoryPack().getSprintRepository();
        for (Integer r : table.getRowsEditedId()) {
            SprintEntity sprintEntity = new SprintEntity();
            sprintEntity.setId((Integer) table.getValueAt(r, 0));
            sprintEntity.setNo((Integer) table.getValueAt(r, 1));
            sprintEntity.setName((String) table.getValueAt(r, 2));
            sprintEntity.setProjectId(window.getProjectId());
            repository.update(sprintEntity);
        }
    }
}
