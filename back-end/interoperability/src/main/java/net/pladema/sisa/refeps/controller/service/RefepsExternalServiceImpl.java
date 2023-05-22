package net.pladema.sisa.refeps.controller.service;

import net.pladema.sisa.refeps.controller.RefepsExternalService;

import net.pladema.sisa.refeps.controller.dto.LicenseDataDto;
import net.pladema.sisa.refeps.controller.dto.ValidatedLicenseDataDto;
import net.pladema.sisa.refeps.services.RefepsService;

import net.pladema.sisa.refeps.services.domain.ValidatedLicenseNumberBo;

import net.pladema.sisa.refeps.services.exceptions.RefepsApiException;
import net.pladema.sisa.refeps.services.exceptions.RefepsLicenseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefepsExternalServiceImpl implements RefepsExternalService {

	private static final Logger LOG = LoggerFactory.getLogger(RefepsExternalServiceImpl.class);

	private final RefepsService refepsService;

	public RefepsExternalServiceImpl(RefepsService refepsService) {
		this.refepsService = refepsService;
	}

	@Override
	public List<ValidatedLicenseNumberBo> validateLicenseNumber(String identificationNumber, List<String> licenseNumbers) throws RefepsApiException, RefepsLicenseException {
		LOG.debug("Validating license numbers => {}", licenseNumbers);
		return refepsService.validateLicenseNumber(identificationNumber, licenseNumbers);
	}

	@Override
	public List<ValidatedLicenseDataDto> validateLicenseNumberAndType(String identificationNumber, List<LicenseDataDto> licenseData) throws RefepsApiException, RefepsLicenseException {
		LOG.debug("Validating license data => {}", licenseData);
		return refepsService.validateLicenseNumberAndType(identificationNumber, licenseData);
	}

}
