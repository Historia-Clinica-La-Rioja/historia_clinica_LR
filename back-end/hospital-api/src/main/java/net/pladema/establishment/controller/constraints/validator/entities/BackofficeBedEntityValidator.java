package net.pladema.establishment.controller.constraints.validator.entities;

import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.establishment.service.BedService;

import org.springframework.stereotype.Component;

import net.pladema.establishment.repository.entity.Bed;
import net.pladema.clinichistory.hospitalization.controller.externalservice.InternmentEpisodeExternalService;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;

@Component
public class BackofficeBedEntityValidator extends BackofficeEntityValidatorAdapter<Bed, Integer> {
	
	InternmentEpisodeExternalService internmentEpisodeExternalService;

	EmergencyCareEpisodeService emergencyCareEpisodeService;
	
	public BackofficeBedEntityValidator(InternmentEpisodeExternalService internmentEpisodeExternalService, EmergencyCareEpisodeService emergencyCareEpisodeService) {
		this.internmentEpisodeExternalService = internmentEpisodeExternalService;
		this.emergencyCareEpisodeService = emergencyCareEpisodeService;
	}

	@Override
	public void assertUpdate(Integer id, Bed entity) {
		if (Boolean.TRUE.equals(this.internmentEpisodeExternalService.existsActiveForBedId(id))) {
			throw new BackofficeValidationException("beds.existsHospitalization");
		}
	}

	@Override
	public void assertDelete(Integer id) {
		this.assertIntermentAndEmergencyEpisode(id);
	}

	private void assertIntermentAndEmergencyEpisode(Integer id) {
		if (Boolean.TRUE.equals(this.internmentEpisodeExternalService.existsActiveForBedId(id)))
			throw new BackofficeValidationException("beds.existsHospitalization");

		if (Boolean.TRUE.equals(this.emergencyCareEpisodeService.isBedOccupiedByEmergencyEpisode(id)))
			throw new BackofficeValidationException("beds.existsEmergencyEpisode");
	}

}
