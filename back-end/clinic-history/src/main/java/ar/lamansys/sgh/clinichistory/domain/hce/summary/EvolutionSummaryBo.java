package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import ar.lamansys.sgh.clinichistory.domain.completedforms.CompletedFormSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EvolutionSummaryBo {

    private Integer consultationID;

    private DocumentDataBo document;

    private ClinicalSpecialtyBo clinicalSpecialty;

    private List<HealthConditionSummaryBo> healthConditions;

    private LocalDateTime startDate;

    private List<ReasonBo> reasons;

    private HealthcareProfessionalBo professional;

    private List<ProcedureBo> procedures = new ArrayList<>();

    private String evolutionNote;

	private String institutionName;

	private ElectronicJointSignatureProfessionalsBo electronicJointSignatureProfessionals;

	private List<CompletedFormSummaryBo> completedForms;

    public EvolutionSummaryBo(Integer id, LocalDateTime startDate, HealthcareProfessionalBo professional, String evolutionNote){
        this.consultationID = id;
        this.startDate = startDate;
        this.professional = professional;
        this.evolutionNote = evolutionNote;
    }

    public EvolutionSummaryBo(OutpatientEvolutionSummaryBo outpatientEvolutionSummaryBo){
        this.consultationID = outpatientEvolutionSummaryBo.getConsultationId();
        this.document = outpatientEvolutionSummaryBo.getDocument();
        this.clinicalSpecialty = outpatientEvolutionSummaryBo.getClinicalSpecialty();
        this.startDate = outpatientEvolutionSummaryBo.getStartDate();
        this.professional = outpatientEvolutionSummaryBo.getProfessional();
        this.evolutionNote = outpatientEvolutionSummaryBo.getEvolutionNote();
    }

    public EvolutionSummaryBo(OdontologyEvolutionSummaryBo odontologyEvolutionSummaryBo){
        this.consultationID = odontologyEvolutionSummaryBo.getConsultationId();
        this.document = odontologyEvolutionSummaryBo.getDocument();
        this.clinicalSpecialty = odontologyEvolutionSummaryBo.getClinicalSpecialty();
        this.startDate = odontologyEvolutionSummaryBo.getStartDate();
        this.professional = odontologyEvolutionSummaryBo.getProfessional();
        this.evolutionNote = odontologyEvolutionSummaryBo.getEvolutionNote();
    }

    public EvolutionSummaryBo(NursingEvolutionSummaryBo nursingEvolutionSummaryBo){
        this.consultationID = nursingEvolutionSummaryBo.getConsultationId();
        this.document = nursingEvolutionSummaryBo.getDocument();
        this.clinicalSpecialty = nursingEvolutionSummaryBo.getClinicalSpecialty();
        this.startDate = nursingEvolutionSummaryBo.getStartDate();
        this.professional = nursingEvolutionSummaryBo.getProfessional();
        this.evolutionNote = nursingEvolutionSummaryBo.getEvolutionNote();
    }
}
