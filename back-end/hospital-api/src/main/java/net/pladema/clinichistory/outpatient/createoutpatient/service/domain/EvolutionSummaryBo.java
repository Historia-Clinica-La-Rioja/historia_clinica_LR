package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import net.pladema.clinichistory.outpatient.repository.domain.OdontologyEvolutionSummaryVo;
import net.pladema.clinichistory.outpatient.repository.domain.OutpatientEvolutionSummaryVo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EvolutionSummaryBo {

    private Integer consultationID;

    private OutpatientClinicalSpecialtyBo clinicalSpecialty;

    private List<HealthConditionSummaryBo> healthConditions;

    private LocalDate startDate;

    private List<ReasonBo> reasons;

    private HealthcareProfessionalBo medic;

    private List<ProcedureBo> procedures = new ArrayList<>();

    private String evolutionNote;

    public EvolutionSummaryBo(Integer id, LocalDate startDate, HealthcareProfessionalBo medic, String evolutionNote){
        this.consultationID = id;
        this.startDate = startDate;
        this.medic = medic;
        this.evolutionNote = evolutionNote;
    }

    public EvolutionSummaryBo(OutpatientEvolutionSummaryVo outpatientEvolutionSummaryVo){
        this.consultationID = outpatientEvolutionSummaryVo.getConsultationID();
        if(outpatientEvolutionSummaryVo.getClinicalSpecialty() != null)
            this.clinicalSpecialty = new OutpatientClinicalSpecialtyBo(outpatientEvolutionSummaryVo.getClinicalSpecialty());
        this.startDate = outpatientEvolutionSummaryVo.getStartDate();
        this.medic = new HealthcareProfessionalBo(outpatientEvolutionSummaryVo.getMedic(), outpatientEvolutionSummaryVo.getPerson());
        this.evolutionNote = outpatientEvolutionSummaryVo.getEvolutionNote();
    }

    public EvolutionSummaryBo(OdontologyEvolutionSummaryVo odontologyEvolutionSummaryVo){
        this.consultationID = odontologyEvolutionSummaryVo.getConsultationId();
        if(odontologyEvolutionSummaryVo.getClinicalSpecialty() != null)
            this.clinicalSpecialty = new OutpatientClinicalSpecialtyBo(odontologyEvolutionSummaryVo.getClinicalSpecialty());
        this.startDate = odontologyEvolutionSummaryVo.getStartDate();
        this.medic = new HealthcareProfessionalBo(odontologyEvolutionSummaryVo.getMedic(), odontologyEvolutionSummaryVo.getPerson());
        this.evolutionNote = odontologyEvolutionSummaryVo.getEvolutionNote();
    }
}
