package pl.kuglin.agile.persistence.entities;

import lombok.Data;

@Data
public class StoryEntity {
    private Integer id;
    private String description;
    private Integer backlogId;

}
