package pl.kuglin.agile.persistence.entities;

import lombok.Data;
import pl.kuglin.agile.persistence.cache.Identifiable;

@Data
public class TaskEntity implements Identifiable<Integer> {
    private Integer id;
    private Integer storyId;
    private String description;
    private Integer estimation;
    private Integer progressId;
    private Integer sprintId;

    @Override
    public Integer getIdentifier() {
        return id;
    }
}
