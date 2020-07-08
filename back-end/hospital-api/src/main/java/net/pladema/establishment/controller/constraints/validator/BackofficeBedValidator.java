package net.pladema.establishment.controller.constraints.validator;

import org.springframework.stereotype.Component;

import net.pladema.establishment.repository.entity.Bed;
import net.pladema.clinichistory.hospitalization.controller.externalservice.InternmentEpisodeExternalService;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;

@Component
public class BackofficeBedValidator extends BackofficeEntityValidatorAdapter<Bed, Integer> {
	
	InternmentEpisodeExternalService internmentEpisodeExternalService;
	
	public BackofficeBedValidator(InternmentEpisodeExternalService internmentEpisodeExternalService) {
		this.internmentEpisodeExternalService = internmentEpisodeExternalService;
	}

	/**
	 * Si la cama tiene un episodio de internación, no se puede
	 * marcar como libre desde el backoffice.
	 */
	@Override
	public void assertUpdate(Integer id, Bed entity) {
		if (entity.getFree() && this.internmentEpisodeExternalService.existsActiveForBedId(id)) {
			throw new BackofficeValidationException("beds.existsInternmentEpisode");
		}
	}

}
