package net.pladema.medicalconsultation.diary.controller.constraints;

import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import org.springframework.stereotype.Component;

@Component
public class BackofficeDoctorsOfficeEntityValidator extends BackofficeEntityValidatorAdapter<DoctorsOffice, Integer> {

	public BackofficeDoctorsOfficeEntityValidator() {
	}

	@Override
	public void assertUpdate(Integer id, DoctorsOffice entity) {
		this.assertCreate(entity);
	}

	@Override
	public void assertCreate(DoctorsOffice entity) {
		if (entity.getClosingTime().isBefore(entity.getOpeningTime())) {
			throw new BackofficeValidationException("doctorsoffices.closingBeforeOpening");
		}
	}

}
