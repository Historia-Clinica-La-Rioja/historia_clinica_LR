package net.pladema.staff.controller.constraints;

import org.springframework.stereotype.Component;

import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.staff.controller.dto.ProfessionalLicenseNumberDto;
import net.pladema.staff.repository.ProfessionalLicenseNumberRepository;
import ar.lamansys.sgh.shared.domain.ELicenseNumberTypeBo;

@Component
public class BackofficeProfessionalLicenseNumbersValidator extends BackofficeEntityValidatorAdapter<ProfessionalLicenseNumberDto, Integer> {

	private final ProfessionalLicenseNumberRepository professionalLicenseNumberRepository;

	public BackofficeProfessionalLicenseNumbersValidator(ProfessionalLicenseNumberRepository professionalLicenseNumberRepository) {
		this.professionalLicenseNumberRepository = professionalLicenseNumberRepository;
	}


	@Override
    public void assertUpdate(Integer id, ProfessionalLicenseNumberDto dto) {
		if (professionalLicenseNumberRepository.existsProfessionalLicense(
				dto.getProfessionalProfessionId(),
				ELicenseNumberTypeBo.map(dto.getTypeId())))
			throw new BackofficeValidationException("professional.licensenumbers.repeated");
		if (professionalLicenseNumberRepository.existsProfessionalLicense(
				dto.getHealthcareProfessionalSpecialtyId(),
				ELicenseNumberTypeBo.map(dto.getTypeId())))
			throw new BackofficeValidationException("professional.specialty.licensenumbers.repeated");
    }

    @Override
    public void assertCreate(ProfessionalLicenseNumberDto dto) {
		validateNotNullBlankEmpty(dto.getLicenseNumber());
		if (professionalLicenseNumberRepository.existsProfessionalLicense(
				dto.getProfessionalProfessionId(),
				ELicenseNumberTypeBo.map(dto.getTypeId())))
			throw new BackofficeValidationException("professional.licensenumbers.repeated");
		if (professionalLicenseNumberRepository.existsProfessionalSpecialtyLicense(
				dto.getHealthcareProfessionalSpecialtyId(),
				ELicenseNumberTypeBo.map(dto.getTypeId())))
			throw new BackofficeValidationException("professional.specialty.licensenumbers.repeated");
    }

	private void validateNotNullBlankEmpty(String licenseNumber) {
		if ((licenseNumber == null) || (licenseNumber.isBlank()) || (licenseNumber.isEmpty())) {
			throw new BackofficeValidationException("professional.licensenumbres.blank");
		}
	}

    @Override
    public void assertDelete(Integer id){
    }
}
