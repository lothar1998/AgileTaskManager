package pl.kuglin.agile.persistence.entities;

import lombok.Data;
import pl.kuglin.agile.persistence.cache.Identifiable;

@Data
public class SprintEntity implements Identifiable<Integer> {
    private Integer id;
    private String name;
    private Integer no;
    private Integer projectId;

    @Override
    public Integer getIdentifier() {
        return id;
    }
}
