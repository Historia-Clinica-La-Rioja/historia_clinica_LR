package ar.lamansys.immunization.infrastructure.input.rest.dto;

import ar.lamansys.immunization.domain.vaccine.VaccineBo;
import ar.lamansys.immunization.domain.vaccine.VaccineRuleBo;
import ar.lamansys.immunization.domain.vaccine.VaccineConditionApplicationBo;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class VaccineInformationDto {

    private final Short id;

    private final String description;

    private List<VaccineConditionsDto> conditions;

    public VaccineInformationDto(VaccineBo vaccineBo) {
        this.id = vaccineBo.getId();
        this.description = vaccineBo.getDescription().getValue();
        this.conditions = loadConditions(vaccineBo.getRules());
    }

    private List<VaccineConditionsDto> loadConditions(List<VaccineRuleBo> rules) {
        List<VaccineConditionsDto> result = new ArrayList<>();
        rules.forEach( rule -> {
            VaccineConditionApplicationBo newCondition = rule.getVaccineConditionApplicationBo();
            result.stream()
                    .filter(condition -> condition.getId().equals(newCondition.getId()))
                    .findFirst()
                    .ifPresentOrElse(
                            condition ->  condition.add(rule.getVaccineSchemeBo(), rule.getVaccineDoseBo()),
                            () -> result.add(new VaccineConditionsDto(newCondition, rule.getVaccineSchemeBo(), rule.getVaccineDoseBo())));
        });
        return result;
    }

}
