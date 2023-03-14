package net.pladema.sisa.refeps.controller;

import net.pladema.sisa.refeps.services.domain.ValidatedLicenseNumberBo;
import net.pladema.sisa.refeps.services.exceptions.RefepsApiException;
import net.pladema.sisa.refeps.services.exceptions.RefepsLicenseException;

import java.util.List;

public interface RefepsExternalService {

	List<ValidatedLicenseNumberBo> validateLicenseNumber(String identificationNumber, List<String> licenseNumbers) throws RefepsApiException, RefepsLicenseException;

}
