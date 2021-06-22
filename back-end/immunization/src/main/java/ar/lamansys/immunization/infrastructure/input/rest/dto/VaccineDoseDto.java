package ar.lamansys.immunization.infrastructure.input.rest.dto;

import ar.lamansys.immunization.domain.vaccine.doses.VaccineDoseBo;
import lombok.Getter;

@Getter
public class VaccineDoseDto {

    private final Short id;

    private final String description;

    private final Short order;

    public VaccineDoseDto(VaccineDoseBo doseBo) {
        this.id = doseBo.getId();
        this.description = doseBo.getDescription();
        this.order = doseBo.getOrder();
    }
}
