package pl.kuglin.agile.persistence.entities;

import lombok.Data;

@Data
public class ProgressEntity {
    private Integer id;
    private String name;
    private String description;
}
