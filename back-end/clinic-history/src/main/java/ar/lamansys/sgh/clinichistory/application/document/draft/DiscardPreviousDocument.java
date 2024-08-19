package ar.lamansys.sgh.clinichistory.application.document.draft;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.ClinicalObservationService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.HealthConditionService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadMedications;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadProcedures;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ObservationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiscardPreviousDocument {

    private final DocumentService documentService;
    private final HealthConditionService healthConditionService;
    private final ClinicalObservationService clinicalObservationService;
    private final LoadProcedures loadProcedures;
    private final LoadMedications loadMedications;

    public void run(IDocumentBo previousDocument) {
        log.debug("Input parameter -> previousDocument {}", previousDocument);

        this.discardPreviousElements(previousDocument);

        Long previousDocumentId = previousDocument.getId();

        documentService.deleteById(previousDocumentId, DocumentStatus.DRAFT_DISCARDED);

        log.debug("Output -> discarded previous document with id {}", previousDocumentId);
    }

    private void discardPreviousElements(IDocumentBo documentBo) {

        discardMainDiagnosis(documentBo);

        discardDiagnosis(documentBo);

        discardSurgeryProcedures(documentBo);

        discardAnthropometricData(documentBo);

        discardMedications(documentBo);

        discardHistories(documentBo);
    }

    private void discardHistories(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getHistories())
                .ifPresent(healthConditionBos -> {
                    healthConditionBos.forEach(healthConditionBo -> {
                        healthConditionBo.setId(null);
                        healthConditionBo.setStatusId(ConditionClinicalStatus.DRAFT_DISCARDED);
                        healthConditionBo.setVerificationId(ConditionVerificationStatus.DISCARDED);
                    });
                    healthConditionService.loadOtherHistories(documentBo.getPatientInfo(), documentBo.getId(), healthConditionBos);
                });
    }

    private void discardMedications(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getMedications())
                .ifPresent(medicationBoList -> {
                    medicationBoList.forEach(medicationBo -> {
                        medicationBo.setId(null);
                        medicationBo.setStatusId(MedicationStatementStatus.DRAFT_DISCARDED);
                    });
                    loadMedications.run(documentBo.getPatientId(), documentBo.getId(), medicationBoList);
                });
    }

    private void discardAnthropometricData(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getAnthropometricData())
                .ifPresent(anthropometricDataBo -> {
                    var previousBloodType = anthropometricDataBo.getBloodType();
                    if (previousBloodType != null) {
                        previousBloodType.setId(null);
                        previousBloodType.setStatusId(ObservationStatus.DRAFT_DISCARDED);
                    }
                    var previousHeight = anthropometricDataBo.getHeight();
                    if (previousHeight != null) {
                        previousHeight.setId(null);
                        previousHeight.setStatusId(ObservationStatus.DRAFT_DISCARDED);
                    }
                    var previousWeight = anthropometricDataBo.getWeight();
                    if (previousWeight != null) {
                        previousWeight.setId(null);
                        previousWeight.setStatusId(ObservationStatus.DRAFT_DISCARDED);
                    }
                    clinicalObservationService.loadAnthropometricData(documentBo.getPatientId(), documentBo.getId(), Optional.of(anthropometricDataBo));
                });
    }

    private void discardSurgeryProcedures(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getSurgeryProcedures())
                .ifPresent(procedureBoList -> {
                    procedureBoList.forEach(procedureBo -> {
                        procedureBo.setId(null);
                        procedureBo.setStatusId(ProceduresStatus.DRAFT_DISCARDED);
                    });
                    loadProcedures.run(documentBo.getPatientId(), documentBo.getId(), procedureBoList);
                });
    }

    private void discardDiagnosis(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getDiagnosis())
                .ifPresent(diagnosisBoList -> {
                    diagnosisBoList.forEach(diagnosisBo -> {
                        diagnosisBo.setId(null);
                        diagnosisBo.setStatusId(ConditionClinicalStatus.DRAFT_DISCARDED);
                        diagnosisBo.setVerificationId(ConditionVerificationStatus.DISCARDED);
                    });
                    healthConditionService.loadDiagnosis(documentBo.getPatientInfo(), documentBo.getId(), diagnosisBoList);
                });
    }

    private void discardMainDiagnosis(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getMainDiagnosis())
                .ifPresent(healthConditionBo -> {
                    healthConditionBo.setId(null);
                    healthConditionBo.setStatusId(ConditionClinicalStatus.DRAFT_DISCARDED);
                    healthConditionBo.setVerificationId(ConditionVerificationStatus.DISCARDED);
                    healthConditionService.loadMainDiagnosis(documentBo.getPatientInfo(), documentBo.getId(), Optional.of(healthConditionBo));
                });
    }
}
