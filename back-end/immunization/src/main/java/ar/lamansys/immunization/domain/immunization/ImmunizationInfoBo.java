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

    private final VaccineDoseBo dose;

    private final VaccineConditionApplicationBo condition;

    private final Short schemeId;

    private final LocalDate administrationDate;

    private final String lotNumber;

    private final String note;

    private final boolean billable;

    public ImmunizationInfoBo(Integer id,
                              Integer institutionId,
                              SnomedBo vaccine,
                              Short conditionId,
                              Short schemeId,
                              Short doseId,
                              LocalDate administrationDate,
                              String lotNumber,
                              String note,
                              boolean billable) {
        this.id = id;
        this.institutionId = institutionId;
        this.vaccine = vaccine;
        this.dose = doseId != null ? VaccineDoseBo.map(doseId) : null;
        this.condition = conditionId != null ? VaccineConditionApplicationBo.map(conditionId) : null;
        this.schemeId = schemeId;
        this.administrationDate = administrationDate;
        this.lotNumber = lotNumber;
        this.note = note;
        this.billable = billable;
    }

    public String getVaccineName() {
        return vaccine.getPt();
    }

    public Short getDoseId() {
        return dose == null ? null : dose.getId();
    }

    public Short getConditionId() {
        return condition == null ? null : condition.getId();
    }
}
