package pl.kuglin.agile.persistence.entities;

import lombok.Data;

@Data
public class TaskEntity {
    private Integer id;
    private Integer storyId;
    private String description;
    private Integer estimation;
    private Integer progressId;
    private Integer sprintId;
}
