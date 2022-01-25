package net.pladema.clinichistory.outpatient.repository.domain;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReasonSummaryBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.person.repository.entity.Person;
import net.pladema.staff.repository.domain.ClinicalSpecialtyVo;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import net.pladema.staff.repository.entity.HealthcareProfessional;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NursingEvolutionSummaryVo implements Serializable {

    private Integer consultationId;

    private ClinicalSpecialtyVo clinicalSpecialty;

    private List<HealthConditionSummaryVo> healthConditions;

    private LocalDate startDate;

    private List<ReasonSummaryBo> reasons;

    private HealthcareProfessional professional;

    private Person person;

    private List<ProcedureSummaryBo> procedures = new ArrayList<>();

    private String evolutionNote;

    public NursingEvolutionSummaryVo(Integer id, LocalDate startDate, ClinicalSpecialty clinicalSpecialty,
                                     HealthcareProfessional professional, Person person, String evolutionNote){
        this.consultationId = id;
        if(clinicalSpecialty != null) {
            clinicalSpecialty.fixSpecialtyType();
            this.clinicalSpecialty = new ClinicalSpecialtyVo(clinicalSpecialty);
        }
        this.startDate = startDate;
        this.professional = professional;
        this.person = person;
        this.evolutionNote = evolutionNote;
    }
}
