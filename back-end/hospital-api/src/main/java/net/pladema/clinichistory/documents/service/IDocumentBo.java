package net.pladema.clinichistory.documents.service;

import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.hospitalization.service.domain.ClinicalSpecialtyBo;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProblemBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProcedureBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ReasonBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;

import java.util.Collections;
import java.util.List;

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

    default VitalSignBo getVitalSigns() {
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

    default ClinicalSpecialtyBo getClinicalSpecialty() {
        return null;
    }

    default Integer getMedicalCoverageId() {
        return null;
    }
    
    short getDocumentType();

    Integer getEncounterId();

    Integer getInstitutionId();

    Short getDocumentSource();

    default String getDocumentStatusId(){
        return isConfirmed() ? DocumentStatus.FINAL : DocumentStatus.DRAFT;
    }

    Integer getPatientId();

    default List<DiagnosticReportBo> getDiagnosticReports(){
        return Collections.emptyList();
    };
}
