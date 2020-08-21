package net.pladema.medicalconsultation.diary.controller.constraints;

import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import net.pladema.establishment.repository.ClinicalSpecialtySectorRepository;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import org.springframework.stereotype.Component;

@Component
public class BackofficeDoctorsOfficeEntityValidator extends BackofficeEntityValidatorAdapter<DoctorsOffice, Integer> {

	ClinicalSpecialtySectorRepository clinicalSpecialtySectorRepository;

	public BackofficeDoctorsOfficeEntityValidator(ClinicalSpecialtySectorRepository clinicalSpecialtySectorRepository) {
		this.clinicalSpecialtySectorRepository = clinicalSpecialtySectorRepository;
	}

	@Override
	public void assertUpdate(Integer id, DoctorsOffice entity) {
		this.assertCreate(entity);
	}

	@Override
	public void assertCreate(DoctorsOffice entity) {
		checkMatchingIds(entity);
	}

	private void checkMatchingIds(DoctorsOffice entity){
		if(!clinicalSpecialtySectorRepository.getInstitutionId(entity.getClinicalSpecialtySectorId())
				.equals(entity.getInstitutionId())){
			throw new BackofficeValidationException("doctorsoffices.matchingIds");
		}
	}

}
