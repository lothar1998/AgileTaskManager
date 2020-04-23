package pl.kuglin.agile.persistence.entities;

import lombok.Data;

@Data
public class SprintEntity {
    private Integer id;
    private String name;
    private Integer no;
    private Integer projectId;
}
