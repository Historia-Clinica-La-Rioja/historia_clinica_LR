package ar.lamansys.immunization.infrastructure.input.rest.dto;

import ar.lamansys.immunization.domain.vaccine.VaccineSchemeBo;
import ar.lamansys.immunization.domain.vaccine.doses.VaccineDoseBo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class VaccineSchemeDto {

    private final Short id;

    private final String description;

    private List<VaccineDoseDto> doses;

    public VaccineSchemeDto(VaccineSchemeBo schemeBo, VaccineDoseBo doseBo) {
        this.id = schemeBo.getId();
        this.description = schemeBo.getDescription();
        this.doses = new ArrayList<>(Collections.singletonList(new VaccineDoseDto(doseBo)));
    }

    public void add(VaccineDoseBo doseBo) {
        if (doses.stream().noneMatch(dose -> dose.getId().equals(doseBo.getId())))
            doses.add(new VaccineDoseDto(doseBo));
    }
}
