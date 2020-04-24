package pl.kuglin.agile.persistence.entities;

import lombok.Data;
import pl.kuglin.agile.persistence.cache.Identifiable;

@Data
public class ProjectEntity implements Identifiable<Integer> {
    private Integer id;
    private String name;
    private String description;

    @Override
    public Integer getIdentifier() {
        return id;
    }
}
