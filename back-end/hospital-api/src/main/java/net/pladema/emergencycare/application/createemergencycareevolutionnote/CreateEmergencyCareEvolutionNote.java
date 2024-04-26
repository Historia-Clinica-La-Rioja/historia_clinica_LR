package net.pladema.emergencycare.application.createemergencycareevolutionnote;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.controller.mapper.EmergencyCareEvolutionNoteMapper;
import net.pladema.emergencycare.service.CreateEmergencyCareEvolutionNoteDocumentService;
import net.pladema.emergencycare.service.CreateEmergencyCareEvolutionNoteService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.EmergencyCareEvolutionNoteReasonService;
import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteBo;
import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteDocumentBo;

import net.pladema.establishment.service.RoomService;
import net.pladema.medicalconsultation.doctorsoffice.service.DoctorsOfficeService;
import net.pladema.medicalconsultation.shockroom.application.FetchShockRoomSectorId;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
@AllArgsConstructor
@Service
public class CreateEmergencyCareEvolutionNote {

	private final EmergencyCareEpisodeService emergencyCareEpisodeService;

	private final CreateEmergencyCareEvolutionNoteService createEmergencyCareEvolutionNoteService;

	private final HealthcareProfessionalExternalServiceImpl healthcareProfessionalExternalService;
	
	private final PatientExternalService patientExternalService;

	private final EmergencyCareEvolutionNoteReasonService emergencyCareEvolutionNoteReasonService;

	private final CreateEmergencyCareEvolutionNoteDocumentService createEmergencyCareEvolutionNoteDocumentService;

	private final RoomService roomService;

	private final DoctorsOfficeService doctorsOfficeService;

	private final FetchShockRoomSectorId fetchShockRoomSectorId;

	@Transactional
	public EmergencyCareEvolutionNoteBo run (Integer institutionId, Integer episodeId, EmergencyCareEvolutionNoteDocumentBo evolutionNoteDocument) {

		assertValidEmergencyCareEvolutionNote(evolutionNoteDocument);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		Integer patientMedicalCoverageId = emergencyCareEpisodeService.getPatientMedicalCoverageIdByEpisode(episodeId);

		EmergencyCareEvolutionNoteBo emergencyCareEvolutionNoteBo = createEmergencyCareEvolutionNoteService.execute(institutionId, evolutionNoteDocument.getPatientId(), doctorId, evolutionNoteDocument.getClinicalSpecialtyId(), patientMedicalCoverageId);

		evolutionNoteDocument.setEncounterId(episodeId);
		evolutionNoteDocument.setInstitutionId(institutionId);
		evolutionNoteDocument.setMedicalCoverageId(patientMedicalCoverageId);
		setEvolutionNotePlace(evolutionNoteDocument, episodeId);
		BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(evolutionNoteDocument.getPatientId());
		if (patientDto.getPerson() != null)
			evolutionNoteDocument.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));
		else
			evolutionNoteDocument.setPatientInfo(new PatientInfoBo(patientDto.getId()));

		emergencyCareEvolutionNoteReasonService.addReasons(emergencyCareEvolutionNoteBo.getId(), evolutionNoteDocument.getReasons());

		Long documentId = createEmergencyCareEvolutionNoteDocumentService.execute(evolutionNoteDocument, emergencyCareEvolutionNoteBo.getId()).getId();
		emergencyCareEvolutionNoteBo.setDocumentId(documentId);

		return emergencyCareEvolutionNoteBo;
	}
	private void assertValidEmergencyCareEvolutionNote(EmergencyCareEvolutionNoteDocumentBo evolutionNote) {
		Assert.isTrue(evolutionNote.getMainDiagnosis() != null, "La nota de evolución de guardia debe contener un diagnóstico principal");
		Assert.isTrue(evolutionNote.getEvolutionNote() != null, "La nota de evolución de guardia debe contener una evolución");
		Assert.isTrue(evolutionNote.getClinicalSpecialtyId() != null, "La nota de evolución de guardia debe tener asociada una especialidad");
	}

	private void setEvolutionNotePlace(EmergencyCareEvolutionNoteDocumentBo emergencyCareEvolutionNoteDocument, Integer episodeId) {
		emergencyCareEpisodeService.getRoomId(episodeId).ifPresent(roomId -> {
			emergencyCareEvolutionNoteDocument.setRoomId(roomId);
			emergencyCareEvolutionNoteDocument.setSectorId(roomService.getSectorId(roomId));
		});
		emergencyCareEpisodeService.getShockRoomId(episodeId).ifPresent(shockRoomId -> {
			emergencyCareEvolutionNoteDocument.setShockRoomId(shockRoomId);
			emergencyCareEvolutionNoteDocument.setSectorId(fetchShockRoomSectorId.execute(shockRoomId));
		});
		emergencyCareEpisodeService.getDoctorsOfficeId(episodeId).ifPresent(doctorsOfficeId -> {
			emergencyCareEvolutionNoteDocument.setDoctorsOfficeId(doctorsOfficeId);
			emergencyCareEvolutionNoteDocument.setSectorId(doctorsOfficeService.getSectorId(doctorsOfficeId));
		});
	}

}
