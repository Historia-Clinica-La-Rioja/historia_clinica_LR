package ar.lamansys.sgh.clinichistory.domain.document.impl;

import ar.lamansys.sgh.clinichistory.domain.document.IEditableDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.visitor.DocumentVisitor;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnalgesicTechniqueBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticTechniqueBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.IpsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MeasuringPointBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PostAnesthesiaStatusBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureDescriptionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AnestheticReportBo implements IEditableDocumentBo {

    private Integer businessObjectId;

    private Long id;

    private Integer patientId;

    private PatientInfoBo patientInfo;

    private Integer encounterId;

    private Integer institutionId;

    private LocalDate patientInternmentAge;

    private Integer patientMedicalCoverageId;

    private LocalDateTime performedDate;

    private Long initialDocumentId;

    private HealthConditionBo mainDiagnosis;

    private List<DiagnosisBo> diagnosis;

    private List<ProcedureBo> surgeryProcedures;

    private AnthropometricDataBo anthropometricData;

    private RiskFactorBo riskFactors;

    private AnestheticHistoryBo anestheticHistory;

    private List<MedicationBo> medications;

    private List<AnestheticSubstanceBo> preMedications;

    private List<HealthConditionBo> histories;

    private ProcedureDescriptionBo procedureDescription;

    private List<AnestheticSubstanceBo> anestheticPlans;

    private List<AnalgesicTechniqueBo> analgesicTechniques;

    private List<AnestheticTechniqueBo> anestheticTechniques;

    private List<AnestheticSubstanceBo> fluidAdministrations;

    private List<AnestheticSubstanceBo> anestheticAgents;

    private List<AnestheticSubstanceBo> nonAnestheticDrugs;

    private List<AnestheticSubstanceBo> antibioticProphylaxis;

    private List<MeasuringPointBo> measuringPoints;

    private PostAnesthesiaStatusBo postAnesthesiaStatus;

    private String anestheticChart;

    private boolean confirmed = true;

    private Long previousDocumentId;

    private short documentType = DocumentType.ANESTHETIC_REPORT;

    private Short documentSource = SourceType.HOSPITALIZATION;

    private Map<String, Object> contextMap;

    private String modificationReason;

    private Integer createdBy;

    private String documentStatusId;

    @Override
    public Integer getPatientId() {
        return patientInfo != null ? patientInfo.getId() : patientId;
    }

    @Override
    public Integer getMedicalCoverageId() {
        return patientMedicalCoverageId;
    }

    @Override
    public void accept(DocumentVisitor documentVisitor) {
        documentVisitor.visitAnestheticReport(this);
    }

    @Override
    public Collection<IpsBo> getIpsComponents() {
        ArrayList<IpsBo> result = new ArrayList<>();

        Optional.ofNullable(mainDiagnosis).ifPresent(result::add);
        Optional.ofNullable(anthropometricData).ifPresent(result::add);
        Optional.ofNullable(riskFactors).ifPresent(result::add);
        Optional.ofNullable(anestheticHistory).ifPresent(result::add);
        Optional.ofNullable(procedureDescription).ifPresent(result::add);
        Optional.ofNullable(postAnesthesiaStatus).ifPresent(result::add);
        result.addAll(diagnosis);
        result.addAll(surgeryProcedures);
        result.addAll(medications);
        result.addAll(preMedications);
        result.addAll(histories);
        result.addAll(anestheticPlans);
        result.addAll(analgesicTechniques);
        result.addAll(anestheticTechniques);
        result.addAll(fluidAdministrations);
        result.addAll(anestheticAgents);
        result.addAll(nonAnestheticDrugs);
        result.addAll(antibioticProphylaxis);
        result.addAll(measuringPoints);

        return result;
    }

    @Override
    public Collection<IpsBo> getIpsComponentsWithStatus() {
        ArrayList<IpsBo> result = new ArrayList<>();
        Optional.ofNullable(mainDiagnosis).ifPresent(result::add);
        Optional.ofNullable(anthropometricData).ifPresent(result::add);
        result.addAll(diagnosis);
        result.addAll(surgeryProcedures);
        result.addAll(medications);
        result.addAll(histories);
        return result;
    }
}
