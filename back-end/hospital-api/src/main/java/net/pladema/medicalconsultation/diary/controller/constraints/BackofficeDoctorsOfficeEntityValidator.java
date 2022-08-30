package net.pladema.medicalconsultation.diary.controller.constraints;

import net.pladema.establishment.repository.SectorRepository;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import org.springframework.stereotype.Component;

@Component
public class BackofficeDoctorsOfficeEntityValidator extends BackofficeEntityValidatorAdapter<DoctorsOffice, Integer> {

	SectorRepository sectorRepository;

	public BackofficeDoctorsOfficeEntityValidator(SectorRepository sectorRepository) {
		this.sectorRepository = sectorRepository;
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
		if(!sectorRepository.getInstitutionId(entity.getSectorId())
				.equals(entity.getInstitutionId())){
			throw new BackofficeValidationException("doctorsoffices.matchingIds");
		}
	}

}
