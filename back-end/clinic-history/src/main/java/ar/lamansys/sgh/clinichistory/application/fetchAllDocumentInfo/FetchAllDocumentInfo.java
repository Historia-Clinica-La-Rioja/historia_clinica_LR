package ar.lamansys.sgh.clinichistory.application.fetchAllDocumentInfo;

import java.util.Optional;

import ar.lamansys.sgh.clinichistory.application.fetchAllDocumentInfo.port.DocumentInvolvedProfessionalStorage;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.application.reason.ReasonService;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FetchAllDocumentInfo {

    private final DocumentService documentService;

	private final NoteService noteService;

	private final ReasonService reasonService;

	private final DocumentInvolvedProfessionalStorage documentInvolvedProfessionalStorage;

    public Optional<DocumentBo> run(Long id) {
        log.debug("FetchDocumentFile with id {}",id);
		return documentService.findById(id)
				.map(this::completeInfo);
		}

	private DocumentBo completeInfo(Document document) {
		var result = new DocumentBo();
		result.setId(document.getId());
		result.setEncounterId(document.getSourceId());
		result.setDocumentSource(document.getSourceTypeId());
		result.setDocumentType(document.getTypeId());
		result.setPerformedDate(document.getCreatedOn());
		result.setClinicalSpecialtyId(document.getClinicalSpecialtyId());
		result.setPatientId(document.getPatientId());
		result.setInstitutionId(document.getInstitutionId());

		var healthCondition = documentService.getHealthConditionFromDocument(document.getId());
		result.setDiagnosis(healthCondition.getDiagnosis());
		result.setMainDiagnosis(healthCondition.getMainDiagnosis());
		result.setPersonalHistories(healthCondition.getPersonalHistories());
		result.setFamilyHistories(healthCondition.getFamilyHistories());
		result.setProblems(healthCondition.getProblems());
		result.setOtherProblems(healthCondition.getOtherProblems());

		result.setReasons(reasonService.fetchFromDocumentId(document.getId()));
		result.setAllergies(documentService.getAllergyIntoleranceStateFromDocument(document.getId()));
		result.setAnthropometricData(documentService.getAnthropometricDataStateFromDocument(document.getId()));
		result.setProcedures(documentService.getProcedureStateFromDocument(document.getId()));
		result.setMedications(documentService.getMedicationStateFromDocument(document.getId()));
		result.setNotes(fetchNotes(document));
		result.setRiskFactors(documentService.getRiskFactorStateFromDocument(document.getId()));
		result.setDentalActions(documentService.getDentalActionsFromDocument(document.getId()));
		result.setExternalCause(documentService.getExternalCauseFromDocument(document.getId()));
		result.setObstetricEvent(documentService.getObstetricEventFromDocument(document.getId()));
		result.setOtherRiskFactors(documentService.getOtherRiskFactors(document.getId()));
		result.setConclusions(documentService.getConclusionsFromDocument(document.getId()));
		result.setInvolvedHealthcareProfessionalIds(documentInvolvedProfessionalStorage.fetchSignerInvolvedProfessionalIdsByDocumentId(document.getId()));
		return result;
	}

	private DocumentObservationsBo fetchNotes(Document document) {
		var result = new DocumentObservationsBo();
		result.setOtherNote(noteService.getDescriptionById(document.getOtherNoteId()));
		result.setIndicationsNote(noteService.getDescriptionById(document.getIndicationsNoteId()));
		result.setEvolutionNote(noteService.getDescriptionById(document.getEvolutionNoteId()));
		result.setPhysicalExamNote(noteService.getDescriptionById(document.getPhysicalExamNoteId()));
		result.setStudiesSummaryNote(noteService.getDescriptionById(document.getStudiesSummaryNoteId()));
		result.setClinicalImpressionNote(noteService.getDescriptionById(document.getClinicalImpressionNoteId()));
		result.setCurrentIllnessNote(noteService.getDescriptionById(document.getCurrentIllnessNoteId()));
		return result;
	}
}
