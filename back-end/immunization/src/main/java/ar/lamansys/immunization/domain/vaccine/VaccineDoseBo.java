package ar.lamansys.immunization.domain.vaccine;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class VaccineDoseBo {

    @ToString.Include
    @EqualsAndHashCode.Include
    private final String description;

    @ToString.Include
    @EqualsAndHashCode.Include
    private final Short order;

    public VaccineDoseBo(String description, Short order) {
        this.description = description;
        this.order = order;
    }
}
