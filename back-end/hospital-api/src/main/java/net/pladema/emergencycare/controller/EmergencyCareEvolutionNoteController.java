package net.pladema.emergencycare.controller;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.mapper.OutpatientConsultationMapper;
import net.pladema.emergencycare.controller.dto.EmergencyCareEvolutionNoteDto;
import net.pladema.emergencycare.controller.mapper.EmergencyCareEvolutionNoteMapper;
import net.pladema.emergencycare.service.CreateEmergencyCareEvolutionNoteDocumentService;
import net.pladema.emergencycare.service.CreateEmergencyCareEvolutionNoteService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.EmergencyCareEvolutionNoteReasonService;
import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteBo;
import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteDocumentBo;

import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/institution/{institutionId}/emergency-care/episodes/{episodeId}/create-evolution-note")
@Tag(name = "Emergency care evolution notes", description = "Emergency care evolution notes")
@Validated
@AllArgsConstructor
public class EmergencyCareEvolutionNoteController {

	private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEvolutionNoteController.class);

	private final EmergencyCareEpisodeService emergencyCareEpisodeService;

	private final CreateEmergencyCareEvolutionNoteService createEmergencyCareEvolutionNoteService;

	private final HealthcareProfessionalExternalServiceImpl healthcareProfessionalExternalService;

	private final EmergencyCareEvolutionNoteMapper emergencyCareEvolutionNoteMapper;

	private final PatientExternalService patientExternalService;

	private final OutpatientConsultationMapper outpatientConsultationMapper;

	private final EmergencyCareEvolutionNoteReasonService emergencyCareEvolutionNoteReasonService;

	private final CreateEmergencyCareEvolutionNoteDocumentService createEmergencyCareEvolutionNoteDocumentService;

	@PostMapping
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<Boolean> saveEmergencyCareEvolutionNote(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "episodeId") Integer episodeId,
			@RequestBody EmergencyCareEvolutionNoteDto evolutionNote
	){
		LOG.debug("Parameters -> institutionId {}, episodeId {}, evolutionNote {}", institutionId, episodeId, evolutionNote);
		assertValidEmergencyCareEvolutionNote(evolutionNote);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		Integer patientMedicalCoverageId = emergencyCareEpisodeService.getPatientMedicalCoverageIdByEpisode(episodeId);

		EmergencyCareEvolutionNoteBo emergencyCareEvolutionNoteBo = createEmergencyCareEvolutionNoteService.execute(institutionId,
				evolutionNote.getPatientId(), doctorId, evolutionNote.getClinicalSpecialtyId(), patientMedicalCoverageId);

		EmergencyCareEvolutionNoteDocumentBo emergencyCareEvolutionNoteDocument = emergencyCareEvolutionNoteMapper.fromEmergencyCareEvolutionNoteDto(evolutionNote);
		emergencyCareEvolutionNoteDocument.setEncounterId(episodeId);
		emergencyCareEvolutionNoteDocument.setInstitutionId(institutionId);

		BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(evolutionNote.getPatientId());
		if (patientDto.getPerson() != null)
			emergencyCareEvolutionNoteDocument.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));
		else
			emergencyCareEvolutionNoteDocument.setPatientInfo(new PatientInfoBo(patientDto.getId()));
		emergencyCareEvolutionNoteDocument.setPatientId(evolutionNote.getPatientId());

		List<ReasonBo> reasons = outpatientConsultationMapper.fromListReasonDto(evolutionNote.getReasons());
		emergencyCareEvolutionNoteDocument.setReasons(reasons);
		emergencyCareEvolutionNoteReasonService.addReasons(emergencyCareEvolutionNoteBo.getId(), reasons);

		createEmergencyCareEvolutionNoteDocumentService.execute(emergencyCareEvolutionNoteDocument, emergencyCareEvolutionNoteBo.getId());

		return ResponseEntity.ok().body(true);
	}

	private void assertValidEmergencyCareEvolutionNote(EmergencyCareEvolutionNoteDto evolutionNote) {
		Assert.isTrue(evolutionNote.getMainDiagnosis() != null, "La nota de evolución de guardia debe contener un diagnóstico principal");
		Assert.isTrue(evolutionNote.getEvolutionNote() != null, "La nota de evolución de guardia debe contener una nota de evolución");
		Assert.isTrue(evolutionNote.getClinicalSpecialtyId() != null, "La nota de evolución de guardia debe tener asociada una especialidad");
	}

}
