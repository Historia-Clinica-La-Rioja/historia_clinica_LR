package net.pladema.sisa.refeps.controller.mapper;

import net.pladema.sisa.refeps.controller.dto.ValidatedLicenseNumberDto;
import net.pladema.sisa.refeps.services.domain.ValidatedLicenseNumberBo;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ValidatedLicenceNumberMapper {

	@Named("toValidatedLicenseNumberDto")
	ValidatedLicenseNumberDto toValidatedLicenseNumberDto(ValidatedLicenseNumberBo validatedLicenceNumber);

	@Named("toValidatedLicenseNumberBo")
	ValidatedLicenseNumberBo toValidatedLicenseNumberBo(ValidatedLicenseNumberDto validatedLicenceNumber);

	@Named("toListValidatedLicenseNumberDto")
	List<ValidatedLicenseNumberDto> toListValidatedLicenseNumberDto(List<ValidatedLicenseNumberBo> validatedLicenceNumbers);

	@Named("toListValidatedLicenseNumberBo")
	List<ValidatedLicenseNumberBo> toListValidatedLicenseNumberBo(List<ValidatedLicenseNumberDto> validatedLicenceNumbers);

}
