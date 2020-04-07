package persistence.entities;

import lombok.Data;

@Data
public class BacklogEntity {
    private Integer id;
    private String description;
    private Integer projectId;
}
