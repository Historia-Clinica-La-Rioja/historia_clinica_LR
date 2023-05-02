package net.pladema.emergencycare.service.impl;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.application.reason.ReasonService;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.AllArgsConstructor;
import net.pladema.emergencycare.repository.EmergencyCareEvolutionNoteRepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareEvolutionNote;
import net.pladema.emergencycare.service.EmergencyCareEvolutionNoteDocumentService;
import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteBo;
import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteDocumentBo;

import net.pladema.staff.service.ClinicalSpecialtyService;
import net.pladema.staff.service.HealthcareProfessionalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmergencyCareEvolutionNoteDocumentServiceImpl implements EmergencyCareEvolutionNoteDocumentService {

	private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEvolutionNoteDocumentServiceImpl.class);

	public static final String OUTPUT = "Output -> {}";

	private final EmergencyCareEvolutionNoteRepository emergencyCareEvolutionNoteRepository;

	private final ReasonService reasonService;

	private final NoteService noteService;

	private final DocumentService documentService;

	private final DocumentFileRepository documentFileRepository;

	private final HealthcareProfessionalService healthcareProfessionalService;

	private final ClinicalSpecialtyService clinicalSpecialtyService;

	private final LocalDateMapper localDateMapper;

	@Override
	public List<EmergencyCareEvolutionNoteDocumentBo> getAllDocumentsByEpisodeId(Integer episodeId) {
		LOG.debug("Input parameters -> emergencyCareEpisodeId {}", episodeId);
		List<EmergencyCareEvolutionNoteDocumentBo> result = emergencyCareEvolutionNoteRepository.findAllByEpisodeId(episodeId)
				.stream().map(evolutionNote -> {
					EmergencyCareEvolutionNoteBo evolutionNoteBo = toEmergencyCareEvolutionNoteBo(evolutionNote);
					return getEmergencyCareEvolutionNoteDocumentRelatedData(evolutionNoteBo);
				}).collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
		return result;
	}

	private EmergencyCareEvolutionNoteBo toEmergencyCareEvolutionNoteBo(EmergencyCareEvolutionNote evolutionNote) {
		EmergencyCareEvolutionNoteBo result = new EmergencyCareEvolutionNoteBo();
		result.setId(evolutionNote.getId());
		result.setDocumentId(evolutionNote.getDocumentId());
		result.setDoctorId(evolutionNote.getDoctorId());
		result.setClinicalSpecialtyId(evolutionNote.getClinicalSpecialtyId());
		result.setPerformedDate(evolutionNote.getCreatedOn());
		return result;
	}

	private EmergencyCareEvolutionNoteDocumentBo getEmergencyCareEvolutionNoteDocumentRelatedData(EmergencyCareEvolutionNoteBo evolutionNote) {
		EmergencyCareEvolutionNoteDocumentBo evolutionNoteBo = new EmergencyCareEvolutionNoteDocumentBo();
		GeneralHealthConditionBo healthCondition = documentService.getHealthConditionFromDocument(evolutionNote.getDocumentId());
		evolutionNoteBo.setMainDiagnosis(healthCondition.getMainDiagnosis());
		evolutionNoteBo.setDiagnosis(healthCondition.getDiagnosis());
		evolutionNoteBo.setFamilyHistories(healthCondition.getFamilyHistories());
		evolutionNoteBo.setAnthropometricData(documentService.getAnthropometricDataStateFromDocument(evolutionNote.getDocumentId()));
		evolutionNoteBo.setRiskFactors(documentService.getRiskFactorStateFromDocument(evolutionNote.getDocumentId()));
		evolutionNoteBo.setAllergies(documentService.getAllergyIntoleranceStateFromDocument(evolutionNote.getDocumentId()));
		evolutionNoteBo.setProcedures(documentService.getProcedureStateFromDocument(evolutionNote.getDocumentId()));
		evolutionNoteBo.setMedications(documentService.getMedicationStateFromDocument(evolutionNote.getDocumentId()));
		evolutionNoteBo.setReasons(reasonService.fetchFromDocumentId(evolutionNote.getDocumentId()));
		evolutionNoteBo.setEvolutionNote(noteService.getEvolutionNoteDescriptionByDocumentId(evolutionNote.getDocumentId()));
		evolutionNoteBo.setPerformedDate(localDateMapper.fromLocalDateTimeToZonedDateTime(evolutionNote.getPerformedDate()).toLocalDateTime());
		evolutionNoteBo.setId(evolutionNote.getDocumentId());
		evolutionNoteBo.setFileName(documentFileRepository.findById(evolutionNote.getDocumentId()).get().getFilename());
		evolutionNoteBo.setProfessional(healthcareProfessionalService.findActiveProfessionalById(evolutionNote.getDoctorId()));
		evolutionNoteBo.setClinicalSpecialtyName(clinicalSpecialtyService.getClinicalSpecialty(evolutionNote.getClinicalSpecialtyId()).get().getName());
		return evolutionNoteBo;
	}

}
