package ar.lamansys.immunization.domain.vaccine;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VaccineConditionApplicationBo {

    @ToString.Include
    private Short id;

    @ToString.Include
    private String description;

    public VaccineConditionApplicationBo(Short id, String description) {
        this.id = id;
        this.description = description;
    }

}
