package net.pladema.sisa.refeps.services;

import net.pladema.sisa.refeps.services.domain.ValidatedLicenseNumberBo;
import net.pladema.sisa.refeps.services.domain.RefepsResourceAttributes;
import net.pladema.sisa.refeps.services.exceptions.RefepsApiException;
import net.pladema.sisa.refeps.services.exceptions.RefepsLicenseException;

import java.util.List;

public interface RefepsService {

	List<ValidatedLicenseNumberBo> validateLicenseNumber(RefepsResourceAttributes attributes, List<String> licenses) throws RefepsApiException, RefepsLicenseException;

}
