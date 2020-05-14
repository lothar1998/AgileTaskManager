package pl.kuglin.agile.persistence;

import lombok.Data;
import pl.kuglin.agile.persistence.CrudRepository;
import pl.kuglin.agile.persistence.ReadRepository;
import pl.kuglin.agile.persistence.entities.ProgressEntity;
import pl.kuglin.agile.persistence.entities.ProjectEntity;
import pl.kuglin.agile.persistence.entities.SprintEntity;
import pl.kuglin.agile.persistence.entities.TaskEntity;

@Data
public class RepositoryPack {
    private CrudRepository<ProjectEntity, Integer> projectRepository;
    private CrudRepository<SprintEntity, Integer> sprintRepository;
    private CrudRepository<TaskEntity, Integer> taskRepository;
    private ReadRepository<ProgressEntity, Integer> progressRepository;
}
