package ar.lamansys.sgh.clinichistory.domain.document;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EDocumentStatus;
import ar.lamansys.sgh.clinichistory.domain.document.visitor.DocumentVisitor;
import ar.lamansys.sgh.clinichistory.domain.completedforms.CompleteParameterizedFormBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ConclusionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DentalActionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentHealthcareProfessionalBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ExternalCauseBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FamilyHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.IpsBo;
import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ObstetricEventBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherRiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProblemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ReferableItemBo;
import ar.lamansys.sgh.shared.domain.general.AddressBo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IDocumentBo {

    default PatientInfoBo getPatientInfo() {
        return null;
    }

	default AddressBo getInstitutionAddress(){return null;}

    default Long getId() {
        return null;
    }

    default boolean isConfirmed() {
        return true;
    }

    default HealthConditionBo getMainDiagnosis() {
        return null;
    }

    default List<DiagnosisBo> getDiagnosis() {
        return Collections.emptyList();
    }

    default List<ProblemBo> getProblems() {
        return Collections.emptyList();
    }

    default List<ProcedureBo> getProcedures() {
        return Collections.emptyList();
    }

    default ReferableItemBo<PersonalHistoryBo> getPersonalHistories() {
        return new ReferableItemBo<>();
    }

    default ReferableItemBo<FamilyHistoryBo> getFamilyHistories() {
        return new ReferableItemBo<>();
    }

    default List<MedicationBo> getMedications() {
        return Collections.emptyList();
    }

    default ReferableItemBo<AllergyConditionBo> getAllergies() {
        return new ReferableItemBo<>();
    }

    default List<ImmunizationBo> getImmunizations() {
        return Collections.emptyList();
    }

    default RiskFactorBo getRiskFactors() {
        return null;
    }

    default List<ReasonBo> getReasons() {
        return Collections.emptyList();
    }

    default AnthropometricDataBo getAnthropometricData() {
        return null;
    }

    default DocumentObservationsBo getNotes() {
        return null;
    }

    default Integer getClinicalSpecialtyId() {
        return null;
    }

	default Integer getClinicalSpecialtySectorId() {
		return null;
	}

    default Integer getMedicalCoverageId() {
        return null;
    }
    
    short getDocumentType();

    Integer getEncounterId();

    Integer getInstitutionId();

    Short getDocumentSource();

	default LocalDate getPatientInternmentAge() {return null;}

    void setId(Long id);

    default String getDocumentStatusId() {
        return EDocumentStatus.getDocumentStatusId(this);
    }

    Integer getPatientId();

    default List<DiagnosticReportBo> getDiagnosticReports(){
        return Collections.emptyList();
    }

	default String getEvolutionNote(){
		return null;
	}

    default List<DentalActionBo> getDentalActions() {
        return Collections.emptyList();
    }

    default LocalDateTime getPerformedDate() { return null; }

	default Long getInitialDocumentId() { return null; }

	default List<HealthConditionBo> getOtherProblems() { return Collections.emptyList(); }

	default ExternalCauseBo getExternalCause() { return null; }

	default ObstetricEventBo getObstetricEvent() { return null; }

	default OtherRiskFactorBo getOtherRiskFactors() { return null; }

	default Boolean getIsArchived() { return null; }

    default List<ConclusionBo> getConclusions() {
        return Collections.emptyList();
    }
	default Integer getSectorId() { return null; }

	default Integer getRoomId() { return null; }

	default Integer getShockRoomId() { return null; }

	default Integer getDoctorsOfficeId() { return null; }

	default List<DocumentHealthcareProfessionalBo> getHealthcareProfessionals() { return Collections.emptyList(); }

	default List<DiagnosisBo> getPreoperativeDiagnosis() { return Collections.emptyList(); }

	default List<DiagnosisBo> getPostoperativeDiagnosis() { return Collections.emptyList(); }

	default List<ProcedureBo> getSurgeryProcedures() { return Collections.emptyList(); }

	default List<ProcedureBo> getAnesthesia() { return Collections.emptyList(); }

	default List<ProcedureBo> getCultures() { return Collections.emptyList(); }

	default List<ProcedureBo> getFrozenSectionBiopsies() { return Collections.emptyList(); }

	default List<ProcedureBo> getDrainages() { return Collections.emptyList(); }

	default String getProsthesisDescription() { return null; }

	default String getDescription() { return null; }

    default boolean isTranscribed() {
        return false;
    }

	default List<Integer> getInvolvedHealthcareProfessionalIds() { return Collections.emptyList(); }

	default UUID getUuid() {return UUID.randomUUID();}

    default Long getPreviousDocumentId() { return null; }

    default void setInitialDocumentId(Long initialDocumentId) {}

    default void setPreviousDocumentId(Long lastDocumentId) {}

    default void setPatientInfo(PatientInfoBo patientInfo) {}

    default void setPatientId(Integer patientId) {}

    default void accept(DocumentVisitor documentVisitor) {
        documentVisitor.visit(this);
    }

    Map<String,Object> getContextMap();

    void setContextMap(Map<String,Object> contextMap);

    default Collection<IpsBo> getIpsComponents() { return new ArrayList<>(); }

    default Collection<IpsBo> getIpsComponentsWithStatus() { return new ArrayList<>(); }

    default void setEncounterId(Integer encounterId) {}

    default void setDocumentSource(Short documentSource) {}

    default void setDocumentType(short documentType) {}

    default void setPerformedDate(LocalDateTime performedDate) {}

    default void setClinicalSpecialtyId(Integer clinicalSpecialtyId) {}

	default void setClinicalSpecialtySectorId(Integer clinicalSpecialtyId) {}

    default void setInstitutionId(Integer institutionId) {}

    default void setNotes(DocumentObservationsBo notes) {}

    default void setBusinessObjectId(Integer businessObjectId) {}

    default Integer getBusinessObjectId() { return null; }

	default List<CompleteParameterizedFormBo> getCompleteForms() { return Collections.emptyList(); }

	default List<IsolationAlertBo> getIsolationAlerts() { return Collections.emptyList(); }
    
}
