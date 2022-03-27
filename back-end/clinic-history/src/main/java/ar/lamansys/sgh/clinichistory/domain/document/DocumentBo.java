package ar.lamansys.sgh.clinichistory.domain.document;

import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DentalActionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthHistoryConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProblemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DocumentBo implements IDocumentBo {

    private Long id;

    private Integer patientId;

    private PatientInfoBo patientInfo;

    private boolean confirmed = true;

    private Integer clinicalSpecialtyId;

    private Integer medicalCoverageId;

    private short documentType;

    private Integer encounterId;

    private Integer institutionId;

    private Short documentSource;

    private HealthConditionBo mainDiagnosis;

    private List<DiagnosisBo> diagnosis = new ArrayList<>();

    private List<ProblemBo> problems = new ArrayList<>();

    private List<ProcedureBo> procedures = new ArrayList<>();

    private List<HealthHistoryConditionBo> personalHistories = new ArrayList<>();

    private List<HealthHistoryConditionBo> familyHistories = new ArrayList<>();

    private List<MedicationBo> medications = new ArrayList<>();

    private List<AllergyConditionBo> allergies = new ArrayList<>();

    private List<ImmunizationBo> immunizations = new ArrayList<>();

    private List<DiagnosticReportBo> diagnosticReports = new ArrayList<>();

    private List<ReasonBo> reasons = new ArrayList<>();

    private List<DentalActionBo> dentalActions = new ArrayList<>();

    private RiskFactorBo riskFactors;

    private AnthropometricDataBo anthropometricData;

    private DocumentObservationsBo notes;

    private LocalDateTime performedDate;


    public String getDocumentStatusId(){
        return isConfirmed() ? DocumentStatus.FINAL : DocumentStatus.DRAFT;
    }

    @Override
    public Integer getPatientId() {
        if (patientInfo != null)
            return patientInfo.getId();
        return patientId;
    }


}
