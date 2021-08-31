package ar.lamansys.immunization.infrastructure.input.rest.dto;

import ar.lamansys.immunization.domain.vaccine.VaccineDoseBo;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineDoseInfoDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class VaccineSchemeDto {

    private final Short id;

    private final String description;

    private List<VaccineDoseInfoDto> doses;

    public VaccineSchemeDto(VaccineSchemeBo schemeBo, VaccineDoseBo doseBo) {
        this.id = schemeBo.getId();
        this.description = schemeBo.getDescription();
        this.doses = doseBo != null ? new ArrayList<>(Collections.singletonList(new VaccineDoseInfoDto(doseBo.getDescription(), doseBo.getOrder()))) : new ArrayList<>();

    }

    public void add(VaccineDoseBo doseBo) {
        if (doseBo == null)
            return;
        if (doses.stream().noneMatch(dose -> dose.equals(doseBo)))
            doses.add(new VaccineDoseInfoDto(doseBo.getDescription(), doseBo.getOrder()));
    }
}
