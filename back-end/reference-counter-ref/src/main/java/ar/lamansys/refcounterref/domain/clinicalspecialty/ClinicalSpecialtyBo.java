package ar.lamansys.refcounterref.domain.clinicalspecialty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClinicalSpecialtyBo {

    private Integer id;

    private String name;

    public ClinicalSpecialtyBo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}