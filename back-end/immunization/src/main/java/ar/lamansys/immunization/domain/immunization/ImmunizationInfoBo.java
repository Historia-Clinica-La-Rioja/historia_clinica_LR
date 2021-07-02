package ar.lamansys.immunization.domain.immunization;

import ar.lamansys.immunization.domain.snomed.SnomedBo;
import ar.lamansys.immunization.domain.vaccine.conditionapplication.VaccineConditionApplicationBo;
import ar.lamansys.immunization.domain.vaccine.doses.VaccineDoseBo;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ImmunizationInfoBo {

    private final Integer id;

    private final Integer institutionId;

    private final SnomedBo vaccine;

    private final SnomedBo commercialVaccine;

    private final VaccineDoseBo dose;

    private final VaccineConditionApplicationBo condition;

    private final Short schemeId;

    private final LocalDate administrationDate;

    private final String batchNumber;

    private final String note;

    public ImmunizationInfoBo(Integer id,
                              Integer institutionId,
                              SnomedBo vaccine,
                              SnomedBo commercialVaccine,
                              Short conditionId,
                              Short schemeId,
                              Short doseId,
                              LocalDate administrationDate,
                              String batchNumber,
                              String note) {
        this.id = id;
        this.institutionId = institutionId;
        this.vaccine = vaccine;
        this.commercialVaccine = commercialVaccine;
        this.dose = VaccineDoseBo.map(doseId);
        this.condition = VaccineConditionApplicationBo.map(conditionId);
        this.schemeId = schemeId;
        this.administrationDate = administrationDate;
        this.batchNumber = batchNumber;
        this.note = note;
    }

    public String getVaccineName() {
        return vaccine.getPt();
    }
}
