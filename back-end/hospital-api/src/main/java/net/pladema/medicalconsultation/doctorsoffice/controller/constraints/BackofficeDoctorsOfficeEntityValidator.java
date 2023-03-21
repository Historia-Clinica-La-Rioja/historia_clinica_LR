package net.pladema.medicalconsultation.doctorsoffice.controller.constraints;

import net.pladema.establishment.repository.SectorRepository;
import net.pladema.medicalconsultation.doctorsoffice.service.exception.DoctorOfficeDescriptionException;
import net.pladema.medicalconsultation.doctorsoffice.service.exception.DoctorOfficeEnumException;
import net.pladema.medicalconsultation.doctorsoffice.service.exception.DoctorOfficeInstitutionIdException;
import net.pladema.medicalconsultation.doctorsoffice.service.exception.DoctorOfficeSectorIdException;
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
		var sectorId = entity.getSectorId();
		var institutionId = entity.getInstitutionId();
		validateSector(sectorId,entity.getDescription());
		validateInstitutionId(institutionId);
		if (!sectorRepository.getInstitutionId(sectorId)
				.equals(institutionId))
			throw new BackofficeValidationException("doctorsoffices.matchingIds");
	}

	private void validateSector(Integer sectorId, String description) {
		if (sectorId == null)
			throw new DoctorOfficeSectorIdException(DoctorOfficeEnumException.SECTOR_ID_NULL,"El id del sector no puede ser nulo.");
		if (description == null)
			throw new DoctorOfficeDescriptionException(DoctorOfficeEnumException.DESCRIPTION_NULL,"La descripción de la oficina del doctor es obligatoria.");
		if (!sectorRepository.existsById(sectorId))
			throw new DoctorOfficeSectorIdException(DoctorOfficeEnumException.SECTOR_NOT_EXISTS,"El sector no existe.");
	}

	private void validateInstitutionId(Integer institutionId) {
		if (institutionId == null)
			throw new DoctorOfficeInstitutionIdException(DoctorOfficeEnumException.INSTITUTION_ID_NULL,"El id del instituto no puede ser nulo.");
	}

}
