package net.pladema.sisa.refeps.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.sisa.refeps.services.RefepsService;
import net.pladema.sisa.refeps.services.domain.ValidatedLicenseNumberBo;
import net.pladema.sisa.refeps.services.exceptions.RefepsApiException;
import net.pladema.sisa.refeps.services.exceptions.RefepsExceptionsEnum;
import net.pladema.sisa.refeps.services.exceptions.RefepsLicenseException;

@Slf4j
@AllArgsConstructor
@ConditionalOnProperty(
		value="ws.sisa.enabled",
		havingValue = "false",
		matchIfMissing = true
)
@Service
public class RefepsServiceVoid implements RefepsService {
	private final FeatureFlagsService featureFlagsService;


	@Override
	public List<ValidatedLicenseNumberBo> validateLicenseNumber(String identificationNumber, List<String> licenses) throws RefepsApiException, RefepsLicenseException {
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_VALIDACION_MATRICULAS_SISA)) {
			throw new RefepsLicenseException(RefepsExceptionsEnum.GENERIC_ERROR, "El servicio de REFEPS no se encuentra habilitado");
		}
		return licenses.stream()
				.map(licence -> new ValidatedLicenseNumberBo(licence, true))
				.collect(Collectors.toList());
	}
}

