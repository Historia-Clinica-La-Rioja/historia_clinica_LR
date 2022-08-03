package net.pladema.staff.controller.constraints;

import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.staff.repository.HealthcareProfessionalSpecialtyRepository;
import net.pladema.staff.repository.entity.HealthcareProfessionalSpecialty;
import org.springframework.stereotype.Component;

@Component
public class HealthcareProfessionalSpecialtyEntityValidator extends BackofficeEntityValidatorAdapter<HealthcareProfessionalSpecialty, Integer> {

	private final HealthcareProfessionalSpecialtyRepository healthcareProfessionalSpecialtyRepository;

	public HealthcareProfessionalSpecialtyEntityValidator(HealthcareProfessionalSpecialtyRepository healthcareProfessionalSpecialtyRepository) {
		this.healthcareProfessionalSpecialtyRepository = healthcareProfessionalSpecialtyRepository;
	}

	@Override
	public void assertCreate(HealthcareProfessionalSpecialty entity) {
		healthcareProfessionalSpecialtyRepository.findByUniqueKey(entity.getHealthcareProfessionalId(), entity.getClinicalSpecialtyId(), entity.getProfessionalSpecialtyId()).
		ifPresent(healthcareProfessionalSpecialty -> {
			if (!healthcareProfessionalSpecialty.isDeleted()) {
				throw new BackofficeValidationException("healthcare-professional.specialty-profession-exists");
			}
		});
	}

	@Override
	public void assertUpdate(Integer id, HealthcareProfessionalSpecialty entity) {
		if (!healthcareProfessionalSpecialtyRepository.existsValues(entity.getHealthcareProfessionalId(),
				entity.getClinicalSpecialtyId(), entity.getProfessionalSpecialtyId()))
			throw new BackofficeValidationException("healthcare-professional.specialty-profession-not-assigned");
	}


	@Override
	public void assertDelete(Integer id){
		HealthcareProfessionalSpecialty specialty = healthcareProfessionalSpecialtyRepository.findById(id)
				.orElseThrow(() -> new BackofficeValidationException("healthcare-professional.specialty-profession-not-exists"));

		Integer professionalId = specialty.getHealthcareProfessionalId();
		
		if (healthcareProfessionalSpecialtyRepository.hasOnlyOneSpecialty(professionalId))
			throw new BackofficeValidationException("healthcare-professional.only-one-specialty");
	}

}
