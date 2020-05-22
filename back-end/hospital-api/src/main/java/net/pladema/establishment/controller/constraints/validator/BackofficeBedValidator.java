package net.pladema.establishment.controller.constraints.validator;

import org.springframework.stereotype.Component;

import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.service.BedService;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;

@Component
public class BackofficeBedValidator extends BackofficeEntityValidatorAdapter<Bed, Integer> {
	
	BedService bedService;
	
	public BackofficeBedValidator(BedService bedService) {
		this.bedService = bedService;
	}
	
	/**
	 * Si la cama tiene un episodio de internación, no se puede
	 * marcar como libre desde el backoffice.
	 */
	@Override
	public void assertUpdate(Integer id, Bed entity) {
		if (entity.getFree() && this.bedService.hasActiveInternmentEpisode(id)) {
			throw new BackofficeValidationException("beds.existsInternmentEpisode");
		}
	}

}
