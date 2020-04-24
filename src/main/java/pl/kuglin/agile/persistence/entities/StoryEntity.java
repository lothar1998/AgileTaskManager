package pl.kuglin.agile.persistence.entities;

import lombok.Data;
import pl.kuglin.agile.persistence.cache.Identifiable;

@Data
public class StoryEntity implements Identifiable<Integer> {
    private Integer id;
    private String description;
    private Integer backlogId;

    @Override
    public Integer getIdentifier() {
        return id;
    }
}
