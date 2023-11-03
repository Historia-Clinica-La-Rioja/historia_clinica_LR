package net.pladema.sisa.refeps.controller;

import java.util.List;

import net.pladema.sisa.refeps.controller.dto.LicenseDataDto;
import net.pladema.sisa.refeps.controller.dto.ValidatedLicenseDataDto;
import net.pladema.sisa.refeps.services.domain.ValidatedLicenseNumberBo;
import net.pladema.sisa.refeps.services.exceptions.RefepsApiException;
import net.pladema.sisa.refeps.services.exceptions.RefepsLicenseException;

public interface RefepsExternalService {

	List<ValidatedLicenseNumberBo> validateLicenseNumber(Integer healthcareProfessionalId, String identificationNumber, List<String> licenseNumbers) throws RefepsApiException, RefepsLicenseException;

	List<ValidatedLicenseDataDto> validateLicenseNumberAndType(Integer healthcareProfessionalId, String identificationNumber, List<LicenseDataDto> licenseData) throws RefepsApiException, RefepsLicenseException;

}
