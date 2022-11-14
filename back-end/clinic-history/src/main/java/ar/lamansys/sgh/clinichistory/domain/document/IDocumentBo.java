package ar.lamansys.sgh.clinichistory.domain.document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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

public interface IDocumentBo {

    default PatientInfoBo getPatientInfo() {
        return null;
    }

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

    default List<HealthHistoryConditionBo> getPersonalHistories() {
        return Collections.emptyList();
    }

    default List<HealthHistoryConditionBo> getFamilyHistories() {
        return Collections.emptyList();
    }

    default List<MedicationBo> getMedications() {
        return Collections.emptyList();
    }

    default List<AllergyConditionBo> getAllergies() {
        return Collections.emptyList();
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

    default Integer getMedicalCoverageId() {
        return null;
    }
    
    short getDocumentType();

    Integer getEncounterId();

    Integer getInstitutionId();

    Short getDocumentSource();

	default LocalDate getPatientInternmentAge() {return null;}

    void setId(Long id);

    default String getDocumentStatusId(){
        return isConfirmed() ? DocumentStatus.FINAL : DocumentStatus.DRAFT;
    }

    Integer getPatientId();

    default List<DiagnosticReportBo> getDiagnosticReports(){
        return Collections.emptyList();
    };

    default List<DentalActionBo> getDentalActions() {
        return Collections.emptyList();
    }

    default LocalDateTime getPerformedDate() { return null; }

	default Long getInitialDocumentId() { return null; }

}
