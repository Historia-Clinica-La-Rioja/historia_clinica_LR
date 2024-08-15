package ar.lamansys.sgh.clinichistory.domain.document;

import ar.lamansys.sgh.clinichistory.domain.ReferableItemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ConclusionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DentalActionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ExternalCauseBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FamilyHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ObstetricEventBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherRiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProblemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentBo implements IDocumentBo {

    private Long id;

    private Integer patientId;

    private PatientInfoBo patientInfo;

    private boolean confirmed = true;

    private Integer clinicalSpecialtyId;

	private Integer clinicalSpecialtySectorId;

    private Integer medicalCoverageId;

    private short documentType;

    private Integer encounterId;

    private Integer institutionId;

    private Short documentSource;

    private HealthConditionBo mainDiagnosis;

    private List<DiagnosisBo> diagnosis = new ArrayList<>();

    private List<ProblemBo> problems = new ArrayList<>();

    private List<ProcedureBo> procedures = new ArrayList<>();

    private ReferableItemBo<PersonalHistoryBo> personalHistories;

    private ReferableItemBo<FamilyHistoryBo> familyHistories;

    private List<MedicationBo> medications = new ArrayList<>();

    private ReferableItemBo<AllergyConditionBo> allergies;

    private List<ImmunizationBo> immunizations = new ArrayList<>();

    private List<DiagnosticReportBo> diagnosticReports = new ArrayList<>();

    private List<ReasonBo> reasons = new ArrayList<>();

    private List<DentalActionBo> dentalActions = new ArrayList<>();

	private List<HealthConditionBo> otherProblems = new ArrayList<>();

    private RiskFactorBo riskFactors;

    private AnthropometricDataBo anthropometricData;

    private DocumentObservationsBo notes;

    private LocalDateTime performedDate;

	private ExternalCauseBo externalCause;
	
	private OtherRiskFactorBo otherRiskFactors;

	private ObstetricEventBo obstetricEvent;

    private List<ConclusionBo> conclusions = new ArrayList<>();

	private List<Integer> involvedHealthcareProfessionalIds;

    private Map<String,Object> contextMap;

    private Integer createdBy;

    private Long initialDocumentId;

    private String documentStatusId;

    @Override
    public Integer getPatientId() {
        if (patientInfo != null)
            return patientInfo.getId();
        return patientId;
    }


}
