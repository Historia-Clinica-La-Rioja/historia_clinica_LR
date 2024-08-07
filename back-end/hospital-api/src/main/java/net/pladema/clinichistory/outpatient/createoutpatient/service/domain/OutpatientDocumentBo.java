package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import ar.lamansys.sgh.clinichistory.domain.ReferableItemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FamilyHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProblemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.domain.completedforms.CompleteParameterizedFormBo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OutpatientDocumentBo implements IDocumentBo {

    private Long id;

    private Integer patientId;

    private PatientInfoBo patientInfo;

    private Integer encounterId;

    private Integer institutionId;

    private Integer doctorId;

    private String reasonId;

    private String evolutionNote;

    private List<ProblemBo> problems = new ArrayList<>();

    private List<ProcedureBo> procedures = new ArrayList<>();

    private ReferableItemBo<PersonalHistoryBo> personalHistories;

    private ReferableItemBo<FamilyHistoryBo> familyHistories;

    private List<MedicationBo> medications = new ArrayList<>();

    private List<ImmunizationBo> immunizations = new ArrayList<>();

    private ReferableItemBo<AllergyConditionBo> allergies;

    private AnthropometricDataBo anthropometricData ;

    private RiskFactorBo riskFactors;

    private List<ReasonBo> reasons = new ArrayList<>();

    private Integer clinicalSpecialtyId;

    private LocalDateTime performedDate;

	private List<Integer> involvedHealthcareProfessionalIds;
    
	private Integer medicalCoverageId;

    private Map<String, Object> contextMap;

	private List<CompleteParameterizedFormBo> completeForms = new ArrayList<>();

    @Override
    public DocumentObservationsBo getNotes() {
        if (evolutionNote == null)
            return null;
        DocumentObservationsBo notes = new DocumentObservationsBo();
        notes.setOtherNote(evolutionNote);
        return notes;
    }

    @Override
    public Integer getPatientId() {
        if (patientInfo != null)
            return patientInfo.getId();
        return patientId;
    }

    @Override
    public short getDocumentType() {
        return DocumentType.OUTPATIENT;
    }

    @Override
    public Short getDocumentSource() {
        return SourceType.OUTPATIENT;
    }

}
