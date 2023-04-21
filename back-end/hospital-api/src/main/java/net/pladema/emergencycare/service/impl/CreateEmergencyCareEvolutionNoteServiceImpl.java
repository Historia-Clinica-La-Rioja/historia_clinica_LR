package net.pladema.emergencycare.service.impl;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.AllArgsConstructor;
import net.pladema.emergencycare.repository.EmergencyCareEvolutionNoteRepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareEvolutionNote;
import net.pladema.emergencycare.service.CreateEmergencyCareEvolutionNoteService;
import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateEmergencyCareEvolutionNoteServiceImpl implements CreateEmergencyCareEvolutionNoteService {

	private static final Logger LOG = LoggerFactory.getLogger(CreateEmergencyCareEvolutionNoteServiceImpl.class);

	public static final String OUTPUT = "Output -> {}";

	private final EmergencyCareEvolutionNoteRepository emergencyCareEvolutionNoteRepository;

	private final LocalDateMapper localDateMapper;

	@Override
	public EmergencyCareEvolutionNoteBo execute(Integer institutionId,
												Integer patientId,
												Integer doctorId, Integer clinicalSpecialtyId, Integer patientMedicalCoverageId) {
		LOG.debug("Input parameters -> institutionId {}, patientId {}, doctorId {} clinicalSpecialtyId {}, patientMedicalCoverageId {}",
				institutionId, patientId, doctorId, clinicalSpecialtyId, patientMedicalCoverageId);
		EmergencyCareEvolutionNote emergencyCareEvolutionNote = new EmergencyCareEvolutionNote(institutionId, patientId, doctorId, clinicalSpecialtyId, patientMedicalCoverageId);
		emergencyCareEvolutionNote = emergencyCareEvolutionNoteRepository.save(emergencyCareEvolutionNote);
		EmergencyCareEvolutionNoteBo result = mapToEmergencyCareEvolutionNoteBo(emergencyCareEvolutionNote);
		LOG.debug(OUTPUT, result);
		return result;
	}

	private EmergencyCareEvolutionNoteBo mapToEmergencyCareEvolutionNoteBo(EmergencyCareEvolutionNote emergencyCareEvolutionNote) {
		EmergencyCareEvolutionNoteBo result = new EmergencyCareEvolutionNoteBo();
		result.setId(emergencyCareEvolutionNote.getId());
		result.setPatientId(emergencyCareEvolutionNote.getPatientId());
		result.setClinicalSpecialtyId(emergencyCareEvolutionNote.getClinicalSpecialtyId());
		result.setInstitutionId(emergencyCareEvolutionNote.getInstitutionId());
		result.setPerformedDate(localDateMapper.fromLocalDateTimeToZonedDateTime(emergencyCareEvolutionNote.getCreatedOn()).toLocalDateTime());
		result.setDoctorId(emergencyCareEvolutionNote.getDoctorId());
		result.setPatientMedicalCoverageId(emergencyCareEvolutionNote.getPatientMedicalCoverageId());
		return result;
	}

}

