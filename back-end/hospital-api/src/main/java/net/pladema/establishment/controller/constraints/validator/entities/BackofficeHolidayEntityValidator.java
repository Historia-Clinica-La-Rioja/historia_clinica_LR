package net.pladema.establishment.controller.constraints.validator.entities;

import net.pladema.establishment.repository.HolidayRepository;
import net.pladema.establishment.repository.entity.Holiday;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class BackofficeHolidayEntityValidator extends BackofficeEntityValidatorAdapter<Holiday, Integer> {

	private final HolidayRepository repository;

	public BackofficeHolidayEntityValidator(HolidayRepository repository){
		this.repository = repository;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertCreate(Holiday entity) {
		assertHoliday(entity);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertUpdate(Integer id, Holiday entity) {
		repository.findById(id).ifPresentOrElse(h -> {
			if (!h.getDescription().equals(entity.getDescription()))
				assertHoliday(entity);},
				() -> new BackofficeValidationException("holiday.invalid.id")
		);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertDelete(Integer id) {
		// Do nothing
	}

	private void assertHoliday (Holiday entity){
		if(repository.getByDateAndDescription(entity.getDate(), entity.getDescription()).isPresent()){
			throw new BackofficeValidationException("holiday.exists.already");
		}
	}

}
