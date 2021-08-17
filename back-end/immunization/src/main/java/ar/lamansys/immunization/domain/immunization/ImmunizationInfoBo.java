package ar.lamansys.immunization.domain.immunization;

import ar.lamansys.immunization.domain.snomed.SnomedBo;
import ar.lamansys.immunization.domain.vaccine.VaccineDoseBo;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class ImmunizationInfoBo {

    @ToString.Include
    private final Integer id;

    @ToString.Include
    private final Integer institutionId;

    private final String institutionInfo;

    private final String doctorInfo;

    @ToString.Include
    private final SnomedBo vaccine;

    @ToString.Include
    private final VaccineDoseBo dose;

    @ToString.Include
    private final Short conditionId;

    @ToString.Include
    private final Short schemeId;

    @ToString.Include
    private final LocalDate administrationDate;

    private final String lotNumber;

    private final String note;

    private final boolean billable;

    public ImmunizationInfoBo(Integer id,
                              Integer institutionId,
                              String institutionInfo,
                              String doctorInfo,
                              SnomedBo vaccine,
                              Short conditionId,
                              Short schemeId,
                              VaccineDoseBo dose,
                              LocalDate administrationDate,
                              String lotNumber,
                              String note,
                              boolean billable) {
        this.id = id;
        this.institutionId = institutionId;
        this.institutionInfo = institutionInfo;
        this.doctorInfo = doctorInfo;
        this.vaccine = vaccine;
        this.dose = dose;
        this.conditionId = conditionId;
        this.schemeId = schemeId;
        this.administrationDate = administrationDate;
        this.lotNumber = lotNumber;
        this.note = note;
        this.billable = billable;
    }

    public String getVaccineName() {
        return vaccine.getPt();
    }
}
