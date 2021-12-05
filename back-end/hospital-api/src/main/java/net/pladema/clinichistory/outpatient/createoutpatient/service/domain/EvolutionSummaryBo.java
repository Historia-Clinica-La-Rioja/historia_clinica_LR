package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import net.pladema.clinichistory.outpatient.repository.domain.NursingEvolutionSummaryVo;
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

    private HealthcareProfessionalBo professional;

    private List<ProcedureBo> procedures = new ArrayList<>();

    private String evolutionNote;

    public EvolutionSummaryBo(Integer id, LocalDate startDate, HealthcareProfessionalBo professional, String evolutionNote){
        this.consultationID = id;
        this.startDate = startDate;
        this.professional = professional;
        this.evolutionNote = evolutionNote;
    }

    public EvolutionSummaryBo(OutpatientEvolutionSummaryVo outpatientEvolutionSummaryVo){
        this.consultationID = outpatientEvolutionSummaryVo.getConsultationID();
        if(outpatientEvolutionSummaryVo.getClinicalSpecialty() != null)
            this.clinicalSpecialty = new OutpatientClinicalSpecialtyBo(outpatientEvolutionSummaryVo.getClinicalSpecialty());
        this.startDate = outpatientEvolutionSummaryVo.getStartDate();
        this.professional = new HealthcareProfessionalBo(outpatientEvolutionSummaryVo.getProfessional(), outpatientEvolutionSummaryVo.getPerson());
        this.evolutionNote = outpatientEvolutionSummaryVo.getEvolutionNote();
    }

    public EvolutionSummaryBo(OdontologyEvolutionSummaryVo odontologyEvolutionSummaryVo){
        this.consultationID = odontologyEvolutionSummaryVo.getConsultationId();
        if(odontologyEvolutionSummaryVo.getClinicalSpecialty() != null)
            this.clinicalSpecialty = new OutpatientClinicalSpecialtyBo(odontologyEvolutionSummaryVo.getClinicalSpecialty());
        this.startDate = odontologyEvolutionSummaryVo.getStartDate();
        this.professional = new HealthcareProfessionalBo(odontologyEvolutionSummaryVo.getProfessional(), odontologyEvolutionSummaryVo.getPerson());
        this.evolutionNote = odontologyEvolutionSummaryVo.getEvolutionNote();
    }

    public EvolutionSummaryBo(NursingEvolutionSummaryVo nursingEvolutionSummaryVo){
        this.consultationID = nursingEvolutionSummaryVo.getConsultationId();
        if(nursingEvolutionSummaryVo.getClinicalSpecialty() != null)
            this.clinicalSpecialty = new OutpatientClinicalSpecialtyBo(nursingEvolutionSummaryVo.getClinicalSpecialty());
        this.startDate = nursingEvolutionSummaryVo.getStartDate();
        this.professional = new HealthcareProfessionalBo(nursingEvolutionSummaryVo.getProfessional(), nursingEvolutionSummaryVo.getPerson());
        this.evolutionNote = nursingEvolutionSummaryVo.getEvolutionNote();
    }
}
