package ar.lamansys.refcounterref.domain.referencenote;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReferenceNoteBo {

    private Integer id;

    private String description;

    public ReferenceNoteBo(Integer id, String description) {
        this.id = id;
        this.description = description;
    }
}