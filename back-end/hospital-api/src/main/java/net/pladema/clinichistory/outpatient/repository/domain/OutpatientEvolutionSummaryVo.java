package net.pladema.clinichistory.outpatient.repository.domain;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ProcedureSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ReasonSummaryVo;
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
public class OutpatientEvolutionSummaryVo implements Serializable {

    private Integer consultationID;

    private ClinicalSpecialtyVo clinicalSpecialty;

    private List<HealthConditionSummaryVo> healthConditions;

    private LocalDate startDate;

    private List<ReasonSummaryVo> reasons;

    private HealthcareProfessional medic;

    private Person person;

    private List<ProcedureSummaryVo> procedures = new ArrayList<>();

    private String evolutionNote;

    public OutpatientEvolutionSummaryVo(Integer id, LocalDate startDate, ClinicalSpecialty clinicalSpecialty,
                                        HealthcareProfessional medic, Person person, String evolutionNote){
        this.consultationID = id;
        if(clinicalSpecialty != null) {
            clinicalSpecialty.fixSpecialtyType();
            this.clinicalSpecialty = new ClinicalSpecialtyVo(clinicalSpecialty);
        }
        this.startDate = startDate;
        this.medic = medic;
        this.person = person;
        this.evolutionNote = evolutionNote;
    }
}
