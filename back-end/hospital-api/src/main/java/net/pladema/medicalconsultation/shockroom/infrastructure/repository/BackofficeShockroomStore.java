package net.pladema.medicalconsultation.shockroom.infrastructure.repository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.medicalconsultation.shockroom.infrastructure.repository.entity.Shockroom;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;

import javax.validation.ConstraintViolationException;

import java.util.Collections;

public class BackofficeShockroomStore extends BackofficeRepository<Shockroom, Integer> {

	private ShockroomRepository shockroomRepository;

	private EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

	public BackofficeShockroomStore(ShockroomRepository shockroomRepository, EmergencyCareEpisodeRepository emergencyCareEpisodeRepository) {
		super(shockroomRepository, new SingleAttributeBackofficeQueryAdapter<Shockroom>("description"));
		this.shockroomRepository = shockroomRepository;
		this.emergencyCareEpisodeRepository = emergencyCareEpisodeRepository;
	}

	@Override
	public void deleteById(Integer id) {
		assertEmergencyEpisodeInProcess(id);
		shockroomRepository.deleteById(id);
	}

	private void assertEmergencyEpisodeInProcess(Integer shockroomId) {
		if (emergencyCareEpisodeRepository.existsEpisodeInOffice(null, shockroomId) > 0)
			throw new BackofficeValidationException("shockrooms.existsEmergencyEpisode");
	}
}
