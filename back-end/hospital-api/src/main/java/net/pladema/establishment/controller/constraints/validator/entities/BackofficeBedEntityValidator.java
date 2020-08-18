package net.pladema.establishment.controller.constraints.validator.entities;

import org.springframework.stereotype.Component;

import net.pladema.establishment.repository.entity.Bed;
import net.pladema.clinichistory.hospitalization.controller.externalservice.InternmentEpisodeExternalService;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;

@Component
public class BackofficeBedEntityValidator extends BackofficeEntityValidatorAdapter<Bed, Integer> {
	
	InternmentEpisodeExternalService internmentEpisodeExternalService;
	
	public BackofficeBedEntityValidator(InternmentEpisodeExternalService internmentEpisodeExternalService) {
		this.internmentEpisodeExternalService = internmentEpisodeExternalService;
	}

	@Override
	public void assertUpdate(Integer id, Bed entity) {
		if (Boolean.TRUE.equals(this.internmentEpisodeExternalService.existsActiveForBedId(id))) {
			throw new BackofficeValidationException("beds.existsHospitalization");
		}
	}

}
