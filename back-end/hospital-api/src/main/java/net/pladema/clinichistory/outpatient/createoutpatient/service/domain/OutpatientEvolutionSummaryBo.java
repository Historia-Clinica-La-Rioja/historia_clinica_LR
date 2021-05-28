package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import net.pladema.clinichistory.hospitalization.service.domain.ClinicalSpecialtyBo;
import net.pladema.clinichistory.outpatient.repository.domain.OutpatientEvolutionSummaryVo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OutpatientEvolutionSummaryBo {

    private Integer consultationID;

    private ClinicalSpecialtyBo clinicalSpecialty;

    private List<HealthConditionSummaryBo> healthConditions;

    private LocalDate startDate;

    private List<ReasonBo> reasons;

    private HealthcareProfessionalBo medic;

    private List<ProcedureBo> procedures = new ArrayList<>();

    private String evolutionNote;

    public OutpatientEvolutionSummaryBo(Integer id, LocalDate startDate, HealthcareProfessionalBo medic, String evolutionNote){
        this.consultationID = id;
        this.startDate = startDate;
        this.medic = medic;
        this.evolutionNote = evolutionNote;
    }

    public OutpatientEvolutionSummaryBo(OutpatientEvolutionSummaryVo outpatientEvolutionSummaryVo){
        this.consultationID = outpatientEvolutionSummaryVo.getConsultationID();
        if(outpatientEvolutionSummaryVo.getClinicalSpecialty() != null)
            this.clinicalSpecialty = new ClinicalSpecialtyBo(outpatientEvolutionSummaryVo.getClinicalSpecialty());
        this.startDate = outpatientEvolutionSummaryVo.getStartDate();
        this.medic = new HealthcareProfessionalBo(outpatientEvolutionSummaryVo.getMedic(), outpatientEvolutionSummaryVo.getPerson());
        this.evolutionNote = outpatientEvolutionSummaryVo.getEvolutionNote();
    }
}
